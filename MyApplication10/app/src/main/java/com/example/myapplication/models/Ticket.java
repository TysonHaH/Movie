package com.example.myapplication.models;

import java.util.List;

public class Ticket {
    private String ticketId;
    private String userId;
    private String showtimeId;
    private List<String> seatNumbers;
    private long bookingTime;

    public Ticket() {}

    public Ticket(String ticketId, String userId, String showtimeId, List<String> seatNumbers, long bookingTime) {
        this.ticketId = ticketId;
        this.userId = userId;
        this.showtimeId = showtimeId;
        this.seatNumbers = seatNumbers;
        this.bookingTime = bookingTime;
    }

    public String getTicketId() { return ticketId; }
    public void setTicketId(String ticketId) { this.ticketId = ticketId; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getShowtimeId() { return showtimeId; }
    public void setShowtimeId(String showtimeId) { this.showtimeId = showtimeId; }
    public List<String> getSeatNumbers() { return seatNumbers; }
    public void setSeatNumbers(List<String> seatNumbers) { this.seatNumbers = seatNumbers; }
    public long getBookingTime() { return bookingTime; }
    public void setBookingTime(long bookingTime) { this.bookingTime = bookingTime; }
}
