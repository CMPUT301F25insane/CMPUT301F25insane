package com.example.camaraderie.image_stuff;

import android.net.Uri;
import android.util.Log;

import com.example.camaraderie.Event;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import android.util.Log;
import android.widget.ImageView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


/**
 * This handles uploading and deleting images from the cloud
 */
public class ImageHandler {
    private static final FirebaseStorage storage = FirebaseStorage.getInstance();

    public interface UploadCallback {
        void onSuccess(String downloadUrl);
        void onFailure(Exception e);
    }

    /**
     * Uploads an image to Firebase Storage for the given event and puts the image url into the event
     * @param event The event object
     * @param imageUri Local image URI selected by the user
     * @param callback Result callback
     */
    public static void uploadEventImage(Event event, Uri imageUri, UploadCallback callback) {

        if (imageUri == null) {
            callback.onFailure(new IllegalArgumentException("Image URI is null"));
            return;
        }

        // The path where the file will be stored in Firebase Storage
        String eventId = event.getEventDocRef().getId();
        StorageReference ref = storage.getReference()
                .child("events")
                .child(eventId)
                .child("cover.jpg");

        // Upload the file
        ref.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {

                    // Now get the download URL
                    ref.getDownloadUrl()
                            .addOnSuccessListener(uri -> {
                                String url = uri.toString();
                                Log.d("EventImageHandler", "Image uploaded: " + url);
                                callback.onSuccess(url);
                            })
                            .addOnFailureListener(callback::onFailure);

                })
                .addOnFailureListener(callback::onFailure);
    }

    /**
     * Deletes the event's existing image from Firebase Storage
     */
    public static void deleteEventImage(Event event) {

        String url = event.getImageUrl();
        if (url == null || url.isEmpty()) return;

        StorageReference ref = storage.getReferenceFromUrl(url);

        ref.delete()
                .addOnSuccessListener(v -> Log.d("EventImageHandler", "Event image deleted"))
                .addOnFailureListener(e ->
                        Log.e("EventImageHandler", "Failed to delete image", e)
                );
    }

}
