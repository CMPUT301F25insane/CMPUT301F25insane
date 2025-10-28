package com.example.camaraderie;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.camaraderie.databinding.FragmentSignupBinding;

public class SignupFragment extends Fragment {

    private FragmentSignupBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSignupBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String emailText = String.valueOf(binding.EmailText.getText());
        String username  = String.valueOf(binding.username.getText());
        String password  = String.valueOf(binding.password.getText());
        String confword  = String.valueOf(binding.confirmPassword.getText());

        if (!password.equals(confword)) {
            throw new RuntimeException("Passwords do not match");
        }


    }
}
