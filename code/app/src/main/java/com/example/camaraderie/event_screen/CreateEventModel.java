package com.example.camaraderie.event_screen;

import static androidx.activity.result.ActivityResultCallerKt.registerForActivityResult;

import android.net.Uri;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts

import com.example.camaraderie.dashboard.EventViewModel;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;


public class CreateEventModel extends ViewModel {

    private final FirebaseStorage storage = FirebaseStorage.getInstance();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    private final MutableLiveData<Uri> uploadImageUrl = new MutableLiveData<>();

    public void uploadEventImage(Uri imageUri){
       uploadImageUrl(imageUri, url -> uploadImageUrl.postValue(url));
    }

    public LiveData<Uri> getUploadedImageUrl(){
        return uploadImageUrl;
    }

    public void uploadImage(Uri fileUri, Consumer<Uri> onUploaded){
        String filename = UUID.randomUUID().toString() + ".jpg";
        StorageReference ref = storage.getReference().child("event_images/" + filename);
        ref.putFile(fileUri).addOnSuccessListener(taskSnapshot -> ref.getDownloadUrl().addOnSuccessListener(uri -> {
            Map<String, Object> data = new HashMap<>();
            data.put("imageUrl", uri.toString());
            data.put("timestamp", System.currentTimeMillis());
            db.collection("event_images").add(data);
            onUploaded.accept(uri);
        }));
    }

    public LiveData<List<String>> getAllImageUrls(){
        MutableLiveData<List<String>> liveData = new MutableLiveData<>();
        db.collection("event_images").get().addOnSuccessListener(queryDocumentSnapshots ->{
            List <String> urls = new ArrayList<>();
            for(DocumentSnapshot doc : queryDocumentSnapshots){
                urls.add(doc.getString("imageUrl"));
            }
            liveData.postValue(urls);
        });
        return liveData;
    }

    public LiveData<List<String>> getEventImageUrls(){
        return getAllImageUrls();
    }


}
