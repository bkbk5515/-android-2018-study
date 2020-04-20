package com.todayplan.nettyfinal;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    public static Intent serviceIntent;

    EditText editText;
    Button button;
    static String mId;
    //static String idByANDROID_ID;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editText = (EditText)findViewById(R.id.edittext);
        button = (Button)findViewById(R.id.button);
        ////ID로 대체
        //idByANDROID_ID = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        /**
         * 서비스 실행전 확인작업
         */
        if (MyService.serviceIntent == null) {
            serviceIntent = new Intent(this, MyService.class);
            startService(serviceIntent);
            Log.d("bkbk5515", "서비스 시작해줌");
            //Toast.makeText(getApplicationContext(), "서비스 시작해줌", Toast.LENGTH_LONG).show();
        } else {
            serviceIntent = MyService.serviceIntent;//getInstance().getApplication();
            Log.d("bkbk5515", "서비스 시작 되어있음");
            //Toast.makeText(getApplicationContext(), "서비스 시작 되어있음", Toast.LENGTH_LONG).show();
        }

        //로그인
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mId = editText.getText().toString();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

//    @Override
//    protected void onDestroy() {//앱을 실행하고 종료(onDestroy)할 때 서비스(RealService)를 종료(stopService)한다.
//        super.onDestroy();
//        if (serviceIntent != null) {
//            stopService(serviceIntent);
//            serviceIntent = null;
//            Log.d("bkbk5515", "Mainactivity onDestroy -> serviceIntent = null");
//        }
//    }//출처: http://forest71.tistory.com/185 [SW 개발이 좋은 사람]
}
