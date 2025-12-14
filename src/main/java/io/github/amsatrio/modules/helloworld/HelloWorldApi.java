package io.github.amsatrio.modules.helloworld;

import org.jboss.resteasy.reactive.RestPath;
import org.jboss.resteasy.reactive.RestQuery;

import io.github.amsatrio.dto.response.AppResponse;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Path("/v1/hello-world")
public class HelloWorldApi {
    @GET
    @Path("/hello")
    @Produces(MediaType.APPLICATION_JSON)
    public AppResponse<String> hello() {
        return AppResponse.ok("Hello world");
    }

    @GET
    @Path("/{param}")
    @Produces(MediaType.APPLICATION_JSON)
    public AppResponse<String> helloByPathParam(@RestPath("param") String param) {
        return AppResponse.ok("Hello " + param);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public AppResponse<String> helloByQueryParam(@RestQuery("param") String param) {
        return AppResponse.ok("Hello " + param);
    }

    @GET
    @Path("/log")
    @Produces(MediaType.APPLICATION_JSON)
    public AppResponse<String> helloLog(@RestQuery("logLevel") String logLevel) {
		if (logLevel.equals("info")) {
			log.info("helloLog info");
		} else if (logLevel.equals("error")) {
			log.error("helloLog error");
		} else {
			log.debug("invalid log level");
		}
        return AppResponse.ok(null);
    }


        @GET
    @Path("/error")
    @Produces(MediaType.APPLICATION_JSON)
    public AppResponse<String> helloError(@RestQuery("errorType") String errorType) {
		if(errorType.equals("bad_request")){
			throw new BadRequestException("sample bad request");
		}

		throw new RuntimeException("invalid error");
    }
}
