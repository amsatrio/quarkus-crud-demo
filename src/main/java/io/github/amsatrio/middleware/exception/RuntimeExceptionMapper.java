package io.github.amsatrio.middleware.exception;

import io.github.amsatrio.dto.response.AppResponse;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Provider
public class RuntimeExceptionMapper implements ExceptionMapper<RuntimeException> {

    @Override
    public Response toResponse(RuntimeException exception) {
        log.error("error:", exception);

        AppResponse<String> appResponse = AppResponse.error(500, exception.getMessage(), exception.getStackTrace());
        
        return Response
                .status(appResponse.getStatus())
                .entity(appResponse)
                .build();
    }
}
