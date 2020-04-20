package android.medical.recyclerview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import java.util.ArrayList;

import static android.support.v7.widget.DividerItemDecoration.HORIZONTAL;


public class MainActivity extends AppCompatActivity {

    private static String TAG = "recyclerview_example";

    private ArrayList<Dictionary> mArrayList;
    private CustomAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private int count = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_main_list);

        /**
         * 출처 : https://bit.ly/2RWeZ17
         * 가로
         */
//        mLinearLayoutManager = new LinearLayoutManager(this); //세로로
        mLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);//가로로

        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        // MainActivity에서 RecyclerView의 데이터에 접근시 사용됩니다.
        mArrayList = new ArrayList<>();

        // RecyclerView를 위해 CustomAdapter를 사용합니다.
        mAdapter = new CustomAdapter( mArrayList);
        mRecyclerView.setAdapter(mAdapter);

//        // RecyclerView의 줄(row) 사이에 수평선을 넣기위해 사용됨(구분선)
//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),mLinearLayoutManager.getOrientation());
//        mRecyclerView.addItemDecoration(dividerItemDecoration);

        //기본 아이템은 공백이 없음. 마지막이 아닌 경우 아이템 간격 30을 준다.
        MyListDecoration decoration = new MyListDecoration();
        mRecyclerView.addItemDecoration(decoration);

        Button buttonInsert = (Button)findViewById(R.id.button_main_insert);
        buttonInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;

                // Dictionary 생성자를 사용하여 ArrayList에 삽입할 데이터를 만듭니다.
                Dictionary dict = new Dictionary("병원이름" + count,"주소" + count, "진료시간" + count);

                //mArrayList.add(0, dict); //RecyclerView의 첫 줄에 삽입
                mArrayList.add(dict); // RecyclerView의 마지막 줄에 삽입

                mAdapter.notifyDataSetChanged(); //변경된 데이터를 화면에 반영
            }
        });
    }
}