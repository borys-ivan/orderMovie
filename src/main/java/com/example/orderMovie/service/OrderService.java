package com.example.orderMovie.service;


import com.example.orderMovie.domain.jpa.Order;
import com.example.orderMovie.domain.query.SearchResult;
import com.example.orderMovie.domain.query.SearchParams;
import com.example.orderMovie.dto.order.OrderCreateUpdateDto;
import com.example.orderMovie.dto.order.OrderViewDto;

import java.util.Map;

public interface OrderService {
    Order findByID(long orderID);
    OrderViewDto read(long orderID);
    OrderViewDto post(OrderCreateUpdateDto dto);
    OrderViewDto put(long orderID, OrderCreateUpdateDto dto);
    OrderViewDto patch(long orderID, Map<String, Object> patchMap);
    OrderViewDto delete(long orderID);
    SearchResult<OrderViewDto> searchList(SearchParams searchParams);
}
