package com.example.aferyannie.learningapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.shashank.sony.fancytoastlib.FancyToast;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class LoginFragment extends Fragment {
    private static final String TAG = "FACEBOOK_LOG";

    private CallbackManager mCallbackManager;
    private FirebaseAuth mAuth;

    Button loginButton;
    LoginButton btnFacebook;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, null, false);

        /** Initialize Firebase Auth. */
        mAuth = FirebaseAuth.getInstance();

        /** Initialize Facebook Login Button. */
        mCallbackManager = CallbackManager.Factory.create();

        loginButton = view.findViewById(R.id.btnLogin);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnFacebook.performClick();
            }
        });

        btnFacebook = view.findViewById(R.id.btnFacebook);
        btnFacebook.setReadPermissions("email", "public_profile");
        btnFacebook.setFragment(this);
        btnFacebook.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        /** Check if user is signed in (non-null) and update UI accordingly. */
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null) {
            updateUI(currentUser);
        }
    }

    private void updateUI(FirebaseUser currentUser){
        TextView navbarNickname = getActivity().findViewById(R.id.nickname);
        TextView navbarEmail = getActivity().findViewById(R.id.email);
        CircleImageView navbarImageView = getActivity().findViewById(R.id.displaypicture);

        NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
        // Set NavigationBar scoreboard visible.
        navigationView.getMenu().getItem(2).setVisible(true);

        navbarEmail.setVisibility(View.VISIBLE);

        navbarNickname.setText(currentUser.getDisplayName());
        navbarEmail.setText(currentUser.getEmail());
        /** Load facebook display picture using Picasso library. */
        Picasso.get()
                .load(currentUser.getPhotoUrl().toString())
                .resize(65, 65)
                .centerCrop()
                .into(navbarImageView);

        getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new LogoutFragment()).commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(LoginFragment.this.getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:success");

                            loginButton.setEnabled(true);

                            FancyToast.makeText(getContext(), "Anda berhasil masuk menggunakan Facebook.",
                                    FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();

                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            Log.w(TAG, "signInWithCredential:failure", task.getException());

                            loginButton.setEnabled(true);

                            FancyToast.makeText(getContext(), "Anda gagal masuk menggunakan Facebook.",
                                    FancyToast.LENGTH_SHORT, FancyToast.CONFUSING, false).show();
                        }
                    }
                });
    }
}
