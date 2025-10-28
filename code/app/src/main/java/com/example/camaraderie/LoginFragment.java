package com.example.camaraderie;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class LoginFragment extends Fragment {

    FirebaseAuth auth = FirebaseAuth.getInstance();


    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EditText email = view.findViewById(R.id.email);
        EditText password = view.findViewById(R.id.password);
        Button loginButton = view.findViewById(R.id.Log_In_Button);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Email = email.getText().toString();
                String Password = password.getText().toString();

                auth.signInWithEmailAndPassword(Email, Password).addOnCompleteListener( task -> {
                    if(task.isSuccessful()){
                        Toast.makeText(requireContext(), "Login Good", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(requireContext(), "Failed!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}