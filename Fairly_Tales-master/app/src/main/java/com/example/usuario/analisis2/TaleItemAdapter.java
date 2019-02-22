package com.example.usuario.analisis2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TaleItemAdapter extends RecyclerView.Adapter<TaleItemAdapter.TaleViewHolder> {
    private ArrayList<Tales> tales;
    private Context context;

    TaleItemAdapter(ArrayList<Tales> tales, Context context) {
        this.tales = tales;
        this.context = context;
    }


    @NonNull
    @Override
    public TaleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View row = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_row, viewGroup, false);
        return new TaleViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull TaleViewHolder taleViewHolder, @SuppressLint("RecyclerView") final int i) {
        Tales tale = tales.get(i);
        Picasso.with(context).load(tale.getLogo()).fit().placeholder(R.drawable.loading).error(R.drawable.error).into(taleViewHolder.logo);
        taleViewHolder.synopsis.setText(tale.getResume());
        taleViewHolder.title.setText(tale.getTitle());
        taleViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("team_detail", tales.get(i));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tales.size();
    }

    class TaleViewHolder extends RecyclerView.ViewHolder {

        private ImageView logo;
        private TextView title;
        private TextView synopsis;

        TaleViewHolder(@NonNull View itemView) {
            super(itemView);
            logo = itemView.findViewById(R.id.tale_image);
            title = itemView.findViewById(R.id.title);
            synopsis = itemView.findViewById(R.id.synopsis);
        }

    }
}
