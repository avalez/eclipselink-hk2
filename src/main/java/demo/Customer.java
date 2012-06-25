package demo;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;
import org.eclipse.persistence.annotations.VirtualAccessMethods;
import org.eclipse.persistence.config.EntityManagerProperties;

@Entity
@Table(name = "CUSTOMER")
@Multitenant
@TenantDiscriminatorColumn(name = "TENANT", contextProperty = EntityManagerProperties.MULTITENANT_PROPERTY_DEFAULT)
@VirtualAccessMethods
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Basic
    private String name;

    public Customer() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    @Transient
    private Map<String, Object> extensions= new HashMap<String, Object>();
 
    public <T> T get(String name) {
        return (T ) extensions.get(name);
    }
 
    public Object set(String name, Object value) {
        return extensions.put(name, value);
    }
}
