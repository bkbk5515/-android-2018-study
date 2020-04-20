package com.example.plan.todayplanfcmtest2;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/*
 - 등록 토큰이 변경되는 경우 -
 1. 앱에서 인스턴스 ID 삭제
 2. 새 기기에서 앱 복원
 3. 사용자가 앱 삭제/재설치
 4. 사용자가 앱 데이터 소거
 */

/**
 * 단말기 ID 생성
 * Static변수로 사용하는게 좋을듯.
 */
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = "MyIID";

    @Override
    public void onTokenRefresh() {
        Log.d(TAG, "onTokenRefresh() 호출됨.");

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed Token : " + refreshedToken);
    }

}
