package com.example.nandhu.alphabets;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static com.example.nandhu.alphabets.R.layout.listitem;

/**
 * Created by Nandhu on 09-03-2017.
 */

public class AlphabetsAdapter extends ArrayAdapter<Alphabets> {
    public AlphabetsAdapter(Context context, ArrayList<Alphabets> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Alphabets item = getItem(position);
        View listview = convertView;
        if(listview == null){
            listview= LayoutInflater.from(getContext()).inflate(listitem,parent,false);
        }

        TextView textView = (TextView) listview.findViewById(R.id.text);
        textView.setText(item.getName());
        ImageView imageview = (ImageView) listview.findViewById(R.id.img);
        imageview.setImageResource(item.getImg());
        return listview;
    }
}
