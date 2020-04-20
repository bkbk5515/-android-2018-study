package android.medical.medicalplan.MainActivity;

import android.content.Intent;
import android.medical.medicalplan.IntroActivity;
import android.medical.medicalplan.Login.LoginActivity;
import android.medical.medicalplan.MainActivity.SearchActivity.SearchActivity;
import android.medical.medicalplan.R;
import android.medical.medicalplan.SignUp.SignUpActivity1;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.kakao.auth.AuthType;
import com.kakao.auth.Session;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //private LinearLayoutManager mLinearLayoutManager, mLinearLayoutManager1;

    /**
     * 리사이클러뷰 어뎁터가 2개가 필요하진 않을것같은데 추후에 생각해보고 신중하게 선택하자!
     */
    private RecyclerView recyclerview_1, recyclerview_2, recyclerview_3;
    private RecyclerViewAdapter01 recyclerview_adapter01;
    private ArrayList<RecyclerViewItem01> recyclerviewItem01;

    private RecyclerViewAdapter03 recyclerview_adapter03;
    private ArrayList<RecyclerViewItem03> recyclerviewItem03;

    Button search_btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerview_1 = (RecyclerView) findViewById(R.id.recyclerview_main_list1);
        recyclerview_2 = (RecyclerView) findViewById(R.id.recyclerview_main_list2);
        recyclerview_3 = (RecyclerView) findViewById(R.id.recyclerview_main_list3);

        //mLinearLayoutManager = new LinearLayoutManager(this); //세로로
        //mLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);//가로로 //출처 : https://bit.ly/2RWeZ17
        recyclerview_1.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerview_2.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerview_3.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        // MainActivity에서 RecyclerView의 데이터에 접근시 사용
        recyclerviewItem01 = new ArrayList<>();
        recyclerviewItem03 = new ArrayList<>();

        // RecyclerView를 위해 CustomAdapter를 사용합니다.
        recyclerview_adapter01 = new RecyclerViewAdapter01(recyclerviewItem01);
        recyclerview_1.setAdapter(recyclerview_adapter01);
        recyclerview_2.setAdapter(recyclerview_adapter01);

        recyclerview_adapter03 = new RecyclerViewAdapter03(recyclerviewItem03);
        recyclerview_3.setAdapter(recyclerview_adapter03);

        /**
         * 동작 확인하기위한 test 메소드드
         */
        recyclerview_set();

        search_btn = (Button)findViewById(R.id.search_btn);
        search_btn.setOnClickListener(this);

//        // RecyclerView의 줄(row) 사이에 수평선을 넣기위해 사용됨(구분선)
//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),mLinearLayoutManager.getOrientation());
//        mRecyclerView.addItemDecoration(dividerItemDecoration);

//        //기본 아이템은 공백이 없음. 마지막이 아닌 경우 아이템 간격 30을 준다.
//        MyListDecoration decoration = new MyListDecoration();
//        mRecyclerView.addItemDecoration(decoration);



    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.search_btn://병의원 검색
                Toast.makeText(getApplicationContext(), "search_btn", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, SearchActivity.class);
                startActivity(intent);
                break;

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void recyclerview_set(){
        //테스트용 for문
        for (int i=0; i<10; i++){
            // RecyclerViewItem 생성자를 사용하여 ArrayList에 삽입할 데이터를 만들기
            RecyclerViewItem01 dict = new RecyclerViewItem01("병원이름" + i,"주소" + i, "진료시간" + i);

            //mArrayList.add(0, dict); //RecyclerView의 첫 줄에 삽입
            recyclerviewItem01.add(dict); // RecyclerView의 마지막 줄에 삽입

            RecyclerViewItem03 dict3 = new RecyclerViewItem03("병원이름" + i,"주소" + i, "진료시간" + i);
            //mArrayList.add(0, dict); //RecyclerView의 첫 줄에 삽입
            recyclerviewItem03.add(dict3); // RecyclerView의 마지막 줄에 삽입
        }
        recyclerview_adapter01.notifyDataSetChanged(); //변경된 데이터를 화면에 반영
        recyclerview_adapter03.notifyDataSetChanged(); //변경된 데이터를 화면에 반영
    }
}
