package com.example.camaraderie.event_screen.user_lists;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavHost;
import androidx.navigation.fragment.NavHostFragment;

import com.example.camaraderie.R;
import com.example.camaraderie.SharedEventViewModel;
import com.example.camaraderie.databinding.FragmentViewListsTestingInterfaceBinding;
import com.example.camaraderie.event_screen.UserListType;
import com.example.camaraderie.event_screen.ViewListViewModel;
import com.example.camaraderie.event_screen.user_lists.accepted_or_cancelled_list.ViewAcceptedOrCancelledFragment;
import com.example.camaraderie.event_screen.user_lists.waitlist_or_selected.ViewWaitlistOrSelectedFragment;
import com.example.camaraderie.event_screen.user_view.UserViewEventFragment;

/**
 * temporary testing interface. most functionalities will be moved to a roulette style thign in the future
 */
public class ListTestingInterfaceFragment extends Fragment {

    private FragmentViewListsTestingInterfaceBinding binding;
//    private ViewListViewModel listViewModel;
    private NavController nav;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        nav = NavHostFragment.findNavController(this);
//        listViewModel = new ViewModelProvider(requireActivity()).get(ViewListViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentViewListsTestingInterfaceBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        binding.listTestingBackButton.setOnClickListener(v -> {nav.popBackStack();});
        SharedEventViewModel svm = new ViewModelProvider(requireActivity()).get(SharedEventViewModel.class);
        ViewListViewModel vm = new ViewModelProvider(requireActivity()).get(ViewListViewModel.class);
        svm.getEvent().observe(getViewLifecycleOwner(), event -> {
            vm.setEvent(event);
            setButtonBindings();
        });


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

    private void setButtonBindings() {
        binding.viewAcceptedButton.setOnClickListener(v -> {
            nav.navigate(
                    R.id.view_accepted_or_cancelled,
                    ViewAcceptedOrCancelledFragment.newInstance(UserListType.ACCEPTEDLIST).getArguments()
            );

        });

        binding.viewCancelledButton.setOnClickListener(v -> {
            nav.navigate(
                    R.id.view_accepted_or_cancelled,
                    ViewAcceptedOrCancelledFragment.newInstance(UserListType.CANCELLEDLIST).getArguments()
            );

        });

        binding.ViewSelectedButton.setOnClickListener(v -> {
            nav.navigate(
                    R.id.fragment_view_waitlist_or_selected,
                    ViewWaitlistOrSelectedFragment.newInstance(UserListType.SELECTEDLIST).getArguments()
            );

        });

        binding.ViewWaitlistButton.setOnClickListener(v -> {
            nav.navigate(
                    R.id.fragment_view_waitlist_or_selected,
                    ViewWaitlistOrSelectedFragment.newInstance(UserListType.WAITLIST).getArguments()
            );

        });
    }
}
