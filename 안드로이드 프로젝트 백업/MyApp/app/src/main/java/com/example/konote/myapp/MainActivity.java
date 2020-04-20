package com.example.konote.myapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.konote.myapp.liveVideoBroadcaster.*;
import com.example.konote.myapp.liveVideoPlayer.LiveVideoPlayerActivity;

public class MainActivity extends AppCompatActivity {

    /**
     * PLEASE WRITE RTMP BASE URL of the your RTMP SERVER.
     */
    public static final String RTMP_BASE_URL = "rtmp://13.125.73.140/dash/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(io.antmedia.android.liveVideoBroadcaster.R.layout.activity_main);
//        setContentView(com.example.konote.myapp.liveVideoBroadcaster.R.layout.activity_main);
        setContentView(R.layout.activity_main);
    }

    public void openVideoBroadcaster(View view) {
        Intent i = new Intent(MainActivity.this, LiveVideoBroadcasterActivity.class);
        startActivity(i);
    }

    public void openVideoPlayer(View view) {
        Intent i = new Intent(MainActivity.this, LiveVideoPlayerActivity.class);
        startActivity(i);
    }
}
