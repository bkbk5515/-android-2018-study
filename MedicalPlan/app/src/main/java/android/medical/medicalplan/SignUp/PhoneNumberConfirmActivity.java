package android.medical.medicalplan.SignUp;

import android.content.Intent;
import android.medical.medicalplan.R;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class PhoneNumberConfirmActivity extends AppCompatActivity implements View.OnClickListener {

    Button button;
    Intent intent;
    EditText phoneNumberText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_phonenumberconfirm);

        phoneNumberText = (EditText)findViewById(R.id.phoneNumber);
        button = (Button) findViewById(R.id.next_btn);
        button.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.next_btn://다음페이지로
                intent = new Intent();
                intent.putExtra("number", phoneNumberText.getText().toString());
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
    }
}
