/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2012 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */
package demo.multitenancy.persistence;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.eclipse.persistence.config.PersistenceUnitProperties;
import org.glassfish.hk2.api.Factory;
import org.jvnet.hk2.annotations.Service;

import demo.multitenancy.TenantManager;
import demo.multitenancy.TenantScoped;

/**
 * @author Andriy Zhanov
 *
 */
@Service
@Singleton
public class EMFFactory implements Factory<EntityManagerFactory> {
    @Inject
    private TenantManager manager;

    /**
     * This method creates EntityManager based on the current tenant. It uses
     * 'Persistence Unit per Tenant' - new persistence contexts with their own
     * caches are created per tenant. It could use 'Persistence Context per
     * Tenant' - single shared persistence unit, so tenant context is specified
     * per persistence Context (EntityManager) using the createEntityManager(Map
     * properties). See http://wiki.eclipse.org/EclipseLink/Examples/JPA/Multitenant
     */
    @TenantScoped
    public EntityManagerFactory provide() {
        String currentTenant = manager.getCurrentTenant();
        System.out.println("Creating EntityManager for " + currentTenant);

        Map<String, String> properties = new HashMap<String, String>();
        properties.put(PersistenceUnitProperties.MULTITENANT_PROPERTY_DEFAULT, currentTenant);   
        properties.put(PersistenceUnitProperties.MULTITENANT_SHARED_EMF, Boolean.FALSE.toString());
        properties.put(PersistenceUnitProperties.SESSION_NAME, currentTenant);
        return Persistence.createEntityManagerFactory("multi-tenant-pu", properties);
    }

   /*
    * @see org.glassfish.hk2.api.Factory#dispose(java.lang.Object)
    */
   @Override
   public void dispose(EntityManagerFactory instance) {
       // No disposal in this case
       
   }
}
