package com.example.aferyannie.learningapp;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.Random;

public class HomeFragment extends Fragment implements View.OnClickListener {
    private Bundle bundle;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, null, false);

        Button btnStart = view.findViewById(R.id.btnStart);
        Button btnStart1 = view.findViewById(R.id.btnStart1);
        Button btnStart2 = view.findViewById(R.id.btnStart2);

        btnStart.setOnClickListener(this);
        btnStart1.setOnClickListener(this);
        btnStart2.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        bundle = new Bundle();
        bundle.putInt("jumlahTest", 0); // TODO: ini buat apa cuk?
        switch (view.getId()) {
            /** Button for numerals. */
            case R.id.btnStart:
                Random randInt = new Random(); // TODO: ganti redundant -> CategoryFragment -> ResultFragment.
                int num = randInt.nextInt(10);
                bundle.putInt("Angka", num);
                CategoryFragment categoryAngka = new CategoryFragment();
                nextFragment(categoryAngka);
                break;
            /** Button for alphabets_upper. */
            case R.id.btnStart1:
                String[] charsUpper = {
                        "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M",
                        "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
                bundle.putStringArray("HurufKapital", charsUpper);
                CategoryFragment categoryHurufKapital = new CategoryFragment();
                nextFragment(categoryHurufKapital);
                break;
            /** Button for alphabets_lower. */
            case R.id.btnStart2:
                String[] charsLower = {
                        "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m",
                        "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
                bundle.putStringArray("HurufKecil", charsLower);
                CategoryFragment categoryHurufKecil = new CategoryFragment();
                nextFragment(categoryHurufKecil);
                break;
        }
    }

    public void nextFragment(CategoryFragment category) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        category = new CategoryFragment();
        category.setArguments(bundle);
        fragmentTransaction.replace(R.id.fragment_container, category);
        fragmentTransaction.commit();
    }

}
