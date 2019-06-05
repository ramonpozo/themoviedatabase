package com.ramon.tmdb.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ramon.tmdb.R;
import com.ramon.tmdb.models.CreditsListed;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CreditsAdapter extends RecyclerView.Adapter<CreditsAdapter.CreditsViewHolder> {
    public final Context context;
    public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";

    private ArrayList<CreditsListed> list;
    private CreditsAdapter.OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(CreditsListed item);
    }

    public CreditsAdapter(Context context) {
        this.list = new ArrayList<CreditsListed>();
        this.context = context;
        setListener();
    }

    private void setListener () {
        this.listener = new CreditsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(CreditsListed top) {
                //Intent intent = new Intent (context.getApplicationContext(), DisplayMovieDetails.class);
                //intent.putExtra(EXTRA_MESSAGE, (int) top.getId() + "");
                //context.startActivity(intent);
            }
        };
    }

    @Override
    public CreditsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_credits_item, parent, false);

        CreditsViewHolder tvh = new CreditsViewHolder(itemView);

        return tvh;
    }

    @Override
    public void onBindViewHolder(CreditsViewHolder holder, int position) {
        final CreditsListed movieTop = list.get(position);

        holder.bind(movieTop, listener);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class CreditsViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvCharacter;
        ImageView ivPoster;

        public CreditsViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tv_credits_name);
            tvCharacter = (TextView) itemView.findViewById(R.id.tv_credits_character);
            ivPoster = (ImageView) itemView.findViewById(R.id.iv_credits_profile);
        }

        public void bind (final CreditsListed cast, final CreditsAdapter.OnItemClickListener listener) {
            Picasso.get().load("http://image.tmdb.org/t/p/w200/" + cast.getProfile_path()).into(ivPoster);

            tvName.setText(cast.getName());
            tvCharacter.setText(cast.getCharacter());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(cast);
                }
            });
        }
    }

    public void swap(List<CreditsListed> newList) {
        list.clear();
        list.addAll(newList);
        notifyDataSetChanged();
    }
}
