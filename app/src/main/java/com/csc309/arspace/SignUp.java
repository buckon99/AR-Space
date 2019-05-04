package com.csc309.arspace;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SignUp extends AppCompatActivity {

    private EditText userID;
    private EditText email;
    private EditText password;
    private EditText confirmPassword;
    private Button signup;
    private TextView login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        userID = findViewById(R.id.userID);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirm_password);
        signup = findViewById(R.id.signup_button);
        login = findViewById(R.id.login_button);

        // if you click sign up button...
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (confirmPassword(password.getText().toString(), confirmPassword.getText().toString()) == false) {
                    // TODO: do something to tell user that the passwords don't match
                }

            }
        });

        // if you click the login text...
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToLoginScreen = new Intent(SignUp.this, Login.class);
                startActivity(goToLoginScreen);
            }
        });
    }

    // checks if the two given passwords match
    private boolean confirmPassword(String pw, String pwConf) {
        return (pw.equals(pwConf));
    }

    // returns
    private boolean validateEntries() {
        String userID = this.userID.getText().toString();
        String email = this.email.getText().toString();
        String password = this.password.getText().toString();

        return (!userID.isEmpty() && !email.isEmpty() && !password.isEmpty());
    }
}
