package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.security.MessageDigest;
import java.util.UUID;
import java.util.concurrent.Executors;

public class Register extends AppCompatActivity {

    private EditText editTextFirstName, editTextLastName, editTextEmail, editTextPassword;
    private Button registerButton;
    private UserDao userDao;

    private UserViewModel userViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Get UserDao instance
        userDao = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().userDao();
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        // Initialize the EditTexts and Button
        editTextFirstName = findViewById(R.id.firstName);
        editTextLastName = findViewById(R.id.lastName);
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        registerButton = findViewById(R.id.register);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        final String firstName = editTextFirstName.getText().toString().trim();
        final String lastName = editTextLastName.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        final String passwordHash = hashPassword(password);

        // Check if user already exists
        userViewModel.findByEmail(email).observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user != null) {
                    // User already exists
                    Toast.makeText(Register.this, "User already exists with this email.", Toast.LENGTH_SHORT).show();
                } else {
                    // No user with this email, create a new user
                    User newUser = new User(); // Assuming User class has a constructor or use setters here
                    newUser.id = UUID.randomUUID().toString(); // Generate a unique ID for the user
                    newUser.firstName = firstName;
                    newUser.lastName = lastName;
                    newUser.email = email;
                    newUser.passwordHash = passwordHash;
                    // Insert user in database
                    Executors.newSingleThreadExecutor().execute(new Runnable() {
                        @Override
                        public void run() {
                            userDao.insert(newUser);

                            // Inform user of success
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(Register.this, "User registered successfully!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                }
            }
        });
    }

    private String hashPassword(String password) {    try {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(password.getBytes("UTF-8"));
        StringBuilder hexString = new StringBuilder();

        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if(hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }

        return hexString.toString();
    } catch (Exception ex) {
        throw new RuntimeException(ex);
    }
    }
}