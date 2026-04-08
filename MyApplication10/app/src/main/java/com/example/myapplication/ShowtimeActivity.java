package com.example.myapplication;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.example.myapplication.adapters.ShowtimeAdapter;
import com.example.myapplication.models.Showtime;
import com.example.myapplication.models.Ticket;
import com.example.myapplication.utils.MovieNotificationWorker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ShowtimeActivity extends AppCompatActivity implements ShowtimeAdapter.OnShowtimeClickListener {

    private TextView tvMovieHeader, tvTheaterHeader;
    private RecyclerView rvShowtimes;
    private ShowtimeAdapter adapter;
    private List<Showtime> showtimeList;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private String movieId, theaterId, movieTitle;
    private FloatingActionButton fabTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showtime);

        tvMovieHeader = findViewById(R.id.tvShowtimeHeader);
        tvTheaterHeader = findViewById(R.id.tvTheaterHeader);
        rvShowtimes = findViewById(R.id.rvShowtimes);
        fabTest = findViewById(R.id.fabTestNotification);

        movieId = getIntent().getStringExtra("movieId");
        theaterId = getIntent().getStringExtra("theaterId");
        movieTitle = getIntent().getStringExtra("movieTitle");
        String theaterName = getIntent().getStringExtra("theaterName");

        tvMovieHeader.setText(movieTitle);
        tvTheaterHeader.setText(theaterName);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        showtimeList = new ArrayList<>();
        adapter = new ShowtimeAdapter(showtimeList, this);
        rvShowtimes.setLayoutManager(new LinearLayoutManager(this));
        rvShowtimes.setAdapter(adapter);

        fetchShowtimes();

        fabTest.setOnClickListener(v -> {
            checkPermissionAndTest();
        });
    }

    private void checkPermissionAndTest() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);
                return;
            }
        }
        
        Toast.makeText(this, "Test Notification in 5 seconds...", Toast.LENGTH_SHORT).show();
        scheduleTestNotification(movieTitle);
    }

    private void scheduleTestNotification(String title) {
        Data inputData = new Data.Builder()
                .putString("movieTitle", title)
                .build();

        OneTimeWorkRequest notificationWork = new OneTimeWorkRequest.Builder(MovieNotificationWorker.class)
                .setInitialDelay(5, TimeUnit.SECONDS)
                .setInputData(inputData)
                .build();

        WorkManager.getInstance(this).enqueue(notificationWork);
    }

    private void fetchShowtimes() {
        db.collection("showtimes")
                .whereEqualTo("movieId", movieId)
                .whereEqualTo("theaterId", theaterId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        showtimeList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Showtime showtime = document.toObject(Showtime.class);
                            showtime.setShowtimeId(document.getId());
                            showtimeList.add(showtime);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(this, "Error fetching showtimes", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onBookClick(Showtime showtime) {
        if (showtime.getAvailableSeats() == null || showtime.getAvailableSeats().isEmpty()) {
            Toast.makeText(this, "No seats available!", Toast.LENGTH_SHORT).show();
            return;
        }

        String seatToBook = showtime.getAvailableSeats().get(0);
        String userId = mAuth.getCurrentUser().getUid();

        db.collection("showtimes").document(showtime.getShowtimeId())
                .update("availableSeats", FieldValue.arrayRemove(seatToBook))
                .addOnSuccessListener(aVoid -> {
                    Ticket ticket = new Ticket(
                            null,
                            userId,
                            showtime.getShowtimeId(),
                            Arrays.asList(seatToBook),
                            System.currentTimeMillis()
                    );

                    db.collection("tickets").add(ticket)
                            .addOnSuccessListener(documentReference -> {
                                Toast.makeText(this, "Booking successful!", Toast.LENGTH_SHORT).show();
                                scheduleNotification(showtime.getStartTime(), movieTitle);
                                fetchShowtimes();
                            })
                            .addOnFailureListener(e -> Toast.makeText(this, "Error creating ticket", Toast.LENGTH_SHORT).show());
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error updating seats", Toast.LENGTH_SHORT).show());
    }

    private void scheduleNotification(long startTime, String title) {
        long delay = startTime - System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(30);
        if (delay > 0) {
            Data inputData = new Data.Builder()
                    .putString("movieTitle", title)
                    .build();

            OneTimeWorkRequest notificationWork = new OneTimeWorkRequest.Builder(MovieNotificationWorker.class)
                    .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                    .setInputData(inputData)
                    .build();

            WorkManager.getInstance(this).enqueue(notificationWork);
        }
    }
}
