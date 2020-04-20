package com.example.konote.photo2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by KONOTE on 2017-11-28.
 */

public class PhotoURL extends AppCompatActivity{

    Handler handler = new Handler();  // 외부쓰레드 에서 메인 UI화면을 그릴때 사용
    Button button;
    Thread t;
    String filename;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_url);
        // 인터넷 상의 이미지 보여주기

        // 1. 권한을 획득한다 (인터넷에 접근할수 있는 권한을 획득한다)  - 메니페스트 파일
        // 2. Thread 에서 웹의 이미지를 받아온다 - honeycomb(3.0) 버젼 부터 바뀜
        // 3. 외부쓰레드에서 메인 UI에 접근하려면 Handler 를 사용해야 한다.
        Intent intent = getIntent();
        filename = (String) intent.getExtras().get("File_Name");

        //Thread t = new Thread(Runnable 객체를 만든다);
        t = new Thread(new Runnable() {
            @Override
            public void run() {    // 오래 거릴 작업을 구현한다
                // TODO Auto-generated method stub
                try{
                    // 걍 외우는게 좋다 -_-;
                    final ImageView iv = (ImageView)findViewById(R.id.imageView);
                    URL url = new URL("http://bkbk55152.vps.phps.kr/newImage/" + filename);
                    InputStream is = url.openStream();
                    final Bitmap bm = BitmapFactory.decodeStream(is);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {  // 화면에 그려줄 작업
                            iv.setImageBitmap(bm);
                        }
                    });
                    iv.setImageBitmap(bm); //비트맵 객체로 보여주기
                } catch(Exception e){

                }

            }
        });

        button = (Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                t.start();
            }
        });


    } // end onCreate
//    public PhotoURL(){}
//    public PhotoURL(String _filename){
//        this.filename = _filename;
//    }
} // end class

//출처: http://bitsoul.tistory.com/26 [Happy Programmer~]