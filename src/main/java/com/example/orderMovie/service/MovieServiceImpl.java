package com.example.orderMovie.service;

import com.example.orderMovie.convert.ConvertUtils;
import com.example.orderMovie.domain.jpa.Movie;
import com.example.orderMovie.domain.query.QueryUtils;
import com.example.orderMovie.domain.query.SearchParams;
import com.example.orderMovie.domain.query.SearchResult;
import com.example.orderMovie.dto.movie.MovieCreateUpdateDto;
import com.example.orderMovie.dto.movie.MovieViewDto;
import com.example.orderMovie.repository.jpa.MovieRepository;
import liquibase.repackaged.org.apache.commons.collections4.IterableUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;
    private final ConvertUtils convertUtils;
    private final QueryUtils queryUtils;

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
        List<Sort.Order> sortOrders = new ArrayList<>();
        sortOrders.add(Sort.Order.asc("movieID"));

        PageRequest page = PageRequest.of(searchParams.getPage(), searchParams.getSizeOfPage(), Sort.by(sortOrders));

        Page<Movie> movies;
        if (searchParams.getOperator() != null) {
            Specification<Movie> orderSpecification = Specification.where(queryUtils.createSpecification(searchParams));
            movies = movieRepository.findAll(orderSpecification, page);
        } else {
            List<Movie> list = IterableUtils.toList(movieRepository.findAll());
            movies = new PageImpl<Movie>(list, page, list.size());
        }

        return queryUtils.convertSearchResult(movies, page, MovieViewDto.class);
    }
}
