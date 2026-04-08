package com.example.myapplication.models;

public class Movie {
    private String movieId;
    private String title;
    private String description;
    private String posterUrl;
    private int duration;
    private float rating;

    public Movie() {}

    public Movie(String movieId, String title, String description, String posterUrl, int duration, float rating) {
        this.movieId = movieId;
        this.title = title;
        this.description = description;
        this.posterUrl = posterUrl;
        this.duration = duration;
        this.rating = rating;
    }

    public String getMovieId() { return movieId; }
    public void setMovieId(String movieId) { this.movieId = movieId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getPosterUrl() { return posterUrl; }
    public void setPosterUrl(String posterUrl) { this.posterUrl = posterUrl; }
    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }
    public float getRating() { return rating; }
    public void setRating(float rating) { this.rating = rating; }
}
