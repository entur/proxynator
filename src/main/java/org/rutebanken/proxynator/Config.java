package org.rutebanken.proxynator;

import org.glassfish.jersey.server.ResourceConfig;
import org.rutebanken.proxynator.rest.ApplicationStatusResource;
import org.springframework.context.annotation.Configuration;

import javax.ws.rs.ApplicationPath;

/**
 *
 */
@Configuration
@ApplicationPath("/proxynator")
public class Config extends ResourceConfig {
    public Config() {
        registerEndpoints();
    }

    private void registerEndpoints() {
        register(ApplicationStatusResource.class);
    }
}
