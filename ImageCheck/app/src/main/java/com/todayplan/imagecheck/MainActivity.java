package com.todayplan.imagecheck;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    FrameLayout frameLayout;
    ImageView imageview1, imageview2, imageview3;
    public Handler handler;
    int a = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        frameLayout = (FrameLayout)findViewById(R.id.framelayout);

        imageview1 = (ImageView) findViewById(R.id.imageview1);
        imageview2 = (ImageView) findViewById(R.id.imageview2);
        imageview3 = (ImageView) findViewById(R.id.imageview3);

        imageview1.setImageResource(R.drawable.image01);
        imageview2.setImageResource(R.drawable.image02);
        imageview3.setImageResource(R.drawable.image03);

//        final Animation animTransRight = AnimationUtils.loadAnimation(this,R.anim.anim_translate_right);
        final Animation animTransLeft = AnimationUtils.loadAnimation(this, R.anim.anim_translate_left);
        final Animation animTransLeft2 = AnimationUtils.loadAnimation(this, R.anim.anim_translate_left2);
//        final Animation animTransAlpha = AnimationUtils.loadAnimation(this,R.anim.anim_translate_alpha);
//        final Animation animTransTwits = AnimationUtils.loadAnimation(this,R.anim.anim_translate_twits);

        Button btnRight = (Button) findViewById(R.id.button);
        Button btnLeft = (Button) findViewById(R.id.button2);
        Button btnAlpha = (Button) findViewById(R.id.button3);
//        Button btnCycle = (Button) findViewById(R.id.button4);

        frameLayout.bringChildToFront(imageview3);
        frameLayout.bringChildToFront(imageview2);
        frameLayout.bringChildToFront(imageview1);

        (new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                while (!Thread.interrupted())
                    try
                    {
                        Thread.sleep(6500); //1초 간격으로 실행
                        runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                imagechanges();
                            }
                        });
                    }
                    catch (InterruptedException e)
                    {
                        Log.d("bkbk5515_error", e.toString());
                    }
            }
        })).start();
        //imagechanges();

        btnRight.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                frameLayout.bringChildToFront(imageview2);
                frameLayout.bringChildToFront(imageview1);

                imageview1.startAnimation(animTransLeft);
                imageview2.startAnimation(animTransLeft2);
            }
        });

        btnLeft.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                frameLayout.bringChildToFront(imageview3);
                frameLayout.bringChildToFront(imageview2);

                frameLayout.bringChildToFront(imageview2);
                imageview2.startAnimation(animTransLeft);
                imageview3.startAnimation(animTransLeft2);
            }
        });

        btnAlpha.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                frameLayout.bringChildToFront(imageview1);
                frameLayout.bringChildToFront(imageview3);

                imageview3.startAnimation(animTransLeft);
                imageview1.startAnimation(animTransLeft2);
            }
        });
//
//        btnCycle.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                imageview1.startAnimation(animTransTwits);
//            }
//        });
//        출처: http://mainia.tistory.com/2861 [녹두장군 - 상상을 현실로]
        //출처: http://mainia.tistory.com/2861 [녹두장군 - 상상을 현실로]
    }

    public void imagechanges(){
        final Animation animTransLeft = AnimationUtils.loadAnimation(this, R.anim.anim_translate_left);
        final Animation animTransLeft2 = AnimationUtils.loadAnimation(this, R.anim.anim_translate_left2);

        if (a == 1) {
            frameLayout.bringChildToFront(imageview3);
            frameLayout.bringChildToFront(imageview2);
            frameLayout.bringChildToFront(imageview1);

            imageview1.startAnimation(animTransLeft);
            imageview2.startAnimation(animTransLeft2);
            a = 2;
        } else if (a == 2) {
            frameLayout.bringChildToFront(imageview1);
            frameLayout.bringChildToFront(imageview3);
            frameLayout.bringChildToFront(imageview2);

            frameLayout.bringChildToFront(imageview2);
            imageview2.startAnimation(animTransLeft);
            imageview3.startAnimation(animTransLeft2);
            a = 3;
        } else if (a == 3) {
            frameLayout.bringChildToFront(imageview2);
            frameLayout.bringChildToFront(imageview1);
            frameLayout.bringChildToFront(imageview3);

            imageview3.startAnimation(animTransLeft);
            imageview1.startAnimation(animTransLeft2);
            a = 1;
        }
    }

    @Override
    protected void onPause() {

        super.onPause();
    }
}
