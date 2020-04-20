package com.todayplan.nettyfinal;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

public class Room extends AppCompatActivity {

    Handler handler;
    public static Handler showUpdate;  // 서비스쪽 핸들러  < = > 액티비티와 통신해야한다

    ListView listView = null;
    ChattingAdapter chattingAdapter;
    ChattingItem chattingItem;

    Button sendbtn;
    EditText mymsg;
    String roomname;

    public static final int SEND_MSG = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        Intent getIntent = getIntent();
        roomname = getIntent.getStringExtra("roomname");
        Log.d("bkbk5515", "room name : " + roomname);

        sendbtn = (Button) findViewById(R.id.button);
        mymsg = (EditText) findViewById(R.id.edittext);

        userInOut("in");

        showUpdate = new Handler(new Handler.Callback() {  // 액티비티 -> 서비스 핸들러 ( 채팅내용 )
            @Override
            public boolean handleMessage(Message msg) {

                switch (msg.what) {
                    case 100: //
                        String activity_msg = msg.obj.toString();
                        setServiceChatting(activity_msg);

                        break;
                }
                return false;
            }
        });

        listView = (ListView) findViewById(R.id.listview);
        chattingAdapter = new ChattingAdapter();
        listView.setAdapter(chattingAdapter);

        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //JsonOBJ
                JSONObject msg_info = new JSONObject();

                try {
                    final String msg = mymsg.getText().toString();
                    if (!msg.equals("")) {

                        msg_info.put("type", "chat");
                        msg_info.put("room", roomname);
                        msg_info.put("id", LoginActivity.mId);
                        msg_info.put("msg", msg);

                        //보내는 JsonOBJ를 스트링으로 변환 후 서버에 전달
                        String jsonString = msg_info.toString();

                        //TextUtils.isEmpty() = null 검사
                        if (!TextUtils.isEmpty(jsonString) && !msg.equals("")) {
                            //new SendmsgTask().execute(jsonString);

                            sendService(jsonString);

                            chattingItem = new ChattingItem(LoginActivity.mId, msg);
                            chattingAdapter.listViewItemList.add(chattingItem);
                            chattingAdapter.notifyDataSetChanged();
                            listView.setSelection(chattingAdapter.getCount() - 1);

                            mymsg.setText("");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void setServiceChatting(String chatting) {
        Log.d("bkbk5515", "setServiceChatList()");

        String id, msg;

        try {
            JSONObject jsonObject = new JSONObject(chatting);
            id = jsonObject.getString("id");
            msg = jsonObject.getString("msg");

            chattingItem = new ChattingItem(id, msg);
            chattingAdapter.listViewItemList.add(chattingItem);

        } catch (Exception e) {
            Log.d("bkbk5515", "Mainactivity showupdate error" + e);
            Toast.makeText(getApplicationContext(), "T0T", Toast.LENGTH_SHORT).show();
        }

        chattingAdapter.notifyDataSetChanged();
        listView.setSelection(chattingAdapter.getCount() - 1);
    }

    public void userInOut(String inout) {
        //JsonOBJ
        JSONObject msg_info = new JSONObject();
        try {
            msg_info.put("type", inout);
            msg_info.put("room", roomname);
            msg_info.put("id", LoginActivity.mId);

            //보내는 JsonOBJ를 스트링으로 변환 후 서버에 전달
            String jsonString = msg_info.toString();

            //TextUtils.isEmpty() = null 검사
            if (!TextUtils.isEmpty(jsonString)) {
                //new SendmsgTask().execute(jsonString);
                sendService(jsonString);
                //my_msg.setText("userInOut()");
                //mymsg.setText("");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendService(String msgJson) {
        Message message = MyService.serviceHandler.obtainMessage();

        message.what = SEND_MSG;
        String msg = new String(msgJson);
        message.obj = msg;

        MyService.serviceHandler.sendMessage(message);
    }

    long pressTime;

    @Override
    public void onBackPressed() {

        if (System.currentTimeMillis() - pressTime < 2000) {

            userInOut("out");

            finish();
            return;
        }
        Toast.makeText(this, "You want?", Toast.LENGTH_LONG).show();
        pressTime = System.currentTimeMillis();

    }


//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        //앱을 실행하고 종료(onDestroy)할 때 서비스(RealService)를 종료(stopService)한다.
//        if (serviceIntent != null) {
//            stopService(serviceIntent);
//            serviceIntent = null;
//            Log.d("bkbk5515", "Mainactivity onDestroy -> serviceIntent = null");
//        }
//    }//출처: http://forest71.tistory.com/185 [SW 개발이 좋은 사람]

}
