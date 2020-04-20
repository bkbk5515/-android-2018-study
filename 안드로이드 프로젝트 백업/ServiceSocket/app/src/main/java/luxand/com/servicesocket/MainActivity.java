package luxand.com.servicesocket;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    MyService myService;
    private Button btnStart, btnEnd, btnget;
    String strring_a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnStart = (Button)findViewById(R.id.btn_start);
        btnEnd = (Button)findViewById(R.id.btn_end);
        btnget = (Button)findViewById(R.id.btn_get);

        //서비스 자동 연결
        Intent intent = new Intent(MainActivity.this,MyService.class);
        startService(intent);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Service 시작",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this,MyService.class);
                startService(intent);
            }
        });
        btnEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Service 끝",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this,MyService.class);
                stopService(intent);
            }
        });
        btnget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myService = new MyService();
//                strring_a = myService.getname();
                strring_a = MyService.sndname;
                Log.d("메인액티비티 받아온 이름 a", strring_a);
                Toast.makeText(getApplicationContext(), "받아온 데이터 : " + strring_a, Toast.LENGTH_LONG).show();
            }
        });
    }




}//출처: http://twinw.tistory.com/50 [흰고래의꿈]
