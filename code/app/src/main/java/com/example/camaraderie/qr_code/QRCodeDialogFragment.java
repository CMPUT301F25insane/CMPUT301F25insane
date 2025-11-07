package com.example.camaraderie.qr_code;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.camaraderie.R;
import com.google.zxing.WriterException;

public class QRCodeDialogFragment extends DialogFragment {

    private ImageView imageView;
    private String eventId;

    public static QRCodeDialogFragment newInstance(String eventId){
        QRCodeDialogFragment fragment = new QRCodeDialogFragment();
        Bundle args = new Bundle();
        Log.d("EVENT ID BEING SENT TO THE QRCODE DIALOG FRAGMENT", eventId);
        args.putString("eventId", eventId);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_fragment_qr_code, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imageView = view.findViewById(R.id.qrImageView);

        Bundle args = getArguments();
        eventId = args.getString("eventId");
        Log.d("EVENT ID BEING RECEIVED TO THE QRCODE DIALOG FRAGMENT", eventId);




        view.findViewById(R.id.back_button).setOnClickListener(v -> dismiss());

        try {
            generateAndDisplayQRCode();
        } catch (WriterException e) {
            throw new RuntimeException(e);
        }
    }

    private void generateAndDisplayQRCode() throws WriterException {

        String deeplink = "com.example.camaraderie://event?id=" + eventId;

        Bitmap qrCode = QRCodeGenerator.generateQRCode(deeplink, 500);
        imageView.setImageBitmap(qrCode);

    }
}
