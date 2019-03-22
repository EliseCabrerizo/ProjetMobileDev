package com.example.projetmobdev.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projetmobdev.BuildConfig;
import com.example.projetmobdev.MainActivity;
import com.example.projetmobdev.R;
import com.example.projetmobdev.api.Service;
import com.example.projetmobdev.model.Movie;
import com.example.projetmobdev.model.MoviesResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ParentAdapter extends RecyclerView.Adapter<ParentAdapter.MyViewHolder> {
        private Context mContext;
        private List<String> movieList;
        private Service apiInterface;

    private static final String LANGUAGE = "fr-FR";

        TextView titleSection;
        public ParentAdapter(Context mContext, List<String> movieList)
        {
        this.mContext=mContext;
        this.movieList=movieList;
        }


@Override
public ParentAdapter.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(MainActivity.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            apiInterface = retrofit.create(Service.class);

            View view= LayoutInflater.from(viewGroup.getContext())
        .inflate(R.layout.parent_recycler,viewGroup,false);
        return new MyViewHolder(view);
        }

@Override
public void onBindViewHolder(final ParentAdapter.MyViewHolder viewHolder, int i){

            String type = movieList.get(i);
            titleSection.setText(type);

    Call<MoviesResponse> call;
    LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
    viewHolder.recyclerView.setLayoutManager(layoutManager);
    viewHolder.recyclerView.setItemAnimator(new DefaultItemAnimator());
    if(type.equals("POPULAR"))
        call = apiInterface.getPopularMovies(BuildConfig.THE_MOVIE_DB_API_TOKEN, LANGUAGE, 1);
    else if(type.equals("UPCOMMING"))
        call = apiInterface.getUpComingMovies(BuildConfig.THE_MOVIE_DB_API_TOKEN, LANGUAGE, 1);
    else if (type.equals("TOP RATED"))
        call = apiInterface.getTopRatedMovies(BuildConfig.THE_MOVIE_DB_API_TOKEN, LANGUAGE, 1);
    else
        call = apiInterface.getPlayingMovies(BuildConfig.THE_MOVIE_DB_API_TOKEN, LANGUAGE, 1);
    call.enqueue(new Callback<MoviesResponse>() {
        @Override
        public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
            if (response.isSuccessful()) {
                List<Movie> movies = response.body().getResults();
                viewHolder.recyclerView.setAdapter(new MoviesAdapter(mContext, movies));
                viewHolder.recyclerView.smoothScrollToPosition(0);
            }
            else {
                Log.e("onResponse", response.message());
            }
        }

        @Override
        public void onFailure(Call<MoviesResponse> call, Throwable t) {
            Log.e("Error", t.getMessage(), t);
            Toast.makeText(mContext, "Error Fetching Data ! ", Toast.LENGTH_SHORT).show();
        }
    });



        }

@Override
public int getItemCount(){
        return movieList.size();
        }

public class MyViewHolder extends RecyclerView.ViewHolder{

    RecyclerView recyclerView;
    public MyViewHolder(View view)
    {
        super(view);
        titleSection = view.findViewById(R.id.titleSection);
        recyclerView= view.findViewById(R.id.rv_child);

    }
    public void bind(String type)
    {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        Call<MoviesResponse> call;
        if(type.equals("POPULAR"))
            call = apiInterface.getPopularMovies(BuildConfig.THE_MOVIE_DB_API_TOKEN, LANGUAGE, 1);
        else if(type.equals("UPCOMMING"))
            call = apiInterface.getUpComingMovies(BuildConfig.THE_MOVIE_DB_API_TOKEN, LANGUAGE, 1);
        else if (type.equals("TOP RATED"))
            call = apiInterface.getTopRatedMovies(BuildConfig.THE_MOVIE_DB_API_TOKEN, LANGUAGE, 1);
        else
            call = apiInterface.getPlayingMovies(BuildConfig.THE_MOVIE_DB_API_TOKEN, LANGUAGE, 1);
        call.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                if (response.isSuccessful()) {
                    List<Movie> movies = response.body().getResults();


                    recyclerView.setAdapter(new MoviesAdapter(mContext, movies));
                    recyclerView.smoothScrollToPosition(0);
                }
                else {
                    Log.e("onResponse", response.message());
                }
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                Log.e("Error", t.getMessage(), t);
                Toast.makeText(mContext, "Error Fetching Data ! ", Toast.LENGTH_SHORT).show();
            }
        });


    }



}
}
