package io.github.amsatrio.dto.response;

import java.util.ArrayList;
import java.util.List;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@RegisterForReflection
public class PaginationResponse<T> {
    private List<T> content;
    private Boolean last;
    private Boolean first;
    private Long totalElements;
    private Long totalPages;

    // === GETTER

    public List<T> getContent() {
        return content == null ? null : new ArrayList<>(content);
    }

    // === SETTER

    public void setContent(List<T> content) {
        this.content = content == null ? null : new ArrayList<>(content);
    }

}
