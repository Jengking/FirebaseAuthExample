package com.exp.firebaseauthexample;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import androidx.lifecycle.Observer;

import com.exp.firebaseauthexample.vm.LoginViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class LoginUnitTest {

    @Mock
    private FirebaseAuth firebaseAuth;

    @Mock
    private Observer<Boolean> loginSuccessObserver;

    @Mock
    private Observer<Boolean> isLoadingObserver;

    private LoginViewModel viewModel;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        viewModel = new LoginViewModel();
        viewModel.init(firebaseAuth);
        viewModel.getLoginSuccess().observeForever(loginSuccessObserver);
        viewModel.getIsLoading().observeForever(isLoadingObserver);
    }

    @Test
    public void  testLoginSuccess() {
        String email = "john@smith.com";
        String password = "abc123";

        FirebaseUser mockFirebaseUser = mock(FirebaseUser.class);
        Task<AuthResult> mockTask = mockSuccessfulTask(mock(AuthResult.class));

        when(firebaseAuth.signInWithEmailAndPassword(email, password)).thenReturn(mockTask);

        viewModel.authenticateUser(email, password);

        verify(isLoadingObserver).onChanged(true);
        verify(firebaseAuth).signInWithEmailAndPassword(email, password);
    }

    private <T> Task<T> mockSuccessfulTask(T result) {
        Task<T> mockTask = mock(Task.class);
        when(mockTask.isSuccessful()).thenReturn(true);
        when(mockTask.getResult()).thenReturn(result);
        doAnswer(invocation -> {
            ((OnCompleteListener<T>) invocation.getArguments()[0]).onComplete(mockTask);
            return null;
        }).when(mockTask).addOnCompleteListener(any());
        return mockTask;
    }
}






