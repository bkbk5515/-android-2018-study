package com.example.konote.weetalk;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

/**
 * Created by KONOTE on 2017-10-08.
 */

public class RegisterActivity extends AppCompatActivity {

    private String userMail;
    private String userPassword;
    private String userPhonenumber;
    private AlertDialog dialog;
    private boolean validate = false; // 사용할 수 있는 아이디인지 체크
    Button checkbutton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        final EditText idtext = (EditText)findViewById(R.id.idtext);
        final EditText passwordtext = (EditText)findViewById(R.id.passwordtext);
        final EditText phonenumbertext = (EditText)findViewById(R.id.phonenumbertext);

        checkbutton = (Button)findViewById(R.id.checkButton);
        checkbutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String userMail = idtext.getText().toString();
                if(validate){
                    return;
                }
                if(userMail.equals("")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    dialog = builder.setMessage("아이디를 입력해 주세요.")
                            .setPositiveButton("확인", null)
                            .create();
                    dialog.show();
                    return;
                }
                Response.Listener<String> RL = new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response){
                        try{
                            Log.d("----", "----");
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if(success){
                                Log.d("----", "사용할수있습니다");
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                dialog = builder.setMessage("사용할 수 있습니다.")
                                        .setPositiveButton("확인", null)
                                        .create();
                                dialog.show();
                                idtext.setEnabled(false);
                                validate = true;
                                idtext.setBackgroundColor(getResources().getColor((R.color.colorGray)));
                                checkbutton.setBackgroundColor(getResources().getColor((R.color.colorGray)));
                            }
                            else{
                                Log.d("----", "사용할수없습니다");
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                dialog = builder.setMessage("사용할 수 없습니다.")
                                        .setNegativeButton("확인", null)
                                        .create();
                                dialog.show();
                            }
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                };
                UserCheck usercheck = new UserCheck(userMail, RL);
                RequestQueue rq = Volley.newRequestQueue(RegisterActivity.this);
                rq.add(usercheck);
            }
        });
        Button registerButton = (Button)findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Log.d("----", "가입하기 버튼 클릭");
                String userMail = idtext.getText().toString();
                String userPassword = passwordtext.getText().toString();
                String userPhonenumber = phonenumbertext.getText().toString();

                if(!validate){
                    Log.d("----", "중복체크를 해주세요");
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    dialog = builder.setMessage("중복체크를 해주세요.")
                            .setNegativeButton("확인", null)
                            .create();
                    dialog.show();
                    return;
                }
                if(userMail.equals("") || userPassword.equals("") || userPhonenumber.equals("")){
                    Log.d("----", "빈칸을 입력해주세요");
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    dialog = builder.setMessage("빈칸을 입력해주세요.")
                            .setNegativeButton("확인", null)
                            .create();
                    dialog.show();
                    return;
                }
                Response.Listener<String> responseListener = new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response){
                        try{
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if(success){
                                Log.d("----", "회원가입 완료");
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                dialog = builder.setMessage("회원가입 완료.")
                                        .setPositiveButton("확인", null)
                                        .create();
                                dialog.show();
                                finish();
                            }
                            else{
                                Log.d("----", "회원가입 실패");
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                dialog = builder.setMessage("회원가입에 실패했습니다.")
                                        .setNegativeButton("확인", null)
                                        .create();
                                dialog.show();
                            }
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                };
                RegisterRequest registerrequest = new RegisterRequest(userMail, userPassword, userPhonenumber, responseListener);
                RequestQueue rq = Volley.newRequestQueue(RegisterActivity.this);
                rq.add(registerrequest);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(dialog != null){
            dialog.dismiss();
            dialog = null;
        }
    }
}
