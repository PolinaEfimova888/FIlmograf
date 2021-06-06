package com.example.filmograf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    GoogleSignInClient mGoogleSignInClient;
    private final static int RC_SIGN_IN = 123;


    EditText tv_mail;
    EditText tv_pass;
    String idToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        tv_mail = (EditText) findViewById(R.id.mail);
        tv_pass = (EditText) findViewById(R.id.pass);

        googleAuthentification ();

        FirebaseUser user =  mAuth.getCurrentUser();

//        mAuthListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                FirebaseUser user = firebaseAuth.getCurrentUser();
//                if (user != null) {
//                    Log.d("mytag", "User is signed in");
//                    // User is signed in
//
//                } else {
//                    Log.d("mytag", "User is signed out");
//                    // User is signed out
//
//                }
//
//            }
//        };
    }

    public boolean userEnterData() {
        if (tv_mail.getText().toString().isEmpty() && tv_pass.getText().toString().isEmpty()) return false;
        return true;
    }

    public void onSignIn(View v) {
        String mail = tv_mail.getText().toString();
        String pass = tv_pass.getText().toString();

        if (userEnterData()) {

            mAuth.signInWithEmailAndPassword(mail, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        goToProfile(mAuth.getCurrentUser());
                        Log.d("mytag", "User is login");
                        Toast.makeText(MainActivity.this, "Aвторизация успешна", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.d("mytag", "User is signed in");
                        Toast.makeText(MainActivity.this, "Aвторизация провалена", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        } else Toast.makeText(MainActivity.this, "Данные не были введены", Toast.LENGTH_SHORT).show();
    }

    public void onRegister (View v){

        String mail = tv_mail.getText().toString();
        String pass = tv_pass.getText().toString();

        Log.d("mytag", mail+pass);

        if (userEnterData()) {
            mAuth.createUserWithEmailAndPassword(mail, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Log.d("mytag", "User is signed register");
                        Toast.makeText(MainActivity.this, "Регистрация успешна", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.d("c", "User is not register");
                        Toast.makeText(MainActivity.this, "Регистрация провалена", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else Toast.makeText(MainActivity.this, "Данные не были введены", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser user = mAuth.getCurrentUser();
        goToProfile(user);
    }

    public void goToProfile(FirebaseUser user) {
        if(user!=null){
            Intent intent = new Intent(MainActivity.this, Profile.class);
            startActivity(intent);
        }
    }

    private void googleAuthentification (){
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    public void googleSignIn(View v) {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("mytag", "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.d("mytag", "Google sign in failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("mytag", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            goToProfile(user);
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.d("mytag", "signInWithCredential:failure", task.getException());
                            //updateUI(null);
                        }
                    }
                });
    }



}