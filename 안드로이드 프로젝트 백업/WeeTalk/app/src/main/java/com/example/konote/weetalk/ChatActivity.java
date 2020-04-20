package com.example.konote.weetalk;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.stetho.Stetho;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.Socket;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG1 = "ChatActivity_";
    private static final String ipText = "115.71.237.242"; // IP지정으로 사용시에 쓸 코드
    private static int port = 5001;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    Handler msghandler;
    SocketClient client;
    ReceiveThread receive;
    SendThread send;
    SendThread2 send2;
    SendThread3 send3;
    Socket socket;
    PipedInputStream sendstream = null;
    PipedOutputStream receivestream = null;
    LinkedList<SocketClient> threadList;

    private String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}; //권한 설정 변수
    private static final int MULTIPLE_PERMISSIONS = 101;//권한 동의 여부 문의 후 CallBack 함수에 쓰일 변수

    private static final int MY_PERMISSION_CAMERA = 1111;
    private static final int REQUEST_TAKE_PHOTO = 2222;
    private static final int REQUEST_TAKE_ALBUM = 3333;
    private static final int REQUEST_IMAGE_CROP = 4444;
    private static final int REQUEST_ADD_MAP = 5555;
    List<Chat> mChat;

    Button sendbtn;
    ImageView plus_btn;
    EditText msgtext;

    int chack;
    int chack2_photo;
    int chack2_map;

    String msg;
    String Json_name;
    String time;

    SharedPreferences userNameSave;

    int RoomNumber;
    String _roomnumber;

    String mCurrentPhotoPath;
    Uri imageUri;
    Uri photoURI, albumURI;

    String imageFileName;//서버에 보낼 사진 이름
    int Json_Photo_Chack;

    private MySQLiteOpenHelper helper; //    출처: http://bitsoul.tistory.com/118 [Happy Programmer~]
    private MyReceiver myReceiver;
    String dbName = "st_file.db";
    int dbVersion = 1; // 데이터베이스 버전
    private SQLiteDatabase db;
    String tag = "SQLite"; // Log 에 사용할 tag

    static int noty;

    double Latitude;
    double Longitude;

    String TAG = "로그";

    String roomname;
    TextView roomnametext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatlist);

        //노티피케이션을 위한 int형 변수, 메인액티비티에선 0으로 셋 된다.
        //0일땐 노티가 울리게끔 설정할 예정
        noty = 1;

        //스테토 DB 웹에서 확인(쉐어드, sqlite)
        Stetho.initializeWithDefaults(this);
//        출처: http://gun0912.tistory.com/69 [박상권의 삽질블로그]

        checkPermissions();

        roomnametext = (TextView)findViewById(R.id.roomnametext);
        final Intent intent = getIntent();
        RoomNumber = (int) intent.getExtras().get("RoomNumber");
        roomname = (String) intent.getExtras().get("RoomName");
        _roomnumber = Integer.toString(RoomNumber);
        Log.d("채팅방 방 번호", _roomnumber);

        roomnametext.setText(roomname);
        //SQLite
        helper = new MySQLiteOpenHelper(this, dbName, null, dbVersion);
        try {
//         // 데이터베이스 객체를 얻어오는 다른 간단한 방법
//         db = openOrCreateDatabase(dbName,  // 데이터베이스파일 이름
//                          Context.MODE_PRIVATE, // 파일 모드
//                          null);    // 커서 팩토리
//
//         String sql = "create table mytable(id integer primary key autoincrement, name text);";
//        db.execSQL(sql);

            db = helper.getWritableDatabase(); // 읽고 쓸수 있는 DB
            //db = helper.getReadableDatabase(); // 읽기 전용 DB select문
        } catch (SQLiteException e) {
            e.printStackTrace();
            Log.e(tag, "데이터베이스를 얻어올 수 없음");
            finish(); // 액티비티 종료
        }

        mChat = new ArrayList<>();
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        sendbtn = (Button) findViewById(R.id.sendbtn);
        plus_btn = (ImageView) findViewById(R.id.plusbtn);
        msgtext = (EditText) findViewById(R.id.msgText);
        threadList = new LinkedList<ChatActivity.SocketClient>();

        //        insert (); // insert 문 - 삽입추가

        select(_roomnumber); // select 문 - 조회
//        update(); // update 문 - 수정변경
//        delete(); // delete 문 - 삭제 행제거
//        select(_roomnumber);
//        출처: http://bitsoul.tistory.com/118 [Happy Programmer~]

        /**
         * 리사이클러뷰에 현재위치 추가시 버튼을 클릭하여 Google Map으로 넘어가야 하기때문에 클릭리스너가 필요하다.
         * 참고 : http://itpangpang.xyz/44
         * 흐름이 어떻게 되냐면
         * 일단 로그 다 찍어보고 화면 클릭했을때 뭐가 뜨는지 눌러본다.
         * 눌러보다가 child를 알아야 됨을 느낌
         *  x, y를 구해봄(구글 디벨로퍼, 상단의 출처 참고함)
         * 로그에 뜨게끔 해보고 화면 오락실 자판기마냥 드려보면 x,y에 맞게 매번 로그가 다르게 뜨는것을 알 수 있음
         * 근데 중요한건 우리가 알아야될게 포지현 아니겠는가?
         * 포지션값을 구하기위해 if 조건을 달아  rv.getChildAdapterPosition(child) 를 로그에 출력
         * 그렇게되면 채팅 아이템마다 순차적으로 포지션이 증가 ( 0~ 끝까지 차례데로 눌러보면 로그에서 알아보기 쉬움)
         * 토스트 띄움으로서 내가 클릭한 메세지가 정삭적으로 불러와지는지 확인 가능
         */
        // 로그가 눌렀다 땠을때 단 한번만 찍히게끔 하기 위한것
        final GestureDetector gestureDetector = new GestureDetector(ChatActivity.this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });
        mRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

                Log.d(TAG, "onInterceptTouchEvent");
                View child = rv.findChildViewUnder(e.getX(), e.getY());
//                //주석 풀면 로그에 x,y값 뜸
//                Log.d(TAG, "e.getX==>"+e.getX());
//                Log.d(TAG, "e.getY==>"+e.getY());
//                Log.d(TAG, "child==>"+child);
                if (mRecyclerView.getAdapter().getItemCount() == 0) {
                    Log.d("리사이클러뷰에 아무것도 없습니다.", "클릭주의");
                } else {
                    int ss = mChat.get(rv.getChildLayoutPosition(child)).getType();
                    Log.d("인트 ss", String.valueOf(ss));
                    if (child != null && gestureDetector.onTouchEvent(e)) {
                        if (ss == 4444 || ss == 5555) {
                            Log.d(TAG, "getChildAdapterPosition=>" + rv.getChildAdapterPosition(child));
                            Log.d(TAG, "getChildLayoutPosition=>" + rv.getChildLayoutPosition(child));
                            Log.d(TAG, "getChildViewHolder=>" + rv.getChildViewHolder(child));
                            Toast.makeText(getApplication(), mChat.get(rv.getChildLayoutPosition(child)).getMsg(), Toast.LENGTH_SHORT).show();

                            String s;
                            s = mChat.get(rv.getChildLayoutPosition(child)).getMsg();
                            String[] array;
                            array = s.split(",");
                            dumpArray(array);

                            String intent_latitude;
                            intent_latitude = array[0];

                            String intent_longitude;
                            intent_longitude = array[1];
                            Log.d("지도버튼클릭 위도", intent_latitude);
                            Log.d("지도버튼클릭 경도", intent_longitude);

                            Intent intent_go_map = new Intent(ChatActivity.this, ChatMapClickView.class);
                            intent_go_map.putExtra("MAP1", intent_latitude);
                            intent_go_map.putExtra("MAP2", intent_longitude);
                            startActivity(intent_go_map);
                        }
                    }
                }
                return false;
            }//출처: http://itpangpang.xyz/44 [ITPangPang]


            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
                Log.d(TAG, "onTouchEvent");
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
                Log.d(TAG, "onRequestDisallowInterceptTouchEvent");
            }
        });

        chack = 0000;
        chack2_photo = 2222;
        chack2_map = 4444;

        msghandler = new Handler() {
            @Override
            public void handleMessage(Message hdmsg) {
                if (hdmsg.what == 0) {
                    Log.d("핸들러 포토체크", String.valueOf(hdmsg.what));
                    msg = hdmsg.obj.toString();

                    msgtext.setText("");

                    Chat chat = new Chat(chack, msg, Json_name, time);
                    mChat.add(chat);

                    mAdapter = new MyAdapter(mChat, getApplicationContext());
                    mRecyclerView.setAdapter(mAdapter);

                    //아래코드(최신목록으로)
                    mRecyclerView.scrollToPosition(mChat.size() - 1);

                    chack = 0000;
                    chack2_photo = 2222;
                    chack2_map = 4444;
                } else if (hdmsg.what == 1) {
                    Log.d("핸들러 포토체크", String.valueOf(hdmsg.what));
                    msg = hdmsg.obj.toString();

                    msgtext.setText("");

                    Chat chat = new Chat(chack2_photo, msg, Json_name, time);
                    mChat.add(chat);
                    Log.d("타입", String.valueOf(chack2_photo));
                    Log.d("내용", msg);
                    Log.d("이름", Json_name);
                    Log.d("시간", time);

                    mAdapter = new MyAdapter(mChat, getApplicationContext());
                    mRecyclerView.setAdapter(mAdapter);

                    //아래코드(최신목록으로)
                    mRecyclerView.scrollToPosition(mChat.size() - 1);

                    chack = 0000;
                    chack2_photo = 2222;
                    chack2_map = 4444;
                } else {
                    Log.d("핸들러 포토체크", String.valueOf(hdmsg.what));
                    msg = hdmsg.obj.toString();

                    msgtext.setText("");

                    Chat chat = new Chat(chack2_map, msg, Json_name, time);
                    mChat.add(chat);
                    Log.d("타입", String.valueOf(chack2_photo));
                    Log.d("내용", msg);
                    Log.d("이름", Json_name);
                    Log.d("시간", time);

                    mAdapter = new MyAdapter(mChat, getApplicationContext());
                    mRecyclerView.setAdapter(mAdapter);

                    //아래코드(최신목록으로)
                    mRecyclerView.scrollToPosition(mChat.size() - 1);

                    chack = 0000;
                    chack2_photo = 2222;
                    chack2_map = 4444;
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
        Button map_btn = (Button) findViewById(R.id.map_btn);
        map_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(ChatActivity.this, ChatMap.class);
//                startActivityForResult(intent, REQUEST_ADD_MAP);
                Intent intent = new Intent(ChatActivity.this, ChatMap.class);
//                intent.putExtra("RoomNumber", RoomNumber);
                startActivityForResult(intent, REQUEST_ADD_MAP);
            }
        });

        plus_btn.setOnClickListener(this);
    }/////////////////////////////////////크리에이트

    // 배열을 화면에, 요소별로 알기 쉽게 출력
    public static void dumpArray(String[] array) {
        for (int i = 0; i < array.length; i++)
            System.out.format("array[%d] = %s%n", i, array[i]);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        noty = 0;
    }/////////////////////////////////////디스트로이

    class SocketClient extends Thread {
        boolean threadAlive;
        String ip;
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

                userNameSave = getSharedPreferences("MyData", MODE_PRIVATE);
                String _mac = userNameSave.getString("userID", "");

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("roomNumber", _roomnumber);
                jsonObject.put("userName", _mac);

                //mac 전송
                output.writeUTF(jsonObject.toString());
                Log.d("방번호 서버에 전송", String.valueOf(_roomnumber));

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
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
                    String SQLite_time;

                    JSONObject jsonObject = new JSONObject(msg);
                    String Json_msg;
                    Json_name = jsonObject.get("userName").toString();
                    Json_msg = jsonObject.get("userMsg").toString();
                    Json_Photo_Chack = (int) jsonObject.get("PhotoCheak");
                    SQLite_time = jsonObject.get("time").toString();

                    //어댑터 time set
                    time = jsonObject.get("time").toString();

                    //SQLite
                    insert(Json_name, Json_msg, Json_Photo_Chack, _roomnumber, SQLite_time); // insert 문 - 삽입추가

                    Log.d("PhotoCheak 서버에서 받은 포토체크", String.valueOf(Json_Photo_Chack));

//                    myReceiver = new MyReceiver(Json_name, _roomnumber, Json_msg, Json_Photo_Chack);
                    //브로드캐스트리시버는 생성자 사용 불가
                    //그렇기때문에
                    if (noty == 0) {
                        Intent intent = new Intent(ChatActivity.this, MyReceiver.class);
                        intent.putExtra("Json_name", Json_name);
                        intent.putExtra("_roomnumber", _roomnumber);
                        intent.putExtra("Json_msg", Json_msg);
                        intent.putExtra("Json_Photo_Chack", Json_Photo_Chack);
                        sendBroadcast(intent);
                    }

                    if (msg != null) {
                        Message hdmsg = msghandler.obtainMessage();
                        hdmsg.what = Json_Photo_Chack;
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
                chack = 1111;

                if (output != null) {
                    if (sendmsg != null) {
                        // 현재시간을 msec 으로 구한다.
                        long now = System.currentTimeMillis();
                        // 현재시간을 date 변수에 저장한다.
                        Date date = new Date(now);
                        // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
                        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        // nowDate 변수에 값을 저장한다.
                        String sendtime = sdfNow.format(date);

                        int PhotoCheak = 0;
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("roomNumber", _roomnumber);
                        jsonObject.put("sendmsg", sendmsg);
                        jsonObject.put("PhotoCheak", PhotoCheak);
                        jsonObject.put("time", sendtime);

                        output.writeUTF(jsonObject.toString());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NullPointerException npe) {
                npe.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    // 사진전송을 위한 send 2
    class SendThread2 extends Thread {
        private Socket socket;
        DataOutputStream output;

        public SendThread2(Socket socket) {
            this.socket = socket;
            try {
                output = new DataOutputStream(socket.getOutputStream());
            } catch (Exception e) {
            }
        }

        public void run() {
            try {
                chack2_photo = 3333;

                if (output != null) {
                    if (imageFileName != null) {
                        // 현재시간을 msec 으로 구한다.
                        long now = System.currentTimeMillis();
                        // 현재시간을 date 변수에 저장한다.
                        Date date = new Date(now);
                        // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
                        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        // nowDate 변수에 값을 저장한다.
                        String sendtime = sdfNow.format(date);

                        int PhotoCheak = 1;
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("roomNumber", _roomnumber);
                        jsonObject.put("sendmsg", imageFileName);
                        jsonObject.put("PhotoCheak", PhotoCheak);
                        jsonObject.put("time", sendtime);

                        output.writeUTF(jsonObject.toString());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NullPointerException npe) {
                npe.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    // 지도전송을 위한 send 3
    class SendThread3 extends Thread {
        private Socket socket;
        DataOutputStream output;

        public SendThread3(Socket socket) {
            this.socket = socket;
            try {
                output = new DataOutputStream(socket.getOutputStream());
            } catch (Exception e) {
            }
        }

        public void run() {
            try {
                chack2_map = 5555;

                if (output != null) {
//                    if (Latitude != null) {
                    // 현재시간을 msec 으로 구한다.
                    long now = System.currentTimeMillis();
                    // 현재시간을 date 변수에 저장한다.
                    Date date = new Date(now);
                    // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
                    SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    // nowDate 변수에 값을 저장한다.
                    String sendtime = sdfNow.format(date);

                    int PhotoCheak = 2;

                    //위도와 경도를 String형태로 한번에 보냄
                    String split;
                    split = Latitude + "," + Longitude;
                    Log.d("스플릿", split);

                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("roomNumber", _roomnumber);
                    jsonObject.put("sendmsg", split);
                    jsonObject.put("PhotoCheak", PhotoCheak);
                    jsonObject.put("time", sendtime);

                    output.writeUTF(jsonObject.toString());
//                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NullPointerException npe) {
                npe.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.plusbtn) {

            DialogInterface.OnClickListener captureCamera = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    captureCamera();
                }
            };

            DialogInterface.OnClickListener getAlbum = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    getAlbum();
                }
            };

            DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            };

            new android.app.AlertDialog.Builder(this)
                    .setTitle("사진전송")
                    .setPositiveButton("사진촬영", captureCamera)
                    .setNegativeButton("앨범", getAlbum)
                    .setNeutralButton("취소", cancelListener)
                    .show();
        }
    }

    private void captureCamera() {
        String state = Environment.getExternalStorageState();
        // 외장 메모리 검사
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    Log.e("captureCamera Error", ex.toString());
                }
                if (photoFile != null) {
                    // getUriForFile의 두 번째 인자는 Manifest provier의 authorites와 일치해야 함
                    Uri providerURI = FileProvider.getUriForFile(this, getPackageName(), photoFile);
                    imageUri = providerURI;

                    // 인텐트에 전달할 때는 FileProvier의 Return값인 content://로만!!, providerURI의 값에 카메라 데이터를 넣어 보냄
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, providerURI);

                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                }
            }
        } else {
            Toast.makeText(this, "저장공간이 접근 불가능한 기기입니다", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    public File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        imageFileName = "JPEG_" + timeStamp + ".jpg";
        File imageFile = null;
        File storageDir = new File(Environment.getExternalStorageDirectory() + "/Pictures", "gyeom");

        if (!storageDir.exists()) {
            Log.i("mCurrentPhotoPath1", storageDir.toString());
            storageDir.mkdirs();
        }

        imageFile = new File(storageDir, imageFileName);
        mCurrentPhotoPath = imageFile.getAbsolutePath();
        Log.d("크리에이트 mCurrentPhotoPath", mCurrentPhotoPath);

        return imageFile;
    }

    private void getAlbum() {
        Log.i("getAlbum", "Call");
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, REQUEST_TAKE_ALBUM);
    }

    private void galleryAddPic() {
        Log.i("galleryAddPic", "Call");
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        // 해당 경로에 있는 파일을 객체화(새로 파일을 만든다는 것으로 이해하면 안 됨)
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);
        Toast.makeText(this, "사진이 앨범에 저장되었습니다.", Toast.LENGTH_SHORT).show();
    }

    // 카메라 전용 크랍
    public void cropImage() {
        Log.i("cropImage", "Call");
        Log.i("cropImage", "photoURI : " + photoURI + " / albumURI : " + albumURI);

        Intent cropIntent = new Intent("com.android.camera.action.CROP");

        // 50x50픽셀미만은 편집할 수 없다는 문구 처리 + 갤러리, 포토 둘다 호환하는 방법
        cropIntent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        cropIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        cropIntent.setDataAndType(photoURI, "image/*");
        //cropIntent.putExtra("outputX", 200); // crop한 이미지의 x축 크기, 결과물의 크기
        //cropIntent.putExtra("outputY", 200); // crop한 이미지의 y축 크기
        //cropIntent.putExtra("aspectX", 1); // crop 박스의 x축 비율, 1&1이면 정사각형
        //cropIntent.putExtra("aspectY", 1); // crop 박스의 y축 비율
        cropIntent.putExtra("scale", true);
        cropIntent.putExtra("output", albumURI); // 크랍된 이미지를 해당 경로에 저장
        startActivityForResult(cropIntent, REQUEST_IMAGE_CROP);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        Log.d(TAG, "리절트");
        if (requestCode == REQUEST_TAKE_PHOTO) {
            if (resultCode == RESULT_OK) {
                try {
                    Log.i("REQUEST_TAKE_PHOTO", "OK");
                    galleryAddPic();
                    //내가 추가한 코드~~~~
                    uploadFile(mCurrentPhotoPath);
                } catch (Exception e) {
                    Log.e("REQUEST_TAKE_PHOTO", e.toString());
                }
            } else {
                Toast.makeText(ChatActivity.this, "사진찍기를 취소하였습니다.", Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == REQUEST_TAKE_ALBUM) {
            if (resultCode == RESULT_OK) {
                if (data.getData() != null) {
                    try {
                        File albumFile = null;
                        albumFile = createImageFile();
                        photoURI = data.getData();
                        albumURI = Uri.fromFile(albumFile);
                        cropImage();
                    } catch (Exception e) {
                        Log.e("TAKE_ALBUM_SINGLE ERROR", e.toString());
                    }
                }
            }
        }
        if (requestCode == REQUEST_IMAGE_CROP) {
            if (resultCode == RESULT_OK) {
                galleryAddPic();

                //내가 추가한 코드~~~~
                uploadFile(mCurrentPhotoPath);
                Log.d("mCurrentPhotoPath", mCurrentPhotoPath);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //지연시키길 원하는 밀리초 뒤에 동작
                        // 내가 추가한 코드
                        send2 = new SendThread2(socket);
                        send2.start();
                    }
                }, 500);
            }
        }
        if (requestCode == REQUEST_ADD_MAP) {
            if (resultCode == RESULT_OK) {
                Log.d("다시 돌아왔다", "값 체크 직전");
                Latitude = (double) data.getExtras().get("Latitude");
                Longitude = (double) data.getExtras().get("Longitude");
                Log.d(TAG1, "받은위도" + String.valueOf(Latitude));
                Log.d(TAG1, "받은경도" + String.valueOf(Longitude));

                send3 = new SendThread3(socket);
                send3.start();
            }
        }
    }

    public void uploadFile(String filePath) {
        String url = "http://bkbk55152.vps.phps.kr/photo.php";
        try {
            Log.d("업로드파일 mCurrentPhotoPath", filePath);
            UploadFile uploadFile = new UploadFile(ChatActivity.this);
            uploadFile.setPath(filePath);
            uploadFile.execute(url);
        } catch (Exception e) {
//            sendLogMsgPHP(e.toString());
        }
    }


    ////////////////////////////////////////////////////////////////////////////////////////
    private boolean checkPermissions() {
        int result;
        List<String> permissionList = new ArrayList<>();
        for (String pm : permissions) {
            result = ContextCompat.checkSelfPermission(this, pm);
            if (result != PackageManager.PERMISSION_GRANTED) { //사용자가 해당 권한을 가지고 있지 않을 경우 리스트에 해당 권한명 추가
                permissionList.add(pm);
            }
        }
        if (!permissionList.isEmpty()) { //권한이 추가되었으면 해당 리스트가 empty가 아니므로 request 즉 권한을 요청합니다.
            ActivityCompat.requestPermissions(this, permissionList.toArray(new String[permissionList.size()]), MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    //아래는 권한 요청 Callback 함수입니다. PERMISSION_GRANTED로 권한을 획득했는지 확인할 수 있습니다. 아래에서는 !=를 사용했기에
    //권한 사용에 동의를 안했을 경우를 if문으로 코딩되었습니다.
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MULTIPLE_PERMISSIONS: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++) {
                        if (permissions[i].equals(this.permissions[0])) {
                            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                                showNoPermissionToastAndFinish();
                            }
                        } else if (permissions[i].equals(this.permissions[1])) {
                            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                                showNoPermissionToastAndFinish();

                            }
                        } else if (permissions[i].equals(this.permissions[2])) {
                            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                                showNoPermissionToastAndFinish();

                            }
                        }
                    }
                } else {
                    showNoPermissionToastAndFinish();
                }
                return;
            }
        }
    }

    //권한 획득에 동의를 하지 않았을 경우 아래 Toast 메세지를 띄우며 해당 Activity를 종료시킵니다.
    private void showNoPermissionToastAndFinish() {
        Toast.makeText(this, "권한 요청에 동의 해주셔야 이용 가능합니다. 설정에서 권한 허용 하시기 바랍니다.", Toast.LENGTH_SHORT).show();
        finish();
    }

    ///////////////////////////SQLite/////////////////////////////////////
    void insert(String _name, String _msg, int _photocheak, String _roomnumber, String _time) {//삽입

        db.execSQL("insert into mytable (name, msg, photocheak, roomnumber, time) values('" + _name + "','" + _msg + "','" + _photocheak + "','" + _roomnumber + "','" + _time + "');");
//        db.execSQL("insert into mytable (name) values('병기');");

        Log.d(tag, "SQLite insert 성공~!");
    }

    void select(String _roomnumber) {//조회
        Cursor c = db.rawQuery("select * from mytable;", null);
        while (c.moveToNext()) {

            int id = c.getInt(0);
            String SQLite_name = c.getString(1);
            String SQLite_msg = c.getString(2);
            int SQLite_photocheak = c.getInt(3);
            String SQLite_roomnumber = c.getString(4);
            String SQLite_time = c.getString(5);

            Log.d("SQLite ID : ", String.valueOf(id));
            Log.d("SQLite 이름 : ", SQLite_name);
            Log.d("SQLite 메세지 : ", SQLite_msg);
            Log.d("SQLite 사진유무 : ", String.valueOf(SQLite_photocheak));
            Log.d("SQLite 방번호 : ", SQLite_roomnumber);
            Log.d("SQLite 시간 : ", SQLite_time);

            userNameSave = getSharedPreferences("MyData", MODE_PRIVATE);
            String SQLite_mac = userNameSave.getString("userID", "");

            //방번호가 현재 방번호와 같다면
            if (SQLite_roomnumber.equals(_roomnumber)) {
                //보낸사람이 나라면
                if (SQLite_name.equals(SQLite_mac)) {
                    int SQLite_cheak = 1111;
                    int SQLite_photo_cheak = 3333;
                    int SQLite_map_cheak = 5555;

                    if (SQLite_photocheak == 0) {//메세지라면
                        Chat chat = new Chat(SQLite_cheak, SQLite_msg, SQLite_name, SQLite_time);
                        mChat.add(chat);

                        mAdapter = new MyAdapter(mChat, getApplicationContext());
                        mRecyclerView.setAdapter(mAdapter);

                        //아래코드(최신목록으로)
                        mRecyclerView.scrollToPosition(mChat.size() - 1);

                        chack = 0000;
                        chack2_photo = 2222;
                        chack2_map = 4444;
                    } else if (SQLite_photocheak == 1) {//사진이라면
                        Chat chat = new Chat(SQLite_photo_cheak, SQLite_msg, SQLite_name, SQLite_time);
                        mChat.add(chat);

                        mAdapter = new MyAdapter(mChat, getApplicationContext());
                        mRecyclerView.setAdapter(mAdapter);

                        //아래코드(최신목록으로)
                        mRecyclerView.scrollToPosition(mChat.size() - 1);

                        chack = 0000;
                        chack2_photo = 2222;
                        chack2_map = 4444;
                    } else {
                        Chat chat = new Chat(SQLite_map_cheak, SQLite_msg, SQLite_name, SQLite_time);
                        mChat.add(chat);

                        mAdapter = new MyAdapter(mChat, getApplicationContext());
                        mRecyclerView.setAdapter(mAdapter);

                        //아래코드(최신목록으로)
                        mRecyclerView.scrollToPosition(mChat.size() - 1);

                        chack = 0000;
                        chack2_photo = 2222;
                        chack2_map = 4444;
                    }
                } else {//보낸사람이 내가 아니라면
                    int SQLite_cheak = 0000;
                    int SQLite_photo_cheak = 2222;
                    int SQLite_map_cheak = 4444;

                    if (SQLite_photocheak == 0) {//메세지라면
                        Chat chat = new Chat(SQLite_cheak, SQLite_msg, SQLite_name, SQLite_time);
                        mChat.add(chat);

                        mAdapter = new MyAdapter(mChat, getApplicationContext());
                        mRecyclerView.setAdapter(mAdapter);

                        //아래코드(최신목록으로)
                        mRecyclerView.scrollToPosition(mChat.size() - 1);

                        chack = 0000;
                        chack2_photo = 2222;
                    } else if (SQLite_photocheak == 1) {//사진이라면
                        Chat chat = new Chat(SQLite_photo_cheak, SQLite_msg, SQLite_name, SQLite_time);
                        mChat.add(chat);

                        mAdapter = new MyAdapter(mChat, getApplicationContext());
                        mRecyclerView.setAdapter(mAdapter);

                        //아래코드(최신목록으로)
                        mRecyclerView.scrollToPosition(mChat.size() - 1);

                        chack = 0000;
                        chack2_photo = 2222;

                    } else {
                        Chat chat = new Chat(SQLite_map_cheak, SQLite_msg, SQLite_name, SQLite_time);
                        mChat.add(chat);

                        mAdapter = new MyAdapter(mChat, getApplicationContext());
                        mRecyclerView.setAdapter(mAdapter);

                        //아래코드(최신목록으로)
                        mRecyclerView.scrollToPosition(mChat.size() - 1);

                        chack = 0000;
                        chack2_photo = 2222;
                    }
                }
            }
            //아래코드(최신목록으로)
            mRecyclerView.scrollToPosition(mChat.size() - 1);
        }
    }

    void update() {//수정변경
//        db.execSQL("update mytable set name='Park' where id=5;");
        Log.d(tag, "update 완료");
    }

    void delete() {//삭제
//        db.execSQL("delete from mytable where id=2;");
        Log.d(tag, "delete 완료");
    }
}