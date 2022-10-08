package com.example.orderMovie.dto.movie;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class MovieViewDto extends MovieCreateUpdateDto {

    private Long movieID;

}
