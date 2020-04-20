package com.example.konote.weetalk;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.Mat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by KONOTE on 2018-01-02.
 *
 *  *  OpenCV를 이용한 얼굴인식 기능 구현을 위해 새로운 프로젝트를 생성했다.
 *
 *  - 흐름 -
 *  1. 안드로이드 프로젝트의 assets 폴더에 넣었던 XML(얼굴과 눈 인식) 파일은 프로젝트 실행시 사용할 수 있도록하기 위해서는
 *     폰의 내부저장소로 옮기는 작업이 필요하다.
 *     그 작업을 해주는것이 이 클레스의 read_cascade_file 메소드 (복사 및 검색하기 하면 찾기 편함)
 *
 *     assets 위치 : 프로젝트 -> app -> src -> main -> assets
 *
 *  2. 내부저장소로부터 XML파일을 읽어온뒤(1번에서 했던 작업때문에 가능한일. 1번생략시 아무것도 못함)
 *     CascadeClassifier (복사 및 검색하기 하면 찾기 편함) 객체를 생성후 xml파일의 얼굴 및 눈을 인식하는 코드를 가져옴
 *
 *  3. 2번에서 자바로 넘긴 영상에서 얼굴과 눈이 인식되면 그 결과에대한 화면을 반환(matresult  변수)
 *
 *  4. 최종 결과(matresult = 변수) 를 폰 화면에 보여지도록 Mat객체(얼굴, 눈 포함) 리턴
 *     onCameraFrame 메소드 (복사 및 검색하기 하면 찾기 편함)
 *
 *  5. onCameraFrame(메소드) 에서 얼굴이 인식되면 보이지않던 버튼을 보여주고 버튼 클릭 시 (imageView.setOnClickListener) (복사 및 검색하기 하면 찾기 편함)
 *     파일이름과 메인엑티비티의 Context를 CameraView로 보내줌(CameraView에서 인텐트를 사용해 다음화면으로 넘기기위해 Context도 넘김)
 *
 */

public class OpenCVActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {
    //TAG
    private static final String TAG = "OpenCV";
    // 카메라버튼을 imageview로 만들었음
    private ImageView imageView;

    //CameraView
    //메인엑티비티에서 촬영한 화면을 CameraView로 넘겨 저장할 수 있도록 한다.
    private CameraView mOpenCvCameraView;

    //사실상 제일 중요한 애들
    //mat input = 화면 전체(빌드 후 카메라에 보이는 모든 화면
    private Mat matInput;
    //mat result = 카메라로 보여지는 화면에서 얼굴과 눈의 형태를 발견한다면 동그라미로 인식/표현된 화면을 출력
    private Mat matResult;

    // 앨범 Uri, Uri.fromFile(파일형태로 받음);
    Uri albumUri;

    //크롭에 사용할 코드
    private static final int REQUEST_IMAGE_CROP = 7777;
    ImageView iv_resultView;

    //private AppCompatActivity mActivity;
    //private final static int CAMERA_FACING = Camera.CameraInfo.CAMERA_FACING_BACK; // 카메라 뒷면

    public static native long loadCascade(String cascadeFileName);

    //얼굴과 눈을 인식(XML에서 처리)
    public static native boolean detect(long cascadeClassifier_face, long cascadeClassifier_eye, long matAddrInput, long matAddrResult);

    //이것을 find로 찾아왔다면 궁금해 할 사항이 이건 뭘까? 일 것이다.
    //이것은 얼굴이 인식된다면(얼굴을 감싸는 원) face가 1로 변함, 마찬가지로 눈 인식되면 수가 증가함
    public long cascadeClassifier_face = 0;
    public long cascadeClassifier_eye = 0;

    // xml 파일을 가져오기 위한 메소드를 추가한다
    // cpp 파일의 loadCascade 함수를 호출하도록 구현되어있음
    private void copyFile(String filename) {
        String baseDir = Environment.getExternalStorageDirectory().getPath();
        String pathDir = baseDir + File.separator + filename;

        // Assests 에 있는 xml 파일을 가져오기 위한것
        AssetManager assetManager = this.getAssets();

        InputStream inputStream = null;
        OutputStream outputStream = null;

        try {
            Log.d(TAG, "copyfile :: 다음 경로로 파일복사 " + pathDir);

            inputStream = assetManager.open(filename);
            outputStream = new FileOutputStream(pathDir);

            byte[] buffer = new byte[1024];
            int read;

            while ((read = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, read);
            }
            inputStream.close();
            inputStream = null;
            outputStream.flush();
            outputStream.close();
            outputStream = null;

        } catch (Exception e) {
            Log.d(TAG, "copyFile :: 파일 복사 중 예외 발생 " + e.toString());
        }
    }

    // xml 파일 읽어오기 위한 함수
    private void read_cascade_file() {
        //assets폴더에 넣은 XML파일을 내 핸드폰 내부저장소로 옮기는 작업
        copyFile("haarcascade_frontalface_alt.xml");
        copyFile("haarcascade_eye_tree_eyeglasses.xml");

        Log.d(TAG, "read_cascade_file:");

        //얼굴을 인식하는 파일 copy
        cascadeClassifier_face = loadCascade("haarcascade_frontalface_alt.xml");

        //눈을 인식하는 파일 copy
        cascadeClassifier_eye = loadCascade("haarcascade_eye_tree_eyeglasses.xml");
    }


    static {
        System.loadLibrary("opencv_java3");
        System.loadLibrary("native-lib");
    }


    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    mOpenCvCameraView.enableView();
                }
                break;
                default: {
                    super.onManagerConnected(status);
                }
                break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.opencvactivity);

        imageView = (ImageView) findViewById(R.id.imageView);
        iv_resultView = (ImageView) findViewById(R.id.showresult_iv);

        // 퍼미션 상태 확인
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //퍼미션 상태 확인
            if (!hasPermissions(PERMISSIONS)) {

                //퍼미션 허가 안되어있다면 사용자에게 요청
                requestPermissions(PERMISSIONS, PERMISSIONS_REQUEST_CODE);
            } else {
                read_cascade_file();
            }
        } else {
            read_cascade_file();
        }

        // 내가 만든 CamerView  를 씌워주자
        mOpenCvCameraView = (CameraView) findViewById(R.id.activity_surface_view);
        //mOpenCvCameraView = (CameraBridgeViewBase)findViewById(R.id.activity_surface_view);
        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(this);
        //mOpenCvCameraView.setCameraIndex(0); // front-camera(1),  back-camera(0)
        mOpenCvCameraView.setCameraIndex(1); // front-camera(1)

        mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);

        // 사진촬영 및 사진 저장
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d(TAG, "카메라 버튼 눌름");

                // 찍을 사진 파일에 대한 이름을 저장한 날짜로 저장해주자
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");

                String currentDatedTime = simpleDateFormat.format(new Date());
                //사진이름
                String filename = Environment.getExternalStorageDirectory().getPath() + "/facePicture_" + currentDatedTime + ".jpg";

                // SD Card 외부저장소에 저장해주기 위해서 절대경로를 가져와 줌
                File storageDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/facePicture");

                Log.d("파일경로", String.valueOf(storageDir));

                // 현재파일이 중복되지 않으면 디렉터리 만들어준다 저장될 디렉터리 만들어 주기 위해서 만듬
                if (!storageDir.exists()) {
                    storageDir.mkdirs();
                }

                //파일이름과 Context를 보내준다.
                mOpenCvCameraView.takePicture(filename, getApplicationContext());
                Log.d(TAG, "메인에서 " + filename + "saved");
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "onResume :: Internal OpenCV library not found.");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_2_0, this, mLoaderCallback);
        } else {
            Log.d(TAG, "onResum :: OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onCameraViewStarted(int width, int height) {

    }

    @Override
    public void onCameraViewStopped() {

    }

    //현재화면을 실시간으로 얼굴인식 유무를 계산 및 출력
    //최종 결과를 안드로이드폰의 화면에 보여지도록 결과 Mat 객체를 리턴.
    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        matInput = inputFrame.rgba();

        Log.e(TAG, "onCameraFram matInput : " + matInput);

        if (matResult != null) matResult.release();
        matResult = new Mat(matInput.rows(), matInput.cols(), matInput.type());

        // ConvertRGBtoGray(matInput.getNativeObjAddr(), matResult.getNativeObjAddr());
        Core.flip(matInput, matInput, 1);

        // Cpp 에서 얼굴 인식 되면 true
        if (detect(cascadeClassifier_face, cascadeClassifier_eye, matInput.getNativeObjAddr(), matResult.getNativeObjAddr())) {
            Log.d(TAG, "얼굴인식 됨");
            // 카메라 버튼 보여지기
            showUIThread();
            return matResult;
        } else {
            Log.d(TAG, "얼굴인식이 안되고 있음");
            // 카메라 버튼 안 보여지기
            goneUIThread();
            return matResult;
        }
    }

    // 퍼미션 관련 메소드
    static final int PERMISSIONS_REQUEST_CODE = 1000;
    String[] PERMISSIONS = {"android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE"};

    private boolean hasPermissions(String[] permissions) {
        int result;
        // 스트링 배열에 있는 퍼미션들의 허가 상태 여부 확인
        for (String perms : permissions) {
            result = ContextCompat.checkSelfPermission(this, perms);
            if (result == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    /// 권한 설정에 대한 결과값 받아오기
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {

            case PERMISSIONS_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean cameraPermissionAccepted = grantResults[0]
                            == PackageManager.PERMISSION_GRANTED;
                    boolean writePermissionAccepted = grantResults[1]
                            == PackageManager.PERMISSION_GRANTED;

                    if (!cameraPermissionAccepted || !writePermissionAccepted) {
                        showDialogForPermission("앱을 실행하려면 퍼미션을 허가하셔야합니다.");
                        return;
                    } else {
                        read_cascade_file();  // 퍼미션이 이미 허가된 경우에 카메라를 전면 카메라로 바꾸기 위한 코드
                    }
                }
                break;
        }
    }

    // 권한 다이얼 로그
    @TargetApi(Build.VERSION_CODES.M)
    private void showDialogForPermission(String msg) {

        AlertDialog.Builder builder = new AlertDialog.Builder(OpenCVActivity.this);
        builder.setTitle("알림");
        builder.setMessage(msg);
        builder.setCancelable(false);
        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                requestPermissions(PERMISSIONS, PERMISSIONS_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                finish();
            }
        });
        builder.create().show();
    }

    // 카메라 버튼이 보여지는 스레드 UI 변경
    public void showUIThread() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                imageView.setVisibility(View.VISIBLE);

            }
        });
    }

    // 카메라 버튼이 없어지는 스레드 UI 변경
    public void goneUIThread() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                imageView.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_IMAGE_CROP:
                if (resultCode == Activity.RESULT_OK) {
                    // 결과 보여주자
                    try {
                        albumUri = data.getData();
                        Log.d("크롭결과 data=", data.getData().getPath());
                        mOpenCvCameraView.setVisibility(View.GONE);
                        imageView.setVisibility(View.GONE);
                        iv_resultView.setVisibility(View.VISIBLE);
                        iv_resultView.setImageURI(albumUri);
                    } catch (Exception e) {
                        Log.e(TAG, "=크롭결과 " + e.getMessage());
                    }
                }
                break;
        }
    }

}
