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
 * This fragment initializes the list of buttons that an organizer can press to go to user list
 */
public class ListTestingInterfaceFragment extends Fragment {

    private FragmentViewListsTestingInterfaceBinding binding;
//    private ViewListViewModel listViewModel;
    private NavController nav;

    /**
     * onCreate initializes the nav
     * @param savedInstanceState If the fragment is being re-created from
     * a previous saved state, this is the state.
     */

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        nav = NavHostFragment.findNavController(this);
//        listViewModel = new ViewModelProvider(requireActivity()).get(ViewListViewModel.class);
    }

    /**
     * onCreateView intializes the actual XML to be inflates and displayed
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return
     * Returns the root of binding as a view
     */

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentViewListsTestingInterfaceBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    /**
     * onViewCreated initalizes the back button and setups up the view models
     * @param view The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     */

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

    /**
     * setButtonBindings method allows for us to setup the onClickListeners for each button to take the user to
     * a specified list
     */

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
