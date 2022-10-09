package com.example.orderMovie.domain.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class SearchParams {

    public enum QueryOperator {
        EQUALS, NOT_EQUALS, GREATER_THAN, DATE_GREATER_THAN, DATE_LESS_THAN, LESS_THAN, IN, LIKE;
    }

    private String field;
    private QueryOperator operator;
    @ApiModelProperty(notes = "search params value", example = "2022-12-31 00:00")
    private String value;
    private List<String> values; //Used in case of IN operator

    @ApiModelProperty(notes = "Number of page")
    private int page = 0;
    @ApiModelProperty(notes = "Items per page", example = "10")
    private int sizeOfPage = 10;

    @ApiModelProperty(example = "ASC")
    private String sortDirection;

    private String sortByField;
}
