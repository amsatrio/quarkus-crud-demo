package io.github.amsatrio.modules.health;

import io.github.amsatrio.dto.response.AppResponse;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Path("/v1/health")
public class HealthApi {
    @GET
    @Path("/status")
    @Produces(MediaType.APPLICATION_JSON)
    public AppResponse<String> status() {
        log.info("status");
        return AppResponse.ok("up");
    }
}
