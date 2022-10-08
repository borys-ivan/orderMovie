package com.example.orderMovie.service;

import com.example.orderMovie.domain.jpa.Movie;
import com.example.orderMovie.domain.query.SearchParams;
import com.example.orderMovie.domain.query.SearchResult;
import com.example.orderMovie.dto.movie.MovieCreateUpdateDto;
import com.example.orderMovie.dto.movie.MovieViewDto;

import java.util.Map;

public interface MovieService {
    Movie findByID(long movieID);
    MovieViewDto read(long movieID);
    MovieViewDto post(MovieCreateUpdateDto dto);
    MovieViewDto put(long movieID, MovieCreateUpdateDto dto);
    MovieViewDto patch(long movieID, Map<String, Object> patchMap);
    MovieViewDto delete(long movieID);
    SearchResult<MovieViewDto> searchList(SearchParams searchParams);
}
