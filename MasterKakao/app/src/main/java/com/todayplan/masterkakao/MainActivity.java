package com.todayplan.masterkakao;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.allenliu.badgeview.BadgeFactory;
import com.allenliu.badgeview.BadgeView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    EditText editText;
    Button button;
    String message;
    TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText) findViewById(R.id.editText);
        button = (Button) findViewById(R.id.btn);


        textView = (TextView)findViewById(R.id.textView);

//        BadgeFactory.createDot(this).setBadgeCount(20).bind(textView);
//        BadgeFactory.createCircle(this).setBadgeCount(20).bind(textView);
//        BadgeFactory.createRectangle(this).setBadgeCount(20).bind(textView);//직사각형
//        BadgeFactory.createOval(this).setBadgeCount(20).bind(textView);//럭비공누워있는모양
//        BadgeFactory.createSquare(this).setBadgeCount(20).bind(textView);//사각형
//        BadgeFactory.createRoundRect(this).setBadgeCount(20).bind(textView);

        BadgeFactory.create(this)
                .setWidthAndHeight(20,20)
                .setTextSize(13)
                .setBadgeGravity(Gravity.RIGHT|Gravity.END)
                .setBadgeCount(20)
                .setShape(BadgeView.SHAPE_CIRCLE)
                .setSpace(30,-10)
                .bind(textView);

        //뱃지 바인드 해제
        //badgeView.unbind();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message = editText.getText().toString();

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, message);
                intent.setPackage("com.kakao.talk");
                startActivity(intent);
//                Intent intent = new Intent(android.content.Intent.ACTION_SEND);
//                intent.setType("text/plain");

            }
        });
    }
}
