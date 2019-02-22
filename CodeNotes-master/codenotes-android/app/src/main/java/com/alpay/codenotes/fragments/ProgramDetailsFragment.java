package com.alpay.codenotes.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.alpay.codenotes.R;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ProgramDetailsFragment extends Fragment {

    private static final String ARG_PARAM = "programText";
    private String[] programText;
    private Unbinder unbinder;
    public View view;

    @BindView(R.id.program_detail_text)
    TextView programDetailTV;

    public ProgramDetailsFragment() {

    }

    public static ProgramDetailsFragment newInstance(String[] programText) {
        ProgramDetailsFragment fragment = new ProgramDetailsFragment();
        Bundle args = new Bundle();
        args.putStringArray(ARG_PARAM, programText);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            programText = getArguments().getStringArray(ARG_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_program_details, container, false);
        unbinder = ButterKnife.bind(this, view);
        programDetailTV.setText(Arrays.toString(programText).replaceAll("\\[|\\]", "\n"));
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        unbinder.unbind();
    }
}
