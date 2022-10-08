package com.example.orderMovie.controller;

import com.example.orderMovie.domain.query.SearchParams;
import com.example.orderMovie.domain.query.SearchResult;
import com.example.orderMovie.dto.movie.MovieCreateUpdateDto;
import com.example.orderMovie.dto.movie.MovieViewDto;
import com.example.orderMovie.service.MovieService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
@Api(tags = { "Movie API" }, description = "")
@RequestMapping(value = "/system/movie")
public class MovieController {

    private final MovieService movieService;


    @GetMapping(value = "/read/{movieID}")
    @ApiOperation(value = "Read the movie by ID")
    public ResponseEntity<MovieViewDto> read(@PathVariable long movieID) {
        log.info("read movie");

        return new ResponseEntity<>(movieService.read(movieID), HttpStatus.OK);
    }

    @PostMapping(value = "/post")
    @ApiOperation(value = "Create the movie")
    public ResponseEntity<MovieViewDto> post(@RequestBody MovieCreateUpdateDto dto) {
        log.info("creating movie");

        return new ResponseEntity<>(movieService.post(dto), HttpStatus.OK);
    }

    @PutMapping(value = "/put/{movieID}")
    @ApiOperation(value = "Put the movie")
    public ResponseEntity<MovieViewDto> put(
            @PathVariable long movieID,
            @RequestBody MovieCreateUpdateDto dto) {
        log.info("Putting movie");
        return new ResponseEntity<>(movieService.put(movieID, dto), HttpStatus.OK);
    }

    @PatchMapping(value = "/patch/{movieID}")
    @ApiOperation(value = "Update the movie")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "Movie",
                    value = "Movie fields to patch",
                    required = true,
                    dataType = "MovieModelEdit",
                    paramType = "body")
    })
    public ResponseEntity<MovieViewDto> patch(
            @PathVariable long movieID,
            @ApiParam(name = "Movie", value = "Movie fields to update", hidden = true)
            @RequestBody Map<String, Object> mapPatch) {
        log.info("Patching movie");
        return new ResponseEntity<>(movieService.patch(movieID, mapPatch), HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete/{movieID}")
    @ApiOperation(value = "Delete the movie")
    public ResponseEntity<MovieViewDto> delete(@PathVariable long movieID) {
        log.info("Patching movie");

        return new ResponseEntity<>(movieService.delete(movieID), HttpStatus.OK);
    }

    @PostMapping(value = "/list")
    @ApiOperation(value = "List movies")
    @ResponseBody
    public ResponseEntity<SearchResult<MovieViewDto>> list(
            @RequestBody SearchParams searchParams) {
        log.info("Searching in DB");

        return new ResponseEntity<>(movieService.searchList(searchParams), HttpStatus.OK);
    }

}
