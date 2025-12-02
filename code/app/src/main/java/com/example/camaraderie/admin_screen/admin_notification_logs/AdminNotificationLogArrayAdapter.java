package com.example.camaraderie.admin_screen.admin_notification_logs;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.Locale;

import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.camaraderie.databinding.FragmentAdminViewNotificationLogsItemBinding;
import com.example.camaraderie.notifications.NotificationData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * This class initializes an array adapter for our list of admin notification logs
 */

public class AdminNotificationLogArrayAdapter extends ArrayAdapter<NotificationData> {

    /**
     * This constructor initializes the adapter
     * @param context
     * @param logs
     */
    public AdminNotificationLogArrayAdapter(@NonNull Context context, @NonNull ArrayList<NotificationData> logs) {
        super(context, 0, logs);
        setNotifyOnChange(true);
    }

    /**
     * This function inflates the view with the correct fragment and initializes the date and initializes the info needed for each notification
     * @param position
     * @param convertView
     * @param parent
     * @return
     */

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        FragmentAdminViewNotificationLogsItemBinding binding;

        if (convertView == null) {
            binding = FragmentAdminViewNotificationLogsItemBinding.inflate(LayoutInflater.from(getContext()), parent, false);
            convertView = binding.getRoot();
            convertView.setTag(binding);
        } else {
            binding = (FragmentAdminViewNotificationLogsItemBinding) convertView.getTag();
        }

        NotificationData notif = getItem(position);
        if (notif == null) return convertView;

        // Format timestamp, since iirc the notif model has timestamps
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

        String formattedTime = sdf.format(notif.getTimestamp());


        binding.logTimestamp.setText(formattedTime);
        binding.logId.setText("ID: " + notif.getRef().getId());
        binding.logTitle.setText(notif.getTitle());
        binding.logMessage.setText(notif.getMessage());

        return convertView;
    }
}