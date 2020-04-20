package android.medical.medicalplan.Login;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.medical.medicalplan.MainActivity.MainActivity;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.content.Intent;
import android.medical.medicalplan.IntroActivity;
import android.medical.medicalplan.R;
import android.medical.medicalplan.SignUp.SignUpActivity1;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kakao.auth.AuthType;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.LoginButton;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;
import com.nhn.android.naverlogin.ui.view.OAuthLoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView kakao_login_btn, naver_login_btn;
    TextView signup, findid, findpw;
    TextView next;
    Intent intent;

    /* http://blog.naver.com/PostView.nhn?blogId=luku756&logNo=221224337262&parentCategoryNo=23&categoryNo=&viewDate=&isShowPopularPosts=true&from=search */
    private static String OAUTH_CLIENT_ID = "zqMQTo2nhVWzQC5XjxA0";
    private static String OAUTH_CLIENT_SECRET = "V6e3U8gKRr";
    private static String OAUTH_CLIENT_NAME = "MedicalPlan";
    private static OAuthLogin mOAuthLoginInstance;
    Context mContext;
    String nickname = null, age = null, gender = null, email = null, reqMsg = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mContext = this;

        kakao_login_btn = (ImageView) findViewById(R.id.kakao_login_btn);
        kakao_login_btn.setOnClickListener(this);
        naver_login_btn = (ImageView) findViewById(R.id.naver_login_btn);
        naver_login_btn.setOnClickListener(this);
        signup = (TextView) findViewById(R.id.signup);
        signup.setOnClickListener(this);
        findid = (TextView) findViewById(R.id.findid);
        findid.setOnClickListener(this);
        findpw = (TextView) findViewById(R.id.findpw);
        findpw.setOnClickListener(this);

        next = (TextView)findViewById(R.id.next);
        next.setOnClickListener(this);


        mOAuthLoginInstance = OAuthLogin.getInstance();
        mOAuthLoginInstance.init(mContext, OAUTH_CLIENT_ID, OAUTH_CLIENT_SECRET, OAUTH_CLIENT_NAME);
        updateLog();


//        this.setTitle("OAuthLoginSample Ver." + OAuthLogin.getVersion());

        //getHashKey();

        //callback = new SessionCallback();
        //Session.getCurrentSession().addCallback(callback);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.kakao_login_btn://카카오로그인
                Toast.makeText(getApplicationContext(), "kakao", Toast.LENGTH_SHORT).show();

                Session session = Session.getCurrentSession();
                session.addCallback(new SessionCallback());
                session.open(AuthType.KAKAO_LOGIN_ALL, LoginActivity.this);

                Log.d(IntroActivity.TAG, "카카오로그인");
                break;

            case R.id.naver_login_btn://네이버로그인
                Toast.makeText(getApplicationContext(), "naver", Toast.LENGTH_SHORT).show();

                mOAuthLoginInstance.startOauthLoginActivity(LoginActivity.this, mOAuthLoginHandler);

                Log.d(IntroActivity.TAG, "네이버로그인");

                break;

            case R.id.signup://회원가입
                Toast.makeText(getApplicationContext(), "signup", Toast.LENGTH_SHORT).show();
                intent = new Intent(getApplicationContext(), SignUpActivity1.class);
                startActivity(intent);
                break;
            case R.id.findid://아이디찾기
                Toast.makeText(getApplicationContext(), "findid", Toast.LENGTH_SHORT).show();
                break;
            case R.id.findpw://비밀번호찾기
                Toast.makeText(getApplicationContext(), "findpw", Toast.LENGTH_SHORT).show();
                break;

            case R.id.next://비밀번호찾기
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                break;
        }
    }

    /**
     * https://re-build.tistory.com/9
     */
    public class SessionCallback implements ISessionCallback {
        // 로그인에 성공한 상태
        @Override
        public void onSessionOpened() {
            Log.d(IntroActivity.TAG, "로그인성공");
            requestMe();
        }

        // 로그인에 실패한 상태
        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            Log.e(IntroActivity.TAG, "SessionCallback :: " + "onSessionOpenFailed : " + exception.getMessage());
        }

        // 사용자 정보 요청
        public void requestMe() {
            // 사용자정보 요청 결과에 대한 Callback
            UserManagement.getInstance().requestMe(new MeResponseCallback() {

                // 세션 오픈 실패. 세션이 삭제된 경우,
                @Override
                public void onSessionClosed(ErrorResult errorResult) {
                    Log.e(IntroActivity.TAG, "SessionCallback :: " + "onSessionClosed : " + errorResult.getErrorMessage());
                }

                // 회원이 아닌 경우,
                @Override
                public void onNotSignedUp() {
                    Log.e(IntroActivity.TAG, "SessionCallback :: " + "onNotSignedUp");
                }

                // 사용자정보 요청에 성공한 경우,
                @Override
                public void onSuccess(UserProfile userProfile) {
                    Log.e(IntroActivity.TAG, "SessionCallback :: " + "onSuccess");

                    String nickname = userProfile.getNickname();
                    String email = userProfile.getEmail();
                    String profileImagePath = userProfile.getProfileImagePath();
                    String thumnailPath = userProfile.getThumbnailImagePath();
                    String UUID = userProfile.getUUID();

                    long id = userProfile.getId();

                    Log.e(IntroActivity.TAG, "Profile nickname: " + nickname);
                    Log.e(IntroActivity.TAG, "Profile email :" + email);
                    Log.e(IntroActivity.TAG, "Profile profileImagePath: " + profileImagePath);
                    Log.e(IntroActivity.TAG, "Profile thumnailPath: " + thumnailPath);
                    Log.e(IntroActivity.TAG, "Profile UUID: " + UUID);
                    Log.e(IntroActivity.TAG, "Profile id: " + id);
                }

                // 사용자 정보 요청 실패
                @Override
                public void onFailure(ErrorResult errorResult) {
                    Log.e(IntroActivity.TAG, "SessionCallback :: " + "onFailure : " + errorResult.getErrorMessage());
                }
            });
        }
    }

    /**
     * 카카오 디벨로퍼 해시키 등록을 위해 해키시 추출하는 메소드
     */
    private void getHashKey() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo("android.medical.medicalplan", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d(IntroActivity.TAG, "key_hash = " + Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }//출처: https://superwony.tistory.com/12 [개발자 키우기]

    private void updateLog() {
        Log.e(IntroActivity.TAG, "-----updateLog-----");
        Log.d(IntroActivity.TAG, "getAccessToken :" + mOAuthLoginInstance.getAccessToken(mContext));
        Log.d(IntroActivity.TAG, "getRefreshToken :" + mOAuthLoginInstance.getRefreshToken(mContext));
        Log.d(IntroActivity.TAG, "getExpiresAt :" + String.valueOf(mOAuthLoginInstance.getExpiresAt(mContext)));
        Log.d(IntroActivity.TAG, "getTokenType :" + mOAuthLoginInstance.getTokenType(mContext));
        Log.d(IntroActivity.TAG, "getState :" + mOAuthLoginInstance.getState(mContext).toString());
    }

    @Override
    protected void onResume() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        super.onResume();
    }

    /**
     * startOAuthLoginActivity() 호출시 인자로 넘기거나, OAuthLoginButton 에 등록해주면 인증이 종료되는 걸 알 수 있다.
     */
    private OAuthLoginHandler mOAuthLoginHandler = new OAuthLoginHandler() {
        @Override
        public void run(boolean success) {
            if (success) {
                Log.d(IntroActivity.TAG, "로그인 성공");

                Log.d(IntroActivity.TAG, "mOAuthLoginHandler_success_getAccessToken : " + mOAuthLoginInstance.getAccessToken(mContext));
                Log.d(IntroActivity.TAG, "mOAuthLoginHandler_success_getRefreshToken : " + mOAuthLoginInstance.getRefreshToken(mContext));
                Log.d(IntroActivity.TAG, "mOAuthLoginHandler_success_getExpiresAt : " + String.valueOf(mOAuthLoginInstance.getExpiresAt(mContext)));
                Log.d(IntroActivity.TAG, "mOAuthLoginHandler_success_getTokenType : " + mOAuthLoginInstance.getTokenType(mContext));
                Log.d(IntroActivity.TAG, "mOAuthLoginHandler_success_getState : " + mOAuthLoginInstance.getState(mContext).toString());

                //성공시
                new RequestApiTask().execute();

            } else {
                Log.d(IntroActivity.TAG, "로그인 실패");
                String errorCode = mOAuthLoginInstance.getLastErrorCode(mContext).getCode();
                String errorDesc = mOAuthLoginInstance.getLastErrorDesc(mContext);
                Toast.makeText(mContext, "errorCode: " + errorCode + ", errorDesc: " + errorDesc, Toast.LENGTH_SHORT).show();
            }
        }
    };

    /**
     * 로그아웃시 참고
     */
//    public void onButtonClick(View v) throws Throwable {
//        switch (v.getId()) {
//            case R.id.buttonOAuth: {
//                mOAuthLoginInstance.startOauthLoginActivity(LoginActivity.this, mOAuthLoginHandler);
//                break;
//            }
//            case R.id.buttonVerifier: {
//                new RequestApiTask().execute();
//                break;
//            }
//            case R.id.buttonRefresh: {
//                new RefreshTokenTask().execute();
//                break;
//            }
//            case R.id.buttonOAuthLogout: {
//                mOAuthLoginInstance.logout(mContext);
//                updateLog();
//                break;
//            }
//            case R.id.buttonOAuthDeleteToken: {
//                new DeleteTokenTask().execute();
//                break;
//            }
//            default:
//                break;
//        }
//    }

    private class DeleteTokenTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            boolean isSuccessDeleteToken = mOAuthLoginInstance.logoutAndDeleteToken(mContext);

            if (!isSuccessDeleteToken) {
                // 서버에서 token 삭제에 실패했어도 클라이언트에 있는 token 은 삭제되어 로그아웃된 상태이다
                // 실패했어도 클라이언트 상에 token 정보가 없기 때문에 추가적으로 해줄 수 있는 것은 없음
                Log.d(IntroActivity.TAG, "errorCode:" + mOAuthLoginInstance.getLastErrorCode(mContext));
                Log.d(IntroActivity.TAG, "errorDesc:" + mOAuthLoginInstance.getLastErrorDesc(mContext));
            }

            return null;
        }

        protected void onPostExecute(Void v) {
            updateLog();
        }
    }

    private class RequestApiTask extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            Log.d(IntroActivity.TAG, "RequestApiTask_onPreExecute:" + (String) "");
//            mApiResultText.setText((String) "");
        }

        @Override
        protected String doInBackground(Void... params) {
            Log.d(IntroActivity.TAG, "RequestApiTask_doInBackground:");

            String url = "https://openapi.naver.com/v1/nid/me";
            String at = mOAuthLoginInstance.getAccessToken(mContext);
            return mOAuthLoginInstance.requestApi(mContext, at, url);
        }

        protected void onPostExecute(String content) {
            Log.d(IntroActivity.TAG, "RequestApiTask_onPostExecute:" + (String) content);

            JSONObject jsonObject;
            try {
                jsonObject = new JSONObject(content);
                JSONObject response1 = jsonObject.getJSONObject("response");
                nickname = response1.getString("nickname");
                age = response1.getString("age");
                gender = response1.getString("gender");
                email = response1.getString("email");

                Log.d(IntroActivity.TAG, "response nickname : " + nickname);
                Log.d(IntroActivity.TAG, "response age : " + age);
                Log.d(IntroActivity.TAG, "response gender : " + gender);
                Log.d(IntroActivity.TAG, "response email : " + email);

                reqMsg = jsonObject.getString("message");

                Log.d(IntroActivity.TAG, "response reqMsg : " + reqMsg);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private class RefreshTokenTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            return mOAuthLoginInstance.refreshAccessToken(mContext);
        }

        protected void onPostExecute(String res) {
            updateLog();
        }
    }
}//[출처] [안드로이드] 네이버 아이디로 로그인 (1) 예제 실행하기|작성자 RedWings

