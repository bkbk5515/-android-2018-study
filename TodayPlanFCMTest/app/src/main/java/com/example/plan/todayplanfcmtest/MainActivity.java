package com.example.plan.todayplanfcmtest;

import android.app.PendingIntent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.NotificationManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    EditText messageInput;
    TextView messageOutput;
    TextView log;

    final String TAG = "TodayPlan";

    public static String PhoneID;

    RequestQueue queue;
    //String AppKey = "AAAA57WtcnI:APA91bFBku20kZR08oN7QypTimXNB3vSleRahlIdgO8N1KE4fxAiomVpW-HDyL3dpFk4bKj2v6E9ZuDXxWzY1YTnNOyFQo5znEWy_IFFyt-e7W2KWbp2p8ErGGnAZ0-90SqwCs_9O1hW";
    //String FCM_URL = "https://fcm.googleapis.com/fcm/send";
    String AppKey = "LrMgyJaOG8spYK2PbRoqsdZvlcXSxWe95awF1a";
    String FCM_URL = "http://api.todayplan.xyz/api/app/test_req";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseMessaging.getInstance().subscribeToTopic("user");

        messageInput = (EditText) findViewById(R.id.messageInput);
        messageOutput = (TextView) findViewById(R.id.messageOutput);
        log = (TextView) findViewById(R.id.log);

        Button sendButton = (Button) findViewById(R.id.sendButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input = messageInput.getText().toString();
                //send(input);
//                fcmsend(input, PhoneID);
                send(input);
            }
        });

        queue = Volley.newRequestQueue(getApplicationContext());

        getRegistrationId();

        Intent intent = getIntent();
        if (intent != null) {
            processIntent(intent);
        }
    }

    public void getRegistrationId() {
        Log.d(TAG, "getRegistrationId() 호출됨.");
        PhoneID = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "regId : " + PhoneID);
    }

//    //여기서 왜 아이디를 보내지?
    public void send(String input) {
        JSONObject requestData = new JSONObject();
        try {
            requestData.put("priority", "high");

            JSONObject dataObj = new JSONObject();
            //EditText 내용
            dataObj.put("title", "새로운 견적요청입니다.");
            dataObj.put("contents", input);
            //ReauestData에 data라는 키의 contents를 넣음(dataOBJ)
            requestData.put("data", dataObj);

            JSONArray idArray = new JSONArray();
            //디바이스 고유 ID
            idArray.put(0, PhoneID);
            //하단의 위와 아래의 코드는 각기 다른 디바이스의 ID이고 세무사가 답변달때 디바이스ID로 사용자를 구분하여 알림을 전송 할 수 있음.
            //글 등록시 전체에게 노티(php에서 작업 해줘야 됨)
            //idArray.put(1, "cu70nt1VaFk:APA91bG8-d_qPDd4CLe9n7YN8gDzVtg_vrd2LxLkSdej0fDoOyy28qFXDZnzLA4fSa0-ep6Ifb3XSsOOVOBQZwEfc-DbRpTTBix0ZkLlxPU0r2voC68WBY5iDqV_Zzivlf8JsYdrtFmy");

            //registration_ids 로 여러명의 사용자에게 노티 전송 가능.
            requestData.put("registration_ids", idArray);
            //php로 메세지와 id 넘김
//            fcmsend(input , PhoneID);

        } catch (Exception e) {
            e.printStackTrace();
        }

        sendData(requestData, new SendResponseListener() {
            @Override
            public void onRequestCompleted() {
                Log.d(TAG, "onRequestCompleted() 호출됨.");
            }

            @Override
            public void onRequestStarted() {
                Log.d(TAG, "onRequestStarted() 호출됨.");
            }

            @Override
            public void onRequestWithError(VolleyError error) {
                Log.d(TAG, "onRequestWithError() 호출됨.");
            }
        });

    }

    public interface SendResponseListener {
        public void onRequestStarted();

        public void onRequestCompleted();

        public void onRequestWithError(VolleyError error);
    }

    public void sendData(JSONObject requestData, final SendResponseListener listener) {
        Log.d("bkbk5515sendJSON", requestData.toString());
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST, FCM_URL, requestData, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                listener.onRequestCompleted();
                Log.d("bkbk5515response", response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onRequestWithError(error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Auth-Key", AppKey);

                return headers;
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };

        request.setShouldCache(false);
        listener.onRequestStarted();
        queue.add(request);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Log.d(TAG, "onNewIntent() called.");
        if (intent != null) {
            processIntent(intent);
        }

        super.onNewIntent(intent);
    }


    private void processIntent(Intent intent) {
        String from = intent.getStringExtra("from");
        if (from == null) {
            Log.d(TAG, "from is null.");
            return;
        }

        String contents = intent.getStringExtra("contents");

        Log.d(TAG, "DATA : " + from + ", " + contents);
        messageOutput.setText("[" + PhoneID + "]로부터 수신한 데이터 : " + contents);
        Log.d("bkbk5515", PhoneID);

    }

    public void println(String data) {
        //log.append(data + "\n");
    }

//    public void fcmsend(String input, String id) {
//        Response.Listener<String> responseListener = new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                try {
//                    JSONObject jsonResponse = new JSONObject(response);
//                    Log.d("받은json", String.valueOf(jsonResponse));
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    Log.d("response에러", String.valueOf(e));
////Toast.makeText(getApplicationContext(),"$response에러",Toast.LENGTH_SHORT).show();
//                }
//            }
//        };
//        FCMPut FCM = new FCMPut(input, id, responseListener);
//        RequestQueue rq = Volley.newRequestQueue(MainActivity.this);
//        rq.add(FCM);
//    }

}