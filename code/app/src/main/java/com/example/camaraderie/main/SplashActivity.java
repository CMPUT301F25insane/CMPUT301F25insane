package com.example.camaraderie.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.camaraderie.R;

/**
 * SplashActivity is the first activity that is opened and runs a little intro animation
 * @author RamizHHH
 */

public class SplashActivity extends AppCompatActivity {

    /**
     * onCreate initalizes the video intro for the app and displays
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getWindow().setBackgroundDrawable(null);


        VideoView videoView = findViewById(R.id.splashVideo);

        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.intro);

        videoView.setVideoURI(videoUri);

        videoView.setOnCompletionListener(mp ->{
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            overridePendingTransition(0, 0);
            finish();
        });


        videoView.start();
    }
}
