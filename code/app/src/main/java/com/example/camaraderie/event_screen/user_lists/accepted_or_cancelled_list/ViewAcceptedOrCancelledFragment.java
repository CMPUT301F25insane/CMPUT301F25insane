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

/**
 * This class allows for the organizer to view the accepted or cancelled list
 */
public class ViewAcceptedOrCancelledFragment extends Fragment {

    private FragmentViewAcceptedOrCancelledBinding binding;
    private ViewListViewModel vm;
    private ArrayList<User> displayedList;
    private String headerText;
    private String capacityText;
    private NavController nav;

    private ViewAcceptedOrCancelledListArrayAdapter adapter;

    /**
     * This static method initalizws the bundle and its arguments to be used on a new
     * instance of this class
     * @param type userlist type to determine list
     * @return instance of fragment with arguments
     */
    public static ViewAcceptedOrCancelledFragment newInstance(UserListType type) {
        ViewAcceptedOrCancelledFragment fragment = new ViewAcceptedOrCancelledFragment();
        Bundle args = new Bundle();
        args.putString("type", type.name());
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Inflate the current view with the required XML file
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return
     * Return the root of the binding as a view
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentViewAcceptedOrCancelledBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    /**
     * onViewCreated displays the lists of accepted or cancelled, depending on the list type
     * It sets up the array adapter, the header text and the capacity
     * @param view The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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

        binding.acceptedOrCancelledBackButton.setOnClickListener(v -> {nav.popBackStack();});
        adapter = new ViewAcceptedOrCancelledListArrayAdapter(requireContext(), 0, displayedList);
        adapter.setNotifyOnChange(true);

        // fill text views
        binding.usersList.setAdapter(adapter);
        binding.cancelledOrAcceptedTextView.setText(headerText);
        binding.capacity.setText(capacityText);
    }

    /**
     * When we destroy the view ensure that the binding is null
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
