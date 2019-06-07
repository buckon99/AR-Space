package com.csc309.arspace;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;

public class SignUp extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private EditText confirmPassword;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        firebaseAuth = FirebaseAuth.getInstance();

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirm_password);
        Button signup = findViewById(R.id.signup_button);
        TextView login = findViewById(R.id.login_button);

        // if you click sign up button...
        signup.setOnClickListener(view -> {
            // check if passwords match AND if all fields are entered
            if (validateEntries(this.email.getText().toString().trim(),
                    this.password.getText().toString().trim(),
                    this.confirmPassword.getText().toString().trim()) &&
                    confirmPassword(password.getText().toString().trim(),
                            confirmPassword.getText().toString().trim())) {
                firebaseAuth.createUserWithEmailAndPassword(email.getText().toString().trim(),
                        password.getText().toString().trim()).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                // THIS IS TEST. DELETE LATER
                                // adds random sample products to the user's database
                                //new Search();
                                // notify the user that registration has been successful
                                Toast.makeText(SignUp.this, "Successful", Toast.LENGTH_SHORT).show();
                                // take user to main screen
                                Intent goToMainScreen = new Intent(SignUp.this, MainActivity.class);
                                startActivity(goToMainScreen);
                            }
                            else {
                                Toast.makeText(SignUp.this, "Unsuccessful", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        // if you click the login text...
         login.setOnClickListener(view -> {
             Intent goToLoginScreen = new Intent(SignUp.this, Login.class);
             startActivity(goToLoginScreen);
         });
    }

    // checks if the two given passwords match
    public boolean confirmPassword(String pw, String pwConf) {
        if (!(pw.equals(pwConf))) {
            Toast.makeText(this, "Passwords don't match", Toast.LENGTH_SHORT).show();
            return false;
        }
        else {
            return true;
        }
    }

    // checks if all required fields are entered
    public boolean validateEntries(String email, String password, String confirmPassword) {

        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Please enter all required fields", Toast.LENGTH_SHORT).show();
            return false;
        }
        else {
            return true;
        }
    }
}
