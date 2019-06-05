package com.ramon.tmdb.activities;

import android.app.SearchManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.ramon.tmdb.R;
import com.ramon.tmdb.adapters.MovieAdapter;
import com.ramon.tmdb.models.MovieFeed;
import com.ramon.tmdb.retrofit.GetMovie;
import com.ramon.tmdb.retrofit.RetrofitInstance;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchMovie extends AppCompatActivity {

    RecyclerView recyclerView;
    MovieAdapter movieAdapter;

    String query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_movie);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        handleIntent(getIntent());

        loadSearch();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            query = intent.getStringExtra(SearchManager.QUERY);

            System.out.println(query);
        }
    }

    public void loadSearch () {
        GetMovie getMovie = RetrofitInstance.getRetrofitInstance().create(GetMovie.class);
        Call<MovieFeed> call = getMovie.getSearch(RetrofitInstance.getApiKey(), "en-US", 1, true, query);

        call.enqueue(new Callback<MovieFeed>() {
            @Override
            public void onResponse(Call<MovieFeed> call, Response<MovieFeed> response) {
                switch (response.code()) {
                    case 200:
                        MovieFeed data = response.body();

                        movieAdapter = new MovieAdapter(getApplicationContext());

                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(SearchMovie.this);
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setAdapter(movieAdapter);

                        movieAdapter.swap(data.getResults());
                        movieAdapter.notifyDataSetChanged();
                        break;
                    case 401:
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onFailure(Call<MovieFeed> call, Throwable t) {
                Toast.makeText(SearchMovie.this, "Loading error, try again later", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
