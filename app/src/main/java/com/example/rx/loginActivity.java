package com.example.rx;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class loginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private String TAG="myLOG";
    private FirebaseUser currentUser;
    private CallbackManager mCallbackManager;
    private Profile profile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        profile= Profile.getCurrentProfile();


        chekUser();

        setContentView(R.layout.activity_login);


        final EditText etEmail,etPassword;
        etEmail = findViewById(R.id.et_log_email);
        etPassword = findViewById(R.id.et_log_password);
        Button btnSignIn=findViewById(R.id.btn_signIn);
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn(etEmail.getText().toString(),etPassword.getText().toString());
            }
        });


        Button btnSignUp = findViewById(R.id.btn_to_signup);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),SignUpActivity.class));
            }
        });


        mCallbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = findViewById(R.id.login_button);

        loginButton.registerCallback(mCallbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {

                        startActivity(new Intent(getApplicationContext(),Menu.class));
                    }

                    @Override
                    public void onCancel() {
                    }

                    @Override
                    public void onError(FacebookException exception) {
                    }
                });

    }
    @Override
    public void onStart() {
        super.onStart();
        currentUser = mAuth.getCurrentUser();

    }
    private void signIn(String email, String password)
    {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithEmail:success");
                            startActivity(new Intent(getApplicationContext(),Menu.class));

                        } else {
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                        }
                    }
                });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void chekUser()
    {
        Log.d(TAG,"profile "+profile+"  Auth "+currentUser);
        if(currentUser==null&&profile==null)
        {
            Log.d(TAG,"net user");
        }
        else {
            Log.d(TAG,"est");
            startActivity(new Intent(getApplicationContext(),Menu.class));
            finish();
        }

    }
}
