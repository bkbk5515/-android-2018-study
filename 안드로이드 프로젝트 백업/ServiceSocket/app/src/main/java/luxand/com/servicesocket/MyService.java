package luxand.com.servicesocket;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Toast;

import java.util.Random;

public class MyService extends Service {

    ServiceThread thread;
    myServiceHandler handler;
    static String sndname;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    public void onCreate() {
        super.onCreate();

        Toast.makeText(getApplicationContext(), "Service is Created", Toast.LENGTH_SHORT).show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handler = new myServiceHandler();
        thread = new ServiceThread(handler);
        thread.start();
        return START_STICKY;
    }

    //서비스가 종료될 때 할 작업
    public void onDestroy() {
        thread.stopForever();
        thread = null;//쓰레기 값을 만들어서 빠르게 회수하라고 null을 넣어줌.
    }

    class myServiceHandler extends Handler {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void handleMessage(android.os.Message msg) {
//            Intent intent = new Intent(MyService.this, MainActivity.class);
//            PendingIntent pendingIntent = PendingIntent.getActivity(MyService.this, 0, intent,PendingIntent.FLAG_UPDATE_CURRENT);
            Toast.makeText(getApplicationContext(), "짠", Toast.LENGTH_SHORT).show();
            String hdr_msg;
            hdr_msg = msg.getData().getString("data");
            Log.d("핸들러에서 받은 msg :", hdr_msg);

            sndname = hdr_msg;
            Log.d("핸들러에서 재정의한 sendname :", sndname);

        }
    }
//    public String getname() {
//        return sndname;
//    }



//
//    public class ServiceThread extends Thread{
//        Handler handler;
//        boolean isRun = true;
//
//        public ServiceThread(Handler handler){
//            this.handler = handler;
//        }
//        public void stopForever(){
//            synchronized (this) {
//                this.isRun = false;
//            }
//        }
//        public void run(){
//            //반복적으로 수행할 작업을 한다.
//            while(isRun){
//                //핸들러 msg에 String 보내기
//                //json보내기 https://stackoverflow.com/questions/20328228/how-to-send-two-string-array-data-through-handler-android
//                Bundle data = new Bundle();
//                data.putString("data", "비안코"); //출처: http://codedb.tistory.com/entry/Android-Message에-Stirng-데이터-담아-핸들러에-sendMessage-하기 [CodeDB]
//
//                Message message = new Message();
//                message.setData(data);
//                Log.d("스레드에서 보내는 msg : ", String.valueOf(message));
//
//                //예제원본
//                //handler.sendEmptyMessage(0);//원래는
//                // handler.sendEmptyMessage(message);//쓰레드에 있는 핸들러에게 메세지를 보냄
//                handler.sendMessage(message);//쓰레드에 있는 핸들러에게 메세지를 보냄
//                try{
//                    Thread.sleep(10000); //10초씩 쉰다.
//                }catch (Exception e) {}
//            }
//        }
//    }


}
//출처: http://twinw.tistory.com/50
//출처: http://newland435.tistory.com/7
//짬뽕