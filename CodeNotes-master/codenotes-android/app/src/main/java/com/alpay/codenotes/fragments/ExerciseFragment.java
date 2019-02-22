package com.alpay.codenotes.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alpay.codenotes.R;
import com.alpay.codenotes.adapter.ExerciseViewAdapter;
import com.alpay.codenotes.models.Exercise;
import com.alpay.codenotes.utils.listener.RecyclerViewOnClickListener;

import java.util.ArrayList;

public class ExerciseFragment extends Fragment {

    private View view;
    private ArrayList<Exercise> exerciseArrayList;
    RecyclerView recyclerView;
    public ExerciseFragment() {
        // default public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_exercise, container, false);
        generateExerciseList();
        return view;
    }

    private void setUpRecyclerView() {
        recyclerView = view.findViewById(R.id.exercise_recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    private void populateRecyclerView() {
        final ExerciseViewAdapter adapter = new ExerciseViewAdapter(view.getContext(), exerciseArrayList);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new RecyclerViewOnClickListener(view.getContext(), new RecyclerViewOnClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //do something
            }
        }));
    }

    private void generateExerciseList() {
        exerciseArrayList = new ArrayList<>();

    }

}
