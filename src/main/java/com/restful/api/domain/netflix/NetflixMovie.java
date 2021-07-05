package com.restful.api.domain.netflix;

import com.restful.api.domain.Movie;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(schema = "netflix", name = "netflixMovie") //table where to find the table.
public class NetflixMovie extends Movie {
    private String actors; //different things other tables do not have
    private String director;

    public NetflixMovie() {
    }

    public NetflixMovie(int id, String title, int release, String genre, String type, String actors, String director) {
        super(id, title, release, genre, type);
        this.actors = actors;
        this.director = director;
    }

    public String getActors() {
        return actors;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }
}
