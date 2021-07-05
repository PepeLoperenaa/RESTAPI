package com.restful.api.repository.amazon;

import com.restful.api.domain.amazon.AmazonMovie;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositories are where we base and do all the operations for the API. We base our films with the repositories
 */
public interface AmazonMovieRepository extends JpaRepository<AmazonMovie, Integer> {

}
