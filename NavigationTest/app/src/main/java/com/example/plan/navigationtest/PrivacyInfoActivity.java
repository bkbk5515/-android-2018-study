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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

//개인정보취급방침
public class PrivacyInfoActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.privacyinfo_activity);

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
                        Intent intent = new Intent(PrivacyInfoActivity.this, FragmentMainActivity.class);
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
                        Intent intent1 = new Intent(PrivacyInfoActivity.this, GoogleMapActivity.class);
                        startActivity(intent1);
                        overridePendingTransition(0, 0);
                        break;

                    case R.id.navigation_item_question:
                        Intent intent5 = new Intent(PrivacyInfoActivity.this, QuestionActivity.class);
                        startActivity(intent5);
                        overridePendingTransition(0, 0);
                        break;

                    case R.id.nav_sub_menu_item00:
                        Intent intent2 = new Intent(PrivacyInfoActivity.this, AllianceActivity.class);
                        startActivity(intent2);
                        overridePendingTransition(0, 0);
                        break;

                    case R.id.nav_sub_menu_item01:
                        break;

                    case R.id.nav_sub_menu_item02:
                        Intent intent4 = new Intent(PrivacyInfoActivity.this, TermsServiceActivity.class);
                        startActivity(intent4);
                        overridePendingTransition(0, 0);
                        break;

                    case R.id.nav_sub_menu_item03:
                        break;

                    case R.id.nav_sub_menu_item04:
                        Intent intent6 = new Intent(PrivacyInfoActivity.this, CompanyInfoActivity.class);
                        startActivity(intent6);
                        overridePendingTransition(0, 0);
                        break;
                }

                return true;
            }
        });

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
