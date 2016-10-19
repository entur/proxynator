package org.rutebanken.proxynator.rest;

import org.rutebanken.proxynator.service.LittleProxyService;
import org.rutebanken.proxynator.service.TraceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Component
@Produces("application/json")
@Path("/health")
public class ApplicationStatusResource {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private LittleProxyService proxy;

    @Autowired
    private TraceService traceService;

    private boolean achieveAlivenessOnce = false;

    @GET
    @Path("/ready")
    public Response isReady() {
        if ( proxy.isAlive() && traceService.isAlive()) {
            achieveAlivenessOnce = true;
            return Response.ok("OK").build();
        }
        return Response.serverError()
                       .status(Response.Status.SERVICE_UNAVAILABLE)
                       .entity("One or more services are not running")
                       .build();
    }

    @GET
    @Path("/up")
    public Response isUp() {
        if (achieveAlivenessOnce) {
            return Response.ok("OK").build();
        }
        return Response.serverError()
                .status(Response.Status.SERVICE_UNAVAILABLE)
                .entity("I have not achieved aliveness even once.")
                .build();
    }

}
