package com.example.konote.weetalk;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {

    private AlertDialog dialog;
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private EditText idtext;
    SharedPreferences userNameSave;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext()); // SDK 초기화 (setContentView 보다 먼저 실행되어야합니다. 안그럼 에러납니다.)
        setContentView(R.layout.login);

        idtext = (EditText) findViewById(R.id.idtext);
        final String ID = idtext.getText().toString();

        callbackManager = CallbackManager.Factory.create();  //로그인 응답을 처리할 콜백 관리자
        loginButton = (LoginButton) findViewById(R.id.buttonId); //페이스북 로그인 버튼
        //유저 정보, 친구정보, 이메일 정보등을 수집하기 위해서는 허가(퍼미션)를 받아야 합니다.
        loginButton.setReadPermissions("public_profile", "user_friends", "email");
        //버튼에 바로 콜백을 등록하는 경우 LoginManager에 콜백을 등록하지 않아도됩니다.
        //반면에 커스텀으로 만든 버튼을 사용할 경우 아래보면 CustomloginButton OnClickListener안에 LoginManager를 이용해서
        //로그인 처리를 해주어야 합니다.
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) { //로그인 성공시 호출되는 메소드
                Log.e("토큰", loginResult.getAccessToken().getToken());
                Log.e("유저아이디", loginResult.getAccessToken().getUserId());
                Log.e("퍼미션 리스트", loginResult.getAccessToken().getPermissions() + "");
                String facebookid = loginResult.getAccessToken().getUserId();
                String facebookpw = String.valueOf(0000);
                String facebooknb = String.valueOf(010);

                //loginResult.getAccessToken() 정보를 가지고 유저 정보를 가져올수 있습니다.
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                try {
                                    Log.e("user profile", object.toString());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                request.executeAsync();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
//                            if(success){
//                                Log.d("----", "회원가입 완료");
//                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
//                                dialog = builder.setMessage("회원가입 완료.")
//                                        .setPositiveButton("확인", null)
//                                        .create();
//                                dialog.show();
//                                finish();
//                            }
//                            else{
//                                Log.d("----", "회원가입 실패");
//                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
//                                dialog = builder.setMessage("회원가입에 실패했습니다.")
//                                        .setNegativeButton("확인", null)
//                                        .create();
//                                dialog.show();
//                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };

                RegisterRequest registerrequest = new RegisterRequest(facebookid, facebookpw, facebooknb, responseListener);
                RequestQueue rq = Volley.newRequestQueue(LoginActivity.this);
                rq.add(registerrequest);

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                LoginActivity.this.startActivity(intent);
                finish();
            }

            @Override
            public void onError(FacebookException error) {
            }

            @Override
            public void onCancel() {
            }
        });

//        CustomloginButton = (Button) findViewById(R.id.loginBtn);
//        CustomloginButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //LoginManager - 요청된 읽기 또는 게시 권한으로 로그인 절차를 시작합니다.
//                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this,
//                        Arrays.asList("public_profile", "user_friends"));
//                LoginManager.getInstance().registerCallback(callbackManager,
//                        new FacebookCallback<LoginResult>() {
//                            @Override
//                            public void onSuccess(LoginResult loginResult) {
//                                Log.e("onSuccess", "onSuccess");
//                            }
//
//                            @Override
//                            public void onCancel() {
//                                Log.e("onCancel", "onCancel");
//                            }
//
//                            @Override
//                            public void onError(FacebookException exception) {
//                                Log.e("onError", "onError " + exception.getLocalizedMessage());
//                            }
//                        });
//            }
//        });

        TextView registerButton = (TextView) findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(registerIntent);
            }
        });

        final EditText idText = (EditText) findViewById(R.id.idtext);
        final EditText passwordText = (EditText) findViewById(R.id.passwordtext);
        final Button loginButton = (Button) findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String userMail = idText.getText().toString();
                String userPassword = passwordText.getText().toString();

                Response.Listener<String> responseLister = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if (success) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                dialog = builder.setMessage("로그인 성공")
                                        .setPositiveButton("확인", null)
                                        .create();
                                dialog.show();
                                Log.d("로그인성공 다이얼로그 출력", "화면전환 직전");

                                //쉐어드로 유저 아이디 저장
                                userNameSave = getSharedPreferences("MyData", MODE_PRIVATE);
                                editor = userNameSave.edit();
                                editor.putString("userID", userMail);
                                editor.commit();
                                Log.d("로그인시 쉐어드 아이디 저장", userMail);

                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                LoginActivity.this.startActivity(intent);
//                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                                intent.putExtra("ID", ID);
//                                LoginActivity.this.startActivity(intent);
                                finish();
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                dialog = builder.setMessage("계정을 다시 확인해주세요")
                                        .setNegativeButton("확인", null)
                                        .create();
                                dialog.show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                LoginRequest loginRequest = new LoginRequest(userMail, userPassword, responseLister);
                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                queue.add(loginRequest);
            }
        });

    }//온 크리에이트 끝


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }
}

//https://www.youtube.com/watch?v=ul8j10pfJJA&index=3&list=PLRx0vPvlEmdD862e43ADbvDeGPUZKDuqL
