package com.rdagnelli.energeye.activities;

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
import com.google.firebase.auth.FirebaseUser;
import com.rdagnelli.energeye.R;

public class SignupActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    private Button signUpButton;
    private Button backButton;
    private EditText emailEditText;
    private EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();

        emailEditText = findViewById(R.id.sign_up_email);
        passwordEditText = findViewById(R.id.sign_up_password);


        signUpButton = findViewById(R.id.sign_up_button);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!emailEditText.getText().toString().equals("") &&
                        !passwordEditText.getText().toString().equals("")) {
                    mAuth.createUserWithEmailAndPassword(emailEditText.getText().toString(), passwordEditText.getText().toString())
                            .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d("SIGNUP", "createUserWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        Toast.makeText(SignupActivity.this, "Account creato con successo", Toast.LENGTH_LONG).show();
                                        finish();
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.d("SIGNUP", "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(SignupActivity.this, "Crazione account fallita",
                                                Toast.LENGTH_SHORT).show();

                                    }

                                    // ...
                                }
                            });
                } else {
                    Toast.makeText(SignupActivity.this, R.string.empty,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        backButton = findViewById(R.id.back_login_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

}
