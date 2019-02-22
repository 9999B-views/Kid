package com.alpay.codenotes.utils;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.alpay.codenotes.R;
import com.alpay.codenotes.base.BaseActivity;
import com.alpay.codenotes.fragments.AddStatementFragment;
import com.alpay.codenotes.fragments.ExerciseFragment;
import com.alpay.codenotes.fragments.HomePageFragment;
import com.alpay.codenotes.fragments.ProgramDetailsFragment;
import com.alpay.codenotes.fragments.SendFeedbackFragment;

public class NavigationManager {

    private static String[] programText = {""};

    public static final String BUNDLE_KEY = "bundlekey";
    public static final String HOME = "home";
    public static final String SEND_FEEDBACK = "sendfb";
    public static final String ADD_NEW_STATEMENT = "add_new_statement";
    public static final String EXERCISE = "exercises";
    public static final String PROGRAM_DETAILS = "programdetails";

    public static void setProgramText(String[] text){
        programText = text;
    }

    public static void openFragment(AppCompatActivity appCompatActivity, String fragmentID){
        FragmentTransaction ft = appCompatActivity.getSupportFragmentManager().beginTransaction();
        if(fragmentID.contentEquals(HOME)){
            HomePageFragment homePageFragment = new HomePageFragment();
            ft.replace(R.id.fragment_container, homePageFragment);
        }else if(fragmentID.contentEquals(SEND_FEEDBACK)){
            SendFeedbackFragment sendFeedbackFragment = new SendFeedbackFragment();
            ft.replace(R.id.fragment_container, sendFeedbackFragment);
        }else if(fragmentID.contentEquals(ADD_NEW_STATEMENT)){
            AddStatementFragment addStatementFragment = new AddStatementFragment();
            ft.replace(R.id.fragment_container, addStatementFragment);
        }else if(fragmentID.contentEquals(EXERCISE)){
            ExerciseFragment exerciseFragment = new ExerciseFragment();
            ft.replace(R.id.fragment_container, exerciseFragment);
        }else if(fragmentID.contentEquals(PROGRAM_DETAILS)){
            Intent intent = new Intent(appCompatActivity, BaseActivity.class);
            intent.putExtra(BUNDLE_KEY, PROGRAM_DETAILS);
            appCompatActivity.startActivity(intent);
        }
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }

    public static void openFragmentFromBase(AppCompatActivity appCompatActivity, String fragmentID){
        FragmentTransaction ft = appCompatActivity.getSupportFragmentManager().beginTransaction();
        if(fragmentID.contentEquals(PROGRAM_DETAILS)){
            ProgramDetailsFragment programDetailsFragment = ProgramDetailsFragment.newInstance(programText);
            ft.replace(R.id.fragment_container, programDetailsFragment);
        }else if(fragmentID.contentEquals(HOME)){
            HomePageFragment homePageFragment = new HomePageFragment();
            ft.replace(R.id.fragment_container, homePageFragment);
        }else if(fragmentID.contentEquals(SEND_FEEDBACK)){
            SendFeedbackFragment sendFeedbackFragment = new SendFeedbackFragment();
            ft.replace(R.id.fragment_container, sendFeedbackFragment);
        }else if(fragmentID.contentEquals(ADD_NEW_STATEMENT)){
            AddStatementFragment addStatementFragment = new AddStatementFragment();
            ft.replace(R.id.fragment_container, addStatementFragment);
        }else if(fragmentID.contentEquals(EXERCISE)){
            ExerciseFragment exerciseFragment = new ExerciseFragment();
            ft.replace(R.id.fragment_container, exerciseFragment);
        }
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }

}
