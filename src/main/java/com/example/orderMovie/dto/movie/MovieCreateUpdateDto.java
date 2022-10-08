package com.example.orderMovie.dto.movie;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class MovieCreateUpdateDto {

    @NotNull
    private String title;
    private String description;
    @NotNull
    @ApiModelProperty(example = "1h 30m" )
    private String duration;
    private Integer rating;
    private Integer ageLimit;
    @ApiModelProperty(example = "UA")
    private String languageTranslate;

}
