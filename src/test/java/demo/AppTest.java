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
package demo;

import java.io.IOException;

import javax.persistence.EntityManager;

import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.api.ServiceLocatorFactory;
import org.glassfish.hk2.bootstrap.HK2Populator;
import org.glassfish.hk2.bootstrap.impl.ClasspathDescriptorFileFinder;
import org.glassfish.hk2.bootstrap.impl.Hk2LoaderPopulatorPostProcessor;

import demo.multitenancy.TenantManager;

import junit.framework.TestCase;

/**
 * Demonstrate multi-tenancy with EclipseLink and HK2.
 * 
 * @author Andriy Zhdanov
 */
public class AppTest extends TestCase {
    private static final String APP_NAME = "EclipseLink-HK2";
    private final static ServiceLocator locator = ServiceLocatorFactory.getInstance().create(APP_NAME);

    static {
    }

    public void setUp() {
        try {
            HK2Populator.populate(locator,
                    new ClasspathDescriptorFileFinder(),
                    new Hk2LoaderPopulatorPostProcessor(null));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // it creates EntityManager per tenant - apparently this is not expected by EclipseLink
    // 1) drop-or-create-tables ddl-generation drops table per tenant
    // 2) setting tenant property on entityManager behaves even more  interesting
    //     first, entityManager does not notice it, i.e. it returns 'wayne'
    //     later, since 'acme' is not in its cache, it notices the property and returns 'acme' incorrectly.
    // Open question: how EclipseLink supposed to switch entityManager between tenants in multiple threads?
    public void testPersistence()
    {
        final EntityManager entityManager = locator.getService(EntityManager.class);
        
        final TenantManager tenantManager = locator.getService(TenantManager.class);
        
        final String tenantAcme = "acme";
        
        final String tenantWayne = "wayne";

        tenantManager.setCurrentTenant(tenantAcme);
        
        entityManager.getTransaction().begin();
        Customer customer = new Customer();
        customer.setName("ACME");
        customer.set("custom", "Acme Corporation");
        entityManager.persist(customer);
        entityManager.getTransaction().commit();

        Long idAcme = customer.getId();
        
        tenantManager.setCurrentTenant(tenantWayne);

        entityManager.getTransaction().begin();
        customer = new Customer();
        customer.setName("Wayne");
        customer.set("custom", "Bruce Wayne Enterprise");
        entityManager.persist(customer);
        entityManager.getTransaction().commit();

        Long idWayne = customer.getId();
        
        //em.setProperty(PersistenceUnitProperties.MULTITENANT_PROPERTY_DEFAULT, tenantAcme); // is not ignored!!   

        customer = entityManager.find(Customer.class, idWayne);
        
        assertNotNull("Customer", customer);
        
        assertEquals("Customer name", "Wayne", customer.getName());

        customer = entityManager.find(Customer.class, idAcme);

        assertNull("Customer", customer);
    }
}
