package com.alpay.codenotes.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alpay.codenotes.R;

import java.util.ArrayList;
import java.util.List;


public class ForLoopRecyclerViewAdapter extends RecyclerView.Adapter<ForLoopRecyclerViewAdapter.ViewHolder> {

    private final List<ForLoopImage> mValues;

    public ForLoopRecyclerViewAdapter(List<ForLoopImage> items) {
        mValues = items;
    }

    @Override
    public ForLoopRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.forloopcardview, parent, false);
        ViewHolder vh = new ViewHolder(view);
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new ForLoopRecyclerViewAdapter(ForLoopImage.ITEMS));
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mImageView.setImageDrawable(mValues.get(position).image);
        holder.mTextView.setText(Integer.toString(position+1));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public TextView mTextView;

        public ViewHolder(View view) {
            super(view);
            mImageView = (ImageView) view.findViewById(R.id.forloopDrawable);
            mTextView = (TextView) view.findViewById(R.id.forloopNumber);
        }
    }

    public static class ForLoopImage {
        public static List<ForLoopImage> ITEMS = new ArrayList<>();
        public Drawable image;

        public ForLoopImage(Context context, Bitmap bitmap) {
            this.image = setResultImage(context, bitmap);
        }

        public Drawable setResultImage(Context context, Bitmap bitmap) {
            Drawable drawable = new BitmapDrawable(context.getResources(), bitmap);
            return drawable;
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mValues.size();
    }
}