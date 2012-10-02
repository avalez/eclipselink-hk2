package demo.multitenancy;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Named;

import org.glassfish.hk2.api.Injectee;
import org.glassfish.hk2.api.InjectionResolver;
import org.glassfish.hk2.api.ServiceHandle;

public class TenantAwareInjectResolver implements InjectionResolver<TenantAwareInject> {
    @Inject @Named(InjectionResolver.SYSTEM_RESOLVER_NAME)
    private InjectionResolver<Inject> systemResolver;

    @Override
    public Object resolve(Injectee injectee, ServiceHandle<?> root) {
        // AnnotatedElement element = injectee.getParent();
        // Field field = (Field) element;
        // Class<?> injecteeType = field.getDeclaringClass();
        Class<?> injecteeType = injectee.getInjecteeClass();
        if (Logger.class.equals(injecteeType)) {
            return Logger.getLogger(injecteeType.getName());
        } else {
            return systemResolver.resolve(injectee, root); 
        }
    }

    @Override
    public boolean isConstructorParameterIndicator() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isMethodParameterIndicator() {
        // TODO Auto-generated method stub
        return false;
    }

}
