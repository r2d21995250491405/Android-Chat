package com.example.firebasechat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignInActivity extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference UsersRef;
    private static final String TAG = "SignInActivity";

    private FirebaseAuth mAuth;
    private EditText emaill, passwordd, name, confpass;
    private Button signBtn;
    private TextView text;

    private boolean loginMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        database = FirebaseDatabase.getInstance();
        UsersRef = database.getReference().child("Users");

        emaill = findViewById(R.id.editTextTextEmailAddress);
        passwordd = findViewById(R.id.editTextTextPassword);
        name = findViewById(R.id.editTextTextPersonName);
        confpass = findViewById(R.id.editTextTextConfirmPassword);

        signBtn = findViewById(R.id.button);

        text = findViewById(R.id.textView2);


        mAuth = FirebaseAuth.getInstance();

        signBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginSignUpUser(emaill.getText().toString().trim(), passwordd.getText().toString().trim());
            }
        });

        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(SignInActivity.this, UserListActivity.class));
        }
    }

    private void loginSignUpUser(String email, String password) {
        if (loginMode) {
            if (passwordd.getText().toString().trim().length() < 7) {
                Toast.makeText(this, "Password must be at least 7 characters", Toast.LENGTH_SHORT).show();
            } else if (emaill.getText().toString().trim().equals("")) {
                Toast.makeText(this, "Input Email", Toast.LENGTH_SHORT).show();
            } else {
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "signInWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Intent intent = new Intent(SignInActivity.this, UserListActivity.class);
                                    intent.putExtra("userName", name.getText().toString().trim());
                                    startActivity(intent);
//                                updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                                    Toast.makeText(SignInActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
//                                updateUI(null);
                                    // ...
                                }

                                // ...
                            }
                        });
            }
        } else {
            if (!passwordd.getText().toString().trim().equals(confpass.getText().toString().trim())) {
                Toast.makeText(this, "Passwords don't match", Toast.LENGTH_SHORT).show();
            } else if (passwordd.getText().toString().trim().length() < 7) {
                Toast.makeText(this, "Password must be at least 7 characters", Toast.LENGTH_SHORT).show();
            } else if (emaill.getText().toString().trim().equals("")) {
                Toast.makeText(this, "Input Email", Toast.LENGTH_SHORT).show();
            } else {
                mAuth.createUserWithEmailAndPassword(email, password)

                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    createUser(user);
                                    Intent intent = new Intent(SignInActivity.this, UserListActivity.class);
                                    intent.putExtra("userName", name.getText().toString().trim());
                                    startActivity(intent);
//                            updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(SignInActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                                }

                                // ...
                            }
                        });
            }
        }
    }

    private void createUser(FirebaseUser firebaseUser) {
        User user = new User();
        user.setId(firebaseUser.getUid());
        user.setEmail(firebaseUser.getEmail());
        user.setName(name.getText().toString().trim());

        UsersRef.push().setValue(user);
    }

    public void toogleLoginMode(View view) {
        if (loginMode) {
            loginMode = false;
            signBtn.setText("Sign Up");
            text.setText("Or, log in");
            confpass.setVisibility(View.VISIBLE);
        } else {
            loginMode = true;
            signBtn.setText("Log In");
            text.setText("Or, sign up");
            confpass.setVisibility(View.GONE);
        }
    }
}