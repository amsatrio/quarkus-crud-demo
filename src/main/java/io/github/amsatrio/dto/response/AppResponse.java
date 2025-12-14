package io.github.amsatrio.dto.response;

import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@RegisterForReflection
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class AppResponse<T> {
    private String path;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date timestamp;

    public Date getTimestamp() {
        return Optional.ofNullable(this.timestamp).orElse(null);
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = (timestamp != null)
                ? new Date(timestamp.getTime())
                : null;
    }

    private int status;

    private String message;

    private T data;

    private StackTraceElement[] trace;

    public StackTraceElement[] getTrace() {
        return trace != null
                ? Arrays.copyOf(trace, trace.length)
                : null;
    }

    public void setTrace(StackTraceElement[] originalTrace) {
        if (originalTrace != null) {
            // Limit stack trace to prevent potential information leakage
            this.trace = Arrays.copyOfRange(
                    originalTrace,
                    0,
                    Math.min(originalTrace.length, 5));
        } else {
            this.trace = null;
        }
    }

    public String toJsonString() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(this);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return null;
    }


    // === CUSTOM

    public static <T> AppResponse<T> ok(T data) {
        AppResponse<T> response = new AppResponse<>();
        response.setTimestamp(new Date());
        response.setStatus(200);
        response.setData(data);
        response.setMessage("success");
        return response;
    }

    public static <T> AppResponse<T> success(int status, String path, T data) {
        AppResponse<T> response = new AppResponse<>();
        response.setTimestamp(new Date());
        response.setStatus(status);
        response.setData(data);
        response.setMessage("success");
        response.setPath(path);
        return response;
    }

    public static <T> AppResponse<T> success(int status, String path) {
        AppResponse<T> response = new AppResponse<>();
        response.setTimestamp(new Date());
        response.setStatus(status);
        response.setMessage("success");
        response.setPath(path);
        return response;
    }

    public static <T> AppResponse<T> error(int status, String message, StackTraceElement[] errorTrace) {
        AppResponse<T> response = new AppResponse<>();
        response.setTimestamp(new Date());
        response.setStatus(status);
        response.setTrace(errorTrace);
        response.setMessage(message);
        return response;
    }

    public static <T> AppResponse<T> error(int status, String message, String path) {
        AppResponse<T> response = new AppResponse<>();
        response.setTimestamp(new Date());
        response.setStatus(status);
        response.setPath(path);
        response.setMessage(message);
        return response;
    }

    public static <T> AppResponse<T> error(int status, String message, T data, StackTraceElement[] trace) {
        AppResponse<T> response = new AppResponse<>();
        response.setTimestamp(new Date());
        response.setStatus(status);
        response.setTrace(trace);
        response.setMessage(message);
        response.setData(data);
        return response;
    }
}
