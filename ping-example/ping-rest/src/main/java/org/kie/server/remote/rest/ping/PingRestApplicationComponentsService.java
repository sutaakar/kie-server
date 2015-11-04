package org.kie.server.remote.rest.ping;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.kie.server.services.api.KieServerApplicationComponentsService;
import org.kie.server.services.api.SupportedTransports;
import org.kie.server.services.ping.PingExtension;
import org.kie.server.services.ping.PingService;

/**
 * Allows to get resource classes using service loader.
 */
public class PingRestApplicationComponentsService implements KieServerApplicationComponentsService {

    private static final String OWNER_EXTENSION = PingExtension.EXTENSION_NAME;

    @Override
    public Collection<Object> getAppComponents( String extension, SupportedTransports type, Object... services ) {
        // skip calls from other than owning extension
        if ( !OWNER_EXTENSION.equals(extension) ) {
            return Collections.emptyList();
        }

        PingService pingService = null;

        for( Object object : services ) { 
            if( PingService.class.isAssignableFrom(object.getClass()) ) { 
                pingService = (PingService) object;
            }
        }

        List<Object> components = new ArrayList<Object>(1);
        if( SupportedTransports.REST.equals(type) ) {
            components.add(new PingResource(pingService));
        }

        return components;
    }

}
