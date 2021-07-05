package com.restful.api.domain.amazon;

import com.restful.api.domain.Movie;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(schema = "amazon", name = "amazonMovie") //Find the table
public class AmazonMovie extends Movie {

    private double rating; //what the table has different from the others

    public AmazonMovie() {
    }

    public AmazonMovie(int id, String title, int release, String genre, String type, double rating) {
        super(id, title, release, genre, type);
        this.rating = rating;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
