package com.example.orderMovie;

import com.example.orderMovie.convert.ConvertUtils;
import com.example.orderMovie.domain.jpa.Movie;
import com.example.orderMovie.domain.query.QueryUtils;
import com.example.orderMovie.dto.movie.MovieCreateUpdateDto;
import com.example.orderMovie.dto.movie.MovieViewDto;
import com.example.orderMovie.repository.jpa.MovieRepository;
import com.example.orderMovie.service.MovieServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import javax.persistence.EntityNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MovieTests {

    @Mock
    private MovieRepository movieRepository;
    private MovieServiceImpl movieService;

    Movie movie;
    MovieCreateUpdateDto dto;

    @BeforeEach
    void setUp() {
        movie = createMovie();
        dto = mapMovieToOrderDto(movie);

        ModelMapper modelMapper = new ModelMapper();
        QueryUtils queryUtils = new QueryUtils(modelMapper);

        ConvertUtils convertUtils = new ConvertUtils(modelMapper);

        movieService = new MovieServiceImpl(movieRepository, convertUtils, queryUtils);
    }

    @Test
    public void postOrder()  {
        doReturn(movie).when(movieRepository).save(any(Movie.class));
        MovieViewDto responseMethod = movieService.post(dto);

        Assertions.assertNotNull(responseMethod.getMovieID());
        Assertions.assertNotNull(responseMethod.getTitle());
    }

    @Test
    public void DeleteOrder()  {
        when(movieRepository.findById(any(Long.class))).thenReturn(Optional.of(movie));
        MovieViewDto responseMethod = movieService.delete(movie.getMovieID());

        Assertions.assertEquals(movie.getMovieID(), responseMethod.getMovieID());
    }

    @Test
    public void deleteOrderThrowEntityNotFoundException()  {
        Exception exception = Assertions.assertThrows(EntityNotFoundException.class, () -> {
            movieService.delete(3L);
        });

        String expectedMessage = "Not Found Movie by ID:3";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void patchOrder()  {
        when(movieRepository.findById(any(Long.class))).thenReturn(Optional.of(movie));

        Map<String, Object> mapPatch = new HashMap<>();
        mapPatch.put("description", "updated description");
        mapPatch.put("rating", "3");

        doReturn(movie).when(movieRepository).save(any(Movie.class));

        MovieViewDto responseMethod = movieService.patch(movie.getMovieID(), mapPatch);

        Assertions.assertEquals(mapPatch.get("description"), responseMethod.getDescription());
        Assertions.assertEquals(mapPatch.get("rating"), responseMethod.getRating().toString());
    }

    private MovieCreateUpdateDto mapMovieToOrderDto(Movie movie) {
        MovieCreateUpdateDto dto = new MovieCreateUpdateDto();
        dto.setTitle(movie.getTitle());
        dto.setDescription(movie.getDescription());
        dto.setDuration(movie.getDuration());
        dto.setRating(movie.getRating());
        dto.setAgeLimit(movie.getAgeLimit());

        return dto;
    }

    private Movie createMovie() {
        Movie movie = new Movie();
        movie.setMovieID(1L);
        movie.setAgeLimit(16);
        movie.setTitle("Cars");
        movie.setRating(10);
        movie.setDuration("1h 30m");

        return movie;
    }

}
