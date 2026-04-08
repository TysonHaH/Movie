package com.example.myapplication.models;

import java.util.List;

public class Showtime {
    private String showtimeId;
    private String movieId;
    private String theaterId;
    private long startTime; // Timestamp
    private List<String> availableSeats;

    public Showtime() {}

    public Showtime(String showtimeId, String movieId, String theaterId, long startTime, List<String> availableSeats) {
        this.showtimeId = showtimeId;
        this.movieId = movieId;
        this.theaterId = theaterId;
        this.startTime = startTime;
        this.availableSeats = availableSeats;
    }

    public String getShowtimeId() { return showtimeId; }
    public void setShowtimeId(String showtimeId) { this.showtimeId = showtimeId; }
    public String getMovieId() { return movieId; }
    public void setMovieId(String movieId) { this.movieId = movieId; }
    public String getTheaterId() { return theaterId; }
    public void setTheaterId(String theaterId) { this.theaterId = theaterId; }
    public long getStartTime() { return startTime; }
    public void setStartTime(long startTime) { this.startTime = startTime; }
    public List<String> getAvailableSeats() { return availableSeats; }
    public void setAvailableSeats(List<String> availableSeats) { this.availableSeats = availableSeats; }
}
