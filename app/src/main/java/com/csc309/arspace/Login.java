package com.csc309.arspace;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private Button login;
    private TextView signup;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();

        email = findViewById(R.id.userID);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login_button);
        signup = findViewById(R.id.signup_button);

        // if you click the login button...
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate(email.getText().toString().trim(), password.getText().toString().trim());
            }
        });

        // if you click the sign up text...
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToSignUp = new Intent(Login.this, SignUp.class);
                startActivity(goToSignUp);
            }
        });
    }

    // checks user's login credentials
    private void validate(String email, String pw) {
        // TODO: connect to a database and create a "real" login system
        // get the correct password hash-value from database
        // compare the user's input password and compare its hash-value to the expected value
            // if the credentials are correct, move on to the next activity
            // if not, send error message and re-do login
        if (email.isEmpty() || pw.isEmpty()) {
            Toast.makeText(this, "Please enter all required fields", Toast.LENGTH_SHORT).show();
        }
        else {
            firebaseAuth.signInWithEmailAndPassword(email, pw)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Intent goToMainScreen = new Intent(Login.this, MainActivity.class);
                                startActivity(goToMainScreen);
                            }
                            else {
                                Toast.makeText(Login.this, "Please enter all required fields", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

        /*
        // for now, hardcode validation process
        if (email.equals("arspace") && pw.equals("12345")) {
            Intent goToMainScreen = new Intent(Login.this, MainActivity.class);
            startActivity(goToMainScreen);
        }*/
    }

}
