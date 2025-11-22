package com.example.camaraderie.event_screen.user_lists.accepted_or_cancelled_list;

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

import com.example.camaraderie.User;
import com.example.camaraderie.databinding.FragmentViewAcceptedOrCancelledBinding;
import com.example.camaraderie.event_screen.UserListType;
import com.example.camaraderie.event_screen.ViewListViewModel;

import java.util.ArrayList;

public class ViewAcceptedOrCancelledFragment extends Fragment {

    private FragmentViewAcceptedOrCancelledBinding binding;
    private ViewListViewModel vm;
    private ArrayList<User> displayedList;
    private String headerText;
    private String capacityText;
    private NavController nav;

    private ViewAcceptedOrCancelledListArrayAdapter adapter;

    public static ViewAcceptedOrCancelledFragment newInstance(UserListType type) {
        ViewAcceptedOrCancelledFragment fragment = new ViewAcceptedOrCancelledFragment();
        Bundle args = new Bundle();
        args.putString("type", type.name());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        vm = new ViewModelProvider(requireActivity()).get(ViewListViewModel.class);
        nav = NavHostFragment.findNavController(this);

        Bundle args = getArguments();
        if (args != null) {
            UserListType type = UserListType.valueOf(args.getString("type"));

            if (type.equals(UserListType.ACCEPTEDLIST)) {
                displayedList = vm.getAcceptedList();
                headerText = "Accepted Entrants:";
                capacityText = displayedList.size() + " / " + vm.getEventCapacity();

            } else if (type.equals(UserListType.CANCELLEDLIST)) {
                displayedList = vm.getCancelledList();
                headerText = "Cancelled Entrants:";
                capacityText = String.valueOf(displayedList.size());
            }
            else {
                throw new RuntimeException("Invalid argument for ViewAcceptedOrCancelledFragment");
            }

        }
        else {
            throw new RuntimeException("ViewAcceptedOrCancelledFragment must have arguments");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentViewAcceptedOrCancelledBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.acceptedOrCancelledBackButton.setOnClickListener(v -> {nav.popBackStack();});

        adapter = new ViewAcceptedOrCancelledListArrayAdapter(requireContext(), 0, displayedList);
        adapter.setNotifyOnChange(true);

        // fill text views
        binding.usersList.setAdapter(adapter);
        binding.cancelledOrAcceptedTextView.setText(headerText);
        binding.capacity.setText(capacityText);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
