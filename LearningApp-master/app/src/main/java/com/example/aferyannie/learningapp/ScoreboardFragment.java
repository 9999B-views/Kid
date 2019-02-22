package com.example.aferyannie.learningapp;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;

public class ScoreboardFragment extends Fragment {
    private static final String TAG = ScoreboardFragment.class.getSimpleName();
    DatabaseReference databaseNames;

    /**
     * Define listView scoreboard.
     */
    ListView listViewScores;
    List<Name> nameList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scoreboard, null, false);

        databaseNames = FirebaseDatabase.getInstance().getReference("scoreboard");
        listViewScores = view.findViewById(R.id.listViewScores);
        nameList = new ArrayList<>();

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "listViewScores: onFetch");
        databaseNames.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                nameList.clear();
                for (DataSnapshot scoreSnapshot : dataSnapshot.getChildren()) {
                    Name name = scoreSnapshot.getValue(Name.class);
                    nameList.add(name);
                }
                NameList adapter = new NameList(getActivity(), nameList);
                listViewScores.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        /** Create onClick listener for each item. */
        listViewScores.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showFragment(new DetailFragment(), R.id.fragment_container, nameList.get(i));
                Log.d(TAG, "listViewScores: onItemClick index " + i);
            }
        });
    }

    public void showFragment(Fragment fragment, int fragmentResourceID, Name data) {
        if (fragment != null) {
            FragmentManager fragmentManager = this.getFragmentManager();
            Bundle bundle = new Bundle();
            bundle.putSerializable("data", data);
            fragment.setArguments(bundle);
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(fragmentResourceID, fragment);
            fragmentTransaction.detach(fragment);
            fragmentTransaction.attach(fragment);
            fragmentTransaction.commit();
        }
    }

}
