package com.example.konote.weetalk;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import java.io.File;

/**
 * Created by KONOTE on 2017-12-15.
 */

public class SetActivity extends AppCompatActivity {

    ImageView imageViewbtn;
    Bitmap bitmap;
    SharedPreferences userNameSave;
    String photofilename;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_activity);

        imageViewbtn = (ImageView) findViewById(R.id.imageView_photo);
        imageViewbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SetActivity.this, OpenCVActivity.class);
                startActivity(intent);
            }
        });

        userNameSave = getSharedPreferences("MyData", MODE_PRIVATE);
        photofilename = userNameSave.getString("photo", "");
        if (photofilename != null) {
            Uri uri = Uri.parse(photofilename);

            // Uri를 파일로 만들어주기
            File file = new File(uri.toString());
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 1;

            bitmap = BitmapFactory.decodeFile(file.getPath(), options);
            imageViewbtn.setImageBitmap(bitmap);
        }

        ImageView friendlist = (ImageView) findViewById(R.id.list_btn1);
        friendlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SetActivity.this, MainActivity.class);
                SetActivity.this.startActivity(intent);
            }
        });
        ImageView room_list = (ImageView) findViewById(R.id.chatlist_btn);
        room_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SetActivity.this, RoomList.class);
                SetActivity.this.startActivity(intent);
            }
        });
        ImageView map_btn = (ImageView) findViewById(R.id.map_btn);
        map_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SetActivity.this, MapActivity.class);
                SetActivity.this.startActivity(intent);
            }
        });
    }
}
