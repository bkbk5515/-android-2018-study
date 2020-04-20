package my.com.redistest1;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.Chart;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;



public class MainActivity extends AppCompatActivity {

    Button sexbtn;
    Button agebtn;
    Button agegetbtn;
    Button sexgetbtn;
    Button livestartbtn, vodclickbtn, vodlankgetbtn;
    Button agecheakbtn;
    Button chartgo;
    int age, avg, agefinal;

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
        agecheakbtn = (Button) findViewById(R.id.agecheakbtn);
        chartgo = (Button) findViewById(R.id.chartbtn);

        //사용자 로그
        //로그인시 1번 서버로 전송
        //ok
        sexbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = "bkbk5515";
                String sex = "0";
                String age = "1992";

                redissexset(sex, id);
            }
        });
        //사용자 로그
        //로그인시 1번 서버로 전송
        //ok
        agebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = "bkbk5515";
                String sex = "0";
                String age = "1992";

                redisageset(age, id);
            }
        });
        //그래프 그릴때 사용할 나이 얻어오기
        //ok
        agegetbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redisageget();
            }
        });
        //그래프 그릴때 사용할 성별 얻어오기
        //ok
        sexgetbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redissexget();
            }
        });
        //방송 시작시 서버에 create
        //file이름을 전송
        //ok
        livestartbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String filename = "bkbk5515_2018_07_12_02_50_54";
                livestart(filename);
            }
        });
        //vod 클릭시 조회수 올리기(?) 확인할것
        //file이름을 전송
        //ok
        vodclickbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String filename = "bkbk5515_2018_07_12_02_50_54";
                vodclick(filename);
            }
        });
        //Hot 한 방송 상단 노출시 filename을 최종적으로 반환할 수 있도록 되어있음
        //메서드 확인
        //ok
        //실제 앱에서 클릭리스너 만들어줘야함.(금방)
        vodlankgetbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lankget();
            }
        });

        //이건 테스트용.
        agecheakbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "누적 덧셈 결과"+agefinal, Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), "최종 값 확인"+avg, Toast.LENGTH_SHORT).show();
            }
        });
        chartgo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ChartActivity.class);
                startActivity(intent);
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

                    String[] no1lank;
                    no1lank = String.valueOf(jsonResponse).split(":");
                    Log.d("json age id", no1lank[0]);

                    int i;
                    for(i=1; i<no1lank.length; i++){
                        String no1lank1;
                        String[] no1lank2;
                        no1lank1 = no1lank[i];
                        no1lank2 = String.valueOf(no1lank1).split(",");
                        //잠깐 주석
                        //Log.d("split으로 변환한 json", no1lank2[0]);
                        age = 0;
                        age = Integer.parseInt(no1lank2[0]);
                        agefinal += age;
                        Log.d("나이 덧셈", String.valueOf(agefinal));
                        avg = agefinal / i;
                    }


                    String no1lank4;
                    String[] no1lank5;
                    no1lank4 = no1lank[3];
                    no1lank5 = String.valueOf(no1lank4).split(",");
                    Log.d("split으로 변환한 json3", no1lank5[0]);

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

                    String word1, word2;

                    sexjson = String.valueOf(jsonResponse);
                    word1 = ":0";
                    word2 = ":1";
                    //https://stackoverflow.com/questions/2808733/why-stringutils-apache-class-is-not-recognized-in-android
                    int count1 = StringUtils.countMatches(sexjson, word1);
                    int count2 = StringUtils.countMatches(sexjson, word2);
                    Log.d("남자", String.valueOf(count1));
                    Log.d("여자", String.valueOf(count2));
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


                    //잠깐 주석
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
