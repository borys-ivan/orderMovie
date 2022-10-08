package com.example.orderMovie.repository.jpa;

import com.example.orderMovie.domain.jpa.Movie;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends CrudRepository<Movie, Long>, JpaSpecificationExecutor<Movie> {
}
