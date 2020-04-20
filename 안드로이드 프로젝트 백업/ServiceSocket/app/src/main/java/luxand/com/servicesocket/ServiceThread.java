package luxand.com.servicesocket;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class ServiceThread extends Thread{
    Handler handler;
    boolean isRun = true;

    public ServiceThread(Handler handler){
        this.handler = handler;
    }

    public void stopForever(){
        synchronized (this) {
            this.isRun = false;
        }
    }

    public void run(){
        //반복적으로 수행할 작업을 한다.
        while(isRun){
            //핸들러 msg에 String 보내기
            //json보내기 https://stackoverflow.com/questions/20328228/how-to-send-two-string-array-data-through-handler-android
            Bundle data = new Bundle();
            data.putString("data", "비안코"); //출처: http://codedb.tistory.com/entry/Android-Message에-Stirng-데이터-담아-핸들러에-sendMessage-하기 [CodeDB]

            Message message = new Message();
            message.setData(data);
            Log.d("스레드에서 보내는 msg : ", String.valueOf(message));

            //예제원본
            //handler.sendEmptyMessage(0);//원래는
            // handler.sendEmptyMessage(message);//쓰레드에 있는 핸들러에게 메세지를 보냄
            handler.sendMessage(message);//쓰레드에 있는 핸들러에게 메세지를 보냄
            try{
                Thread.sleep(10000); //10초씩 쉰다.
            }catch (Exception e) {}
        }
    }
}//출처: http://twinw.tistory.com/50 [흰고래의꿈]//출처: http://twinw.tistory.com/50 [흰고래의꿈]
