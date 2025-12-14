package io.github.amsatrio.middleware.exception;

import io.github.amsatrio.dto.exception.NotFoundException;
import io.github.amsatrio.dto.response.AppResponse;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Provider
public class NotFoundExceptionMapper implements ExceptionMapper<NotFoundException> {

    @Override
    public Response toResponse(NotFoundException exception) {
        log.error("error:", exception);

        AppResponse<String> appResponse = AppResponse.error(404, exception.getMessage(), exception.getStackTrace());
        
        return Response
                .status(appResponse.getStatus())
                .entity(appResponse)
                .build();
    }
}
