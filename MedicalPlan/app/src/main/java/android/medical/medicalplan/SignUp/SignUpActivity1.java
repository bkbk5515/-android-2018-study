package android.medical.medicalplan.SignUp;

import android.content.Intent;
import android.medical.medicalplan.R;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class SignUpActivity1 extends AppCompatActivity implements View.OnClickListener {

    Button next_btn;
    Intent intent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup1);

        next_btn = (Button) findViewById(R.id.next_btn);
        next_btn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        //Intent calIntent = new Intent(this, CalculateActivity.class);
        switch (v.getId()){
            case R.id.next_btn://다음페이지로
                intent = new Intent(getApplicationContext(), SignUpActivity2.class);
                startActivity(intent);
                break;
//            case R.id.mainMinusIv:
//                calIntent.putExtra("isPlus",false);
//                break;
        }
//        startActivityForResult(calIntent,3000);
    }
}
