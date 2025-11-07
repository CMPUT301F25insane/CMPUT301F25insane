package com.example.camaraderie.admin_screen;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.camaraderie.R;
import com.example.camaraderie.User;
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

    public AdminUsersViewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAdminUsersViewBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = FirebaseFirestore.getInstance();
        usersRef = db.collection("Users");

        userArrayList = new ArrayList<>();

        userArrayAdapter = new UserArrayAdaptor(requireContext(),userArrayList);

        binding.list.setAdapter(userArrayAdapter);

        //for loading data
        loadList();

        binding.backButton.setOnClickListener( v ->
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_admin_user_data_screen_view_to_admin_main_screen)
        );
    }
    private void loadList(){
        usersRef.addSnapshotListener((value, error) -> {
            if (error != null) {
                Log.e("Firestore", error.toString());
                return;
            }
            if (value != null) {
                userArrayList.clear();
                for (QueryDocumentSnapshot snapshot: value){
                    User user = snapshot.toObject(User.class);
                    userArrayList.add(user);
                }
                userArrayAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Avoid memory leaks
    }
}