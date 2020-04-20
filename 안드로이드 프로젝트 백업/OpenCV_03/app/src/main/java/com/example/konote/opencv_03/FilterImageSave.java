//package com.example.konote.opencv_03;
//
///**
// * Created by KONOTE on 2018-01-02.
// */
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.hardware.Camera;
//import android.net.Uri;
//import android.os.AsyncTask;
//import android.os.Environment;
//import android.util.AttributeSet;
//import android.util.Log;
//import android.view.Surface;
//
//import org.opencv.android.CameraBridgeViewBase;
//import org.opencv.android.JavaCameraView;
//import org.opencv.core.Mat;
//
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//
//public class FilterImageSave  extends JavaCameraView implements Camera.PictureCallback {
//
//    private static final String TAG = "=cameraView=";
//
//    //메인에서 넘겨받는 파일이름
//    private String mPictureFileName;
//    //메인에서 넘겨받는 Context
//    private Context mContext;
//
//    public FilterImageSave(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        mContext = context;
//    }
//
//    public void takePicture(final String fileName, Context context) {
//        Log.i(TAG, "TAKING PICTURE");
//
//        //Main 에서 파일이름이 넘어온다
//        this.mPictureFileName = fileName;
//        this.mContext = context;
//
//        Log.d("=takePicure=", "context:" + mContext);
//
//
//        //SurfaceView 사용방법 : http://androidhuman.com/308
//        mCamera.setPreviewCallback(null);
//
//        mCamera.takePicture(null, null, this);
//
//        mCamera.startPreview();
//    }
//
//    //파일화시킴
//    @Override
//    public void onPictureTaken(byte[] bytes, Camera camera) {
//        Log.i(TAG, "Saving a bitmap to file");
//
//        try {
//            //FileOutputStream이란 https://wikidocs.net/227 참고
//            FileOutputStream fos = new FileOutputStream(mPictureFileName);
//
//            fos.write(bytes);
//            fos.flush();
//            //자바에서 알아서 닫아주지만 제데로 닫지 않은상태에서 파일을 실행시키게 되면 에러 발생 할 수 있음
//            //수동적으로 닫아주는 습관이 중요함
//            fos.close();
//
//            Log.d("=onTakenPicture=", "wrote bytes : " + bytes.length + " to " + mPictureFileName);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (java.io.IOException e) {
//            Log.e("PictureDemo", "Exception in photoCallback", e);
//        }
//
//        // 찍은 결과 즉, 경로값 /storage/emulated/0
//
//        Intent gotoIntent = new Intent(mContext, ShowAfterPic.class);
//        gotoIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        gotoIntent.putExtra("filename", mPictureFileName);
//        mContext.startActivity(gotoIntent);
//    }
//}
