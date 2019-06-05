package com.ramon.tmdb.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ramon.tmdb.R;
import com.ramon.tmdb.activities.DisplayMovieDetails;
import com.ramon.tmdb.models.MovieListed;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.TopViewHolder> {
    public final Context context;
    public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";

    private ArrayList<MovieListed> list;
    private MovieAdapter.OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(MovieListed item);
    }

    public MovieAdapter(Context context) {
        this.list = new ArrayList<MovieListed>();
        this.context = context;
        setListener();
    }

    private void setListener () {
        this.listener = new MovieAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(MovieListed top) {
                Intent intent = new Intent (context.getApplicationContext(), DisplayMovieDetails.class);
                intent.putExtra(EXTRA_MESSAGE, (int) top.getId() + "");
                context.startActivity(intent);
            }
        };
    }

    @Override
    public MovieAdapter.TopViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_news_item, parent, false);

        MovieAdapter.TopViewHolder tvh = new MovieAdapter.TopViewHolder(itemView);

        return tvh;
    }

    @Override
    public void onBindViewHolder(MovieAdapter.TopViewHolder holder, int position) {
        final MovieListed movieTop = list.get(position);

        holder.bind(movieTop, listener);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class TopViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvRating;
        ImageView ivPoster;

        public TopViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tv_news_name);
            tvRating = (TextView) itemView.findViewById(R.id.tv_news_rating);
            ivPoster = (ImageView) itemView.findViewById(R.id.iv_news_poster);
        }

        public void bind (final MovieListed top, final MovieAdapter.OnItemClickListener listener) {
            Picasso.get().load("http://image.tmdb.org/t/p/w200/" + top.getPoster_path()).into(ivPoster);

            tvName.setText(top.getTitle());
            tvRating.setText("Rating: " + top.getVote_average() + "/10");

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(top);
                }
            });
        }
    }

    public void swap(List<MovieListed> newList) {
        list.clear();
        list.addAll(newList);
        notifyDataSetChanged();
    }
}
