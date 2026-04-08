package com.example.myapplication.utils;

import com.example.myapplication.models.Movie;
import com.example.myapplication.models.Showtime;
import com.example.myapplication.models.Theater;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.List;

public class DataSeeder {

    public static void seedData(FirebaseFirestore db) {
        // 1. Seed Movies
        Movie m1 = new Movie("m1", "Avengers: Endgame", "The grave course of events set in motion by Thanos...", "https://image.tmdb.org/t/p/w500/or06vSqzWkaGvHmyZ0pTvxS9ZpX.jpg", 181, 4.8f);
        Movie m2 = new Movie("m2", "Spider-Man: No Way Home", "With Spider-Man's identity now revealed...", "https://image.tmdb.org/t/p/w500/1g0mXp9NRUuhp0v77TcyfyOpO3X.jpg", 148, 4.7f);
        Movie m3 = new Movie("m3", "Inception", "A thief who steals corporate secrets through the use of dream-sharing technology...", "https://image.tmdb.org/t/p/w500/edv5bs1pSdfLqbSCcLX9no9H6Ls.jpg", 148, 4.9f);

        db.collection("movies").document(m1.getMovieId()).set(m1);
        db.collection("movies").document(m2.getMovieId()).set(m2);
        db.collection("movies").document(m3.getMovieId()).set(m3);

        // 2. Seed Theaters
        Theater t1 = new Theater("t1", "CGV Vincom Center", "72 Le Thanh Ton, Dist 1, HCMC");
        Theater t2 = new Theater("t2", "Lotte Cinema Cantavil", "Cantavil Premier, Dist 2, HCMC");

        db.collection("theaters").document(t1.getTheaterId()).set(t1);
        db.collection("theaters").document(t2.getTheaterId()).set(t2);

        // 3. Seed Showtimes (Thêm nhiều suất chiếu hơn cho cả 2 rạp)
        List<String> seats = Arrays.asList("A1", "A2", "A3", "B1", "B2", "B3", "C1", "C2");
        long now = System.currentTimeMillis();

        // Suất chiếu cho Avengers (m1) ở cả 2 rạp
        db.collection("showtimes").document("s1").set(new Showtime("s1", "m1", "t1", now + 3600000, seats));
        db.collection("showtimes").document("s2").set(new Showtime("s2", "m1", "t2", now + 5400000, seats)); // Thêm cho Lotte

        // Suất chiếu cho Spider-Man (m2) ở cả 2 rạp
        db.collection("showtimes").document("s3").set(new Showtime("s3", "m2", "t1", now + 7200000, seats));
        db.collection("showtimes").document("s4").set(new Showtime("s4", "m2", "t2", now + 3600000, seats));

        // Suất chiếu cho Inception (m3)
        db.collection("showtimes").document("s5").set(new Showtime("s5", "m3", "t1", now + 9000000, seats));
        db.collection("showtimes").document("s6").set(new Showtime("s6", "m3", "t2", now + 10800000, seats));
    }
}
