package com.example.projetmobdev;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.projetmobdev.model.Movie;


public class DetailActivity extends AppCompatActivity{
    private ImageView movieBackdrop;
    private TextView movieTitle,movieGenres,movieOverview,movieOverviewLabel,movieReleaseDate;
    private RatingBar movieRating;
    private LinearLayout movieTrailers;
    private LinearLayout movieReviews;
    private Button customButton;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        //add back button
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        movieBackdrop = findViewById(R.id.thumbnail_image_header);
        movieTitle = findViewById(R.id.movieDetailsTitle);
        movieGenres = findViewById(R.id.movieDetailsGenres);
        movieOverview = findViewById(R.id.movieDetailsOverview);
        movieOverviewLabel = findViewById(R.id.summaryLabel);
        movieReleaseDate = findViewById(R.id.movieDetailsReleaseDate);
        movieRating = findViewById(R.id.movieDetailsRating);
        movieTrailers = findViewById(R.id.movieTrailers);
        movieReviews = findViewById(R.id.movieReviews);
        customButton = findViewById(R.id.button);


        if(getIntent().getSerializableExtra("like").equals(true))
            customButton.setEnabled(false);
        else
        {
            for(int i=0;i<MainActivity.listUser.size();i++)
            {
                if (getIntent().getSerializableExtra("original_title").equals(MainActivity.listUser.get(i).getOriginalTitle()))
                    customButton.setEnabled(false);
            }
        }
        Intent intentThatStartedThisActivity = getIntent();
        if(intentThatStartedThisActivity.hasExtra("original_title"))
        {
            String thumbnail = getIntent().getExtras().getString("poster_path");
            String movieName = getIntent().getExtras().getString("original_title");
            String synopsis = getIntent().getExtras().getString("overview");

            String rating = getIntent().getExtras().getString("vote_average");
            String dateofRelease = getIntent().getExtras().getString("release_date");
            Glide.with(this)
                    .load("http://image.tmdb.org/t/p/original"+thumbnail)
                    .into(movieBackdrop);



            movieTitle.setText(movieName);
            movieOverviewLabel.setVisibility(View.VISIBLE);
            movieOverview.setText(synopsis);
            movieRating.setVisibility(View.VISIBLE);
            movieRating.setRating(Float.valueOf(rating)/2);
            movieReleaseDate.setText(dateofRelease);

            customButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String temp = getIntent().getSerializableExtra("like").toString();
                    if(getIntent().getSerializableExtra("like").equals(false))
                    {
                        MainActivity.listUser.add((Movie) getIntent().getSerializableExtra("movie"));
                        int j =MainActivity.listUser.indexOf((Movie) getIntent().getSerializableExtra("movie"));
                        MainActivity.listUser.get(j).setLike(true);
                        customButton.setEnabled(false);
                    }
                    else if(MainActivity.listUser.contains((Movie) getIntent().getSerializableExtra("movie")))
                    {
                        MainActivity.listUser.remove((Movie) getIntent().getSerializableExtra("movie"));
                        customButton.setEnabled(true);
                    }
                }
            });
        }
        else
        {
            Toast.makeText(this,"No API Data",Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id=item.getItemId();
        if(id==android.R.id.home)
            this.finish();
        return super.onOptionsItemSelected(item);
    }
}
