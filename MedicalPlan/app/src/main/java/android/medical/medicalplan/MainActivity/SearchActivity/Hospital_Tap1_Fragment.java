package android.medical.medicalplan.MainActivity.SearchActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.medical.medicalplan.R;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Hospital_Tap1_Fragment extends Fragment implements View.OnClickListener{

    SearchActivity activity;

    Button tap2, tap3;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.activity_search_hospital_tap1, container, false);

        tap2 = (Button)rootview.findViewById(R.id.tap2);
        tap2.setOnClickListener(this);
        tap3 = (Button)rootview.findViewById(R.id.tap3);
        tap3.setOnClickListener(this);



        return rootview;// 플레그먼트 화면으로 보여주게 된다.
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tap2:
                activity.onFragmentChange(2);
                break;
            case R.id.tap3:
                activity.onFragmentChange(3);
                break;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (SearchActivity) getActivity();
    }
    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }
}
