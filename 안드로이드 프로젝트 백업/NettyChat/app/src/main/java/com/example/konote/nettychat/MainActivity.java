package com.example.konote.nettychat;

import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Handler handler;
    String data;
    SocketChannel socketChannel;
    private static final String HOST = "13.125.73.140";
    private static final int PORT = 5001;

    Button sendMsgBtn;
    EditText sendMsgText;
//    TextView serverGetText;

    ListView chatListView = null;
    ListViewItem listViewItem;
    ListViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sendMsgBtn = (Button) findViewById(R.id.sendMsgBtn);
        sendMsgText = (EditText) findViewById(R.id.sendMsgText);
//        serverGetText = (TextView) findViewById(R.id.serverGetText);

        adapter = new ListViewAdapter();
        chatListView = (ListView) findViewById(R.id.chatList);
        chatListView.setAdapter(adapter);
        listViewItem = new ListViewItem();


        handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    socketChannel = SocketChannel.open();
                    socketChannel.configureBlocking(true);
                    socketChannel.connect(new InetSocketAddress(HOST, PORT));
                    Log.d("호스트, 포트 연결", "성공");
                } catch (Exception ioe) {
                    Log.d("첫번째 스레드 에러메세지", ioe.getMessage() + " / Connection timed out 뜨던곳");
                    ioe.printStackTrace();
                }
                checkUpdate.start();
            }
        }).start();

        sendMsgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String return_msg = sendMsgText.getText().toString();
                //입력된 메세지 없을때 전송하지 않기위한 if else문
                if(return_msg.equals("")){
                    Log.d("return_msg", return_msg);
                }else {
                    try {
                        if (!TextUtils.isEmpty("[bkbk5515] " + return_msg)) {

                            new SendmsgTask().execute("[bkbk5515] " + return_msg);

                            listViewItem = new ListViewItem();
                            listViewItem.setChattext("[bkbk5515] " + return_msg);
                            adapter.listViewItemList.add(listViewItem);
                            adapter.notifyDataSetChanged();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private class SendmsgTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... strings) {
            try {
                Log.d("SendmsgTask", "SendmsgTask try들어옴");
                socketChannel
                        .socket()
                        .getOutputStream()
                        .write(strings[0].getBytes("UTF-8")); // 서버로

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    sendMsgText.setText("");
                }
            });
        }
    }

    private Thread checkUpdate = new Thread() {
        public void run() {
            try {
                Log.d("checkUpdate", "들어옴");
                receive();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    void receive() {
        while (true) {
            try {
                ByteBuffer byteBuffer = ByteBuffer.allocate(256);

                //서버가 비정상적으로 종료했을 경우 IOException 발생
                int readByteCount = socketChannel.read(byteBuffer); //데이터받기

                Log.d("readByteCount", readByteCount + "");

                //서버가 정상적으로 Socket의 close()를 호출했을 경우
                if (readByteCount == -1) {
                    throw new IOException();
                }

                byteBuffer.flip(); // 문자열로 변환
                Charset charset = Charset.forName("UTF-8");
                data = charset.decode(byteBuffer).toString();

                Log.d("서버에서 받은 메세지 receive : ", data);

                handler.post(showUpdate);
            } catch (IOException e) {
                Log.d("서버로부터 받은 메세지", e.getMessage() + "");
                try {
                    socketChannel.close();
                    break;
                } catch (IOException ee) {
                    ee.printStackTrace();
                }
            }
        }
    }

    private Runnable showUpdate = new Runnable() {
        public void run() {
            //유저 네임을 Coming word 자리에 넣어주면 됨.
            //String receive = "Coming word : " + data;
            String receive = "" + data;
            //#serverGetText.setText(receive);

            listViewItem = new ListViewItem();
            listViewItem.setChattext(receive);
            adapter.listViewItemList.add(listViewItem);
            adapter.notifyDataSetChanged();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            socketChannel.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
} //출처: http://altongmon.tistory.com/505?category=799997 [IOS를 Java]
