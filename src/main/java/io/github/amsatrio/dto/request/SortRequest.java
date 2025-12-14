package io.github.amsatrio.dto.request;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import io.quarkus.runtime.annotations.RegisterForReflection;
import io.vertx.core.json.jackson.DatabindCodec;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@RegisterForReflection
public class SortRequest implements Serializable {
    private String id;
    private boolean desc;

    public static List<SortRequest> from(String input) {
        ObjectMapper objectMapper = DatabindCodec.mapper().registerModule(new JavaTimeModule());
        try {
            return objectMapper.readValue(input, new TypeReference<List<SortRequest>>() {
            });
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize response from String", e);
        }
    }
}
