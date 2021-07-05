package com.restful.api.domain.disney;

import com.restful.api.domain.Movie;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(schema = "disney", name = "disneyMovie") //table where disney is located
public class DisneyMovie extends Movie {
    private String actors; //different data other databases do not have
    private String director;
    private double rating;

    public DisneyMovie() {
    }

    public DisneyMovie(int id, String title, int release, String genre, String type, String actors, String director, double rating) {
        super(id, title, release, genre, type);
        this.actors = actors;
        this.director = director;
        this.rating = rating;
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

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
