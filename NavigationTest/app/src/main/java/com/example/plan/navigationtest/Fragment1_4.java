package com.example.plan.navigationtest;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import static android.content.Context.MODE_PRIVATE;

public class Fragment1_4 extends Fragment {
    // fragment_main.xml 파일과 인플레이션으로 연결해주는것을 메모리 객체화를 시켜주어야한다
    FragmentMainActivity activity;
    Button btn1, nextbtn, beforebtn;
    ListView listview1, listview2;
    String strText, strText2;
    int nullcheck = 0;
    ViewGroup rootview;
    ArrayAdapter Adapter, Adapter1;

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
        rootview = (ViewGroup) inflater.inflate(R.layout.fragment1_4, container, false);
        // rootview가 플래그먼트 화면으로 보이게 된다. 부분화면을 보여주고자하는 틀로 생각하면 된다.
        // fragment_main 파일과 MainFragment와 연결 해준다.
        // 인플레이션 과정을 통해서 받을 수 있다.

        final String[] listviewitemlist = {"서울시", "경기도", "인천시", "부산시", "대전시", "대구시", "울산시", "세종시", "광주시", "전라남도", "전라북도", "강원도", "충청북도", "충청남도", "제주도"};

        final String[] 서울시 = {"종로구", "중구", "용산구", "성동구", "광진구", "동대문구", "중랑구", "성북구", "강북구", "도봉구", "노원구", "은평구"
                , "서대문구", "마포구", "양천구", "강서구", "구로구", "금천구", "영등포구", "동작구", "관악구", "서초구", "강남구", "송파구", "강동구"};

        final String[] 경기도 = {"수원시", "수원시 장안구", "수원시 권선구", "수원시 팔달구", "수원시 영통구", "성남시", "성남시 수정구", "성남시 중원구", "성남시 분당구",
                "의정부시", "안양시", "안양시 만안구", "안양시 동안구", "부천시", "광명시", "평택시", "동두천시", "안산시", "안산시 상록구", "안산시 단원구", "고양시", "고양시 덕양구", "고양시 일산동구",
                "고양시 일산서구", "과천시", "구리시", "남양주시", "오산시", "시흥시", "군포시", "의왕시", "하남시", "용인시", "용인시 처인구", "용인시 기흥구", "용인시 수지구", "파주시", "이천시", "안성시",
                "김포시", "화성시", "광주시", "양주시", "포천시", "여주시", "연천군", "가평군", "양평군"};
        final String[] 인천시 = {"중구", "동구", "남구", "연수구", "남동구", "부평구", "계양구", "서구", "강화군", "옹진군"};
        final String[] 부산시 = {"중구", "서구", "동구", "영도구", "부산진구", "동래구", "남구", "북구", "해운대구", "사하구", "금정구", "강서구", "연제구", "수영구", "사상구", "기장군"};
        final String[] 대전시 = {"동구", "중구", "서구", "유성구", "대덕구"};
        final String[] 대구시 = {"중구", "동구", "서구", "남구", "북구", "수성구", "달서구", "달성군"};
        final String[] 울산시 = {"중구", "남구", "동구", "북구", "울주군"};
        final String[] 세종시 = {"가람동", "고운동", "금남면", "나성동", "다정동", "대평동", "도담동", "반곡동", "보람동", "부강면", "새롬동", "소담동", "소정면", "아름동", "어진동", "연기면", "연동면", "연서면", "장군면", "전동면", "전의면", "조치원읍", "종촌동", "한솔동"};
        final String[] 광주시 = {"동구", "서구", "남구", "북구", "광산구"};
        final String[] 전라남도 = {"목포시", "여수시", "순천시", "나주시", "광양시", "담양군", "곡성군", "구례군", "고흥군", "보성군", "화순군", "장흥군", "강진군", "해남군", "영암군", "무안군", "함평군", "영광군", "장성군", "완도군", "진도군"};
        final String[] 전라북도 = {"전주시", "전주시 완산구", "전주시 덕진구", "군산시", "익산시", "정읍시", "남원시", "김제시", "완주군", "진안군", "무주군", "장수군", "임실군", "순창군", "고창군", "부안군"};
        final String[] 강원도 = {"춘천시", "원주시", "강릉시", "동해시", "태백시", "속초시", "삼척시", "홍천군", "횡성군", "영월군", "평창군", "정선군", "철원군", "화천군", "양구군", "인제군", "고성군", "양양군"};
        final String[] 충청북도 = {"청주시", "청주시 상당구", "청주시 서원구", "청주시 흥덕구", "청주시 청원구", "충주시", "제천시", "보은군", "옥천군", "영동군", "증평군", "진천군", "괴산군", "음성군", "단양군"};
        final String[] 충청남도 = {"천안시", "천안시 동남구", "천안시 서북구", "공주시", "보령시", "아산시", "서산시", "논산시", "계룡시", "당진시", "금산군", "부여군", "서천군", "청양군", "홍성군", "예산군", "태안군"};
        final String[] 제주도 = {"제주시", "서귀포시"};

//        View view = inflater.inflate(R.layout.fragment1_4, null) ;
//        ArrayAdapter Adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, listviewitemlist) ;
        Adapter = new ArrayAdapter(activity, android.R.layout.simple_list_item_1, listviewitemlist);

        listview1 = (ListView) rootview.findViewById(R.id.listview1);
        listview1.setAdapter(Adapter);

        listview1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                // get TextView's Text.
                strText = (String) parent.getItemAtPosition(position);
                //Toast.makeText(activity, strText, Toast.LENGTH_SHORT).show();

                if (strText.equals("서울시")) {
                    nullcheck = 0;
                    Adapter1 = new ArrayAdapter(activity, android.R.layout.simple_list_item_1, 서울시);
                    listview2 = (ListView) rootview.findViewById(R.id.listview2);
                    listview2.setAdapter(Adapter1);
                } else if (strText.equals("경기도")) {
                    nullcheck = 0;
                    Adapter1 = new ArrayAdapter(activity, android.R.layout.simple_list_item_1, 경기도);
                    listview2 = (ListView) rootview.findViewById(R.id.listview2);
                    listview2.setAdapter(Adapter1);
                } else if (strText.equals("인천시")) {
                    nullcheck = 0;
                    Adapter1 = new ArrayAdapter(activity, android.R.layout.simple_list_item_1, 인천시);
                    listview2 = (ListView) rootview.findViewById(R.id.listview2);
                    listview2.setAdapter(Adapter1);
                } else if (strText.equals("부산시")) {
                    nullcheck = 0;
                    Adapter1 = new ArrayAdapter(activity, android.R.layout.simple_list_item_1, 부산시);
                    listview2 = (ListView) rootview.findViewById(R.id.listview2);
                    listview2.setAdapter(Adapter1);
                } else if (strText.equals("대전시")) {
                    nullcheck = 0;
                    Adapter1 = new ArrayAdapter(activity, android.R.layout.simple_list_item_1, 대전시);
                    listview2 = (ListView) rootview.findViewById(R.id.listview2);
                    listview2.setAdapter(Adapter1);
                } else if (strText.equals("대구시")) {
                    nullcheck = 0;
                    Adapter1 = new ArrayAdapter(activity, android.R.layout.simple_list_item_1, 대구시);
                    listview2 = (ListView) rootview.findViewById(R.id.listview2);
                    listview2.setAdapter(Adapter1);
                } else if (strText.equals("울산시")) {
                    nullcheck = 0;
                    Adapter1 = new ArrayAdapter(activity, android.R.layout.simple_list_item_1, 울산시);
                    listview2 = (ListView) rootview.findViewById(R.id.listview2);
                    listview2.setAdapter(Adapter1);
                } else if (strText.equals("세종시")) {
                    nullcheck = 0;
                    Adapter1 = new ArrayAdapter(activity, android.R.layout.simple_list_item_1, 세종시);
                    listview2 = (ListView) rootview.findViewById(R.id.listview2);
                    listview2.setAdapter(Adapter1);
                } else if (strText.equals("광주시")) {
                    nullcheck = 0;
                    Adapter1 = new ArrayAdapter(activity, android.R.layout.simple_list_item_1, 광주시);
                    listview2 = (ListView) rootview.findViewById(R.id.listview2);
                    listview2.setAdapter(Adapter1);
                } else if (strText.equals("전라남도")) {
                    nullcheck = 0;
                    Adapter1 = new ArrayAdapter(activity, android.R.layout.simple_list_item_1, 전라남도);
                    listview2 = (ListView) rootview.findViewById(R.id.listview2);
                    listview2.setAdapter(Adapter1);
                } else if (strText.equals("전라북도")) {
                    nullcheck = 0;
                    Adapter1 = new ArrayAdapter(activity, android.R.layout.simple_list_item_1, 전라북도);
                    listview2 = (ListView) rootview.findViewById(R.id.listview2);
                    listview2.setAdapter(Adapter1);
                } else if (strText.equals("강원도")) {
                    nullcheck = 0;
                    Adapter1 = new ArrayAdapter(activity, android.R.layout.simple_list_item_1, 강원도);
                    listview2 = (ListView) rootview.findViewById(R.id.listview2);
                    listview2.setAdapter(Adapter1);
                } else if (strText.equals("충청북도")) {
                    nullcheck = 0;
                    Adapter1 = new ArrayAdapter(activity, android.R.layout.simple_list_item_1, 충청북도);
                    listview2 = (ListView) rootview.findViewById(R.id.listview2);
                    listview2.setAdapter(Adapter1);
                } else if (strText.equals("충청남도")) {
                    nullcheck = 0;
                    Adapter1 = new ArrayAdapter(activity, android.R.layout.simple_list_item_1, 충청남도);
                    listview2 = (ListView) rootview.findViewById(R.id.listview2);
                    listview2.setAdapter(Adapter1);
                } else if (strText.equals("제주도")) {
                    nullcheck = 0;
                    Adapter1 = new ArrayAdapter(activity, android.R.layout.simple_list_item_1, 제주도);
                    listview2 = (ListView) rootview.findViewById(R.id.listview2);
                    listview2.setAdapter(Adapter1);
                }
                // TODO : use strText
            }
        });//출처: http://recipes4dev.tistory.com/42?category=605791 [개발자를 위한 레시피]
        listview2 = (ListView) rootview.findViewById(R.id.listview2);
        listview2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                nullcheck = 1;
                strText2 = (String) parent.getItemAtPosition(position);
                //Toast.makeText(activity, strText2, Toast.LENGTH_SHORT).show();
                // TODO : use strText
            }
        });


        nextbtn = (Button) rootview.findViewById(R.id.nextbtn);
        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nullcheck == 0) {
                    Toast.makeText(activity, "입력되지 않은 항목이 있습니다.", Toast.LENGTH_SHORT).show();
                } else {

                    savedata = activity.getSharedPreferences("MyData", MODE_PRIVATE);
                    saveeditor = savedata.edit();
                    saveeditor.putString("4", strText + ", " + strText2);
                    saveeditor.commit();

                    activity.onFragmentChange(4);
                }
            }
        });

        beforebtn = (Button) rootview.findViewById(R.id.beforebtn);
        beforebtn.setOnClickListener(new View.OnClickListener() {
            // 요청을 보내야 하는데 메인 액티비티에 다가 메소드를 하나 만들어야 한다.
            @Override
            public void onClick(View v) {
                activity.onFragmentChange(2);
            }
        });
        //getActivity();      // 액티비티위에 올라가게 한다.
        return rootview;            // 플레그먼트 화면으로 보여주게 된다.
    }
}
