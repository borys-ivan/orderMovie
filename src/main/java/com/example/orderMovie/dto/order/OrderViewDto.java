package com.example.orderMovie.dto.order;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class OrderViewDto extends OrderCreateUpdateDto {

    private Long orderID;

}
