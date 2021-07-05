package com.restful.api.repository.netflix;

import com.restful.api.domain.netflix.NetflixMovie;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositories are where we base and do all the operations for the API. We base our films with the repositories
 */
public interface NetflixMovieRepository extends JpaRepository<NetflixMovie, Integer> {
}
