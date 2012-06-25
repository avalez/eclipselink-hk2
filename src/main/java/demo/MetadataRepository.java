package demo;

import java.util.Map;

import org.eclipse.persistence.internal.jpa.metadata.xml.XMLEntityMappings;
import org.eclipse.persistence.jpa.metadata.MetadataSourceAdapter;
import org.eclipse.persistence.logging.SessionLog;

public class MetadataRepository extends MetadataSourceAdapter {

    @Override
    public XMLEntityMappings getEntityMappings(Map<String, Object> properties,
            ClassLoader classLoader, SessionLog log) {
        XMLEntityMappings entityMappings = super.getEntityMappings(properties, classLoader, log);
        // TODO: extend mapping progrmatically, but entityMappings is NULL
        return entityMappings;
    }

    @Override
    public Map<String, Object> getPropertyOverrides(
            Map<String, Object> properties, ClassLoader classLoader,
            SessionLog log) {
        return properties;
    }

}
