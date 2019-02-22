package com.example.aferyannie.learningapp;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by aferyannie on 04/10/18.
 */

public class NameList extends ArrayAdapter<Name>{
    private Activity context;
    private List<Name> nameList;

    public NameList(Activity context, List<Name> nameList){
        super(context, R.layout.list_layout, nameList);
        this.context = context;
        this.nameList = nameList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.list_layout,null, true);

        TextView txtNames = listViewItem.findViewById(R.id.txtNames);
        TextView txtScores = listViewItem.findViewById(R.id.txtScores);

        Name name = nameList.get(position);
        txtNames.setText(name.getName());
        txtScores.setText(name.getScore().toString());

        return listViewItem;
    }
}
