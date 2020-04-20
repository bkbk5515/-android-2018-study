package com.example.konote.weetalk;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by KONOTE on 2017-11-20.
 */

public class RoomList extends AppCompatActivity {

//    private static final String ipText = "115.71.237.242"; // IP지정으로 사용시에 쓸 코드
//    private static int port = 5001;
//    SocketClient client;
//    ReceiveThread receive;
//    Socket socket;
//    PipedInputStream sendstream = null;
//    PipedOutputStream receivestream = null;
//    LinkedList<SocketClient> threadList;

    ListView listview = null;
    ListViewAdapter adapter;
    ListViewItem listViewItem;
    TextView roomnameadd;
    Button roomnameaddbtn;
    private AlertDialog dialog;
    private boolean validate = false; // 사용할 수 있는 아이디인지 체크
    SharedPreferences userNameSave;

    private MySQLiteOpenHelper helper;
    String dbName = "st_file.db";
    int dbVersion = 1; // 데이터베이스 버전
    private SQLiteDatabase db;
    String tag = "SQLite"; // Log 에 사용할 tag

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.room_list);
        ////////////////////////////////////////////////////////////
        Response.Listener<String> RL = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("받아온 방 목록", response);
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray jsonarray = jsonResponse.getJSONArray("response");

                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject object = jsonarray.getJSONObject(i);
                        String room_name = object.getString("roomname");
                        Log.d("방제", room_name);
                        roomnameadd.setText("");

                        ListViewItem listViewItem = new ListViewItem();
                        listViewItem.setRoomName(room_name);
                        adapter.listViewItemList.add(listViewItem);
                        // listview 갱신
                        adapter.notifyDataSetChanged();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        RoomListAdd roomListMake = new RoomListAdd(RL);
        RequestQueue rq_roomlist = Volley.newRequestQueue(RoomList.this);
        rq_roomlist.add(roomListMake);
        ////////////////////////////////////////////////////////////
        adapter = new ListViewAdapter();
        listview = (ListView) findViewById(R.id.listview1);
        listview.setAdapter(adapter);
        listViewItem = new ListViewItem();

        roomnameadd = (TextView) findViewById(R.id.room_name);
        roomnameaddbtn = (Button) findViewById(R.id.room_add);

        ImageView friendlist = (ImageView) findViewById(R.id.list_btn1);
        friendlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RoomList.this, MainActivity.class);
                RoomList.this.startActivity(intent);
            }
        });
        ImageView map_btn = (ImageView) findViewById(R.id.map_btn);
        map_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RoomList.this, MapActivity.class);
                RoomList.this.startActivity(intent);
            }
        });
        ImageView set_btn = (ImageView) findViewById(R.id.set_btn);
        set_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RoomList.this, SetActivity.class);
                RoomList.this.startActivity(intent);
            }
        });

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                // get item
                ListViewItem item = (ListViewItem) parent.getItemAtPosition(position);
                Log.d("방제목", item.getRoomName());

                Intent intent2 = new Intent(RoomList.this, ChatActivity.class);
                intent2.putExtra("RoomNumber", position);
                intent2.putExtra("RoomName", item.getRoomName());
                startActivity(intent2);
            }
        });

        roomnameaddbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String roomnamelistadd = roomnameadd.getText().toString();
                if (validate) {
                    return;
                }
                if (roomnamelistadd.equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RoomList.this);
                    dialog = builder.setMessage("방 제목을 입력해 주세요.")
                            .setPositiveButton("확인", null)
                            .create();
                    dialog.show();
                    return;
                }
                Response.Listener<String> RL = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
//                            userNameSave = getSharedPreferences("MyData", MODE_PRIVATE);
//                            String _mac = userNameSave.getString("userID", "");
                            if (success) {
                                roomnameadd.setText("");
                                ListViewItem listViewItem = new ListViewItem();
                                listViewItem.setRoomName(roomnamelistadd);
                                adapter.listViewItemList.add(listViewItem);
                                // listview 갱신
                                adapter.notifyDataSetChanged();
                            } else {
//                                findText.setText("");
//                                ListViewItem listViewItem = new ListViewItem();
//                                listViewItem.setUserID(userMail);
//                                adapter.listViewItemList.add(listViewItem);
//                                // listview 갱신
//                                adapter.notifyDataSetChanged();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                RoomMake roomMake = new RoomMake(roomnamelistadd, RL);
                RequestQueue rq = Volley.newRequestQueue(RoomList.this);
                rq.add(roomMake);
            }
        });
    }//////////////////////////크리에이트 끝

//
//    class SocketClient extends Thread {
//        boolean threadAlive;
//        String ip;
//        int port;
//
//        //InputStream inputStream = null;
//        OutputStream outputStream = null;
//        BufferedReader br = null;
//        private DataOutputStream output = null;
//
//        public SocketClient(String ip, int port) {
//            threadAlive = true;
//            this.ip = ip;
//            this.port = port;
//        }
//
//        @Override
//        public void run() {
//            try {
//                // 연결후 바로 ReceiveThread 시작
//                socket = new Socket(ip, Integer.parseInt(String.valueOf(port)));
//                //inputStream = socket.getInputStream();
//                output = new DataOutputStream(socket.getOutputStream());
//                receive = new ReceiveThread(socket);
//                receive.start();
//
//                userNameSave = getSharedPreferences("MyData", MODE_PRIVATE);
//                String _mac = userNameSave.getString("userID", "");
//
//                JSONObject jsonObject = new JSONObject();
//                jsonObject.put("roomNumber", _roomnumber);
//                jsonObject.put("userName", _mac);
//
//                //mac 전송
//                output.writeUTF(jsonObject.toString());
//                Log.d("방번호 서버에 전송", String.valueOf(_roomnumber));
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//
//    class ReceiveThread extends Thread {
//        private Socket socket = null;
//        DataInputStream input;
//
//        public ReceiveThread(Socket socket) {
//            this.socket = socket;
//            try {
//                input = new DataInputStream(socket.getInputStream());
//            } catch (Exception e) {
//            }
//        }
//
//        // 메세지 수신후 Handler로 전달
//        public void run() {
//            try {
//                while (input != null) {
//                    String msg = input.readUTF();
//                    String SQLite_time;
//
//                    JSONObject jsonObject = new JSONObject(msg);
//                    String Json_msg;
//                    Json_name = jsonObject.get("userName").toString();
//                    Json_msg = jsonObject.get("userMsg").toString();
//                    Json_Photo_Chack = (int) jsonObject.get("PhotoCheak");
//
//                    //sqlite time
//                    SQLite_time = jsonObject.get("time").toString();
//
//                    //어댑터 time set
//                    time = jsonObject.get("time").toString();
//
//                    //SQLite
//                    insert (Json_name, Json_msg, Json_Photo_Chack, _roomnumber, SQLite_time); // insert 문 - 삽입추가
//
//                    Log.d("PhotoCheak 서버에서 받은 포토체크", String.valueOf(Json_Photo_Chack));
//                    Log.d("PhotoCheak 서버에서 받은 사진이름", Json_msg);
//
////                    //핸들러로 보내는 메세지 재정의(그냥 저장할것이라 필요없어서 주석 처리함
////                    if (msg != null) {
////                        Message hdmsg = msghandler.obtainMessage();
////                        hdmsg.what = Json_Photo_Chack;
////                        hdmsg.obj = Json_msg;
////
////                        msghandler.sendMessage(hdmsg);
////                        Log.d(ACTIVITY_SERVICE, hdmsg.obj.toString());
////                    }
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    ///////////////////////////SQLite/////////////////////////////////////
//    void insert (String _name, String _msg, int _photocheak, String _roomnumber, String _time) {//삽입
//
//        db.execSQL("insert into mytable (name, msg, photocheak, roomnumber, time) values('"+ _name + "','" + _msg + "','" + _photocheak + "','" + _roomnumber + "','" + _time +"');");
////        db.execSQL("insert into mytable (name) values('병기');");
//
//        Log.d(tag, "SQLite insert 성공~!");
//    }
}
