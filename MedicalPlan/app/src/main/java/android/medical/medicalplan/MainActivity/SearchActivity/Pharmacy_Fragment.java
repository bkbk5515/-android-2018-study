package android.medical.medicalplan.MainActivity.SearchActivity;

import android.content.Context;
import android.medical.medicalplan.R;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class Pharmacy_Fragment extends Fragment implements View.OnClickListener{

    SearchActivity activity;

//    EditText editText;
//    ImageView search_image_btn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.activity_search_pharmacy, container, false);

//        editText = (EditText)rootview.findViewById(R.id.edittext);
//        search_image_btn = (ImageView) rootview.findViewById(R.id.search_image_btn);
//        search_image_btn.setOnClickListener(this);

        return rootview;// 플레그먼트 화면으로 보여주게 된다.
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.search_image_btn://검색
//                String text = editText.getText().toString();
//                Toast.makeText(activity, text, Toast.LENGTH_SHORT).show();
//                editText.setText("");
//                break;
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
