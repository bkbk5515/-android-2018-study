package com.example.plan.navigationtest;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import static android.content.Context.MODE_PRIVATE;


public class Fragment1_8 extends Fragment {
    // fragment_main.xml 파일과 인플레이션으로 연결해주는것을 메모리 객체화를 시켜주어야한다
    FragmentMainActivity activity;
    Button btn1, nextbtn, beforebtn;
    int nullcheck = 0;

    private SharedPreferences savedata;
    private SharedPreferences.Editor saveeditor;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        activity = (FragmentMainActivity) getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();

        activity = null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 인플레이션이 가능하다, container 이쪽으로 붙여달라, fragment_main을
        ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.fragment1_8, container, false);
        // rootview가 플래그먼트 화면으로 보이게 된다. 부분화면을 보여주고자하는 틀로 생각하면 된다.
        // fragment_main 파일과 MainFragment와 연결 해준다.
        // 인플레이션 과정을 통해서 받을 수 있다.

        nextbtn = (Button) rootview.findViewById(R.id.nextbtn);
        nextbtn.setOnClickListener(new View.OnClickListener() {
            // 요청을 보내야 하는데 메인 액티비티에 다가 메소드를 하나 만들어야 한다.
            @Override
            public void onClick(View v) {
                if(nullcheck == 0) {
                    Toast.makeText(activity, "입력되지 않은 항목이 있습니다.", Toast.LENGTH_SHORT).show();
                }else{

                    savedata = activity.getSharedPreferences("MyData", MODE_PRIVATE);
                    saveeditor = savedata.edit();
                    String replaceString;
                    replaceString = btn1.getText().toString().replace("\n", "");
                    saveeditor.putString("8", replaceString);
                    saveeditor.commit();

                    activity.onFragmentChange(8);
                }
            }
        });
        btn1 = (Button) rootview.findViewById(R.id.btn1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                nullcheck = 1;
                btn1.setBackgroundResource(R.drawable.buttonshapeclick);
            }
        });

        beforebtn = (Button) rootview.findViewById(R.id.beforebtn);
        beforebtn.setOnClickListener(new View.OnClickListener() {
            // 요청을 보내야 하는데 메인 액티비티에 다가 메소드를 하나 만들어야 한다.
            @Override
            public void onClick(View v) {
                activity.onFragmentChange(6);
            }
        });
        //getActivity();      // 액티비티위에 올라가게 한다.
        return rootview;            // 플레그먼트 화면으로 보여주게 된다.
    }
}