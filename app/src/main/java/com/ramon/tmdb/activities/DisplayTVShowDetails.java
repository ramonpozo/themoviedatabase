package com.ramon.tmdb.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ramon.tmdb.R;
import com.ramon.tmdb.adapters.CreditsAdapter;
import com.ramon.tmdb.models.CreditsFeed;
import com.ramon.tmdb.models.TVShowDetail;
import com.ramon.tmdb.retrofit.GetTVShow;
import com.ramon.tmdb.retrofit.RetrofitInstance;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DisplayTVShowDetails extends AppCompatActivity {
    private int id;

    ImageView ivPoster;
    TextView tvName;
    TextView tvDuration;
    TextView tvGenres;
    TextView tvRate;
    TextView tvOverview;

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_movie_details);

        Intent intent = getIntent();
        id = Integer.parseInt(intent.getStringExtra(ListDisplayer.EXTRA_MESSAGE));

        System.out.println(id + "------------------------------------------");

        ivPoster = (ImageView) findViewById(R.id.iv_movie_poster);
        tvName = (TextView) findViewById(R.id.tv_movie_name);
        tvDuration = (TextView) findViewById(R.id.tv_movie_duration);
        tvGenres = (TextView) findViewById(R.id.tv_movie_genres);
        tvRate = (TextView) findViewById(R.id.tv_movie_rate);
        tvOverview = (TextView) findViewById(R.id.tv_movie_overview);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_credits);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        loadJSON();
        loadCredits();
    }

    private void loadJSON() {
        GetTVShow getTVShow = RetrofitInstance.getRetrofitInstance().create(GetTVShow.class);
        Call<TVShowDetail> call = getTVShow.getTVShow(id, RetrofitInstance.getApiKey());

        call.enqueue(new Callback<TVShowDetail>() {
            @Override
            public void onResponse(Call<TVShowDetail> call, Response<TVShowDetail> response) {
                switch (response.code()) {
                    case 200:
                        TVShowDetail data = response.body();

                        String genres = "";

                        for (Object g: data.getGenres()) {
                            String[] resultSplit = g.toString().split("=");

                            genres += resultSplit[2].replace("}", "    ");
                        }

                        tvName.setText(data.getName());
                        tvDuration.setText("First emission: " + data.getFirst_air_date());
                        tvRate.setText("Rating: " + data.getVote_average() + "/10");
                        tvGenres.setText(genres);
                        tvOverview.setText(data.getOverview());
                        Picasso.get().load("http://image.tmdb.org/t/p/w500/" + data.getPoster_path()).into(ivPoster);

                        break;
                    case 401:
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onFailure(Call<TVShowDetail> call, Throwable t) {
                Toast.makeText(DisplayTVShowDetails.this, "Loading error, try again later", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadCredits() {
        GetTVShow getTVShow = RetrofitInstance.getRetrofitInstance().create(GetTVShow.class);
        Call<CreditsFeed> call = getTVShow.getCredits(id, RetrofitInstance.getApiKey());

        call.enqueue(new Callback<CreditsFeed>() {
            @Override
            public void onResponse(Call<CreditsFeed> call, Response<CreditsFeed> response) {
                switch (response.code()) {
                    case 200:
                        CreditsFeed data = response.body();

                        CreditsAdapter creditsAdapter = new CreditsAdapter(getApplicationContext());

                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(DisplayTVShowDetails.this);
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setAdapter(creditsAdapter);

                        creditsAdapter.swap(data.getCast());
                        creditsAdapter.notifyDataSetChanged();
                        break;
                    case 401:
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onFailure(Call<CreditsFeed> call, Throwable t) {
                Toast.makeText(DisplayTVShowDetails.this, "Loading error, try again later", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
