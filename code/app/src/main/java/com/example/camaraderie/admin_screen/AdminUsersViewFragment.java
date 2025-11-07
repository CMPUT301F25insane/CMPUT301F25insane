package com.example.camaraderie.admin_screen;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedDispatcher;
import com.example.camaraderie.R;
import com.example.camaraderie.User;
import com.example.camaraderie.databinding.FragmentAdminDashboardBinding;
import com.example.camaraderie.databinding.FragmentAdminUsersViewBinding;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

/**
 * AdminUsersViewFragment extends Fragment and is used for viewing the list of registered users and having the ability
 * to look their profile if the admin wishes to do so
 */

public class AdminUsersViewFragment extends Fragment {

    private FragmentAdminUsersViewBinding binding;
    FirebaseFirestore db;
    private CollectionReference usersRef;
    private ListenerRegistration userListener;
    private ArrayList<User> userArrayList;
    private UserArrayAdaptor userArrayAdapter;
    private NavController nav;
//
//    public AdminUsersViewFragment() {
//        // Required empty public constructor
//    }

    /**
     * onCreateView initializes the fragment and sets the binding to the appropriate xml
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     * @return
     * We return the root of the binding as a view
     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAdminUsersViewBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    /**
     * onViewCreated initializes the components of the xml to be used for backend implementation
     * We first initialize the database db and we setup the collection reference to the Users collecton
     * We setup the usersArrayList to an empty one
     * We setup nav for navigation
     * We use the userArrayAdapter and set it up with our arraylist
     * Use binding to setup the adapter
     * we load the data using the loadlist function
     * We setup the back button using naviagtion
     * @param view The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     */

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = FirebaseFirestore.getInstance();
        usersRef = db.collection("Users");

        userArrayList = new ArrayList<User>();
        nav = NavHostFragment.findNavController(AdminUsersViewFragment.this);

        userArrayAdapter = new UserArrayAdaptor(requireContext(), userArrayList, nav); //,nav in constructor

        binding.list.setAdapter(userArrayAdapter);

        //for loading data
        loadList();

        binding.backButton.setOnClickListener( v ->
                nav.navigate(R.id.action_admin_user_data_screen_view_to_admin_main_screen)
                //nav.popBackStack()
        );
    }

    /**
     * We create a private function loadlist that allows us to use a snapshot listener to find
     * Users and all them to our arraylist
     * We then refresh the list
     */

    private void loadList(){
        userListener = usersRef.addSnapshotListener((value, error) -> {
            if (error != null) {
                Log.e("Firestore", error.toString());
                throw new RuntimeException("Fuck you (users version");
            }
            if (value != null && !value.isEmpty()) {
                userArrayList.clear();
                for (QueryDocumentSnapshot snapshot: value){
                    User user = snapshot.toObject(User.class);
                    userArrayList.add(user);
                }

                userArrayAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * onDestroyView removes the listener and sets binding to null to avoid memory leaks
     */

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (userListener != null) {userListener.remove();}
        binding = null;
    }

    /**
     * onDestory sets the binding to null to avoid memory leaks
     */

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}