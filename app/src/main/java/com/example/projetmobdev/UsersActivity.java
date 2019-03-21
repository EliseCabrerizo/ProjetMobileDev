package com.example.projetmobdev;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.projetmobdev.adapter.MoviesAdapter;
import com.example.projetmobdev.adapter.MoviesAdapterSearch;
import com.example.projetmobdev.api.Service;
import com.example.projetmobdev.model.Movie;
import com.example.projetmobdev.model.MoviesResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UsersActivity extends AppCompatActivity {

    private static final String BASE_URL = "http://api.themoviedb.org/3/";
    private RecyclerView recyclerView;
    ProgressDialog pd;
    private SwipeRefreshLayout swipeContainer;
    private static final String LANGUAGE = "fr-FR";
    private Service apiInterface;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pd = new ProgressDialog(this);
        pd.setMessage("Fetching movies...");
        pd.setCancelable(false);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.main_content);
        swipeContainer.setColorSchemeResources(android.R.color.holo_orange_dark);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initViews();
                Toast.makeText(UsersActivity.this, "Movies Refreshed", Toast.LENGTH_SHORT).show();
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(new GridLayoutManager(this,3));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiInterface = retrofit.create(Service.class);
        initViews();
    }

    private void initViews() {
        recyclerView.setAdapter(new MoviesAdapter(getApplicationContext(), MainActivity.listUser));
        recyclerView.smoothScrollToPosition(0);
        //pd.show();
        //loadJSON();
    }

    private void loadJSON() {
        try {
            if (BuildConfig.THE_MOVIE_DB_API_TOKEN.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please obtain API key firstly from themoviedb.org", Toast.LENGTH_SHORT).show();
                pd.dismiss();
                return;
            }
            Call<MoviesResponse> call = apiInterface.getPopularMovies(BuildConfig.THE_MOVIE_DB_API_TOKEN, LANGUAGE, 1);
            call.enqueue(new Callback<MoviesResponse>() {
                @Override
                public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                    if (response.isSuccessful()) {
                        List<Movie> movies = response.body().getResults();
                            recyclerView.setAdapter(new MoviesAdapter(getApplicationContext(), MainActivity.listUser));

                        recyclerView.smoothScrollToPosition(0);

                        if (swipeContainer.isRefreshing()) {
                            swipeContainer.setRefreshing(false);
                        }
                        pd.dismiss();
                    }
                    else {
                        Log.e("onResponse", response.message());
                    }
                }


                @Override
                public void onFailure(Call<MoviesResponse> call, Throwable t) {
                    Log.e("Error", t.getMessage(), t);
                    Toast.makeText(UsersActivity.this, "Error Fetching Data ! ", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Log.e("Error", "", e);
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }


}
