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
     * @param context app context
     * @param logs deserialized logs from db
     */
    public AdminNotificationLogArrayAdapter(@NonNull Context context, @NonNull ArrayList<NotificationData> logs) {
        super(context, 0, logs);
        setNotifyOnChange(true);
    }

    /**
     * This function inflates the view with the correct fragment
     * and initializes the date and initializes the info needed for each notification
     * @param position The position of the item within the adapter's data set of the item whose view
     *        we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *        is non-null and of an appropriate type before using. If it is not possible to convert
     *        this view to display the correct data, this method can create a new view.
     *        Heterogeneous lists can specify their number of view types, so that this View is
     *        always of the right type (see {@link #getViewTypeCount()} and
     *        {@link #getItemViewType(int)}).
     * @param parent The parent that this view will eventually be attached to
     * @return created item view
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