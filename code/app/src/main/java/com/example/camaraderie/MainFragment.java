package com.example.camaraderie;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.camaraderie.databinding.FragmentMainTestBinding;

import java.util.ArrayList;

public class MainFragment extends Fragment {

    private FragmentMainTestBinding binding;
    private DashboardEventArrayAdapter dashboardEventArrayAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentMainTestBinding.inflate(getLayoutInflater());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //ArrayList<Event> localEvents = binding.getRoot().getLocalEvents();

        //dashboardEventArrayAdapter = new DashboardEventArrayAdapter(this, localEvents);
        binding.eventsList.setAdapter(dashboardEventArrayAdapter);
    }
}
