package com.example.konote.weetalk;

import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.Log;

import org.opencv.android.JavaCameraView;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Created by KONOTE on 2018-01-02.
 *
 *  *  - 해당 클레스를 만들어야만 했던 이유 -
 *
 *  1. 사진촬영 후 MatInput 을 비트맵화 시켜 인텐트로 다음엑티비티에 넘기려 했으나 파일 크기때문에 불가능했다.
 *
 *  2. 비트맵을 byte배열로 나누어 인텐트로 보내려 해봤지만 그것역시 실패했다.
 *
 *  3. Mat을 long 변수로 인텐트 전달 뒤 전달받은 long변수를 다시 Mat으로 변환 후 비트맵화 시켜 이미지뷰에 띄우려 해봤지만 그것도 실패.
 *
 *  4. 크롭또는 비트맵팩토리로 용량을 줄이자니 화질이 매우 구리거나 사용 불가의 이미지였음.
 *
 *  5. 이쯤에서 생각을 해보니 인스타그램도 사진촬영후 원본은 저장하고 부가기능 첨부시 따로저장한다는것을 느낌
 *
 *
 `*  결론) 그렇다면 사진을 찍는순간 일단 내 디바이스에 저장 후 다음화면에서 그 이미지를 불러오는것이
 *        가장 완벽한 고화질의 이미지를 불러올 수 있겠구나 싶어서 만든 클레스임
 *
 *
 *  - 흐름 -
 *  1. 넘겨받은 FileName 과 Context를 확인 (takePicture 메소드) (복사 및 검색하기 하면 찾기 편함)
 *
 *  2. 메인엑티비티에서 넘겨받은 FileName을 onPictureTaken 에서 파일화 시킴
 *     (검색 : FileOutputStream fos = new FileOutputStream(mPictureFileName);)
 *
 *  3. 파일화 완료 후 메인엑티비티에서 받은 Context를 이용해 인텐트로 화면전환을 함
 *     인텐트를 통해 넘기는 정보 : 파일이름
 *
 *     --- 사실상 MainActivity에서 ShowAfterPic로 이동 ---
 *
 */

public class CameraView extends JavaCameraView implements Camera.PictureCallback {


    private static final String TAG = "=cameraView=";

    //메인에서 넘겨받는 파일이름
    private String mPictureFileName;
    //메인에서 넘겨받는 Context
    private Context mContext;

    public CameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public void takePicture(final String fileName, Context context) {
        Log.i(TAG, "TAKING PICTURE");

        //Main 에서 파일이름이 넘어온다
        this.mPictureFileName = fileName;
        this.mContext = context;

        Log.d("=takePicure=", "context:" + mContext);


        //SurfaceView 사용방법 : http://androidhuman.com/308
        mCamera.setPreviewCallback(null);

        mCamera.takePicture(null, null, this);

        mCamera.startPreview();
    }

    //파일화시킴
    @Override
    public void onPictureTaken(byte[] bytes, Camera camera) {
        Log.i(TAG, "Saving a bitmap to file");

        try {
            //FileOutputStream이란 https://wikidocs.net/227 참고
            FileOutputStream fos = new FileOutputStream(mPictureFileName);

            fos.write(bytes);
            fos.flush();
            //자바에서 알아서 닫아주지만 제데로 닫지 않은상태에서 파일을 실행시키게 되면 에러 발생 할 수 있음
            //수동적으로 닫아주는 습관이 중요함
            fos.close();

            Log.d("=onTakenPicture=", "wrote bytes : " + bytes.length + " to " + mPictureFileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (java.io.IOException e) {
            Log.e("PictureDemo", "Exception in photoCallback", e);
        }

        // 찍은 결과 즉, 경로값 /storage/emulated/0

        Intent gotoIntent = new Intent(mContext, ShowAfterPic.class);
        gotoIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        gotoIntent.putExtra("filename", mPictureFileName);
        mContext.startActivity(gotoIntent);
    }

}
