package com.ramon.tmdb.activities;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.ramon.tmdb.R;
import com.ramon.tmdb.adapters.MovieNewsAdapter;
import com.ramon.tmdb.adapters.MovieAdapter;
import com.ramon.tmdb.adapters.TVShowAdapter;
import com.ramon.tmdb.adapters.TVShowNewsAdapter;
import com.ramon.tmdb.models.MovieFeed;
import com.ramon.tmdb.models.MovieNewsFeed;
import com.ramon.tmdb.models.TVShowFeed;
import com.ramon.tmdb.models.TVShowNewsFeed;
import com.ramon.tmdb.retrofit.GetMovie;
import com.ramon.tmdb.retrofit.GetTVShow;
import com.ramon.tmdb.retrofit.RetrofitInstance;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListDisplayer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    RecyclerView recyclerView;
    MovieNewsAdapter movieNewsAdapter;
    MovieAdapter movieAdapter;

    TVShowAdapter tvShowAdapter;
    TVShowNewsAdapter tvShowNewsAdapter;

    TextView tvHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_displayer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        tvHeader = (TextView) findViewById(R.id.tv_header);
        tvHeader.setText("Movie News");

        loadMovieNews();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.list_displayer, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(new ComponentName(this, SearchMovie.class)));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        recyclerView.setAdapter(null);

        if (id == R.id.menu_movie_news) {
            tvHeader.setText("Movie News");
            loadMovieNews();
        } else if (id == R.id.menu_tv_news) {
            tvHeader.setText("TVShow News");
            loadTVShowNews();
        } else if (id == R.id.menu_movies_top) {
            tvHeader.setText("Top Rated Movies");
            loadMovieTop();
        } else if (id == R.id.menu_movies_discover) {
            tvHeader.setText("Discover Movies");
            loadMovieDiscover();
        } else if (id == R.id.menu_tv_top) {
            tvHeader.setText("Top Rated TVShows");
            loadTVShowTop();
        } else if (id == R.id.menu_tv_discover) {
            tvHeader.setText("Discover TVShows");
            loadTVShowDiscover();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void loadMovieNews() {
        GetMovie getMovie = RetrofitInstance.getRetrofitInstance().create(GetMovie.class);
        Call<MovieNewsFeed> call = getMovie.getNews(RetrofitInstance.getApiKey());

        call.enqueue(new Callback<MovieNewsFeed>() {
            @Override
            public void onResponse(Call<MovieNewsFeed> call, Response<MovieNewsFeed> response) {
                switch (response.code()) {
                    case 200:
                        MovieNewsFeed data = response.body();

                        movieNewsAdapter = new MovieNewsAdapter(getApplicationContext());

                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ListDisplayer.this);
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setAdapter(movieNewsAdapter);

                        movieNewsAdapter.swap(data.getResults());
                        movieNewsAdapter.notifyDataSetChanged();
                        break;
                    case 401:
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onFailure(Call<MovieNewsFeed> call, Throwable t) {
                Toast.makeText(ListDisplayer.this, "Loading error, try again later", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadMovieTop() {
        GetMovie getMovie = RetrofitInstance.getRetrofitInstance().create(GetMovie.class);
        Call<MovieFeed> call = getMovie.getTop(RetrofitInstance.getApiKey());

        call.enqueue(new Callback<MovieFeed>() {
            @Override
            public void onResponse(Call<MovieFeed> call, Response<MovieFeed> response) {
                switch (response.code()) {
                    case 200:
                        MovieFeed data = response.body();

                        movieAdapter = new MovieAdapter(getApplicationContext());

                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ListDisplayer.this);
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
                Toast.makeText(ListDisplayer.this, "Loading error, try again later", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadMovieDiscover() {
        GetMovie getMovie = RetrofitInstance.getRetrofitInstance().create(GetMovie.class);
        Call<MovieFeed> call = getMovie.getDiscover(RetrofitInstance.getApiKey());

        call.enqueue(new Callback<MovieFeed>() {
            @Override
            public void onResponse(Call<MovieFeed> call, Response<MovieFeed> response) {
                switch (response.code()) {
                    case 200:
                        MovieFeed data = response.body();

                        movieAdapter = new MovieAdapter(getApplicationContext());

                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ListDisplayer.this);
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
                Toast.makeText(ListDisplayer.this, "Loading error, try again later", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadTVShowNews() {
        GetTVShow getTVShow = RetrofitInstance.getRetrofitInstance().create(GetTVShow.class);
        Call<TVShowNewsFeed> call = getTVShow.getNews(RetrofitInstance.getApiKey());

        call.enqueue(new Callback<TVShowNewsFeed>() {
            @Override
            public void onResponse(Call<TVShowNewsFeed> call, Response<TVShowNewsFeed> response) {
                switch (response.code()) {
                    case 200:
                        TVShowNewsFeed data = response.body();

                        tvShowNewsAdapter = new TVShowNewsAdapter(getApplicationContext());

                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ListDisplayer.this);
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setAdapter(tvShowNewsAdapter);

                        tvShowNewsAdapter.swap(data.getResults());
                        tvShowNewsAdapter.notifyDataSetChanged();
                        break;
                    case 401:
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onFailure(Call<TVShowNewsFeed> call, Throwable t) {
                Toast.makeText(ListDisplayer.this, "Loading error, try again later", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadTVShowTop() {
        GetTVShow getTVShow = RetrofitInstance.getRetrofitInstance().create(GetTVShow.class);
        Call<TVShowFeed> call = getTVShow.getTop(RetrofitInstance.getApiKey());

        call.enqueue(new Callback<TVShowFeed>() {
            @Override
            public void onResponse(Call<TVShowFeed> call, Response<TVShowFeed> response) {
                switch (response.code()) {
                    case 200:
                        TVShowFeed data = response.body();

                        tvShowAdapter = new TVShowAdapter(getApplicationContext());

                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ListDisplayer.this);
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setAdapter(tvShowAdapter);

                        tvShowAdapter.swap(data.getResults());
                        tvShowAdapter.notifyDataSetChanged();
                        break;
                    case 401:
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onFailure(Call<TVShowFeed> call, Throwable t) {
                Toast.makeText(ListDisplayer.this, "Loading error, try again later", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadTVShowDiscover() {
        GetTVShow getTVShow = RetrofitInstance.getRetrofitInstance().create(GetTVShow.class);
        Call<TVShowFeed> call = getTVShow.getDiscover(RetrofitInstance.getApiKey());

        call.enqueue(new Callback<TVShowFeed>() {
            @Override
            public void onResponse(Call<TVShowFeed> call, Response<TVShowFeed> response) {
                switch (response.code()) {
                    case 200:
                        TVShowFeed data = response.body();

                        tvShowAdapter = new TVShowAdapter(getApplicationContext());

                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ListDisplayer.this);
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setAdapter(tvShowAdapter);

                        tvShowAdapter.swap(data.getResults());
                        tvShowAdapter.notifyDataSetChanged();
                        break;
                    case 401:
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onFailure(Call<TVShowFeed> call, Throwable t) {
                Toast.makeText(ListDisplayer.this, "Loading error, try again later", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
