package com.example.konote.weetalk;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.melnykov.fab.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    ListView listview = null;
    ListViewAdapter2 adapter2;
    ListViewItem2 listViewItem2;
    TextView findText;
    Button findaddBtn;
    private AlertDialog dialog;
    private boolean validate = false; // 사용할 수 있는 아이디인지 체크
    SharedPreferences userNameSave;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ChatActivity.noty = 0;

        userNameSave = getSharedPreferences("MyData", MODE_PRIVATE);
        String _mac = userNameSave.getString("userID", "");

        ////////////////////////////////////////////////////////////
        //유저에 맞는 친구추가
        Response.Listener<String> responseListener3 = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("받아온 친구목록", response);
                    JSONObject jsonResponse2 = new JSONObject(response);
                    JSONArray jsonarray2 = jsonResponse2.getJSONArray("response");

                    for(int i = 0; i < jsonarray2.length(); i++){
                        Log.d("FOR 들어옴", "--------------------");
                        JSONObject object2 = jsonarray2.getJSONObject(i);
                        String friend_user_name = object2.getString("username2");
                        Log.d("친구이름", friend_user_name);

                        ListViewItem2 listViewItem2 = new ListViewItem2();
                        listViewItem2.setUserID(friend_user_name);
                        adapter2.listViewItemList2.add(listViewItem2);
                        adapter2.notifyDataSetChanged();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        FriendUserAdd frienduseradd = new FriendUserAdd(_mac, responseListener3);
        RequestQueue rq_2 = Volley.newRequestQueue(MainActivity.this);
        rq_2.add(frienduseradd);
        ////////////////////////////////////////////////////////////

        // listview 생성 및 adapter 지정.
        adapter2 = new ListViewAdapter2();
        listview = (ListView) findViewById(R.id.listview1);
        listview.setAdapter(adapter2);
        listViewItem2 = new ListViewItem2();
        ////////////////////////////////////////////////////////////
        findText = (TextView) findViewById(R.id.findText);
        findaddBtn = (Button) findViewById(R.id.findaddBtn);

        ImageView roomListbtn = (ImageView) findViewById(R.id.chatlist_btn);
        roomListbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RoomList.class);
                MainActivity.this.startActivity(intent);
            }
        });
        ImageView map_btn = (ImageView) findViewById(R.id.map_btn);
        map_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MapActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });
        ImageView set_btn = (ImageView) findViewById(R.id.set_btn);
        set_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SetActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });

        findaddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String userMail = findText.getText().toString();
                if (validate) {
                    return;
                }
                if (userMail.equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    dialog = builder.setMessage("아이디를 입력해 주세요.")
                            .setPositiveButton("확인", null)
                            .create();
                    dialog.show();
                    return;
                }
                Response.Listener<String> RL = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if (success) {
                                findText.setText("");
                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                dialog = builder.setMessage("존재하지 않는 아이디입니다.")
                                        .setPositiveButton("확인", null)
                                        .create();
                                dialog.show();
                            } else {
                                findText.setText("");
                                ListViewItem2 listViewItem2 = new ListViewItem2();
                                listViewItem2.setUserID(userMail);
                                adapter2.listViewItemList2.add(listViewItem2);
                                // listview 갱신
                                adapter2.notifyDataSetChanged();

                                //친구추가 후 DB에 입력
                                Response.Listener<String> responseListener2 = new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONObject jsonResponse = new JSONObject(response);
                                            boolean success = jsonResponse.getBoolean("success");
                                            Toast.makeText(MainActivity.this, userMail+"를 친구추가함.", Toast.LENGTH_SHORT).show();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                };
                                userNameSave = getSharedPreferences("MyData", MODE_PRIVATE);
                                String _mac = userNameSave.getString("userID", "");

                                FriendAdd friendadd = new FriendAdd(_mac, userMail, responseListener2);
                                RequestQueue rq2 = Volley.newRequestQueue(MainActivity.this);
                                rq2.add(friendadd);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                UserCheck usercheck = new UserCheck(userMail, RL);
                RequestQueue rq = Volley.newRequestQueue(MainActivity.this);
                rq.add(usercheck);
            }
        });

    }/////////////////////////온크리에이트끝
}