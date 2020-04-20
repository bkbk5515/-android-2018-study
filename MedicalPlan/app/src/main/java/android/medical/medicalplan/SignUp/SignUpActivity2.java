package android.medical.medicalplan.SignUp;

import android.content.Intent;
import android.medical.medicalplan.R;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class SignUpActivity2 extends AppCompatActivity implements View.OnClickListener {

    Button confirm_btn, next_btn;
    RadioGroup rg;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup2);

        rg = (RadioGroup) findViewById(R.id.radioGroup1);

        confirm_btn = (Button)findViewById(R.id.confirm_btn);
        confirm_btn.setOnClickListener(this);
        next_btn = (Button) findViewById(R.id.next_btn);
        next_btn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        //Intent calIntent = new Intent(this, CalculateActivity.class);
        switch (v.getId()) {
            case R.id.confirm_btn:
                Intent intent = new Intent(this, PhoneNumberConfirmActivity.class);
                startActivityForResult(intent, 1000);
                break;

            case R.id.next_btn://다음페이지로
                /**
                 * 라디오버튼 결과값 반환
                 */
                int id = rg.getCheckedRadioButtonId();
                RadioButton rb = (RadioButton) findViewById(id);
                //Toast.makeText(getApplicationContext(), "성 : " + rb.getText().toString(), Toast.LENGTH_SHORT).show();
                /** 라디오버튼 결과값 반환 **/

                Toast.makeText(getApplicationContext(), "end", Toast.LENGTH_SHORT).show();
                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            switch (requestCode){
                case 1000:
                    Toast.makeText(getApplicationContext(), data.getStringExtra("number"), Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

}
