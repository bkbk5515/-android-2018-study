package com.example.plan.navigationtest;

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
import android.widget.Toast;

public class FragmentMainActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    Fragment1_1 fragment1;
    Fragment1_2 fragment2;
    Fragment1_3 fragment3;
    Fragment1_4 fragment4;
    Fragment1_5 fragment5;
    Fragment1_6 fragment6;
    Fragment1_7 fragment7;
    Fragment1_8 fragment8;
    Fragment1_9 fragment9;
    Fragment1_10 fragment10;
    Fragment1_11 Fragment1_11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragmentmain_activity);

        fragment1 = new Fragment1_1();
        fragment2 = new Fragment1_2();
        fragment3 = new Fragment1_3();
        fragment4 = new Fragment1_4();
        fragment5 = new Fragment1_5();
        fragment6 = new Fragment1_6();
        fragment7 = new Fragment1_7();
        fragment8 = new Fragment1_8();
        fragment9 = new Fragment1_9();
        fragment10 = new Fragment1_10();
        Fragment1_11 = new Fragment1_11();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setDisplayHomeAsUpEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();

                int id = menuItem.getItemId();
                switch (id) {
                    case R.id.navigation_item_attachment:
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
                        Intent intent1 = new Intent(FragmentMainActivity.this, GoogleMapActivity.class);
                        startActivity(intent1);
                        overridePendingTransition(0, 0);
                        break;

                    case R.id.navigation_item_question:
                        Intent intent5 = new Intent(FragmentMainActivity.this, QuestionActivity.class);
                        startActivity(intent5);
                        overridePendingTransition(0, 0);
                        break;

                    case R.id.nav_sub_menu_item00:
                        Intent intent2 = new Intent(FragmentMainActivity.this, AllianceActivity.class);
                        startActivity(intent2);
                        overridePendingTransition(0, 0);
                        break;

                    case R.id.nav_sub_menu_item01:
                        Intent intent3 = new Intent(FragmentMainActivity.this, PrivacyInfoActivity.class);
                        startActivity(intent3);
                        overridePendingTransition(0, 0);
                        break;

                    case R.id.nav_sub_menu_item02:
                        Intent intent4 = new Intent(FragmentMainActivity.this, TermsServiceActivity.class);
                        startActivity(intent4);
                        overridePendingTransition(0, 0);
                        break;

                    case R.id.nav_sub_menu_item03:
                        break;

                    case R.id.nav_sub_menu_item04:
                        Intent intent6 = new Intent(FragmentMainActivity.this, CompanyInfoActivity.class);
                        startActivity(intent6);
                        overridePendingTransition(0, 0);
                        break;
                }

                return true;
            }
        });
        //-----------------------------------------------------------------------
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment1).commit();

    }


    public void onFragmentChange(int index) {
        if (index == 0) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment1).commit();
        } else if (index == 1) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment2).commit();
        } else if (index == 2) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment3).commit();
        } else if (index == 3) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment4).commit();
        } else if (index == 4) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment5).commit();
        } else if (index == 5) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment6).commit();
        } else if (index == 6) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment7).commit();
        } else if (index == 7) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment8).commit();
        } else if (index == 8) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment9).commit();
        } else if (index == 9) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment10).commit();
        } else if (index == 10) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, Fragment1_11).commit();
        }
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

    //이전 액티비티 돌아갈 때 애니메이션 제거
    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0,0);
        //액티비티 애니메이션 x
    }//출처: http://puzzleleaf.tistory.com/62 [퍼즐잎의 성장일지]
}