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
import com.ramon.tmdb.models.MovieNewsListed;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MovieNewsAdapter extends RecyclerView.Adapter<MovieNewsAdapter.NewsViewHolder> {
    public final Context context;
    public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";

    private ArrayList<MovieNewsListed> list;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(MovieNewsListed item);
    }

    public MovieNewsAdapter(Context context) {
        this.list = new ArrayList<MovieNewsListed>();
        this.context = context;
        setListener();
    }

    private void setListener () {
        this.listener = new MovieNewsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(MovieNewsListed news) {
                Intent intent = new Intent (context.getApplicationContext(), DisplayMovieDetails.class);
                intent.putExtra(EXTRA_MESSAGE, (int) news.getId() + "");
                context.startActivity(intent);
            }
        };
    }
    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_news_item, parent, false);

        NewsViewHolder nvh = new NewsViewHolder(itemView);

        return nvh;
    }

    @Override
    public void onBindViewHolder(NewsViewHolder holder, int position) {
        final MovieNewsListed movieNewsListed = list.get(position);

        holder.bind(movieNewsListed, listener);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class NewsViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        ImageView ivPoster;

        public NewsViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tv_news_name);
            ivPoster = (ImageView) itemView.findViewById(R.id.iv_news_poster);
        }

        public void bind (final MovieNewsListed news, final OnItemClickListener listener) {
            Picasso.get().load("http://image.tmdb.org/t/p/w200/" + news.getPoster_path()).into(ivPoster);

            tvName.setText(news.getTitle());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(news);
                }
            });
        }
    }

    public void swap(List<MovieNewsListed> newList) {
        list.clear();
        list.addAll(newList);
        notifyDataSetChanged();
    }
}
