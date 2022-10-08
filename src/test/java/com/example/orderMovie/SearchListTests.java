package com.example.orderMovie;

import com.example.orderMovie.convert.ConvertUtils;
import com.example.orderMovie.domain.jpa.Movie;
import com.example.orderMovie.domain.jpa.Order;
import com.example.orderMovie.domain.query.QueryUtils;
import com.example.orderMovie.domain.query.SearchParams;
import com.example.orderMovie.domain.query.SearchResult;
import com.example.orderMovie.dto.movie.MovieViewDto;
import com.example.orderMovie.dto.order.OrderViewDto;
import com.example.orderMovie.repository.jpa.MovieRepository;
import com.example.orderMovie.repository.jpa.OrderRepository;
import com.example.orderMovie.service.MovieService;
import com.example.orderMovie.service.MovieServiceImpl;
import com.example.orderMovie.service.OrderService;
import com.example.orderMovie.service.OrderServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
public class SearchListTests {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MovieRepository movieRepository;

    Order newOrder1;
    Order newOrder2;
    Movie movie1;

    OrderService orderService;
    MovieService movieService;

    @Before
    public void init() {
        ModelMapper modelMapper = new ModelMapper();
        QueryUtils queryUtils = new QueryUtils(modelMapper);

        ConvertUtils convertUtils = new ConvertUtils(modelMapper);
        movieService = new MovieServiceImpl(movieRepository, convertUtils, queryUtils);
        orderService = new OrderServiceImpl(modelMapper, orderRepository, movieService, queryUtils, convertUtils);

        Movie movie = createMovie();
        movie1 = movieRepository.save(movie);

        Order order1 = createOrder();
        order1.setOrderID(0L);
        order1.setCustomerName("test1");
        order1.setPrice(BigDecimal.valueOf(11));
        order1.setDateSession(LocalDateTime.now().plusDays(11));
        newOrder1 = orderRepository.save(order1);

        Order order2 = createOrder();
        order2.setOrderID(0L);
        order2.setCustomerName("test2");
        order2.setPrice(BigDecimal.valueOf(22));
        order2.setDateSession(LocalDateTime.now().plusDays(12));
        newOrder2 = orderRepository.save(order2);

    }

    @Test
    public void searchOrderList()  {
        SearchParams searchParams = new SearchParams();
        searchParams.setPage(0);
        searchParams.setSizeOfPage(5);
        searchParams.setField("dateSession");
        searchParams.setValue(generateDate(10));
        searchParams.setOperator(SearchParams.QueryOperator.DATE_GREATER_THAN);

        SearchResult<OrderViewDto> responseMethod = orderService.searchList(searchParams);

        assertEquals(responseMethod.getNumberOfElements(), 2);
    }

    @Test
    public void searchMovieList()  {
        SearchParams searchParams = new SearchParams();
        searchParams.setPage(0);
        searchParams.setSizeOfPage(5);
        searchParams.setField("title");
        searchParams.setValue("Hot-weells");
        searchParams.setOperator(SearchParams.QueryOperator.EQUALS);

        SearchResult<MovieViewDto> responseMethod = movieService.searchList(searchParams);

        assertEquals(responseMethod.getNumberOfElements(), 1);
    }

    private Order createOrder() {
        Order order = new Order();
        order.setOrderID(1000L);
        order.setCustomerName("John");
        order.setCustomerAge(27);
        order.setPrice(BigDecimal.valueOf(10.00));
        order.setMovie(movie1);
        order.setDateSession(LocalDateTime.now());

        return order;
    }

    private Movie createMovie() {
        Movie movie = new Movie();
        movie.setMovieID(1000L);
        movie.setAgeLimit(16);
        movie.setTitle("Hot-weells");
        movie.setRating(10);
        movie.setDuration("1h 30m");

        return movie;
    }

    private String generateDate(int plusDays) {
        LocalDateTime myDateObj = LocalDateTime.now().plusDays(plusDays);
        System.out.println("Before formatting: " + myDateObj);
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        return myDateObj.format(myFormatObj);
    }

}
