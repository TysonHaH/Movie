package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.adapters.TheaterAdapter;
import com.example.myapplication.models.Movie;
import com.example.myapplication.models.Theater;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class MovieDetailActivity extends AppCompatActivity implements TheaterAdapter.OnTheaterClickListener {

    private ImageView ivBackdrop;
    private TextView tvTitle, tvDesc;
    private RecyclerView rvTheaters;
    private TheaterAdapter adapter;
    private List<Theater> theaterList;
    private FirebaseFirestore db;
    private String movieId;
    private String movieTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        ivBackdrop = findViewById(R.id.ivMovieBackdrop);
        tvTitle = findViewById(R.id.tvDetailTitle);
        tvDesc = findViewById(R.id.tvDetailDesc);
        rvTheaters = findViewById(R.id.rvTheaters);

        movieId = getIntent().getStringExtra("movieId");
        movieTitle = getIntent().getStringExtra("movieTitle");
        String movieDesc = getIntent().getStringExtra("movieDesc");
        String moviePoster = getIntent().getStringExtra("moviePoster");

        tvTitle.setText(movieTitle);
        tvDesc.setText(movieDesc);
        Glide.with(this).load(moviePoster).into(ivBackdrop);

        db = FirebaseFirestore.getInstance();
        theaterList = new ArrayList<>();
        adapter = new TheaterAdapter(theaterList, this);
        rvTheaters.setLayoutManager(new LinearLayoutManager(this));
        rvTheaters.setAdapter(adapter);

        fetchTheaters();
    }

    private void fetchTheaters() {
        // Simple logic: fetch all theaters. 
        // Real app: fetch theaters that have showtimes for this movieId.
        db.collection("theaters")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        theaterList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Theater theater = document.toObject(Theater.class);
                            theater.setTheaterId(document.getId());
                            theaterList.add(theater);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(this, "Error fetching theaters", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onTheaterClick(Theater theater) {
        Intent intent = new Intent(this, ShowtimeActivity.class);
        intent.putExtra("movieId", movieId);
        intent.putExtra("movieTitle", movieTitle);
        intent.putExtra("theaterId", theater.getTheaterId());
        intent.putExtra("theaterName", theater.getName());
        startActivity(intent);
    }
}
