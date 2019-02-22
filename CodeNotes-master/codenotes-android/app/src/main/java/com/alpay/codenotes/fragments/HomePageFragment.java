package com.alpay.codenotes.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alpay.codenotes.ProjectActivity;
import com.alpay.codenotes.R;
import com.alpay.codenotes.utils.Constants;
import com.alpay.codenotes.utils.NavigationManager;
import com.alpay.codenotes.utils.Utils;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class HomePageFragment extends Fragment {

    public View view;
    private Unbinder unbinder;

    public HomePageFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_homepage, container, false);
        unbinder = ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        unbinder.unbind();
    }

    @OnClick(R.id.websitecard_link)
    public void visitWebsite(){
        String url = "https://asabuncuoglu13.github.io/CodeNotes/";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    @OnClick(R.id.button_learn)
    public void openTutorial() {

    }

    @OnClick(R.id.button_code)
    public void openCoding() {
        Intent intent = new Intent(getActivity(), ProjectActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.button_get)
    public void downloadCards() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.CARD_PDF_URL));
        Utils.showOKDialog(getActivity(), R.string.download_printable_files, intent);
    }

    @OnClick(R.id.newStatementButton)
    public void openNewStatementActivity(){
        NavigationManager.openFragment((AppCompatActivity) getActivity(), NavigationManager.ADD_NEW_STATEMENT);
    }
}
