package com.example.camaraderie;

import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

/*
 *Updates the current information of the user
 *  */
public class UpdateUser extends AppCompatActivity {
    private FirebaseFirestore db;
    private CollectionReference usersRef;

    private DocumentReference userDocRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_user);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = FirebaseFirestore.getInstance();
        usersRef = db.collection("Users");


        EditText name1 = findViewById(R.id.update_name);
        EditText email1 = findViewById(R.id.update_email);
        EditText phoneNo1 = findViewById(R.id.update_phone_no);
        EditText address1 = findViewById(R.id.update_address);

        Button save = findViewById(R.id.update_save);
        Button cancel = findViewById(R.id.update_cancel);


        //String androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        String userId = Settings.Secure.ANDROID_ID;

        userDocRef = usersRef.document(userId);

        userDocRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String name3 = documentSnapshot.getString("Full Name");
                String email3 = documentSnapshot.getString("Email");
                String phone3 = documentSnapshot.getString("Phone Number");
                String address3 = documentSnapshot.getString("Address");

                name1.setText(name3);
                email1.setText(email3);
                phoneNo1.setText(phone3);
                address1.setText(address3);
            } else {
                Toast.makeText(UpdateUser.this, "No existing user data found.", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e ->
                Toast.makeText(UpdateUser.this, "Failed to load data: " + e.getMessage(), Toast.LENGTH_SHORT).show()
        );

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name2 = name1.getText().toString().trim();
                String email2 = email1.getText().toString().trim();
                String phoneNo2 = phoneNo1.getText().toString().trim();
                String address2 = address1.getText().toString().trim();

                User user = new User(name2, phoneNo2, email2, address2, userId);
                userDocRef
                        .set(user)
                        //.update("Full Name", name2, "Phone Number", phoneNo2, "Email", email2, "Address", address2)
                        .addOnSuccessListener(aVoid ->
                                Toast.makeText(UpdateUser.this, "Profile updated!", Toast.LENGTH_SHORT).show()
                        )
                        .addOnFailureListener(e ->
                                Toast.makeText(UpdateUser.this, "Update failed: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                        );
            }
        });

        cancel.setOnClickListener(v -> {
            finish();
        });
    }
}