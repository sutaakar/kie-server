package org.kie.server.remote.rest.ping;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import org.kie.server.services.ping.PingService;

@Path("server/containers/{id}")
public class PingResource {

    private PingService pingService;

    public PingResource(PingService pingService) {
        this.pingService = pingService;
    }


    @GET
    @Path("hello/{name}")
    @Produces({MediaType.TEXT_PLAIN})
    public String getName(@Context HttpHeaders headers, @PathParam("id") String containerId,
            @PathParam("name") String name) {

        return pingService.sayHello(name);
    }
}
