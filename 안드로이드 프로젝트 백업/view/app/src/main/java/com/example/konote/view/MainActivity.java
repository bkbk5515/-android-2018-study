package com.example.konote.view;

import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private static final String ipText = "115.71.237.242"; // IP지정으로 사용시에 쓸 코드
    private static int port = 5001;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    Handler msghandler;
    SocketClient client;
    ReceiveThread receive;
    SendThread send;
    Socket socket;
    PipedInputStream sendstream = null;
    PipedOutputStream receivestream = null;
    LinkedList<SocketClient> threadList;
    List<Chat> mChat;

    Button sendbtn;
    EditText msgtext;

    int chack;
    //    String mac;//유저이름
    String msg;
    String Json_name;
    String time;

    SharedPreferences userNameSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mChat = new ArrayList<>();
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        sendbtn = (Button) findViewById(R.id.sendbtn);
        msgtext = (EditText) findViewById(R.id.msgText);
        threadList = new LinkedList<MainActivity.SocketClient>();

        msghandler = new Handler() {
            @Override
            public void handleMessage(Message hdmsg) {
                if (hdmsg.what == 1111) {
                    msg = hdmsg.obj.toString();

                    int type = hdmsg.what;

                    // 현재시간을 msec 으로 구한다.
                    long now = System.currentTimeMillis();
                    // 현재시간을 date 변수에 저장한다.
                    Date date = new Date(now);
                    // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
                    SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    // nowDate 변수에 값을 저장한다.
                    time = sdfNow.format(date);

                    msgtext.setText("");

                    Chat chat = new Chat(chack, msg, Json_name);
                    mChat.add(chat);

//                    mAdapter = new MyAdapter(chack, mChat, jsonname, time);
                    mAdapter = new MyAdapter(mChat);
                    mRecyclerView.setAdapter(mAdapter);

                    //아래코드(최신목록으로)
                    mRecyclerView.scrollToPosition(mChat.size() - 1);

                    chack = 0000;

                    //아래코드는 새로고침 기능임
                    //mAdapter.notifyItemInserted(mChat.size()-1);

//                    //Json으로 이름과 메세지 받아옴
//                    JSONObject jsonObject = new JSONObject();
//                    try {
//                        jsonname = jsonObject.getString("userID");
//                        jsonmsg = jsonObject.getString("userMSG");
//                        Log.d("Json 이름 받아오기", jsonname);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
                }
            }
        };
        client = new SocketClient(ipText, port);
        threadList.add(client);
        client.start();

        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //SendThread 시작
                if (msgtext.getText().toString() != null) {
                    send = new SendThread(socket);
                    send.start();
                    //시작후 edittext 초기화
                    msgtext.setText("");
                }
            }
        });
//        mAdapter = new MyAdapter(mChat);
//        mRecyclerView.setAdapter(mAdapter);
    }

    class SocketClient extends Thread {
        boolean threadAlive;
        String ip;
        /////////////String mac;
        int port;

        //InputStream inputStream = null;
        OutputStream outputStream = null;
        BufferedReader br = null;

        private DataOutputStream output = null;

        public SocketClient(String ip, int port) {
            threadAlive = true;
            this.ip = ip;
            this.port = port;
        }

        @Override
        public void run() {
            try {
                // 연결후 바로 ReceiveThread 시작
                socket = new Socket(ip, Integer.parseInt(String.valueOf(port)));
                //inputStream = socket.getInputStream();
                output = new DataOutputStream(socket.getOutputStream());
                receive = new ReceiveThread(socket);
                receive.start();

//                //mac주소를 받아오기위해 설정
//                WifiManager mng = (WifiManager) getSystemService(WIFI_SERVICE);
//                WifiInfo info = mng.getConnectionInfo();
//                mac = info.getMacAddress();
                ////////////////mac = "김병기";

                userNameSave = getSharedPreferences("MyData", MODE_PRIVATE);
                String _mac = userNameSave.getString("userID", "");

                //mac 전송
                output.writeUTF(_mac);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    class ReceiveThread extends Thread {
        private Socket socket = null;
        DataInputStream input;

        public ReceiveThread(Socket socket) {
            this.socket = socket;
            try {
                input = new DataInputStream(socket.getInputStream());
            } catch (Exception e) {
            }
        }

        // 메세지 수신후 Handler로 전달
        public void run() {
            try {
                while (input != null) {
                    String msg = input.readUTF();

                    JSONObject jsonObject = new JSONObject(msg);
                    String Json_msg;
                    Json_name = jsonObject.get("userName").toString();
                    Json_msg = jsonObject.get("userMsg").toString();

                    if (msg != null) {
                        Message hdmsg = msghandler.obtainMessage();
                        hdmsg.what = 1111;
                        hdmsg.obj = Json_msg;

                        msghandler.sendMessage(hdmsg);
                        Log.d(ACTIVITY_SERVICE, hdmsg.obj.toString());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    class SendThread extends Thread {
        private Socket socket;
        String sendmsg = msgtext.getText().toString();
        DataOutputStream output;

        public SendThread(Socket socket) {
            this.socket = socket;
            try {
                output = new DataOutputStream(socket.getOutputStream());
            } catch (Exception e) {
            }
        }

        public void run() {
            try {
                // 메세지 전송부 (누군지 식별하기위한 방법으로 mac를 사용)
                chack = 1111;

                if (output != null) {
                    if (sendmsg != null) {
//                        output.writeUTF(mac + "  :  " + sendmsg);
                        output.writeUTF(sendmsg);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NullPointerException npe) {
                npe.printStackTrace();
            }
        }
    }
}
