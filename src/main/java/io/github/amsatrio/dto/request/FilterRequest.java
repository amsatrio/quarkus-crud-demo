package io.github.amsatrio.dto.request;

import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import io.github.amsatrio.dto.enumerator.FilterDataType;
import io.github.amsatrio.dto.enumerator.FilterMatchMode;
import io.quarkus.runtime.annotations.RegisterForReflection;
import io.vertx.core.json.jackson.DatabindCodec;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@RegisterForReflection
public class FilterRequest {
    private String id;
    private Object value;
    private FilterMatchMode matchMode = FilterMatchMode.CONTAINS;
    private FilterDataType dataType = FilterDataType.TEXT;

    public static List<FilterRequest> from(String input) {
        ObjectMapper objectMapper = DatabindCodec.mapper().registerModule(new JavaTimeModule());
        try {
            return objectMapper.readValue(input, new TypeReference<List<FilterRequest>>() {
            });
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize response from String", e);
        }
    }
}
