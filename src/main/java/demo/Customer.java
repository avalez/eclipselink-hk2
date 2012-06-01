package demo;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;

@Entity
@Table(name = "CUSTOMER")
@Multitenant
@TenantDiscriminatorColumn(name = "TENANT", contextProperty = "multi-tenant.id")
public class Customer {

    @Id
    @GeneratedValue
    private String id;
    
    @Basic
    private String name;

    public Customer() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
}
