package com.csc309.arspace;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    private EditText email;
    private EditText password;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();

        email = findViewById(R.id.userID);
        password = findViewById(R.id.password);
        Button login = findViewById(R.id.login_button);
        TextView signup = findViewById(R.id.signup_button);

        // if you click the login button...
        login.setOnClickListener(view -> validate(email.getText().toString().trim(),
                password.getText().toString().trim()));

        // if you click the sign up text...
        signup.setOnClickListener(view -> {
            Intent goToSignUp = new Intent(Login.this, SignUp.class);
            startActivity(goToSignUp);
        });
    }

    // checks user's login credentials
    public void validate(String email, String pw) {
        // compare the user's input password and compare its hash-value to the expected value
            // if the credentials are correct, move on to the next activity
            // if not, send error message and re-do login
        if (email.isEmpty() || pw.isEmpty()) {
            Toast.makeText(this,"Please enter all required fields", Toast.LENGTH_SHORT).show();
        }
        else {
            firebaseAuth.signInWithEmailAndPassword(email, pw)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            Intent goToMainScreen = new Intent(Login.this, MainActivity.class);
                            startActivity(goToMainScreen);
                        }
                        else {
                            Toast.makeText(Login.this, "Credentials Invalid", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}
