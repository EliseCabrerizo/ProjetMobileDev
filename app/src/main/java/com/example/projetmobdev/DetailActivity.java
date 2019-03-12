package com.example.projetmobdev;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class DetailActivity extends AppCompatActivity{
    private ImageView movieBackdrop;
    private TextView movieTitle,movieGenres,movieOverview,movieOverviewLabel,movieReleaseDate;
    private RatingBar movieRating;
    private LinearLayout movieTrailers;
    private LinearLayout movieReviews;


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        movieBackdrop = findViewById(R.id.thumbnail_image_header);
        movieTitle = findViewById(R.id.movieDetailsTitle);
        movieGenres = findViewById(R.id.movieDetailsGenres);
        movieOverview = findViewById(R.id.movieDetailsOverview);
        movieOverviewLabel = findViewById(R.id.summaryLabel);
        movieReleaseDate = findViewById(R.id.movieDetailsReleaseDate);
        movieRating = findViewById(R.id.movieDetailsRating);
        movieTrailers = findViewById(R.id.movieTrailers);
        movieReviews = findViewById(R.id.movieReviews);



        Intent intentThatStartedThisActivity = getIntent();
        if(intentThatStartedThisActivity.hasExtra("original_title"))
        {
            String thumbnail = getIntent().getExtras().getString("poster_path");
            String movieName = getIntent().getExtras().getString("original_title");
            String synopsis = getIntent().getExtras().getString("overview");

            //double rating = getIntent().getExtras().getDouble("vote_average");
            String rating = getIntent().getExtras().getString("vote_average");
            String dateofRelease = getIntent().getExtras().getString("release_date");
            String movieoverview = getIntent().getExtras().getString("overview");
            Glide.with(this)
                    .load("http://image.tmdb.org/t/p/original"+thumbnail)
                    .into(movieBackdrop);



            movieTitle.setText(movieName);
            movieOverviewLabel.setVisibility(View.VISIBLE);
            movieOverview.setText(synopsis);
            movieRating.setVisibility(View.VISIBLE);
            movieRating.setRating(Float.valueOf(rating)/2);
            movieReleaseDate.setText(dateofRelease);

        }
        else
        {
            Toast.makeText(this,"No API Data",Toast.LENGTH_SHORT).show();
        }
    }

}
