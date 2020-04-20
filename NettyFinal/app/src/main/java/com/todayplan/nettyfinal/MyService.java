package com.todayplan.nettyfinal;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.PowerManager;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MyService extends Service {
    private final String MYSERVICE_TAG = "bkbk5515";
    public static Intent serviceIntent = null;

    public static int MSG_SEND_TO_ACTIVITY = 100;    // 서버 -> 서비스 -> 액티비티
    public static int CHATLIST_SEND_TO_ACTIVITY = 101;

    private String HOST = "192.1.1.145";
//    private String HOST = "192.1.1.55";

    private int PORT = 8888;

    private SocketChannel socketChannel;

    private Thread main_Connect_Thread;
    public static Thread receive_thread; // 서버로부터 메세지 받는 스레드 /
    public static Send_thread send_thread;   // 클라이언트가 채팅액티비티에서 받은 내용을 서버로 보낼때 쓰는 스레드*/

    public final IBinder mBinder = new MyBinder(); /*/ 클라이언트 activity에게 반환할 Ibinder 객체 생성*/
    public static Handler serviceHandler;  /*/ 서비스쪽 핸들러  < = > 액티비티와 통신해야한다 */

    public MyService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(MYSERVICE_TAG, "onCreate()");

        /**
         * 알람(서비스 종료시 재시작하기 위해 만든 메소드)이 시작되어있다면
         * 종료
         */
        unregisterRestartAlarm();

        // 서버 -> 서비스 받는 스레드 객체 생성
        receive_thread = new Receive_thread();

        serviceHandler = new Handler(new Handler.Callback() {  // 액티비티 -> 서비스 핸들러 ( 채팅내용 )
            @Override
            public boolean handleMessage(Message msg) {

                switch (msg.what) {

                    case Room.SEND_MSG:   // 시청자액티비티에서 채팅메세지 요청
                        String activity_msg = msg.obj.toString();
                        Log.i(MYSERVICE_TAG, "입력된 메세지 : " + activity_msg);

                        send_thread = new Send_thread(activity_msg);  // 채팅메세지 서비스 -> 서버로 전송
                        send_thread.start();

                        break;

                    case MainActivity.SEND_GETROOMLIST: // 방 목록 요청
                        String getRoomList = msg.obj.toString();
                        Log.i(MYSERVICE_TAG, "방 목록 요청 전달받음:" + getRoomList);

                        send_thread = new Send_thread(getRoomList);
                        send_thread.start();
                        break;
                }
                return false;
            }
        });
    }

    /**
     * 액티비티에서 전송한 인텐트를 받아서 처리하는 콜백 메소드
     * startService() 실행하면 거치는 곳
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(MYSERVICE_TAG, "onStartCommand()");

        serviceIntent = intent;

        /** 서비스가 비정상적으로 종료되었을 때 시스템이 자동으로 재시작하도록 해당 상수 리턴*/
        if (intent == null) {
            Log.e(MYSERVICE_TAG, "intent = null");
            return Service.START_NOT_STICKY;
        } else {
            Log.d(MYSERVICE_TAG, "onStartCommand() intent != null ");

            //서버와 연동하는 스레드 실행
            main_Connect_Thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        socketChannel = SocketChannel.open();
                        socketChannel.configureBlocking(true);
                        socketChannel.connect(new InetSocketAddress(HOST, PORT));
                        Log.e(MYSERVICE_TAG, "서버 연결 성공");

                        receive_thread.start();
                        //Log.e(MYSERVICE_TAG, "receive_thread 시작");
                    } catch (Exception e) {
                        Log.e(MYSERVICE_TAG, "서버 연결 실패 : " + e);
                        e.printStackTrace();
                    }
                }
            });
            main_Connect_Thread.start();
        }

        return super.onStartCommand(intent, flags, startId);
        //START_NOT_STICKY
        //START_STICKY
        //START_REDELIVER_INTENT
    }

    public class Receive_thread extends Thread {
        String serverMsg = null;

        public Receive_thread() {
        }

        @Override
        public void run() {
            super.run();
            while (true) {
                try {
                    ByteBuffer byteBuffer = ByteBuffer.allocate(2048);    // 할당량이 작아 서버에서 보낸 값을 다 못 받아온다
                    int readByteCount = socketChannel.read(byteBuffer);
                    //Log.d(MYSERVICE_TAG, "Receive_thread : readByteBuffer" + readByteCount);

                    //서버가 정상적으로 소켓의 close를 호출했을 경우
                    if (readByteCount == -1) {
                        throw new IOException();
                    }
                    byteBuffer.flip(); // 바이트버퍼 문자열로 변환
                    Charset charset = Charset.forName("UTF-8");// 한글로도 변환가능
                    serverMsg = charset.decode(byteBuffer).toString();

                    Log.i(MYSERVICE_TAG, "서버에서 받은 메세지 : " + serverMsg);

                    if (serverMsg != null) {

                        try {
                            JSONObject jsonObject = new JSONObject(serverMsg);
                            String returnType = jsonObject.getString("type");

                            //일반 채팅
                            if (returnType.equals("chat")) {
                                String id = jsonObject.getString("id");
                                String usermsg = jsonObject.getString("msg");
                                /**
                                 * 노티 생성
                                 */
                                sendPushNotification(usermsg);

                                try {
                                    if (Room.showUpdate.obtainMessage() != null) {
                                        Message msg = Room.showUpdate.obtainMessage();
                                        msg.what = MSG_SEND_TO_ACTIVITY;
                                        msg.obj = serverMsg;

                                        Log.d("bkbk5515", "메세지 수신 후 Activity로 전송할 Message : " + msg);

                                        Room.showUpdate.sendMessage(msg);
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();// 시청자 액티비티의 핸들러가 null point 뜰때 예외처리해주고 여기에서 방송자액티비티의 핸들러로 메세지를 보내준다
                                    Log.e("null에러", String.valueOf(e));
                                }

                                // 채팅 방 목록
                            }else if(returnType.equals("chatroomlist")){
                                /**
                                 * 방목록
                                 */
                                try {
                                    if (MainActivity.showUpdate.obtainMessage() != null) {
                                        Log.d(MYSERVICE_TAG, "if (MainActivity.showUpdate.obtainMessage() != null)");
                                        Message msg = MainActivity.showUpdate.obtainMessage();
                                        msg.what = CHATLIST_SEND_TO_ACTIVITY;
                                        msg.obj = serverMsg;

                                        Log.d("bkbk5515", "메세지 수신 후 Activity로 전송할 Message : " + msg);

                                        MainActivity.showUpdate.sendMessage(msg);
                                    }
//                                    JSONArray jsonarray = new JSONObject(serverMsg).getJSONArray("response");
//
//                                    String id, roomname;
//                                    for (int i = 0; i < jsonarray.length(); i++) {
//                                        JSONObject jObject = jsonarray.getJSONObject(i);
//
//                                        id = jObject.optString("id");
//                                        roomname = jObject.optString("room");
//                                        Log.d("bkbk5515", id);
//                                        Log.d("bkbk5515", roomname);
//
//                                    }
                                }catch (Exception ex){
                                    Log.e(MYSERVICE_TAG+"chatroomlist", String.valueOf(ex));
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {
                        Log.e(MYSERVICE_TAG, "서버에서 받은 메세지 = null ");
                    }
                } catch (IOException e) {
                    Log.e(MYSERVICE_TAG, "receive_thread 정지 error메세지 : " + e);
                    try {
                        socketChannel.close();
                        Log.e(MYSERVICE_TAG, "소켓채널 끊음 에러메세지 : " + e);

                        //sendPushNotification("ToT");

                        //서버문제로 소켓이 끊키면 재시작
                        //ServerErrorReset();
//
//                        serviceIntent = null;
//                        Thread.currentThread().interrupt();//모든 스레드 죽임
//
//                        if (main_Connect_Thread != null) {
//                            Log.d(MYSERVICE_TAG, "main_Connect_Thread != null");
//                            main_Connect_Thread.interrupt();
//                            main_Connect_Thread = null;
//                        }
//                        if (receive_thread != null) {
//                            Log.d(MYSERVICE_TAG, "receive_thread != null");
//                            receive_thread.interrupt();
//                            receive_thread = null;
//                        }
//                        if (send_thread != null) {
//                            Log.d(MYSERVICE_TAG, "send_thread != null");
//                            send_thread.interrupt();
//                            send_thread = null;
//                        }
//                        registerRestartAlarm();

                        break;
                    } catch (IOException e1) {
                        Log.e(MYSERVICE_TAG, "소켓채널 끊음 catch문 : " + e1);
                    }
                }
            }
        }
    }

    public class Send_thread extends Thread {
        String sendmsg;

        public Send_thread(String sendmsg) {
            this.sendmsg = sendmsg;
        }

        @Override
        public void run() {
            super.run();
            try {
                socketChannel.socket().getOutputStream().write(sendmsg.getBytes("UTF-8"));
            } catch (IOException e) {
                Log.e(MYSERVICE_TAG, "Send_thread catch error메세지 : " + e);
                e.printStackTrace();
            }
        }
    }

    /**
     * 음악플레이어 처럼 액티비티에서 정지 누르면 서비스에서도 정지되듯이 액티비티와 서비스 간 연결되있는거 구현
     * 액티비티에서 데이터 전달 onBind(Intent intent)
     * 서비스에서 return 값 IBinder 이다
     * 클라이언트가 bindService()를 호출하면 호출되는 메소드
     * 바인더 객체를 클라이언트에게 반환하는 메소드
     * 바인더 객체를 클라이언트에게 반환하기 위해서는 서비스 안에 Binder 클래스를 정의해야한다
     * 서비스 바인딩은 연결된 액티비티가 사라지면 서비스도 소멸된다 ( 백그라운드에서 무한히 실행되진 않는다 )
     */
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        Log.i(MYSERVICE_TAG, "onBind()");
        // name=intent.getExtras().getString("name","do");
        //  Log.w(MYSERVICE_TAG,"이름 받아옴 : "+name);
        return mBinder;
        // return messenger.getBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(MYSERVICE_TAG, "onUnbind()");
        Toast.makeText(MyService.this, "언바인드됨", Toast.LENGTH_SHORT).show();
        return super.onUnbind(intent);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(MYSERVICE_TAG, "onDestroy()");

        serviceIntent = null;

        //알람 설정(서비스 재시작 위함)
        registerRestartAlarm();
        Thread.currentThread().interrupt();//모든 스레드 죽임

        if (main_Connect_Thread != null) {
            Log.d(MYSERVICE_TAG, "main_Connect_Thread != null");
            main_Connect_Thread.interrupt();
            main_Connect_Thread = null;
        }
        if (receive_thread != null) {
            Log.d(MYSERVICE_TAG, "receive_thread != null");
            receive_thread.interrupt();
            receive_thread = null;
        }
        if (send_thread != null) {
            Log.d(MYSERVICE_TAG, "send_thread != null");
            send_thread.interrupt();
            send_thread = null;
        }
        //Toast.makeText(getApplicationContext(), "Destroy", Toast.LENGTH_SHORT).show();
    }

    public void ServerErrorReset() {
        serviceIntent = null;

        //알람 설정(서비스 재시작 위함)
        registerRestartAlarm();
        Thread.currentThread().interrupt();//모든 스레드 죽임

        if (main_Connect_Thread != null) {
            Log.d(MYSERVICE_TAG, "main_Connect_Thread != null");
            main_Connect_Thread.interrupt();
            main_Connect_Thread = null;
        }
        if (receive_thread != null) {
            Log.d(MYSERVICE_TAG, "receive_thread != null");
            receive_thread.interrupt();
            receive_thread = null;
        }
        if (send_thread != null) {
            Log.d(MYSERVICE_TAG, "send_thread != null");
            send_thread.interrupt();
            send_thread = null;
        }
    }

    public void registerRestartAlarm() {
        Log.d(MYSERVICE_TAG, "알람 설정");
        Intent intent = new Intent(this, RestartService.class);
        PendingIntent sender = PendingIntent.getService(this, 0, intent, 0);
        long firstTime = SystemClock.elapsedRealtime();
        firstTime += 1 * 1000; // 10초 후에 알람이벤트 발생
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstTime, 1 * 1000, sender);
    }

    public void unregisterRestartAlarm() {
        Log.d(MYSERVICE_TAG, "알람 해제");
        Intent intent = new Intent(this, RestartService.class);
        PendingIntent sender = PendingIntent.getService(this, 0, intent, 0);
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        am.cancel(sender);
    }//출처: https://aroundck.tistory.com/123 [돼지왕 왕돼지 놀이터]


    public void sendPushNotification(String serverMsg) {
        //Log.d(MYSERVICE_TAG, "sendPushNotification 전달받음 : " + serverMsg);

        String channelId = "channel";
        String channelName = "Channel Name";

        NotificationManager notifManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(channelId, channelName, importance);
            notifManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channelId);
        Intent notificationIntent = new Intent(getApplicationContext(), MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        int requestID = (int) System.currentTimeMillis();
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), requestID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        builder
                .setTicker("NettyFinal 알림")
                .setContentTitle("NettyFinal") // 큰제목
                .setContentText(serverMsg)  // 내용
                .setDefaults(Notification.DEFAULT_ALL) // 알림, 사운드 진동 설정
                .setAutoCancel(true) // 알림 터치시 반응 후 삭제
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
//                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.sado_icon_1))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)//노티 터치시 반응
        ;

        notifManager.notify(0, builder.build());

        PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
        /**
         * 수정한 부분 있음.
         * 화면이 꺼져있을때 잠든 휴대폰을 깨우는 역할.
         */
        @SuppressLint("InvalidWakeLockTag") PowerManager.WakeLock wakelock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
        wakelock.acquire(5000);

        //notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    /**
     * 바인더 클래스 정의 클라이언트에게 반환해줄 바인더 객체를 위한 클래스 바인더 클래스를 상속해서 클래스를 정의
     * onBind()
     */
    public class MyBinder extends Binder {
        public MyService getService() {
            return MyService.this;
        }
    }
}
