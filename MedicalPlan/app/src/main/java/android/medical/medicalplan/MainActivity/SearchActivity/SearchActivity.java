package android.medical.medicalplan.MainActivity.SearchActivity;

import android.content.Intent;
import android.media.Image;
import android.medical.medicalplan.R;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {

    FrameLayout frameLayout;

    Hospital_Tap1_Fragment hospital_tap1_fragment;
    Hospital_Tap2_Fragment hospital_tap2_fragment;
    Hospital_Tap3_Fragment hospital_tap3_fragment;

    Pharmacy_Fragment pharmacy_fragment;

    ImageView back;

    Button btn1, btn2;

    EditText editText;
    ImageView search_image_btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        /**
         * 프래그먼트 class
         */
        hospital_tap1_fragment = new Hospital_Tap1_Fragment();
        hospital_tap2_fragment = new Hospital_Tap2_Fragment();
        hospital_tap3_fragment = new Hospital_Tap3_Fragment();

        pharmacy_fragment = new Pharmacy_Fragment();

        /**
         * 프레임 레이아웃
         */
        frameLayout = (FrameLayout) findViewById(R.id.container);

        back = (ImageView)findViewById(R.id.image_cancel);
        back.setOnClickListener(this);
        btn1 = (Button) findViewById(R.id.button_hospital);
        btn1.setOnClickListener(this);
        btn2 = (Button) findViewById(R.id.button_pharmacy);
        btn2.setOnClickListener(this);
        editText = (EditText) findViewById(R.id.edittext);
        search_image_btn = (ImageView) findViewById(R.id.search_image_btn);
        search_image_btn.setOnClickListener(this);

        getSupportFragmentManager().beginTransaction().replace(R.id.container, hospital_tap1_fragment).commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_cancel://뒤로가기
                Toast.makeText(getApplicationContext(), "back", Toast.LENGTH_SHORT).show();
                super.onBackPressed();
                break;

            case R.id.button_hospital://병원
                btn1.setBackgroundResource(R.drawable.button_on);
                btn2.setBackgroundResource(R.drawable.button_off);

                //병원
                getSupportFragmentManager().beginTransaction().replace(R.id.container, hospital_tap1_fragment).commit();
                break;
            case R.id.button_pharmacy://약국
                btn1.setBackgroundResource(R.drawable.button_off);
                btn2.setBackgroundResource(R.drawable.button_on);

                //약국
                getSupportFragmentManager().beginTransaction().replace(R.id.container, pharmacy_fragment).commit();
                break;

            case R.id.search_image_btn://검색
                String text = editText.getText().toString();
                Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
                editText.setText("");
                break;
        }
    }


    public void onFragmentChange(int index) {
        if (index == 1) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, hospital_tap1_fragment).commit();
        } else if (index == 2) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, hospital_tap2_fragment).commit();
        } else if (index == 3) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, hospital_tap3_fragment).commit();
        }
    }
}
