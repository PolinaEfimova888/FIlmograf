package com.example.filmograf;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;


import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;


public class Profile extends AppCompatActivity {

    TextView name, mail;
    Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        logout = findViewById(R.id.logout);
        name = findViewById(R.id.name);
        mail = findViewById(R.id.mail);

        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
        if(signInAccount != null){
            name.setText(signInAccount.getDisplayName());
            mail.setText(signInAccount.getEmail());
        }
        Intent intent = new Intent(Profile.this, MovieActivity.class);
        startActivity(intent);

    }

    public void logOut(View v) {
        FirebaseAuth.getInstance().signOut();

        Intent intent = new Intent(Profile.this,MainActivity.class);
        startActivity(intent);
    }

    public void goToList(View v) {
        Intent intent = new Intent(Profile.this, PopularMovieListActivity.class);
        startActivity(intent);
    }

}
