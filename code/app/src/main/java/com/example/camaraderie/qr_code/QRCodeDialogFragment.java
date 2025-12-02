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

/**
 * Dialog fragment that displays a QR code for the event
 * @author Tahmid
 */
public class QRCodeDialogFragment extends DialogFragment {

    private ImageView imageView;
    private String eventId;

    /**
     * Constructor for a QRCodeDialogFragment
     * @param eventId id to bind to the QR code
     * @return return fragment for the QR code
     */
    public static QRCodeDialogFragment newInstance(String eventId){
        QRCodeDialogFragment fragment = new QRCodeDialogFragment();
        Bundle args = new Bundle();
        Log.d("EVENT ID BEING SENT TO THE QRCODE DIALOG FRAGMENT", eventId);
        args.putString("eventId", eventId);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     *
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return return inflated view
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_fragment_qr_code, container, false);
    }

    /**
     * set view bindings, display qr code
     * Called immediately after {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}
     * has returned, but before any saved state has been restored
     * @param view The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     * @throws RuntimeException if QR code generation fails
     */
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

    /**
     * Creates a deeplink and generates a QR code for it
     * @throws WriterException occurs if qrcode fails to generate
     */
    private void generateAndDisplayQRCode() throws WriterException {

        String deeplink = "com.example.camaraderie://event?id=" + eventId;

        Bitmap qrCode = QRCodeGenerator.generateQRCode(deeplink, 500);
        imageView.setImageBitmap(qrCode);

    }
}
