package com.example.orderMovie;


import com.example.orderMovie.convert.ConvertUtils;
import com.example.orderMovie.domain.jpa.Movie;
import com.example.orderMovie.domain.jpa.Order;
import com.example.orderMovie.domain.query.QueryUtils;
import com.example.orderMovie.dto.order.OrderCreateUpdateDto;
import com.example.orderMovie.dto.order.OrderViewDto;
import com.example.orderMovie.repository.jpa.MovieRepository;
import com.example.orderMovie.repository.jpa.OrderRepository;
import com.example.orderMovie.service.MovieService;
import com.example.orderMovie.service.MovieServiceImpl;
import com.example.orderMovie.service.OrderServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderMovieTests {
	@Mock
	private OrderRepository orderRepository;
	@Mock
	private MovieRepository movieRepository;
	private OrderServiceImpl orderService;

	Order order;
	OrderCreateUpdateDto dto;

	@BeforeEach
	void setUp() {
		order = createOrder();
		dto = mapOrderToOrderDto(order);

		ModelMapper modelMapper = new ModelMapper();
		QueryUtils queryUtils = new QueryUtils(modelMapper);

		ConvertUtils convertUtils = new ConvertUtils(modelMapper);
		MovieService movieService = new MovieServiceImpl(movieRepository, convertUtils, queryUtils);

		lenient().when(movieRepository.findById(dto.getMovieID())).thenReturn(Optional.of(createMovie()));

		orderService = new OrderServiceImpl(modelMapper, orderRepository, movieService, queryUtils, convertUtils);
	}

	@Test
	public void postOrder()  {
		doReturn(order).when(orderRepository).save(any(Order.class));
		OrderViewDto responseMethod = orderService.post(dto);

		Assertions.assertNotNull(responseMethod.getMovieID());
		Assertions.assertNotNull(responseMethod.getPrice());
	}

	@Test
	public void postOrderMovieWithDiscount()  {
		order.setPrice(BigDecimal.valueOf(75.00));
		doReturn(order).when(orderRepository).save(any(Order.class));

		dto.setIsBirthday(true);
		dto.setPrice(BigDecimal.valueOf(100.00));
		dto.setPercentDiscount(25);
		OrderViewDto responseMethod = orderService.post(dto);

		Assertions.assertEquals(order.getIsBirthday(), responseMethod.getIsBirthday());
		Assertions.assertEquals(BigDecimal.valueOf(75.00), responseMethod.getPrice());
	}

	@Test
	public void postOrderMovieThrowAgeLimitException()  {
		dto.setCustomerAge(12);

		Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
			orderService.post(dto);
		});

		String expectedMessage = "The Person is to younger then limit";
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	public void postOrderThrowEntityNotFoundException()  {
		dto.setMovieID(2L);
		lenient().when(movieRepository.findById(dto.getMovieID())).thenReturn(Optional.empty());

		Exception exception = Assertions.assertThrows(EntityNotFoundException.class, () -> {
			orderService.post(dto);
		});

		String expectedMessage = "Not Found Movie by ID:2";
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	public void DeleteOrder()  {
		when(orderRepository.findById(any(Long.class))).thenReturn(Optional.of(order));
		OrderViewDto responseMethod = orderService.delete(order.getOrderID());

		Assertions.assertEquals(order.getOrderID(), responseMethod.getOrderID());
	}

	@Test
	public void deleteOrderThrowEntityNotFoundException()  {
		Exception exception = Assertions.assertThrows(EntityNotFoundException.class, () -> {
			orderService.delete(3L);
		});

		String expectedMessage = "Not Found Order movie by ID:3";
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	public void patchOrder()  {
		when(orderRepository.findById(any(Long.class))).thenReturn(Optional.of(order));

		Map<String, Object> mapPatch = new HashMap<>();
		mapPatch.put("customerName", "updated CustomerName");
		mapPatch.put("dateSession", "2022-12-12 14:00");

		doReturn(order).when(orderRepository).save(any(Order.class));

		OrderViewDto responseMethod = orderService.patch(order.getOrderID(), mapPatch);

		Assertions.assertEquals(mapPatch.get("customerName"), responseMethod.getCustomerName());
		Assertions.assertEquals("2022-12-12T14:00", responseMethod.getDateSession());
	}

	private OrderCreateUpdateDto mapOrderToOrderDto(Order order) {
		OrderCreateUpdateDto dto = new OrderCreateUpdateDto();
		dto.setCustomerName(order.getCustomerName());
		dto.setCustomerAge(order.getCustomerAge());
		dto.setPrice(order.getPrice());
		dto.setMovieID(order.getMovieID());
		dto.setDateSession("2022-10-10 14:00");

		return dto;
	}

	private Order createOrder() {
		Order order = new Order();
		order.setOrderID(1L);
		order.setCustomerName("John");
		order.setCustomerAge(27);
		order.setPrice(BigDecimal.valueOf(11.00));
		order.setMovieID(3L);
		order.setDateSession(LocalDateTime.now());

		return order;
	}

	private Movie createMovie() {
		Movie movie = new Movie();
		movie.setMovieID(1L);
		movie.setAgeLimit(16);
		movie.setTitle("Piu");
		movie.setRating(10);
		movie.setDuration("1h 30m");

		return movie;
	}

}
