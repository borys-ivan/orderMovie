package com.example.orderMovie.service;

import com.example.orderMovie.convert.ConvertUtils;
import com.example.orderMovie.domain.jpa.Movie;
import com.example.orderMovie.domain.jpa.Order;
import com.example.orderMovie.domain.query.SearchResult;
import com.example.orderMovie.domain.query.SearchParams;
import com.example.orderMovie.domain.query.QueryUtils;
import com.example.orderMovie.dto.order.OrderCreateUpdateDto;
import com.example.orderMovie.dto.order.OrderViewDto;
import com.example.orderMovie.repository.jpa.OrderRepository;
import liquibase.repackaged.org.apache.commons.collections4.IterableUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class OrderServiceImpl implements OrderService {

    private final ModelMapper modelMapper;
    private final OrderRepository orderRepository;
    private final MovieService movieService;
    private final QueryUtils queryUtils;
    private final ConvertUtils convertUtils;


    @Override
    public Order findByID(long orderID) {
        return orderRepository.findById(orderID)
                .orElseThrow(() -> new EntityNotFoundException("Not Found Order movie by ID:"+orderID));
    }

    @Override
    public OrderViewDto read(long orderID) {
        return convertUtils.convert(findByID(orderID), new OrderViewDto());
    }

    @Override
    public OrderViewDto post(OrderCreateUpdateDto dto) {
        // convert entity to DTO
        Order newOrder = convertUtils.convert(dto, new Order());

        Movie movie = movieService.findByID(dto.getMovieID());
        newOrder.setMovie(movie);

        Integer percentDiscount = newOrder.getPercentDiscount();
        if (newOrder.getIsBirthday() && percentDiscount != null) {
            newOrder.setPercentDiscount(percentDiscount);
            BigDecimal newDiscountPrice = newOrder.getPrice().multiply(new BigDecimal(percentDiscount/100.0));

            newOrder.setPrice(newOrder.getPrice().subtract(newDiscountPrice).setScale(2, RoundingMode.UP));
        }

        Integer age = newOrder.getCustomerAge();
        Integer ageLimit = movie.getAgeLimit();
        if ( age != null && ageLimit != null && age < ageLimit) {
            throw new IllegalArgumentException("The Person is to younger then limit");
        }

        Order order = orderRepository.save(newOrder);

        // convert entity to DTO
        OrderViewDto createdOrder = convertUtils.convert(order, new OrderViewDto());

        log.info("created movie order");
        return createdOrder;
    }

    @Override
    public OrderViewDto put(long orderID, OrderCreateUpdateDto dto) {
        Order order = findByID(orderID);

        if (!Objects.equals(dto.getMovieID(), order.getMovieID())) {
            Movie movie = movieService.findByID(dto.getMovieID());
            order.setMovie(movie);
        }

        convertUtils.convert(dto, order);
        Order updatedOrder = orderRepository.save(order);
        OrderViewDto orderView = modelMapper.map(updatedOrder, OrderViewDto.class);

        log.info("updated movie order");

        return orderView;
    }

    @Override
    public OrderViewDto patch(long orderID, Map<String, Object> patchMap) {
        OrderCreateUpdateDto orderCreateUpdateDto = read(orderID);

        convertUtils.convert(patchMap, orderCreateUpdateDto);

        log.info("updating movie order");

        return put(orderID, orderCreateUpdateDto);
    }

    @Override
    public OrderViewDto delete(long orderID) {
        Order order = findByID(orderID);
        orderRepository.delete(order);

        OrderViewDto deletedOrder = convertUtils.convert(order, new OrderViewDto());

        log.info("Removed movie order");

        return deletedOrder;
    }

    @Override
    public SearchResult<OrderViewDto> searchList(SearchParams searchParams) {
        List<Sort.Order> sortOrders = new ArrayList<>();
        sortOrders.add(Sort.Order.asc("orderID"));

        PageRequest page = PageRequest.of(searchParams.getPage(), searchParams.getSizeOfPage(), Sort.by(sortOrders));

        Page<Order> order;
        if (searchParams.getOperator() != null) {
            Specification<Order> orderSpecification = Specification.where(queryUtils.createSpecification(searchParams));
            order = orderRepository.findAll(orderSpecification, page);
        } else {
            List<Order> list = IterableUtils.toList(orderRepository.findAll());
            order = new PageImpl<Order>(list, page, list.size());
        }

        return queryUtils.convertSearchResult(order, page, OrderViewDto.class);
    }
}
