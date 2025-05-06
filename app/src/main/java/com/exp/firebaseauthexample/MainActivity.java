package com.exp.firebaseauthexample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.exp.firebaseauthexample.vm.LoginViewModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private TextInputEditText emailText, passwordText;
    private LinearLayout container;
    private TextView successText;
    private TextView registerText;
    private ProgressBar progressBar;
    private LoginViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (FirebaseApp.getApps(this).isEmpty()) {
            FirebaseApp.initializeApp(this);
        }

        setContentView(R.layout.activity_main);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        //auth.useEmulator("10.0.2.2", 9099);
        //viewModel setup
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        viewModel.init(auth);

        emailText = findViewById(R.id.emailEditText);
        passwordText = findViewById(R.id.passwordEditText);
        Button loginButton = findViewById(R.id.loginButton);
        container = findViewById(R.id.formContainer);
        successText = findViewById(R.id.successText);
        progressBar = findViewById(R.id.progressBar);
        registerText = findViewById(R.id.registerText);

        loginButton.setOnClickListener(v -> authenticateUser());

        registerText.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        //observe livedata
        observeViewModel();
    }

    private void authenticateUser() {
        String email = emailText.getText().toString().trim();
        String password = passwordText.getText().toString().trim();

        viewModel.authenticateUser(email, password);
    }

    private void observeViewModel() {
        viewModel.getLoginSuccess().observe(this, success -> {
            if (success != null && success) {
                container.setVisibility(View.GONE);
                successText.setVisibility(View.VISIBLE);
            }
        });

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
    }

    private void showError(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }
}








