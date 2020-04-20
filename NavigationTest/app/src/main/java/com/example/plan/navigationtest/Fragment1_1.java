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


public class Fragment1_1 extends Fragment {
    // fragment_main.xml 파일과 인플레이션으로 연결해주는것을 메모리 객체화를 시켜주어야한다
    FragmentMainActivity activity;
    Button nextbtn, btn1, btn2, btn3, btn4, btn5, btn6;
    int nullcheck = 0;

    private SharedPreferences savedata;
    private SharedPreferences.Editor saveeditor;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.fragment1_1, container, false);

        nextbtn = (Button) rootview.findViewById(R.id.nextbtn);
        nextbtn.setOnClickListener(new View.OnClickListener() {
            // 요청을 보내야 하는데 메인 액티비티에 다가 메소드를 하나 만들어야 한다.
            @Override
            public void onClick(View v) {
                if (nullcheck == 0) {
                    Toast.makeText(activity, "입력되지 않은 항목이 있습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    savedata = activity.getSharedPreferences("MyData", MODE_PRIVATE);
                    saveeditor = savedata.edit();
                    String replaceString;
                    replaceString = btn1.getText().toString().replace("\n", "");
                    saveeditor.putString("1", replaceString);
                    saveeditor.commit();

                    activity.onFragmentChange(1);
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
//        btn2 = (Button) rootview.findViewById(R.id.btn2);
//        btn2.setOnClickListener(new View.OnClickListener() {
//            @SuppressLint("ResourceAsColor")
//            @Override
//            public void onClick(View view) {
//                btn2.setBackgroundColor(R.color.white);//#FF1493
//                btn1.setBackgroundResource(android.R.drawable.btn_default);
//            }
//        });
        //getActivity();      // 액티비티위에 올라가게 한다.
        return rootview;            // 플레그먼트 화면으로 보여주게 된다.
        //--------------------------------------------------------------------------

    }

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

}