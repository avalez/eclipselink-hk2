package demo;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import org.eclipse.persistence.config.EntityManagerProperties;
import org.eclipse.persistence.config.PersistenceUnitProperties;

public class App 
{
    public static void main( String[] args )
    {
        Map<String, String> properties = new HashMap<String, String>();
        properties.put(PersistenceUnitProperties.MULTITENANT_PROPERTY_DEFAULT, "707");   
        properties.put(PersistenceUnitProperties.MULTITENANT_SHARED_EMF, Boolean.FALSE.toString());
        properties.put(PersistenceUnitProperties.SESSION_NAME, "708@example.com");
        EntityManager em = Persistence.createEntityManagerFactory("multi-tenant-pu", properties).createEntityManager();
        
        //EntityManager em = Persistence.createEntityManagerFactory("multi-tenant-pu").createEntityManager();
        em.getTransaction().begin();
        em.setProperty(EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT, "708");

        Customer customer = new Customer();
        customer.setName("AMCE");
        customer.set("custom", "test");
        em.persist(customer);
        em.getTransaction().commit();
    }
}
