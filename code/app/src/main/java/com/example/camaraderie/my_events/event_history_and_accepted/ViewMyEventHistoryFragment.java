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

/**
 * This class sets up the event history fragment
 */
public class ViewMyEventHistoryFragment extends Fragment {

    private FragmentViewMyEventHistoryBinding binding;
    private MyAcceptedEventsArrayAdapter adapter;
    private SharedEventViewModel svm;
    private MyEventsViewModel vm;
    private NavController nav;

    /**
     * onCreate sets up the nav and the ViewModelProviders
     * @param savedInstanceState If the fragment is being re-created from
     * a previous saved state, this is the state.
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        nav = NavHostFragment.findNavController(this);
        vm = new ViewModelProvider(requireActivity()).get(MyEventsViewModel.class);
        svm = new ViewModelProvider(requireActivity()).get(SharedEventViewModel.class);
    }

    /**
     * OnCreateView inflates the view with XML
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return
     * Returns the root of the binding as a view
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentViewMyEventHistoryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    /**
     * onViewCreated sets up the list with the array adapter and displays the list
     * @param view The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     */

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

    /**
     * sets binding to null
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
