package com.example.plan.navigationtest;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import static android.content.Context.MODE_PRIVATE;


public class Fragment1_10 extends Fragment {
    FragmentMainActivity activity;
    Button btn1, nextbtn, beforebtn;
    TextView cheaktextview1, cheaktextview2, cheaktextview3, cheaktextview4, cheaktextview5, cheaktextview6, cheaktextview7, cheaktextview8, cheaktextview9;
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
        ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.fragment1_10, container, false);

        cheaktextview1 = (TextView) rootview.findViewById(R.id.cheaktextview1);
        cheaktextview2 = (TextView) rootview.findViewById(R.id.cheaktextview2);
        cheaktextview3 = (TextView) rootview.findViewById(R.id.cheaktextview3);
        cheaktextview4 = (TextView) rootview.findViewById(R.id.cheaktextview4);
        cheaktextview5 = (TextView) rootview.findViewById(R.id.cheaktextview5);
        cheaktextview6 = (TextView) rootview.findViewById(R.id.cheaktextview6);
        cheaktextview7 = (TextView) rootview.findViewById(R.id.cheaktextview7);
        cheaktextview8 = (TextView) rootview.findViewById(R.id.cheaktextview8);
        cheaktextview9 = (TextView) rootview.findViewById(R.id.cheaktextview9);

        savedata = activity.getSharedPreferences("MyData", MODE_PRIVATE);
        String text1 = savedata.getString("1", "");
        String text2 = savedata.getString("2", "");
        String text3 = savedata.getString("3", "");
        String text4 = savedata.getString("4", "");
        String text5 = savedata.getString("5", "");
        String text6 = savedata.getString("6", "");
        String text7 = savedata.getString("7", "");
        String text8 = savedata.getString("8", "");
        String text9 = savedata.getString("9", "");

        cheaktextview1.setText(text1);
        cheaktextview2.setText(text2);
        cheaktextview3.setText(text3);
        cheaktextview4.setText(text4);
        cheaktextview5.setText(text5);
        cheaktextview6.setText(text6);
        cheaktextview7.setText(text7);
        cheaktextview8.setText(text8);
        cheaktextview9.setText(text9);


        nextbtn = (Button) rootview.findViewById(R.id.nextbtn);
        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savedata = activity.getSharedPreferences("MyData", MODE_PRIVATE);
                saveeditor = savedata.edit();
                saveeditor.clear();
                saveeditor.commit();
                activity.onFragmentChange(10);
            }
        });

        beforebtn = (Button) rootview.findViewById(R.id.beforebtn);
        beforebtn.setOnClickListener(new View.OnClickListener() {
            // 요청을 보내야 하는데 메인 액티비티에 다가 메소드를 하나 만들어야 한다.
            @Override
            public void onClick(View v) {
                activity.onFragmentChange(8);
            }
        });
        //getActivity();      // 액티비티위에 올라가게 한다.
        return rootview;            // 플레그먼트 화면으로 보여주게 된다.
    }
}