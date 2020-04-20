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
import android.widget.EditText;
import android.widget.Toast;

import static android.content.Context.MODE_PRIVATE;

public class Fragment1_11 extends Fragment {
    // fragment_main.xml 파일과 인플레이션으로 연결해주는것을 메모리 객체화를 시켜주어야한다
    FragmentMainActivity activity;
    Button sendbtn, checkbtn, nextbtn, beforebtn;
    EditText nameeditText, phonenumbereditText, checkphonenumbereditText;

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
        ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.fragment_end, container, false);

//        nameeditText = (EditText) rootview.findViewById(R.id.nameText);

        nextbtn = (Button) rootview.findViewById(R.id.nextbtn);
        nextbtn.setOnClickListener(new View.OnClickListener() {
            // 요청을 보내야 하는데 메인 액티비티에 다가 메소드를 하나 만들어야 한다.
            @Override
            public void onClick(View v) {
                Toast.makeText(activity, "제출 완료", Toast.LENGTH_SHORT).show();
            }
        });

        beforebtn = (Button) rootview.findViewById(R.id.beforebtn);
        beforebtn.setOnClickListener(new View.OnClickListener() {
            // 요청을 보내야 하는데 메인 액티비티에 다가 메소드를 하나 만들어야 한다.
            @Override
            public void onClick(View v) {
                activity.onFragmentChange(9);
            }
        });
        //getActivity();      // 액티비티위에 올라가게 한다.
        return rootview;            // 플레그먼트 화면으로 보여주게 된다.
    }
}