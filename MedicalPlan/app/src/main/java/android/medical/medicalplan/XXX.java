package android.medical.medicalplan;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class XXX extends AppCompatActivity {

    Button btn1, btn2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xxx);

        btn1 = (Button)findViewById(R.id.button_l);
        btn2 = (Button)findViewById(R.id.button_r);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn1.setBackgroundResource(R.drawable.button_on);
                btn2.setBackgroundResource(R.drawable.button_off);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn1.setBackgroundResource(R.drawable.button_off);
                btn2.setBackgroundResource(R.drawable.button_on);
            }
        });

    }
}
