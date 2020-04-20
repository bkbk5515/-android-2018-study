package com.example.plan.navigationtest;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class QuestionActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;

    ListView listview = null;
    QuestionActivityListviewAdapter adapter;
    QuestionActivityListviewItem listViewItem;
    String titletext, answertext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setDisplayHomeAsUpEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        /**
         *
         */
        adapter = new QuestionActivityListviewAdapter();
        listview = (ListView) findViewById(R.id.listview);
        listview.setAdapter(adapter);
        listViewItem = new QuestionActivityListviewItem();

        addlistview();

        //리스트뷰 클릭 이벤트 처리
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                // get item
                QuestionActivityListviewItem item = (QuestionActivityListviewItem) parent.getItemAtPosition(position);

                String titledialog = item.getTitle();
                String answerdialog = item.getAnswer();
                show(titledialog, answerdialog);
            }
        });
        /**
         *
         */

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();

                int id = menuItem.getItemId();
                switch (id) {
                    case R.id.navigation_item_attachment:
                        Intent intent = new Intent(QuestionActivity.this, FragmentMainActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        break;

                    case R.id.navigation_item_images:
                        break;

                    case R.id.navigation_item_kakao:
                        break;

                    case R.id.navigation_item_call:
                        String tel = "tel:"+ "01075535515";
                        startActivity(new Intent("android.intent.action.DIAL", Uri.parse(tel)));
                        break;

                    case R.id.navigation_item_map:
                        Intent intent1 = new Intent(QuestionActivity.this, GoogleMapActivity.class);
                        startActivity(intent1);
                        overridePendingTransition(0, 0);
                        break;

                    case R.id.navigation_item_question:
                        break;

                    case R.id.nav_sub_menu_item00:
                        Intent intent2 = new Intent(QuestionActivity.this, AllianceActivity.class);
                        startActivity(intent2);
                        overridePendingTransition(0, 0);
                        break;

                    case R.id.nav_sub_menu_item01:
                        Intent intent3 = new Intent(QuestionActivity.this, PrivacyInfoActivity.class);
                        startActivity(intent3);
                        overridePendingTransition(0, 0);
                        break;

                    case R.id.nav_sub_menu_item02:
                        Intent intent4 = new Intent(QuestionActivity.this, TermsServiceActivity.class);
                        startActivity(intent4);
                        overridePendingTransition(0, 0);
                        break;

                    case R.id.nav_sub_menu_item03:
                        break;

                    case R.id.nav_sub_menu_item04:
                        Intent intent6 = new Intent(QuestionActivity.this, CompanyInfoActivity.class);
                        startActivity(intent6);
                        overridePendingTransition(0, 0);
                        break;
                }
                return true;
            }
        });
    }

    public void addlistview(){
        JSONObject listitem1 = new JSONObject();
        try {
            listitem1.put("title", "회원가입은 필요 없나요?");
            listitem1.put("answer", "네. 회원가입 없이 무료로 세무 견적을 받으실 수 있습니다.");
            JSONObject listitem2 = new JSONObject();
            listitem2.put("title", "별도의 수수료가 발생하나요?");
            listitem2.put("answer", "아니요. 무료입니다.");

            JSONArray jsonArraySet = new JSONArray();

            jsonArraySet.put(listitem1);
            jsonArraySet.put(listitem2);

            JSONObject listitemobj = new JSONObject();
            listitemobj.put("listitem", jsonArraySet);

            Log.d("listitemjson", listitemobj.toString());
            String jsonString = listitemobj.toString();

            JSONObject jsonResponse = new JSONObject(jsonString);
            JSONArray jsonarray = jsonResponse.getJSONArray("listitem");

            Log.d("bkbk5515", jsonarray.toString());

            for(int i = 0; i < jsonarray.length(); i++){
                JSONObject jsonobj = jsonarray.getJSONObject(i);

                titletext = jsonobj.getString("title");
                answertext = jsonobj.getString("answer");

                Log.d("bkbk5515", titletext);
                Log.d("bkbk5515", answertext);

                listViewItem = new QuestionActivityListviewItem(titletext, answertext);

                adapter.listViewItemList.add(listViewItem);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("에러", String.valueOf(e));
        }
        adapter.notifyDataSetChanged();
    }
    void show(String title, String answer)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(answer);
        builder.setPositiveButton("확인",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
//        builder.setNegativeButton("아니오",
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        Toast.makeText(getApplicationContext(),"아니오를 선택했습니다.",Toast.LENGTH_LONG).show();
//                    }
//                });
        builder.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
//        Toast.makeText(MainActivity.this, "새로운 알림 클릭.", Toast.LENGTH_SHORT).show();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {

            case android.R.id.home:
                //왼쪽 네비게이션 바 클릭
//                Toast.makeText(MainActivity.this, "메뉴 클릭", Toast.LENGTH_SHORT).show();
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;

            case R.id.action_settings:
//                Toast.makeText(MainActivity.this, "새로운 알림 클릭", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}