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
import com.ramon.tmdb.activities.DisplayTVShowDetails;
import com.ramon.tmdb.models.TVShowNewsListed;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class TVShowNewsAdapter extends RecyclerView.Adapter<TVShowNewsAdapter.TVShowNewsViewHolder> {
    public final Context context;
    public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";

    private ArrayList<TVShowNewsListed> list;
    private TVShowNewsAdapter.OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(TVShowNewsListed item);
    }

    public TVShowNewsAdapter(Context context) {
        this.list = new ArrayList<TVShowNewsListed>();
        this.context = context;
        setListener();
    }

    private void setListener () {
        this.listener = new TVShowNewsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(TVShowNewsListed tv) {
                Intent intent = new Intent (context.getApplicationContext(), DisplayTVShowDetails.class);
                intent.putExtra(EXTRA_MESSAGE, (int) tv.getId() + "");
                context.startActivity(intent);
            }
        };
    }

    @Override
    public TVShowNewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_news_item, parent, false);

        TVShowNewsViewHolder tvh = new TVShowNewsViewHolder(itemView);

        return tvh;
    }

    @Override
    public void onBindViewHolder(TVShowNewsViewHolder holder, int position) {
        final TVShowNewsListed tv = list.get(position);

        holder.bind(tv, listener);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class TVShowNewsViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvRating;
        ImageView ivPoster;

        public TVShowNewsViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tv_news_name);
            tvRating = (TextView) itemView.findViewById(R.id.tv_news_rating);
            ivPoster = (ImageView) itemView.findViewById(R.id.iv_news_poster);
        }

        public void bind (final TVShowNewsListed top, final TVShowNewsAdapter.OnItemClickListener listener) {
            Picasso.get().load("http://image.tmdb.org/t/p/w200/" + top.getPoster_path()).into(ivPoster);

            tvName.setText(top.getName());
            tvRating.setText("Rating: " + top.getVote_average() + "/10");

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(top);
                }
            });
        }
    }

    public void swap(List<TVShowNewsListed> newList) {
        list.clear();
        list.addAll(newList);
        notifyDataSetChanged();
    }
}
