package com.example.projetmobdev;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.projetmobdev.adapter.MoviesAdapterSearch;
import com.example.projetmobdev.adapter.ParentAdapter;
import com.example.projetmobdev.api.Service;
import com.example.projetmobdev.model.Movie;
import com.example.projetmobdev.model.MoviesResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, SearchView.OnCloseListener {

    public static final String BASE_URL = "http://api.themoviedb.org/3/";
    private RecyclerView recyclerView;
    private SearchView searchView;
    public static ProgressDialog pd;
    private SwipeRefreshLayout swipeContainer;
    private static final String LANGUAGE = "fr-FR";
    private Service apiInterface;
    public static List<Movie> listUser  = new ArrayList<Movie>();

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
                Toast.makeText(MainActivity.this, "Movies Refreshed", Toast.LENGTH_SHORT).show();
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiInterface = retrofit.create(Service.class);
        initViews();
    }

    private void initViews() {
        pd.show();
        loadJSON("popular", "");
    }

    private void loadJSON(final String theme, String query) {
        try {
            if (BuildConfig.THE_MOVIE_DB_API_TOKEN.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please obtain API key firstly from themoviedb.org", Toast.LENGTH_SHORT).show();
                pd.dismiss();
                return;
            }

            if (theme.equals("popular"))
            {
                String[] tab = {"POPULAR","TOP RATED","UPCOMMING","NOW PLAYING"};
                List<String> moviesName = Arrays.asList(tab);
                recyclerView.setAdapter(new ParentAdapter(getApplicationContext(), moviesName));
                recyclerView.smoothScrollToPosition(0);
                if (swipeContainer.isRefreshing()) {
                    swipeContainer.setRefreshing(false);
                }
                pd.dismiss();
            }

            else
            {
                Call<MoviesResponse> call= apiInterface.getSearchMovie(BuildConfig.THE_MOVIE_DB_API_TOKEN, LANGUAGE, query, 1);
                    call.enqueue(new Callback<MoviesResponse>() {
                @Override
                public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                    if (response.isSuccessful()) {
                        List<Movie> movies = response.body().getResults();


                            recyclerView.setAdapter(new MoviesAdapterSearch(getApplicationContext(), movies));
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
                    Toast.makeText(MainActivity.this, "Error Fetching Data ! ", Toast.LENGTH_SHORT).show();
                }
            });
            }
        } catch (Exception e) {
            Log.e("Error", "", e);
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) menuItem.getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(this);
        searchView.setOnCloseListener(this);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if(!query.equals(""))
        {
            loadJSON("search", query);
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        loadJSON("search", query);
        return false;
    }


    @Override
    public boolean onClose() {
        searchView.onActionViewCollapsed();
        loadJSON("popular", "");
        return true;
    }
    @Override

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_favorite) {
            Intent myIntent = new Intent(MainActivity.this, UsersActivity.class);
            this.startActivity(myIntent);
        }

        return super.onOptionsItemSelected(item);
    }
}
