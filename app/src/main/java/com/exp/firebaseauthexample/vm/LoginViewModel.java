package com.exp.firebaseauthexample.vm;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class LoginViewModel extends ViewModel {

    //using this method to validate email - because on test unit unable to run the native email validator.
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

    private FirebaseAuth firebaseAuth;
    private final MutableLiveData<Boolean> loginSuccess = new MutableLiveData<>();
    private final MutableLiveData<Boolean> registerSuccess = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<FirebaseUser> currentUser = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    public void init(FirebaseAuth auth) {
        this.firebaseAuth = auth;
    }

    public void authenticateUser(String email, String password) {
        if (validationChecks(email, password)) {
            isLoading.setValue(true);

            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener( task -> {
                        isLoading.setValue(false);
                        if (task.isSuccessful()) {
                            currentUser.setValue(firebaseAuth.getCurrentUser());
                            loginSuccess.setValue(true);
                        } else {
                            loginSuccess.setValue(false);
                            if (task.getException() != null) {
                                errorMessage.setValue(task.getException().getMessage());
                            } else {
                                errorMessage.setValue("Authentication failed");
                            }
                        }
                    });
        }
    }

    public void registerUser(String email, String password) {
        if (validationChecks(email, password)) {
            isLoading.setValue(true);
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        isLoading.setValue(false);
                        if (task.isSuccessful()) {
                            registerSuccess.setValue(true);
                        } else {
                            registerSuccess.setValue(false);
                            if (task.getException() != null) {
                                errorMessage.setValue(task.getException().getMessage());
                            } else {
                                errorMessage.setValue("Registration failed");
                            }
                        }
                    });
        }
    }

    private boolean validationChecks(String email, String password) {
        if (email.isEmpty()) {
            errorMessage.setValue("Email is required");
            return false;
        }

        if (!isValidEmail(email)) {
            errorMessage.setValue("Invalid email format");
            return false;
        }

        if (password.isEmpty()) {
            errorMessage.setValue("Password is required");
            return false;
        }
        return true;
    }

    //LiveData getters
    public LiveData<Boolean> getLoginSuccess() {
        return loginSuccess;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<FirebaseUser> getCurrentUser() {
        return currentUser;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<Boolean> getRegisterSuccess() {
        return registerSuccess;
    }

    public boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }
}
