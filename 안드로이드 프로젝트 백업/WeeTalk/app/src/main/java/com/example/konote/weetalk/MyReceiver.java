package com.example.konote.weetalk;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class MyReceiver extends BroadcastReceiver {

    String name, roomnumber, msg;
    int photo;

    int rnb;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
//        Resources res = context.getResources();

        this.name = intent.getExtras().getString("Json_name");
        this.roomnumber = intent.getExtras().getString("_roomnumber");
        this.msg = intent.getExtras().getString("Json_msg");
        this.photo = (int) intent.getExtras().get("Json_Photo_Chack");

        //인텐트로 넘길 방번호 인트로 형변환
        rnb = Integer.parseInt(roomnumber);

        Log.d("리시브 들어옴", "리시브 들어옴");
        Log.d("리시브이름", name);
        Log.d("리시브방번호", roomnumber);
        Log.d("리시브메세지", msg);
        Log.d("리시브사진유무", String.valueOf(photo));

        Intent notificationIntent = new Intent(context, ChatActivity.class);
        notificationIntent.putExtra("RoomNumber", rnb); //전달할 값 키벨류
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        if(photo == 0) {
            builder.setContentTitle("새로운 메세지 도착.")
                    .setContentText(name + " : " + msg)//메세지를 출력함
                    .setTicker(name + " : " + msg)// ex) 김병기 : 밥 먹었냐?
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(contentIntent)
                    .setAutoCancel(true)
                    .setWhen(System.currentTimeMillis())
                    .setDefaults(Notification.DEFAULT_ALL);
        }else{
            builder.setContentTitle("새로운 메세지 도착.")
                    .setContentText(name + " : 사진")//메세지를 출력함
                    .setTicker(name + " : " + msg)// ex) 김병기 : 밥 먹었냐?
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(contentIntent)
                    .setAutoCancel(true)
                    .setWhen(System.currentTimeMillis())
                    .setDefaults(Notification.DEFAULT_ALL);
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            builder.setCategory(Notification.CATEGORY_MESSAGE)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setVisibility(Notification.VISIBILITY_PUBLIC);
        }
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(0, builder.build());
    }
}
