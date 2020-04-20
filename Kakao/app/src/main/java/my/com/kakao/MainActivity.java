package my.com.kakao;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private WebView mWebView;
    String TAG = "테그";
    JSONObject jsonObject;
    String myUrl;
    String tid;
    String pg_token;
    Button btn;
    public static final String INTENT_PROTOCOL_START = "intent:";
    public static final String INTENT_PROTOCOL_INTENT = "#Intent;";
    public static final String INTENT_PROTOCOL_END = ";end;";
    public static final String GOOGLE_PLAY_STORE_PREFIX = "market://details?id=";

    String name;
    int price, price_vat, price_vat_add;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        name = "콩풍선";
        price = 50000;
        price_vat = (int) (price*0.1);
        price_vat_add = price+price_vat;
        kakao1(name, price_vat_add, price_vat);

//        btn = (Button)findViewById(R.id.btn);
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, Main2.class);
//                intent.putExtra("url", myUrl);
//                startActivity(intent);
//            }
//        });
        new Handler().postDelayed(new Runnable() {// 1 초 후에 실행
            @Override
            public void run() {
                // 웹뷰 셋팅팅
                mWebView = (WebView) findViewById(R.id.webview);
                mWebView.getSettings().setJavaScriptEnabled(true);
                //mWebView.loadUrl("http://www.pois.co.kr/mobile/login.do");

                mWebView.loadUrl(myUrl); // 접속 URL
                mWebView.setWebChromeClient(new WebChromeClient());
                //출처: http://yamea-guide.tistory.com/236 [기타치는 개발자의 야매 가이드]
                mWebView.setWebViewClient(new WebViewClient() {
                    // 링크 클릭에 대한 반응
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        if (url != null && url.startsWith("intent:")) {
                            Log.e("1번 intent://" , url);
                            try {
                                Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                                Intent existPackage = getPackageManager().getLaunchIntentForPackage(intent.getPackage());
                                if (existPackage != null) {
                                    view.getContext().startActivity(intent);
                                } else {
                                    Intent marketIntent = new Intent(Intent.ACTION_VIEW);
                                    marketIntent.setData(Uri.parse("market://details?id="+intent.getPackage()));
                                    view.getContext().startActivity(marketIntent);
                                }
                                return true;
                            }catch (Exception e) {
                                Log.e(TAG,e.getMessage());
                            }
                        }
                        else if(url != null && url.startsWith("http://13.125.73.140/")){
                            Log.e("2번 URL" , url);
                            String newurl = url + "&tid="+tid;
                            Log.e("newURL" , newurl);
                            try {
                                Intent intent = Intent.parseUri(newurl, Intent.URI_INTENT_SCHEME);
//                                Intent existPackage = getPackageManager().getLaunchIntentForPackage(intent.getPackage());
//                                if (existPackage != null) {
                                    view.getContext().startActivity(intent);
//                                } else {
//                                    Intent marketIntent = new Intent(Intent.ACTION_VIEW);
//                                    marketIntent.setData(Uri.parse("market://details?id="+intent.getPackage()));
//                                    view.getContext().startActivity(marketIntent);
//                                }
                                return true;
                            }catch (Exception e) {
                                Log.e(TAG,e.getMessage());
                            }
                        }
                        else if (url != null && url.startsWith("market://")) {
                            try {
                                Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                                if (intent != null) {
                                    view.getContext().startActivity(intent);
                                }
                                return true;
                            } catch (URISyntaxException e) {
                                Log.e(TAG,e.getMessage());
                            }
                        }else if(url != null){
                            try {
                                Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                                if (intent != null) {
                                    view.getContext().startActivity(intent);
                                }
                                return true;
                            } catch (URISyntaxException e) {
                                Log.e(TAG,e.getMessage());
                            }
                        }
                        //else if(url.contains())
                        view.loadUrl(url);
                        return false;
                    }

                    // 웹페이지 호출시 오류 발생에 대한 처리
                    @Override
                    public void onReceivedError(WebView view, int errorcode, String description, String fallingUrl) {
                        Log.e("오류발생",fallingUrl);
                    }
                    // 페이지 로딩 시작시 호출
                    @Override
                    public void onPageStarted(WebView view, String url, Bitmap favicon) {
                        //progress.setVisibility(View.VISIBLE);
                        Log.e("페이지로딩",url);
                    }
                    //페이지 로딩 종료시 호출
                    @Override
                    public void onPageFinished(WebView view, String Url) {
                        //progress.setVisibility(View.GONE);
                        Log.e("페이지로딩끝",Url);
                    }
                });
            }
        }, 1500);
    }

//    @Override
//    public void onBackPressed() {
//        if(mWebView.canGoBack()){
//            mWebView.goBack();
//        }else{
//            super.onBackPressed();
//        }
//    }


    public void kakao1(String n, int p1, int p2) {
        Response.Listener<String> responseListener = new Response.Listener<String>(){
            @Override
            public void onResponse(String response){
                try{
                    jsonObject = new JSONObject(response);
                    Log.d("kakaoo", response);
                    Log.d("jsonObject", (String) jsonObject.get("next_redirect_mobile_url"));
                    myUrl = (String) jsonObject.get("next_redirect_mobile_url");
                    tid = (String) jsonObject.get("tid");

                }
                catch (Exception e){
                    e.printStackTrace();
                    Log.d("kakaoo 에러", String.valueOf(e));
                }
            }
        };
        Kakaoget1 kakaoget1 = new Kakaoget1(n, p1, p2, responseListener);
        RequestQueue rq = Volley.newRequestQueue(MainActivity.this);
        rq.add(kakaoget1);
    }
}