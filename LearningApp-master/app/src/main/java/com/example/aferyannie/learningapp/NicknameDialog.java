package com.example.aferyannie.learningapp;

import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shashank.sony.fancytoastlib.FancyToast;

public class NicknameDialog extends DialogFragment {
    private static final String TAG = NicknameDialog.class.getSimpleName();

    DatabaseReference databaseNames;
    TextInputLayout inputName;
    TextView btnName, btnSkip;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_nickname, null, false);

        databaseNames = FirebaseDatabase.getInstance().getReference("scoreboard");
        Log.d(TAG, "inputName: onDisplay");

        inputName = view.findViewById(R.id.inputName);
        btnName = view.findViewById(R.id.btnName);
        btnSkip = view.findViewById(R.id.btnSkip);

        btnName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNames();
            }
        });
        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "inputName: onSkip save data");
                FancyToast.makeText(getContext(), "Anda memilih untuk tidak menyimpan skor.",
                        FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();

                getDialog().dismiss();
                showFragment(new HomeFragment(),R.id.fragment_container);
            }
        });
        return view;
    }

    private void saveNames() {
        String name = inputName.getEditText().getText().toString().trim();
        Bundle bundle = getArguments(); // Get arguments from bundle in ResultFragment.
        double c = bundle.getDouble("Skor"); // Get bundle with key "Skor".
        if (!TextUtils.isEmpty(name) && name.length() <= 10) {
                inputName.setError(null);

                // Define current time using epoch timestamp.
                Long now = System.currentTimeMillis();

                String id = databaseNames.push().getKey();
                Name nickname = new Name(name, c, now);
                databaseNames.child(id).setValue(nickname);

                Log.d(TAG, "inputName: onSuccess");
                FancyToast.makeText(getContext(), "Skor telah sukses disimpan. Jangan lupa cek papan skor ya!",
                        FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();

                getDialog().dismiss();
                showFragment(new HomeFragment(), R.id.fragment_container);
        } else if(name.length() > 10){
            inputName.setError("Nama mu terlalu panjang");
            Log.d(TAG, "inputName: onPending insert data");
            return;
        } else {
            inputName.setError("Isi nama untuk simpan skor");
            Log.d(TAG, "inputName: onPending insert data");
        }
    }

    public void showFragment(Fragment fragment, int fragmentResourceID) {
        if (fragment != null) {
            FragmentManager fragmentManager = this.getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(fragmentResourceID, fragment);
            fragmentTransaction.detach(fragment);
            fragmentTransaction.attach(fragment);
            fragmentTransaction.commit();
        }
    }

}
