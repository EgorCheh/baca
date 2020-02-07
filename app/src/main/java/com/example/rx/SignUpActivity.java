package com.example.rx;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText etEmail, etPassword, etPassword2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        etPassword = findViewById(R.id.et_password);
        etPassword2 = findViewById(R.id.et_password2);
        etEmail=findViewById(R.id.et_mail);
        Button btnSetNewUser = findViewById(R.id.btn_set_new_user);
        btnSetNewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUser();
            }
        });



    }

    private void setUser(){

      String email =etEmail.getText().toString().trim();
      String password = etPassword.getText().toString().trim();
      String password2 = etPassword2.getText().toString().trim();
        if(check(password2,password,email))
        mAuth.createUserWithEmailAndPassword(email,password )
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Toast.makeText(getApplicationContext(), "Authentication ok.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        else {
            Toast.makeText(this,"Error full" ,Toast.LENGTH_LONG).show();
        }
    }


    private boolean check(String password2,String password,String email){
        if(password2.isEmpty()||password.isEmpty()||email.isEmpty())
        {
            Toast.makeText(getApplicationContext(),"empty error",Toast.LENGTH_LONG).show();
            return false;
        }
        if(password.contentEquals(password2))
        {
            Toast.makeText(getApplicationContext(),"equals error",Toast.LENGTH_LONG).show();
            return false;

        }


        return true;
    }

    private boolean chekNumber(String password)
    {
        for (int i = 0; i < password.length(); i++)
            if (Character.isDigit(password.charAt(i)))
               return false;
        return true;
    }
    private boolean chekUpp(String password)
    {
        for (int i = 0; i < password.length(); i++)
            if (Character.isUpperCase(password.charAt(i)))
                return false;
        return true;
    }
    @Override
    public void onStart() {
        super.onStart();
    }
}
