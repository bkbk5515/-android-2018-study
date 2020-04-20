package my.com.kakaoblog;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    JSONObject jsonObject;
    JSONArray jsonarray;
    JSONObject object;

    private String url = null;
    private String thumbnail = null;
    private String title = null;
    private String contents = null;
    String[] arraysum = new String[10];

    String s_name;
    ListView listview = null;
    KakaoCafeListViewAdapter adapter;
    KakaoCafeListViewItem listViewItem;

    Button btn;
    EditText text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adapter = new KakaoCafeListViewAdapter(getApplicationContext());
        listview = (ListView) findViewById(R.id.listview_cafe);
        listview.setAdapter(adapter);

        text = (EditText)findViewById(R.id.edittext);

        btn = (Button)findViewById(R.id.sbtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listview = null;
                adapter = new KakaoCafeListViewAdapter(getApplicationContext());
                listview = (ListView) findViewById(R.id.listview_cafe);
                listview.setAdapter(adapter);

                s_name = text.getText().toString();
                text.setText("");
//                s_name = "오사카 맛집";
                kakao_cafe_get(s_name);
            }
        });

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                // get item
                KakaoCafeListViewItem item = (KakaoCafeListViewItem) parent.getItemAtPosition(position);

                String s_url = item.getUrl();

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(s_url));
                startActivity(intent);
            }
        });
//        new Handler().postDelayed(new Runnable() {// 1 초 후에 실행
//            @Override
//            public void run() {
//                s_name = "오사카 맛집";
//                kakao_cafe_get(s_name);
//                Log.d("ㅇㅇ", "ㅇㅇ");
//            }
//        }, 1000);
    }

    public void kakao_cafe_get(String n) {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    jsonObject = new JSONObject(response);
                    Log.d("kakaoo", response);
                    //documents
                    JSONArray jarray = new JSONObject(response).getJSONArray("documents");
                    for (int i = 0; i < jarray.length(); i++) {
                        HashMap map = new HashMap<>();
                        JSONObject jObject = jarray.getJSONObject(i);

                        url = jObject.optString("url");
                        thumbnail = jObject.optString("thumbnail");
                        title = jObject.optString("title");
                        contents = jObject.optString("contents");

                        String s1, s11;
                        s1 = title.replace("<b>", "");
                        s11 = s1.replace("</b>", " ");
                        String s2, s22;
                        s2 = contents.replace("<b>", "");
                        s22 = s2.replace("</b>", " ");

                        Log.d("url", url);
                        Log.d("thumbnail", thumbnail);
                        Log.d("title", title);
                        Log.d("contents", contents);

//                        listViewItem.setUrl(url);
//                        listViewItem.setThumbnail(thumbnail);
//                        listViewItem.setTitle(title);
//                        listViewItem.setContents(contents);

                        if (thumbnail.equals("")) {
                            String nullString;
                            nullString = "https://icon2.kisspng.com/20180623/gil/kisspng-lg-g4-android-marshmallow-android-nougat-android-o-this-nox-5b2ee3d31564b2.7066279915297996350876.jpg";
                            listViewItem = new KakaoCafeListViewItem(url, nullString, s11, s22);
                        } else {
                            listViewItem = new KakaoCafeListViewItem(url, thumbnail, s11, s22);
                        }


                        adapter.listViewItemList.add(listViewItem);

                        Log.d("리스트뷰 아이템", "입력");

//                        arraysum[0] = code;
//                        arraysum[1] = seq_code;
//                        arraysum[2] = food_name;
//                        arraysum[3] = thumb_img;
//                        arraysum[4] = sell_com;
//                        arraysum[5] = barcode;
//                        arraysum[6] = volume;
//                        arraysum[7] = food_type;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("kakaoo 에러", String.valueOf(e));
                }
                adapter.notifyDataSetChanged();
            }
        };
        kakaocafeget kakao_cafeget = new kakaocafeget(n, responseListener);
        RequestQueue rq = Volley.newRequestQueue(MainActivity.this);
        rq.add(kakao_cafeget);
    }
}
