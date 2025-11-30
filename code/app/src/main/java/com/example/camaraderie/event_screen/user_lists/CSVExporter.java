package com.example.camaraderie.event_screen.user_lists;


import static androidx.core.content.ContextCompat.startActivity;
import static com.example.camaraderie.main.Camaraderie.getContext;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.example.camaraderie.User;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class CSVExporter {

    public static void createCSV(ArrayList<User> users, String eventTitle) {

        StringBuilder sb = new StringBuilder();

        sb.append("Name,Email,Phone,UserId\n");

        for (User u : users) {
            sb.append(sanitize(u.getFirstName())).append(",");
            sb.append(sanitize(u.getEmail())).append(",");
            sb.append(sanitize(u.getPhoneNumber())).append(",");
            sb.append(sanitize(u.getDocRef().getId())).append("\n");
        }

        String csvData = sb.toString();
        saveCSVToDownloads(csvData, eventTitle + "_accepted.csv");
    }

    private static String sanitize(String s) {
        if (s == null) return "";
        return s.replace(",", " ");
    }

    private static void saveCSVToDownloads(String csv, String filename) {
        Context context = getContext();
        if (context == null) return;

        ContentValues values = new ContentValues();
        values.put(MediaStore.Downloads.DISPLAY_NAME, filename);
        values.put(MediaStore.Downloads.MIME_TYPE, "text/csv");
        values.put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);

        ContentResolver resolver = context.getContentResolver();
        Uri uri = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values);
        }

        try (OutputStream os = resolver.openOutputStream(uri)) {
            os.write(csv.getBytes(StandardCharsets.UTF_8));
            os.flush();

            Toast.makeText(context, "CSV exported to Downloads", Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            Log.e("CSV", "Failed to save file", e);
            Toast.makeText(context, "Failed to export CSV", Toast.LENGTH_SHORT).show();
        }
    }


}
