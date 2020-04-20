package com.todayplan.daum;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private WebView daum_webView;
    private Handler handler;
    private TextView daum_result;



    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        daum_result = (TextView) findViewById(R.id.daum_result);
        // WebView 초기화
        init_webView();
        // 핸들러를 통한 JavaScript 이벤트 반응
        handler = new Handler();
    }

    public void init_webView() {
        // WebView 설정
        daum_webView = (WebView) findViewById(R.id.daum_webview);
        // JavaScript 허용
        daum_webView.getSettings().setJavaScriptEnabled(true);
        // JavaScript의 window.open 허용
        daum_webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        // JavaScript이벤트에 대응할 함수를 정의 한 클래스를 붙여줌
        daum_webView.addJavascriptInterface(new AndroidBridge(), "TodayPlan");
        // web client 를 chrome 으로 설정
        daum_webView.setWebChromeClient(new WebChromeClient());
        // webview url load. php 파일 주소
//        daum_webView.loadUrl("http://13.125.73.140/daum/daum.php");
        daum_webView.loadUrl("http://crm.todayplan.xyz/test/to_address");
    }
    private class AndroidBridge {
        @JavascriptInterface
        public void setAddress(final String arg1, final String arg2, final String arg3) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Log.d("arg1", arg1);//우편번호
                    Log.d("arg2", arg2);//도로명주소 또는 지번주소
                    Log.d("arg3", arg3);//주택명 또는 건물명

                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("arg1", arg1);
                    resultIntent.putExtra("arg2", arg2);
                    resultIntent.putExtra("arg3", arg3);
                    setResult(RESULT_OK,resultIntent);
                    finish();//출처: http://liveonthekeyboard.tistory.com/150 [키위남]
                    //daum_result.setText(String.format("(%s) %s %s", arg1, arg2, arg3));
                    // WebView를 초기화 하지않으면 재사용할 수 없음
                    //init_webView();
                }
            });
        }
    }
}
