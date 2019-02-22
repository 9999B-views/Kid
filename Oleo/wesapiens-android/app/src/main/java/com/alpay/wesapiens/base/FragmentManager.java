package com.alpay.wesapiens.base;

import com.alpay.wesapiens.R;
import com.alpay.wesapiens.fragments.CreateGameFragment;
import com.alpay.wesapiens.fragments.HomeFragment;
import com.alpay.wesapiens.fragments.MapFragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

public class FragmentManager {

    public static final String BUNDLE_KEY = "bundlekey";
    public static final String  CREATE_GAME = "creategame";
    public static final String HOME = "home";
    public static final String MAP = "map";

    public static final int PHYSICS = 0;
    public static final int CHEMISTRY = 1;
    public static final int BIOLOGY = 2;

    public static void openFragment(AppCompatActivity appCompatActivity, String fragmentID) {
        FragmentTransaction ft = appCompatActivity.getSupportFragmentManager().beginTransaction();
        if (fragmentID.contentEquals(CREATE_GAME)) {
            CreateGameFragment createGameFragment = CreateGameFragment.newInstance();
            ft.replace(R.id.fragment_container, createGameFragment);
        } else if (fragmentID.contentEquals(HOME)) {
            HomeFragment homeFragment = HomeFragment.newInstance();
            ft.replace(R.id.fragment_container, homeFragment);
        } else if (fragmentID.contentEquals(MAP)) {
            MapFragment mapFragment = new MapFragment();
            ft.replace(R.id.fragment_container, mapFragment);
        }
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }

}
