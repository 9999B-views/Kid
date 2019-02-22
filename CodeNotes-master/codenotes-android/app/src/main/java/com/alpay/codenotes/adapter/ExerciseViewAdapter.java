package com.alpay.codenotes.adapter;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alpay.codenotes.R;
import com.alpay.codenotes.models.Exercise;

import java.util.ArrayList;


public class ExerciseViewAdapter extends RecyclerView.Adapter<ExerciseViewHolder> {

    private Context mContext;
    private ArrayList<Exercise> mExerciseList;

    public  ExerciseViewAdapter(Context mContext, ArrayList<Exercise> mExerciseList) {
        this.mContext = mContext;
        this.mExerciseList = mExerciseList;
    }

    @Override
    public ExerciseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View mView = layoutInflater.inflate(R.layout.exercise_card_layout, parent, false);
        return new ExerciseViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(final ExerciseViewHolder holder, int position) {
        holder.mImage.setImageDrawable(setResultImage(mContext, mExerciseList.get(position).getImage()));
        holder.mTitle.setText(mExerciseList.get(position).getName());
        holder.mDetail.setText(mExerciseList.get(position).getDescription());
        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // intent to detail activity
            }
        });
    }

    public Drawable setResultImage(Context context, String encodedImage) {
        byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        Drawable drawable = new BitmapDrawable(context.getResources(), bitmap);
        return drawable;
    }

    @Override
    public int getItemCount() {
        return mExerciseList.size();
    }
}

class ExerciseViewHolder extends RecyclerView.ViewHolder {

    ImageView mImage;
    TextView mTitle;
    TextView mDetail;
    CardView mCardView;

    ExerciseViewHolder(View itemView) {
        super(itemView);
        mImage = itemView.findViewById(R.id.exercise_card_thumbnail);
        mTitle = itemView.findViewById(R.id.exercise_card_title);
        mDetail = itemView.findViewById(R.id.exercise_card_detail);
        mCardView = itemView.findViewById(R.id.exercise_card_view);
    }
}