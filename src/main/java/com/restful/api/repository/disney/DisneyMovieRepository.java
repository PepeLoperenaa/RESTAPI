package com.restful.api.repository.disney;

import com.restful.api.domain.disney.DisneyMovie;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositories are where we base and do all the operations for the API. We base our films with the repositories
 */
public interface DisneyMovieRepository extends JpaRepository<DisneyMovie, Integer> {
}
