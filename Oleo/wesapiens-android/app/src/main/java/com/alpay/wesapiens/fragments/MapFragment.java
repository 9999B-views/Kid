package com.alpay.wesapiens.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alpay.wesapiens.R;
import com.alpay.wesapiens.adapter.MenuListAdapter;
import com.alpay.wesapiens.models.MenuItem;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MapFragment extends Fragment {

    public View view;
    private Unbinder unbinder;

    @BindView(R.id.select_menu)
    RecyclerView recyclerView;

    @OnClick(R.id.physics)
    public void selectPhysicsTopic(){
        preparePhysicsView();
    }

    @OnClick(R.id.chemistry)
    public void selectChemistryTopic(){
        prepareChemistryView();
    }

    @OnClick(R.id.biology)
    public void selectBiologyTopic(){
        prepareBiologyView();
    }

    ArrayList<MenuItem> menuDrawableCodes = new ArrayList<>();
    MenuListAdapter menuListAdapter;

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_map, container, false);
        unbinder = ButterKnife.bind(this, view);
        menuListAdapter = new MenuListAdapter((AppCompatActivity) getActivity(), menuDrawableCodes);
        return view;
    }

    private void setupRecylerView(MenuListAdapter adapter) {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }


    private void prepareBiologyView() {
        menuDrawableCodes.clear();
        menuDrawableCodes.add(new MenuItem(getResources().getString(R.string.ch_cell), R.drawable.ic_b_cell, false));
        menuDrawableCodes.add(new MenuItem(getResources().getString(R.string.ch_mitosis), R.drawable.ic_b_mitosis, false));
        menuDrawableCodes.add(new MenuItem(getResources().getString(R.string.ch_mayosis), R.drawable.ic_b_mayosis, false));
        menuDrawableCodes.add(new MenuItem(getResources().getString(R.string.ch_reproduction), R.drawable.ic_b_reproduction, false));
        menuListAdapter.notifyDataSetChanged();
        setupRecylerView(menuListAdapter);
    }

    private void prepareChemistryView() {
        menuDrawableCodes.clear();
        menuDrawableCodes.add(new MenuItem(getResources().getString(R.string.ch_element), R.drawable.ic_c_element, false));
        menuDrawableCodes.add(new MenuItem(getResources().getString(R.string.ch_substance), R.drawable.ic_c_substance, false));
        menuDrawableCodes.add(new MenuItem(getResources().getString(R.string.ch_solution), R.drawable.ic_c_solution, false));
        menuDrawableCodes.add(new MenuItem(getResources().getString(R.string.ch_recycle), R.drawable.ic_c_recycle, false));
        menuListAdapter.notifyDataSetChanged();
        setupRecylerView(menuListAdapter);
    }

    private void preparePhysicsView() {
        menuDrawableCodes.clear();
        menuDrawableCodes.add(new MenuItem(getResources().getString(R.string.ch_force), R.drawable.ic_p_force, true));
        menuDrawableCodes.add(new MenuItem(getResources().getString(R.string.ch_energy), R.drawable.ic_p_energy, false));
        menuDrawableCodes.add(new MenuItem(getResources().getString(R.string.ch_lens), R.drawable.ic_p_lens, false));
        menuDrawableCodes.add(new MenuItem(getResources().getString(R.string.ch_optic), R.drawable.ic_p_optic, false));
        menuListAdapter.notifyDataSetChanged();
        setupRecylerView(menuListAdapter);
    }
}
