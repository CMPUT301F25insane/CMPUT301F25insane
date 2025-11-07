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

import com.example.camaraderie.R;
import com.example.camaraderie.User;
import com.example.camaraderie.databinding.FragmentAdminDashboardBinding;
import com.example.camaraderie.databinding.FragmentAdminUsersViewBinding;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class AdminUsersViewFragment extends Fragment {

    private FragmentAdminUsersViewBinding binding;
    FirebaseFirestore db;
    private CollectionReference usersRef;
    private ArrayList<User> userArrayList;
    private UserArrayAdaptor userArrayAdapter;
    private NavController nav;
//
//    public AdminUsersViewFragment() {
//        // Required empty public constructor
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAdminUsersViewBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = FirebaseFirestore.getInstance();
        usersRef = db.collection("Users");

        userArrayList = new ArrayList<User>();
        nav = NavHostFragment.findNavController(AdminUsersViewFragment.this);

        userArrayAdapter = new UserArrayAdaptor(requireContext(), userArrayList, nav);

        binding.list.setAdapter(userArrayAdapter);

        //for loading data
        loadList();

        binding.backButton.setOnClickListener( v ->
                NavHostFragment.findNavController(this).popBackStack()
        );
    }
    private void loadList(){
        usersRef.addSnapshotListener((value, error) -> {
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
}