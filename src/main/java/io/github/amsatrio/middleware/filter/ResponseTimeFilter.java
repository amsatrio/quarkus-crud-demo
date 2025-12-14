package io.github.amsatrio.middleware.filter;

import java.io.IOException;
import org.jboss.logging.Logger;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Provider
public class ResponseTimeFilter implements ContainerResponseFilter, ContainerRequestFilter {

    private static final String START_TIME_KEY = "start-time";

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        log.info("incoming");
        requestContext.setProperty(START_TIME_KEY, System.nanoTime());
    }

    @Override
    public void filter(ContainerRequestContext requestContext,
            ContainerResponseContext responseContext) throws IOException {
        log.info("outgoing");

        Object startTimeObj = requestContext.getProperty(START_TIME_KEY);

        if (startTimeObj instanceof Long) {
            long startTime = (Long) startTimeObj;
            long endTime = System.nanoTime();

            double elapsedMillis = (endTime - startTime) / 1_000_000.0;

            String method = requestContext.getMethod();
            String path = requestContext.getUriInfo().getAbsolutePath().toString();
            int status = responseContext.getStatus();

            log.info("["+method+" "+path+"] Status: "+status+", Response Time: "+elapsedMillis+" ms");

            responseContext.getHeaders().add("X-Response-Time-ms", String.format("%.2f", elapsedMillis));
        } else {
            log.warn("Could not find start time in request context.");
        }
    }
}