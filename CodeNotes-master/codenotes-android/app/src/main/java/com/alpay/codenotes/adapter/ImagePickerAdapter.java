package com.alpay.codenotes.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.alpay.codenotes.R;
import com.alpay.codenotes.utils.interpreter.TextRecognition;

import java.io.File;
import java.util.ArrayList;

public class ImagePickerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<String> list = new ArrayList<>();
    private Context context;

    public ImagePickerAdapter(Context context) {
        this.context = context;
    }

    public void addImage(ArrayList<String> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public String[] getImageList(){
        String[] array = new String[list.size()];
        array = list.toArray(array);
        return array;
    }

    public Bitmap[] getImageBitmapList(Context context) {
        Bitmap[] array = new Bitmap[list.size()];
        for (int i = 0; i < list.size(); i++) {
            File f = new File(list.get(i));
            array[i] = new BitmapDrawable(context.getResources(), f.getAbsolutePath()).getBitmap();
        }
        return array;
    }

    public void clear() {
        final int size = list.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                list.remove(0);
            }

            notifyItemRangeRemoved(0, size);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.image, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        File f = new File(list.get(position));
        Bitmap d = new BitmapDrawable(context.getResources(), f.getAbsolutePath()).getBitmap();
        Bitmap scaled = com.fxn.utility.Utility.getScaledBitmap(512, d);
        ((Holder) holder).iv.setImageBitmap(scaled);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        public ImageView iv;


        public Holder(View itemView) {
            super(itemView);
            iv = itemView.findViewById(R.id.iv);
        }
    }
}