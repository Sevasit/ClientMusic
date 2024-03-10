package com.example.musicyoungbysevasit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    public EditText inputEmail, inputPassword;
    public ProgressBar progressBar;
    //ประกาศเพื่อเรียกใช้Authentication ของ Firebase
    public FirebaseAuth registerAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        inputEmail = (EditText) findViewById(R.id.edtEmail);
        inputPassword = (EditText) findViewById(R.id.edtPassword);
        progressBar = (ProgressBar) findViewById(R.id.progressBar2);
        registerAuth = FirebaseAuth.getInstance();
    }

    public void Signup(View view) {

        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();

        if (TextUtils.isEmpty(email.trim())) {
            Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(password.trim())) {
            Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
        }
        else if (password.length() < 6) {
            Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
        }else{
        progressBar.setVisibility(View.VISIBLE);

        registerAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Toast.makeText(RegisterActivity.this, "Complete Register User",
                                Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        if (!task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "Error" + task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Intent open = new Intent(RegisterActivity.this, UpdateProfile.class);
                            startActivity(open);
                            finish();
                        }//ปิด if
                    }
                });
        }
    }//ปิด method

    public void login (View view){
        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
        finish();
    }
}