package com.example.cinefast;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Signup extends AppCompatActivity {

    private EditText etEmail, etPassword, etConfirm;
    private Button btnSignup, bBack;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("Users");

        etEmail = findViewById(R.id.etEmailSignup);
        etPassword = findViewById(R.id.etPasswordSignup);
        etConfirm = findViewById(R.id.etConfirmPassword);
        btnSignup = findViewById(R.id.btnSignup);
        bBack = findViewById(R.id.bBackSignup);

        bBack.setOnClickListener(v -> finish());

        btnSignup.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String pass = etPassword.getText().toString().trim();
            String confirm = etConfirm.getText().toString().trim();

            if (email.isEmpty() || pass.isEmpty() || confirm.isEmpty())
                Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
            else if (pass.length() < 8)
                Toast.makeText(this, "Password must be at least 8 characters", Toast.LENGTH_SHORT).show();
            else if (!pass.equals(confirm))
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            else
                register(email, pass);
        });
    }

    private void register(String email, String pass) {
        mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnSuccessListener(authResult -> {
                    String uid = mAuth.getCurrentUser().getUid();

                    HashMap<String, Object> map = new HashMap<>();
                    map.put("email", email);

                    mDatabase.child(uid).setValue(map).addOnSuccessListener(unused -> {
                        getSharedPreferences("Login Preferences", MODE_PRIVATE)
                                .edit().putBoolean("isLoggedIn", true).apply();

                        startActivity(new Intent(Signup.this, MainActivity.class));
                        finish();
                    });
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show());
    }
}