package org.rutebanken.proxynator.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    @GET
    @Path("/ready")
    public Response isReady() {
        logger.debug("Checking readiness...");
        return Response.ok("OK").build();
            /*return Response.serverError()
                       .status(Response.Status.SERVICE_UNAVAILABLE)
                       .entity("No etcd connection")
                       .build();*/
    }

    @GET
    @Path("/up")
    public Response isUp() {
        return Response.ok("OK").build();
    }

}
