package com.example.projetmobdev;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.projetmobdev.adapter.MoviesAdapter;
import com.example.projetmobdev.api.Service;
import com.example.projetmobdev.model.Movie;
import com.example.projetmobdev.model.MoviesResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String BASE_URL="http://api.themoviedb.org/3/";
    private RecyclerView recyclerView;
    private MoviesAdapter adapter;
    private List<Movie> movieList;
    ProgressDialog pd;
    private SwipeRefreshLayout swipeContainer;
    private static final String LANGUAGE="fr-FR";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        swipeContainer= (SwipeRefreshLayout) findViewById(R.id.main_content);
        swipeContainer.setColorSchemeResources(android.R.color.holo_orange_dark);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initViews();
                Toast.makeText(MainActivity.this,"Movies Refreshed",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public Activity getActivity()
    {
        Context context=this;
        while(context instanceof ContextWrapper)
        {
            if(context instanceof Activity)
            {
                return (Activity) context;
            }
            context=((ContextWrapper) context).getBaseContext();
        }
        return null;
    }
    private void initViews()
    {
        pd = new ProgressDialog(this);
        pd.setMessage("Fetching movies...");
        pd.setCancelable(false);
        pd.show();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        recyclerView= (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(layoutManager);
        movieList=new ArrayList<>();
        adapter=new MoviesAdapter(this,movieList);

         /*if(getActivity().getResources().getConfiguration().orientation== Configuration.ORIENTATION_PORTRAIT)
         {
             recyclerView.setLayoutManager(new GridLayoutManager(this,1));
         }
         else
         {
             recyclerView.setLayoutManager(new GridLayoutManager(this,4));
         }*/
         recyclerView.setItemAnimator(new DefaultItemAnimator());
         recyclerView.setAdapter(adapter);
         adapter.notifyDataSetChanged();

         loadJSON();
    }

    private void loadJSON()

    {
        try{
            if(BuildConfig.THE_MOVIE_DB_API_TOKEN.isEmpty()){
                Toast.makeText(getApplicationContext(),"Please obtain API key firstly from themoviedb.org",Toast.LENGTH_SHORT).show();
                pd.dismiss();
                return;
            }


            Retrofit retrofit = retrofit=new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            Service apiInterface = retrofit.create(Service.class);
            Call<MoviesResponse> call = apiInterface.getPopularMovies(BuildConfig.THE_MOVIE_DB_API_TOKEN,LANGUAGE,1);
            call.enqueue(new Callback<MoviesResponse>() {
                @Override
                public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                    List<Movie> movies = response.body().getResults();
                    recyclerView.setAdapter(new MoviesAdapter(getApplicationContext(),movies));
                    recyclerView.smoothScrollToPosition(0);

                    if(swipeContainer.isRefreshing())
                    {
                        swipeContainer.setRefreshing(false);
                    }
                    pd.dismiss();
                }

                @Override
                public void onFailure(Call<MoviesResponse> call, Throwable t) {
                    Log.e("Error","", t);
                    Toast.makeText(MainActivity.this,"Error Fetching Data ! ",Toast.LENGTH_SHORT).show();
                }
            });
        }
        catch (Exception e)
        {
            Log.e("Error","",e);
            Toast.makeText(this,e.toString(),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public  boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return  true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_search:
                return true;
            default :
                return super.onOptionsItemSelected(item);
        }
    }
}
