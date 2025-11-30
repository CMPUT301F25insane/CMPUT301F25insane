package com.example.camaraderie.my_events.event_history_and_accepted;

import static com.example.camaraderie.main.Camaraderie.getUser;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.camaraderie.SharedEventViewModel;
import com.example.camaraderie.databinding.FragmentViewMyEventHistoryBinding;
import com.example.camaraderie.my_events.MyEventsViewModel;

import java.util.ArrayList;

public class ViewMyEventHistoryFragment extends Fragment {

    private FragmentViewMyEventHistoryBinding binding;
    private MyAcceptedEventsArrayAdapter adapter;
    private SharedEventViewModel svm;
    private MyEventsViewModel vm;
    private NavController nav;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        nav = NavHostFragment.findNavController(this);
        vm = new ViewModelProvider(requireActivity()).get(MyEventsViewModel.class);
        svm = new ViewModelProvider(requireActivity()).get(SharedEventViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentViewMyEventHistoryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new MyAcceptedEventsArrayAdapter(requireContext(), new ArrayList<>(), svm, nav);
        adapter.setNotifyOnChange(true);
        binding.historyListView.setAdapter(adapter);

        vm.getUserEventsFromList(getUser().getUserEventHistory(),
                events -> {
                    adapter.addAll(events);
                });

        binding.historyBackButton.setOnClickListener(v -> nav.popBackStack());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
