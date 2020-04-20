package com.todayplan.nettyfinal;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    Handler handler;
    public static Handler showUpdate;  //서비스쪽 핸들러  < = > 액티비티와 통신해야한다

    ListView listView = null;
    ChatRoomListViewAdapter chatRoomListViewAdapter;
    ChatRoomListViewItem chatRoomListViewItem;

    Button makeroom;

    //Button a, b;
    //TextView my_msg;
    //EditText mymsg;
    //Button makea, makeb, getRoomListbtn;

    //    public static final int SEND_MSG = 0;
    public static final int SEND_GETROOMLIST = 1;
    //출처: http://itmining.tistory.com/16 [IT 마이닝]


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listview);
        chatRoomListViewAdapter = new ChatRoomListViewAdapter();
        listView.setAdapter(chatRoomListViewAdapter);

        //채팅 방 목록 가져옴
        getChatRoomList("roomlist");

        /**
         * 방 만들기.
         */
        makeroom = (Button) findViewById(R.id.makeroom);
        makeroom.setOnClickListener(new OnSingleClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onSingleClick(View v) {

                AlertDialog.Builder ad = new AlertDialog.Builder(MainActivity.this);
                ad.setTitle("Chat Room Name");       // 제목 설정
                //ad.setMessage("");   // 내용 설정
                final EditText et = new EditText(MainActivity.this);
                ad.setView(et);
                et.setTextColor(R.color.colorPrimaryDark);

                //edittext pw모드로 변경
                //et.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                //et.setTransformationMethod(PasswordTransformationMethod.getInstance());

                // 확인 버튼 설정
                ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String roomname;
                        roomname = et.getText().toString();
                        if (roomname.equals("")) {
                            Toast.makeText(MainActivity.this, "Check EditText", Toast.LENGTH_SHORT).show();
                        } else {

                            //JsonOBJ
                            JSONObject msg_info = new JSONObject();
                            try {
                                msg_info.put("type", "make");
                                msg_info.put("room", roomname);
                                msg_info.put("id", LoginActivity.mId);

                                //보내는 JsonOBJ를 스트링으로 변환 후 서버에 전달
                                String jsonString = msg_info.toString();
                                sendService(jsonString);

                                Intent intent = new Intent(getApplicationContext(), Room.class);
                                intent.putExtra("roomname", roomname);
                                startActivity(intent);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            dialog.dismiss();//다이얼로그 닫음
                        }
                    }
                });
                // 취소 버튼 설정
                ad.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();//닫음
                    }
                });
                //다이얼로그 띄움
                ad.show();
            }
        });

        /**
         * 리스트 클릭
         * 방 입장
         */
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ChatRoomListViewItem item = (ChatRoomListViewItem)parent.getItemAtPosition(position);

                String roomname = item.getRoom_Name();
                Intent intent = new Intent(getApplicationContext(), Room.class);
                intent.putExtra("roomname", roomname);
                startActivity(intent);
            }
        });

        /**
         * 서비스에서 건내받은 데이터 처리
         */
        showUpdate = new Handler(new Handler.Callback() {  // 액티비티 -> 서비스 핸들러 ( 채팅내용 )
            @Override
            public boolean handleMessage(Message msg) {

                switch (msg.what) {
                    case 101:   // 시청자액티비티에서 채팅메세지 요청
                        String activity_msg = msg.obj.toString();
                        setServiceChatList(activity_msg);

                        break;
                }
                return false;
            }
        });


//        /**
//         * 서비스
//         */
//        if (MyService.serviceIntent == null) {
//            serviceIntent = new Intent(this, MyService.class);
//            startService(serviceIntent);
//            Log.d("bkbk5515", "서비스 시작해줌");
//            //Toast.makeText(getApplicationContext(), "서비스 시작해줌", Toast.LENGTH_LONG).show();
//        } else {
//            serviceIntent = MyService.serviceIntent;//getInstance().getApplication();
//            Log.d("bkbk5515", "서비스 시작 되어있음");
//            //Toast.makeText(getApplicationContext(), "서비스 시작 되어있음", Toast.LENGTH_LONG).show();
//        }
//
//        idByANDROID_ID = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
//        //출처: http://kanzler.tistory.com/64 [kanzler의 세상 이야기]

        /**
         * 초기화
         */
//        a = (Button) findViewById(R.id.go_a);
//        b = (Button) findViewById(R.id.go_b);
//        makea = (Button) findViewById(R.id.makea);
//        makeb = (Button) findViewById(R.id.makeb);


        /**
         * 방만들기
         */
//        makea.setOnClickListener(new OnSingleClickListener() {
//            @Override
//            public void onSingleClick(View v) {
//                //JsonOBJ
//                JSONObject msg_info = new JSONObject();
//                try {
//                    msg_info.put("type", "make");
//                    msg_info.put("room", "A");
//                    msg_info.put("id", LoginActivity.idByANDROID_ID);
//
//                    //보내는 JsonOBJ를 스트링으로 변환 후 서버에 전달
//                    String jsonString = msg_info.toString();
//                    sendService(jsonString);
//
//                    Intent intent = new Intent(getApplicationContext(), Room.class);
//                    intent.putExtra("roomname", "A");
//                    startActivity(intent);
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//        makeb.setOnClickListener(new OnSingleClickListener() {
//            @Override
//            public void onSingleClick(View v) {
//                //JsonOBJ
//                JSONObject msg_info = new JSONObject();
//                try {
//                    msg_info.put("type", "make");
//                    msg_info.put("room", "B");
//                    msg_info.put("id", LoginActivity.idByANDROID_ID);
//
//                    //보내는 JsonOBJ를 스트링으로 변환 후 서버에 전달
//                    String jsonString = msg_info.toString();
//                    sendService(jsonString);
//
//                    Intent intent = new Intent(getApplicationContext(), Room.class);
//                    intent.putExtra("roomname", "B");
//                    startActivity(intent);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });

        /**
         * 단순 입장
         * 임장시 roomname intent로 넘겨줌
         */
//        a.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String roomname = a.getText().toString();
//
//                Intent intent = new Intent(getApplicationContext(), Room.class);
//                intent.putExtra("roomname", roomname);
//                startActivity(intent);
//            }
//        });
//        b.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String roomname = b.getText().toString();
//
//                Intent intent = new Intent(getApplicationContext(), Room.class);
//                intent.putExtra("roomname", roomname);
//                startActivity(intent);
//            }
//        });


    }

    public void setServiceChatList(String serverMsg){
        Log.d("bkbk5515", "setServiceChatList()");

        try {
            JSONArray jsonarray = new JSONObject(serverMsg).getJSONArray("response");

            String id, roomname;
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jObject = jsonarray.getJSONObject(i);

                id = jObject.optString("id");
                roomname = jObject.optString("room");
                Log.d("bkbk5515", id);
                Log.d("bkbk5515", roomname);

                chatRoomListViewItem = new ChatRoomListViewItem(id, roomname);
                chatRoomListViewAdapter.listViewItemList.add(chatRoomListViewItem);
            }
        } catch (Exception e) {
            Log.d("bkbk5515", "Mainactivity showupdate error" + e);
            Toast.makeText(getApplicationContext(), "T0T", Toast.LENGTH_SHORT).show();
        }
        chatRoomListViewAdapter.notifyDataSetChanged();
    }

    public void getChatRoomList(String type) {
        Log.d("bkbk5515", "getChatRoomList()");
        //JsonOBJ
        JSONObject msg_info = new JSONObject();
        try {
            msg_info.put("type", type);
            msg_info.put("id", LoginActivity.mId);


            //보내는 JsonOBJ를 스트링으로 변환 후 서버에 전달
            String jsonString = msg_info.toString();

            //TextUtils.isEmpty() = null 검사
            if (!TextUtils.isEmpty(jsonString)) {
                //new SendmsgTask().execute(jsonString);
                sendService(jsonString);
//                my_msg.setText(jsonString);
//                mymsg.setText("");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendService(String msgJson) {
        Message message = MyService.serviceHandler.obtainMessage();

        message.what = SEND_GETROOMLIST;
        String msg = new String(msgJson);
        message.obj = msg;

        MyService.serviceHandler.sendMessage(message);

    }

    //앱을 실행하고 종료(onDestroy)할 때 서비스(RealService)를 종료(stopService)한다.
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (LoginActivity.serviceIntent != null) {
            stopService(LoginActivity.serviceIntent);
            LoginActivity.serviceIntent = null;
            Log.d("bkbk5515", "Mainactivity onDestroy -> serviceIntent = null");
        }
    }//출처: http://forest71.tistory.com/185 [SW 개발이 좋은 사람]


//
//    /**
//     * 메세지 전송
//     */
//    private class SendmsgTask extends AsyncTask<String, Void, Void> {
//        @Override
//        protected Void doInBackground(String... strings) {
//            try {
//                socketChannel
//                        .socket()
//                        .getOutputStream()
//                        //Strings[0]이 String형태의 JsonOBJ
//                        .write(strings[0].getBytes("UTF-8")); // 서버로
//            } catch (IOException e) {
//                Log.d("doInBackground_error", String.valueOf(e));
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    mymsg.setText("");
//                }
//            });
//        }
//    }
//
//    /**
//     * 받은메세지 가공
//     * 메세지 수신 후 처리
//     * 받은 메세지 UI 처리 스레드 실행
//     */
//    void receive() {
//        while (true) {
//            try {
//                ByteBuffer byteBuffer = ByteBuffer.allocate(2048);
//                //서버가 비정상적으로 종료했을 경우 IOException 발생
//                int readByteCount = socketChannel.read(byteBuffer); //데이터받기
//                Log.d("bkbk5515", "readByteCount - "+readByteCount);
//                //서버가 정상적으로 Socket의 close()를 호출했을 경우
//                if (readByteCount == -1) {
//                    throw new IOException();
//                }
//
//                byteBuffer.flip(); // 문자열로 변환
//                Charset charset = Charset.forName("UTF-8");
//                data = charset.decode(byteBuffer).toString();
//                Log.d("bkbk5515", "receive msg : " + data);
//                handler.post(showUpdate);
//            } catch (IOException e) {
//                Log.d("bkbk5515", "getMsg "+e.getMessage());
//                try {
//                    socketChannel.close();
//                    break;
//                } catch (IOException ee) {
//                    ee.printStackTrace();
//                }
//            }
//        }
//    }
//
//    /**
//     * 리시브 실행
//     */
//    private Thread checkUpdate = new Thread() {
//        public void run() {
//            try {
//                receive();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    };
//
//    /**
//     * 받은 메세지 UI 처리
//     */
//    private Runnable showUpdatemsg = new Runnable() {
//        public void run() {
//            //String receive = "서버에서받음 : " + data;
//            String receive = data;
//            //binding.receiveMsgTv.setText(receive);
//            server_msg.setText(receive);
//            Toast.makeText(getApplicationContext(), "^0^", Toast.LENGTH_SHORT).show();
//        }
//    };
//
//
//
//    /**
//     * 디스트로이 소켓 연결 끊음
//     */
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        try {
//            socketChannel.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
//출처: https://altongmon.tistory.com/505?category=799997 [IOS를 Java]