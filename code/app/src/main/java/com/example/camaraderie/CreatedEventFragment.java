package com.example.camaraderie;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.ContentView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.camaraderie.databinding.CreatedEventBinding;

public class CreatedEventFragment extends Fragment {

    private CreatedEventBinding binding;

    @Override
    public View onCreateView (LayoutInflater inflater,
                              ViewGroup container,
                              Bundle savedInstanceState) {
        binding = CreatedEventBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /**
         * I need the current User's UID to let them join events.
         */


        /**
         * I need the Event object to fill all the text views, show the attendees, and show the poster
         */
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
