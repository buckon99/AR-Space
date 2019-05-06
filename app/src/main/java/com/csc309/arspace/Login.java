package com.csc309.arspace;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Login extends AppCompatActivity {

    private EditText userID;
    private EditText password;
    private Button login;
    private TextView signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userID = findViewById(R.id.userID);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login_button);
        signup = findViewById(R.id.signup_button);

        // if you click the login button...
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate(userID.getText().toString(), password.getText().toString());
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
    private void validate(String id, String pw) {
        // TODO: connect to a database and create a "real" login system
        // get the correct password hash-value from database
        // compare the user's input password and compare its hash-value to the expected value
            // if the credentials are correct, move on to the next activity
            // if not, send error message and re-do login

        // for now, hardcode validation process
        if (id.equals("arspace") && pw.equals("12345")) {
            Intent goToMainScreen = new Intent(Login.this, MainActivity.class);
            startActivity(goToMainScreen);
        }
    }

}
