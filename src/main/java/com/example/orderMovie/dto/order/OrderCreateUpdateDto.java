package com.example.orderMovie.dto.order;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(value="OrderModelEdit", description="Model for updating order.")
public class OrderCreateUpdateDto {

    private String customerName;
    private Integer customerAge;
    @NotNull
    @ApiModelProperty(example = "A" )
    private String hall;
    @NotNull
    private BigDecimal price;
    private String additional;
    @NotNull
    @ApiModelProperty(notes = "Date of movie session", example = "2022-12-31 12:00" )
    private String dateSession;
    private Integer percentDiscount = 0;
    private Boolean isBirthday = false;
    @NotNull
    private Long movieID;
}
