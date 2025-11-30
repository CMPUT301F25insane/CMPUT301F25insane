package com.example.camaraderie.admin_screen;

import static com.example.camaraderie.main.MainActivity.user;

import static com.example.camaraderie.image_stuff.ImageHandler.deleteEventImage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;

import com.bumptech.glide.Glide;
import com.example.camaraderie.R;
import com.example.camaraderie.Event;
import com.example.camaraderie.SharedEventViewModel;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

/**
 * EventArrayAdapter extends ArrayAdapter to be used to list the events that the admin can view or delete
 */
public class PictureArrayAdapter extends ArrayAdapter<Event> {

    FirebaseFirestore db;
    SharedEventViewModel svm;
    ArrayList<Event> events;

    /**
     * This is a constructor that initializes all the required attributes for the array adapter to to function how we
     * want it to
     * @param context
     * @param events
     * @param svm
     */

    public PictureArrayAdapter(@NonNull Context context, ArrayList<Event> events, SharedEventViewModel svm){
        super(context, 0, events);
        this.db = FirebaseFirestore.getInstance();
        this.events = events;

        this.svm = svm;
    }

    /**
     * getView is used for initializing the view of events for our custom display of events
     * We initialize the view using the layout inflater
     * We set all the textviews to the values of the events like their name, registration deadline etc
     * @param position
     * @param convertView
     * @param parent
     * @return
     * We return a view for each list item
     */

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_admin_images_view_item, parent, false);
        }

        Event event = getItem(position);
        if (event == null) {
            return view;
        }

        ImageView imageView = view.findViewById(R.id.imageView);
        Button remove = view.findViewById(R.id.RemoveButton);


        Glide.with(this.getContext()).load(event.getImageUrl()).into(imageView);


        /**
         * Lastly we have a remove button that the admin can use to remove a event from the the collection if it violates
         * guidelines or for whatever reason the admin has
         */

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteEventImage(event);
                event.setImageUrl(null);
                Toast.makeText(getContext(), "Image Removed", Toast.LENGTH_SHORT).show();
                imageView.setImageBitmap(null);
                event.getEventDocRef().update("imageUrl", FieldValue.delete());
            }
        });
        return view;
    }
}
