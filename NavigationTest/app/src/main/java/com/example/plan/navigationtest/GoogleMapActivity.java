package com.example.plan.navigationtest;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class GoogleMapActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    // 구글 맵 참조변수 생성
    GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.googlemap_activity);

        // SupportMapFragment을 통해 레이아웃에 만든 fragment의 ID를 참조하고 구글맵을 호출한다.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this); // getMapAsync must be called on the main thread.
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // 구글 맵 객체를 불러온다.
        mMap = googleMap;

        // 서울에 대한 위치 설정37.501743, 127.036085
        LatLng seoul1 = new LatLng(37.501743, 127.036085);
        // 구글 맵에 표시할 마커에 대한 옵션 설정
        MarkerOptions makerOptions1 = new MarkerOptions();
        makerOptions1.position(seoul1);
        makerOptions1.title("세무법인지오역삼지점");
        makerOptions1.snippet("서울특별시 강남구 역삼동");
        makerOptions1.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

        // 서울에 대한 위치 설정 37.498019, 127.037847
        LatLng seoul2 = new LatLng(37.498019, 127.037847);
        // 구글 맵에 표시할 마커에 대한 옵션 설정
        MarkerOptions makerOptions2 = new MarkerOptions();
        makerOptions2.position(seoul2);
        makerOptions2.title("세무법인박앤파트너스");
        makerOptions2.snippet("서울특별시 강남구 역삼1동 738-49");
        makerOptions2.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

        // 서울에 대한 위치 설정 37.498899, 127.034275
        LatLng seoul3 = new LatLng(37.498899, 127.034275);
        // 구글 맵에 표시할 마커에 대한 옵션 설정
        MarkerOptions makerOptions3 = new MarkerOptions();
        makerOptions3.position(seoul3);
        makerOptions3.title("세무법인정상");
        makerOptions3.snippet("서울특별시 강남구 역삼1동 테헤란로20길 12");
        makerOptions3.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

        // 서울에 대한 위치 설정 37.500010, 127.038954
        LatLng seoul4 = new LatLng(37.500010, 127.038954);
        // 구글 맵에 표시할 마커에 대한 옵션 설정
        MarkerOptions makerOptions4 = new MarkerOptions();
        makerOptions4.position(seoul4);
        makerOptions4.title("예일세무법인");
        makerOptions4.snippet("서울특별시 강남구 역삼동 725-7");
        makerOptions4.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

        // 서울에 대한 위치 설정 37.502589, 127.035016
        LatLng seoul5 = new LatLng(37.502589, 127.035016);
        // 구글 맵에 표시할 마커에 대한 옵션 설정
        MarkerOptions makerOptions5 = new MarkerOptions();
        makerOptions5.position(seoul5);
        makerOptions5.title("세무법인세광");
        makerOptions5.snippet("서울특별시 강남구 역삼1동 640-21");
        makerOptions5.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

        // 서울에 대한 위치 설정 37.4966802,  127.0314399
        LatLng seoul6 = new LatLng(37.4970143, 127.031172);
        // 구글 맵에 표시할 마커에 대한 옵션 설정
        MarkerOptions makerOptions6 = new MarkerOptions();
        makerOptions6.position(seoul6);
        makerOptions6.title("오늘TAC");
        makerOptions6.snippet("서울특별시 강남구 테헤란로8길 33");
        makerOptions6.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));



        // 마커를 생성한다.
        mMap.addMarker(makerOptions1);
        mMap.addMarker(makerOptions2);
        mMap.addMarker(makerOptions3);
        mMap.addMarker(makerOptions4);
        mMap.addMarker(makerOptions5);
        mMap.addMarker(makerOptions6);

        mMap.setOnMarkerClickListener(this);

        //카메라를 서울 위치로 옮긴다.
        mMap.moveCamera(CameraUpdateFactory.newLatLng(seoul1));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener(){
            @Override
            public void onInfoWindowClick(Marker marker) {
                Uri uri = Uri.parse("http://tax.ontac.co.kr/Front_s/main/main.html");
                Intent it  = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(it);
            }
        });
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Toast.makeText(this, marker.getTitle(), Toast.LENGTH_SHORT).show();
        return false;
    }

    //이전 액티비티 돌아갈 때 애니메이션 제거
    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0,0);
        //액티비티 애니메이션 x
    }//출처: http://puzzleleaf.tistory.com/62 [퍼즐잎의 성장일지]

}