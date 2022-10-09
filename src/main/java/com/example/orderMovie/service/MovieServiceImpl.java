package com.example.orderMovie.service;

import com.example.orderMovie.convert.ConvertUtils;
import com.example.orderMovie.domain.jpa.Movie;
import com.example.orderMovie.domain.query.QueryUtils;
import com.example.orderMovie.domain.query.SearchParams;
import com.example.orderMovie.domain.query.SearchResult;
import com.example.orderMovie.dto.movie.MovieCreateUpdateDto;
import com.example.orderMovie.dto.movie.MovieViewDto;
import com.example.orderMovie.repository.jpa.MovieRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;
    private final ConvertUtils convertUtils;
    private final QueryUtils queryUtils;

    String[] SORT = {"movieID", "title", "rating", "age_limit"};

    @Override
    public Movie findByID(long movieID) {
        return movieRepository.findById(movieID)
                .orElseThrow(() -> new EntityNotFoundException("Not Found Movie by ID:"+movieID));
    }

    @Override
    public MovieViewDto read(long movieID) {
        return convertUtils.convert(findByID(movieID), new MovieViewDto());
    }

    @Override
    public MovieViewDto post(MovieCreateUpdateDto dto) {
        // convert entity to DTO
        Movie newMovie = convertUtils.convert(dto, new Movie());

        //newMovie.setCreated(new Date());

        Movie movie = movieRepository.save(newMovie);

        // convert entity to DTO
        MovieViewDto createdMovie =convertUtils.convert(movie, new MovieViewDto());

        log.info("created movie");
        return createdMovie;
    }

    @Override
    public MovieViewDto put(long movieID, MovieCreateUpdateDto dto) {
        Movie movie = findByID(movieID);

        convertUtils.convert(dto, movie);
        Movie updatedMovie = movieRepository.save(movie);

        MovieViewDto updatedViewDto = convertUtils.convert(updatedMovie, new MovieViewDto());

        log.info("updated movie");

        return updatedViewDto;
    }

    @Override
    public MovieViewDto patch(long movieID, Map<String, Object> patchMap) {
        MovieCreateUpdateDto movieCreateUpdateDto = read(movieID);
        convertUtils.convert(patchMap, movieCreateUpdateDto);

        log.info("updating movie");

        return put(movieID, movieCreateUpdateDto);
    }

    @Override
    public MovieViewDto delete(long movieID) {
        Movie movie = findByID(movieID);
        movieRepository.delete(movie);

        MovieViewDto deletedOrder = convertUtils.convert(movie, new MovieViewDto());

        log.info("Removed movie");

        return deletedOrder;
    }

    @Override
    public SearchResult<MovieViewDto> searchList(SearchParams searchParams) {
        List<String> sortableFields = Arrays.stream(SORT).collect(Collectors.toList());
        List<Sort.Order> sortOrders = new ArrayList<>();

        Sort.Direction direction;
        if ( "ASC".equalsIgnoreCase(searchParams.getSortDirection()) || "DESC".equalsIgnoreCase(searchParams.getSortDirection()) ) {
            direction = Sort.Direction.valueOf(searchParams.getSortDirection().toUpperCase());
        } else {
            direction = Sort.Direction.ASC;
        }

        String sortBy;
        if (sortableFields.contains(searchParams.getSortByField())) {
            sortBy = searchParams.getSortByField();
        } else {
            sortBy = "movieID";
        }
        Sort.Order sort = new Sort.Order(direction, sortBy);
        sortOrders.add(sort);

        PageRequest page = PageRequest.of(searchParams.getPage(), searchParams.getSizeOfPage(), Sort.by(sortOrders));

        Page<Movie> movies;
        if (searchParams.getOperator() != null) {
            Specification<Movie> movieSpecification = Specification.where(queryUtils.createSpecification(searchParams));
            movies = movieRepository.findAll(movieSpecification, page);
        } else {
            movies = movieRepository.findAll(page);
        }

        return queryUtils.convertSearchResult(movies, page, MovieViewDto.class);
    }
}
