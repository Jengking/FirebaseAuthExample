package com.exp.firebaseauthexample;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.exp.firebaseauthexample.vm.LoginViewModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText emailText, passwordText;
    private LinearLayout container;
    private TextView successText;
    private ProgressBar progressBar;
    private LoginViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (FirebaseApp.getApps(this).isEmpty()) {
            FirebaseApp.initializeApp(this);
        }

        setContentView(R.layout.activity_register);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        viewModel.init(auth);

        emailText = findViewById(R.id.emailEditText);
        passwordText = findViewById(R.id.passwordEditText);
        Button registerButton = findViewById(R.id.loginButton);
        container = findViewById(R.id.formContainer);
        successText = findViewById(R.id.successText);
        progressBar = findViewById(R.id.progressBar);

        registerButton.setOnClickListener(v -> {
            registerUser();
        });
        observeViewModel();
    }

    private void registerUser() {
        String email = emailText.getText().toString().trim();
        String password = passwordText.getText().toString().trim();

        viewModel.registerUser(email, password);
    }

    private void observeViewModel() {
        viewModel.getErrorMessage().observe(this, errorMessage -> {
            if (errorMessage != null && !errorMessage.isEmpty()) {
                showError(errorMessage);
            }
        });

        viewModel.getIsLoading().observe(this, isLoading -> {
            if (isLoading != null) {
                progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            }
        });

        viewModel.getRegisterSuccess().observe(this, success -> {
            if (success != null && success) {
                finish();
            }
        });
    }

    private void showError(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }
}