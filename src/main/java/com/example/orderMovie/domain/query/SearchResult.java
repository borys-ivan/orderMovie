package com.example.orderMovie.domain.query;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchResult<T> implements Serializable {

    private Integer size;
    private Integer number;
    private Long totalElements;
    private Integer totalPages;
    private Boolean hasPrevious;
    private Boolean hasNext;
    private Integer numberOfElements;
    private Long offset;
    private List<T> content;

}
