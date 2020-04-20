package com.todayplan.daum;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class A extends AppCompatActivity {

    Button btn;
    TextView textView;
    String arg1, arg2, arg3;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a);

        btn = (Button)findViewById(R.id.btn);
        textView = (TextView)findViewById(R.id.textview);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(A.this, MainActivity.class);
                startActivityForResult(intent, 9999);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            switch (requestCode){
            // MainActivity 에서 요청할 때 보낸 요청 코드 (3000)
                case 9999:
//                    textView.setText(data.getStringExtra("arg1"));
                    arg1 = data.getStringExtra("arg1");
                    arg2 = data.getStringExtra("arg2");
                    arg3 = data.getStringExtra("arg3");

                    textView.setText(arg1+", "+arg2+", "+arg3);
                    break;
            }
        }
    }//출처: http://liveonthekeyboard.tistory.com/150 [키위남]
}
