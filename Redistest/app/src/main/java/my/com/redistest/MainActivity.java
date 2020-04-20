package my.com.redistest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;


public class MainActivity extends AppCompatActivity {

    Button sexbtn;
    Button agebtn;
    Button agegetbtn;
    Button sexgetbtn;
    Button livestartbtn, vodclickbtn, vodlankgetbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sexbtn = (Button) findViewById(R.id.sexbtn);
        agebtn = (Button) findViewById(R.id.agebtn);
        agegetbtn = (Button) findViewById(R.id.ageget);
        sexgetbtn = (Button) findViewById(R.id.sexget);
        livestartbtn = (Button) findViewById(R.id.livestartbtn);
        vodclickbtn = (Button) findViewById(R.id.vodclickbtn);
        vodlankgetbtn = (Button) findViewById(R.id.lankgetbtn);

        sexbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = "bkbk5515";
                String sex = "0";
                String age = "1992";

                redissexset(sex, id);
            }
        });
        agebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = "bkbk5515";
                String sex = "0";
                String age = "1992";

                redisageset(age, id);
            }
        });
        agegetbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redisageget();
            }
        });
        sexgetbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redissexget();
            }
        });
        livestartbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String filename = "2018_08_02_bkbk5515";
                livestart(filename);
            }
        });
        vodclickbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String filename = "2018_08_02_bkbk5515";
                vodclick(filename);
            }
        });
        vodlankgetbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lankget();
            }
        });
    }

    public void redissexset(String sex, String id){
        Response.Listener<String> responseListener = new Response.Listener<String>(){
            @Override
            public void onResponse(String response){
                try{
                    JSONObject jsonResponse = new JSONObject(response);
                    Log.d("받은 age json", String.valueOf(jsonResponse));
//                    boolean success = jsonResponse.getBoolean("success");
//                    if(success){
//                        Log.d("성공", String.valueOf(jsonResponse));
//                        Toast.makeText(getApplicationContext(), "성공", Toast.LENGTH_SHORT).show();
//                    }
//                    else{
//                        Log.d("실패", "저장 실패");
//                        Toast.makeText(getApplicationContext(), "실패", Toast.LENGTH_SHORT).show();
//                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                    Log.d("$response 에러", String.valueOf(e));
                    Toast.makeText(getApplicationContext(), "$response 에러", Toast.LENGTH_SHORT).show();
                }
            }
        };
        RedisSet sexSet = new RedisSet(sex, id, responseListener);
        RequestQueue rq = Volley.newRequestQueue(MainActivity.this);
        rq.add(sexSet);
    }

    public void redisageset(String age, String id){
        Response.Listener<String> responseListener = new Response.Listener<String>(){
            @Override
            public void onResponse(String response){
                try{
                    JSONObject jsonResponse = new JSONObject(response);
                    Log.d("받은 Json", String.valueOf(jsonResponse));
                }
                catch (Exception e){
                    e.printStackTrace();
                    Log.d("$response 에러", String.valueOf(e));
                    Toast.makeText(getApplicationContext(), "$response 에러", Toast.LENGTH_SHORT).show();
                }
            }
        };
        RedisAgeAdd ageSet = new RedisAgeAdd(age, id, responseListener);
        RequestQueue rq = Volley.newRequestQueue(MainActivity.this);
        rq.add(ageSet);
    }


    public void redisageget(){
        Response.Listener<String> responseListener = new Response.Listener<String>(){
            @Override
            public void onResponse(String response){
                try{
                    JSONObject jsonResponse = new JSONObject(response);
                    Log.d("받은 Json", String.valueOf(jsonResponse));


                }
                catch (Exception e){
                    e.printStackTrace();
                    Log.d("$response 에러", String.valueOf(e));
                    Toast.makeText(getApplicationContext(), "$response 에러", Toast.LENGTH_SHORT).show();
                }
            }
        };
        RedisAgeGet ageget = new RedisAgeGet(responseListener);
        RequestQueue rq = Volley.newRequestQueue(MainActivity.this);
        rq.add(ageget);
    }
    public void redissexget(){
        Response.Listener<String> responseListener = new Response.Listener<String>(){
            @Override
            public void onResponse(String response){
                try{
                    JSONObject jsonResponse = new JSONObject(response);
                    Log.d("받은 Json", String.valueOf(jsonResponse));
                    String sexjson;

                    sexjson = String.valueOf(jsonResponse);
                    word1 = ":0";
                    word2 = ":1";
                    //https://stackoverflow.com/questions/2808733/why-stringutils-apache-class-is-not-recognized-in-android
                    //int count1 = StringUtils.countOccurrences(sexjson, word1);
                    int count1 = getCharNumber(sexjson,word1);
                    int count2 = StringUtils.countOccurrences(sexjson, word2);


                }
                catch (Exception e){
                    e.printStackTrace();
                    Log.d("$response 에러", String.valueOf(e));
                    Toast.makeText(getApplicationContext(), "$response 에러", Toast.LENGTH_SHORT).show();
                }
            }
        };
        RedisSexGet sexGet = new RedisSexGet(responseListener);
        RequestQueue rq = Volley.newRequestQueue(MainActivity.this);
        rq.add(sexGet);
    }

    public void livestart(String filename){
        Response.Listener<String> responseListener = new Response.Listener<String>(){
            @Override
            public void onResponse(String response){
                try{
                    JSONObject jsonResponse = new JSONObject(response);
                    Log.d("받은 Json", String.valueOf(jsonResponse));
                }
                catch (Exception e){
                    e.printStackTrace();
                    Log.d("$response 에러", String.valueOf(e));
                    Toast.makeText(getApplicationContext(), "$response 에러", Toast.LENGTH_SHORT).show();
                }
            }
        };
        LiveStart liveStart = new LiveStart(filename, responseListener);
        RequestQueue rq = Volley.newRequestQueue(MainActivity.this);
        rq.add(liveStart);
    }
    public void vodclick(String filename){
        Response.Listener<String> responseListener = new Response.Listener<String>(){
            @Override
            public void onResponse(String response){
                try{
                    JSONObject jsonResponse = new JSONObject(response);
                    Log.d("받은 Json", String.valueOf(jsonResponse));
                }
                catch (Exception e){
                    e.printStackTrace();
                    Log.d("$response 에러", String.valueOf(e));
                    Toast.makeText(getApplicationContext(), "$response 에러", Toast.LENGTH_SHORT).show();
                }
            }
        };
        VodClick vodClick = new VodClick(filename, responseListener);
        RequestQueue rq = Volley.newRequestQueue(MainActivity.this);
        rq.add(vodClick);
    }
    public void lankget(){
        Response.Listener<String> responseListener = new Response.Listener<String>(){
            @Override
            public void onResponse(String response){
                try{
                    JSONObject jsonResponse = new JSONObject(response);
                    Log.d("받은 Json", String.valueOf(jsonResponse));
                    String[] no1lank;
                    no1lank = String.valueOf(jsonResponse).split(":");
                    Log.d("split으로 변환한 json1", no1lank[0]);

                    String[] no1lank1;
                    no1lank1 = no1lank[0].split("\\{");
                    Log.d("split으로 변환한 json2",no1lank1[1]);

                    String finallank;
                    finallank = no1lank1[1].replace("\"", "");
                    Log.d("replace으로 변환한 json3",finallank);
                }
                catch (Exception e){
                    e.printStackTrace();
                    Log.d("$response 에러", String.valueOf(e));
                    Toast.makeText(getApplicationContext(), "$response 에러", Toast.LENGTH_SHORT).show();
                }
            }
        };
        VodLankGet vodLankGet = new VodLankGet(responseListener);
        RequestQueue rq = Volley.newRequestQueue(MainActivity.this);
        rq.add(vodLankGet);
    }
}
