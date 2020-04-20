package com.example.konote.opencv_03;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;


/**
 * ShowAfterPic를 만든이유 : 촬영 후 만들어진 파일을 불러와 이미지뷰에 띄워줌
 * <p>
 * - 흐름 -
 * 1. 넘겨받은 파일이름(String)을 Uri로 바꿔줌
 * 2. Uri로 파일을 만들어줌
 * 3. 비트맵팩토리로 옵션 설정(size = 4)
 * 4. 파일을 비트맵으로 변경
 * 5. 이미지뷰에 피트맵을 보여줌
 */

public class ShowAfterPic extends AppCompatActivity {

    private static final String TAG = "ShowAfterPic";

    //원본 사진을 이미지뷰에 처음 띄워주는 메인 이미지뷰
    //필터 클릭시 이미지뷰가 해당 필터로 바뀜
    private ImageView imageView;

    //원본 비트맵
    private Bitmap bitmap;// 사진경로를 받아와 비트맵으로 바꿔준것을 받는 비트맵 전역객체
    private String mCurrnetImagePath;  // /storage/emulated/0/ 절대경로

    private Bitmap copybitmap1;  // 필터 미리보기에 적용시킬 비트맵
    private Bitmap copybitmap2;  // 필터 미리보기에 적용시킬 비트맵
    private Bitmap copybitmap3;  // 필터 미리보기에 적용시킬 비트맵
    private Bitmap copybitmap4;  // 필터 미리보기에 적용시킬 비트맵
    private Bitmap copybitmap5;  // 필터 미리보기에 적용시킬 비트맵
    private Bitmap copybitmap6;  // 필터 미리보기에 적용시킬 비트맵
    private Bitmap copybitmap7;  // 필터 미리보기에 적용시킬 비트맵
    private Bitmap copybitmap8;  // 필터 미리보기에 적용시킬 비트맵
    private Bitmap copybitmap9;  // 필터 미리보기에 적용시킬 비트맵
    private Bitmap copybitmap10; // 필터 미리보기에 적용시킬 비트맵

    private ImageView image1, image2, image3, image4, image5, image6, image7, image8, image9, image10, image11; // 필터 미리보기 스크롤 이미지뷰

    private Mat matInput;
    private Mat matResult;

    private Button savebtn;

    private CameraView mOpenCvCameraView;

    static {
        System.loadLibrary("opencv_java3");
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_after_pic);

        // 원본이미지
        imageView = (ImageView) findViewById(R.id.imageView);//처음 실행시 원본, 추후 하단의 필터 클릭시 해당 필터이미지로 변경됨

        image1 = (ImageView) findViewById(R.id.image1);//원본이미지
        image2 = (ImageView) findViewById(R.id.image2);//캐니 필터1
        image3 = (ImageView) findViewById(R.id.image3);//세피아 필터2
        image4 = (ImageView) findViewById(R.id.image4);//필터3
        image5 = (ImageView) findViewById(R.id.image5);//필터4
        image6 = (ImageView) findViewById(R.id.image6);//필터5
        image7 = (ImageView) findViewById(R.id.image7);//필터5

        savebtn = (Button) findViewById(R.id.button);
//
//        image8 = (ImageView) findViewById(R.id.image8);//필터5
//        image9 = (ImageView) findViewById(R.id.image9);//필터5
//        image10 = (ImageView) findViewById(R.id.image10);//필터5
//        image11 = (ImageView) findViewById(R.id.image11);//필터5


        //저장된 사진을 원본 이미지뷰에 뿌려준다.
        if (getIntent() != null) {
            //인텐트로 넘겨받은 파일 이름
            mCurrnetImagePath = getIntent().getStringExtra("filename");

            Log.d(TAG, "파일경로 잘 불러오나" + mCurrnetImagePath);

            // /storage/emulated/0/ string 형태 파일경로 Uri 로 형태변환 해주기
            Uri uri = Uri.parse(mCurrnetImagePath);

            // Uri를 파일로 만들어주기
            File file = new File(uri.toString());
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 4;

            bitmap = BitmapFactory.decodeFile(file.getPath(), options);  // 비트맵형식으로 만들어 주기
            // 수정용으로 복사
            copybitmap1 = BitmapFactory.decodeFile(file.getPath(), options);
            copybitmap2 = BitmapFactory.decodeFile(file.getPath(), options);
            copybitmap3 = BitmapFactory.decodeFile(file.getPath(), options);
            copybitmap4 = BitmapFactory.decodeFile(file.getPath(), options);
            copybitmap5 = BitmapFactory.decodeFile(file.getPath(), options);
            copybitmap6 = BitmapFactory.decodeFile(file.getPath(), options);
//
//            copybitmap7 = BitmapFactory.decodeFile(file.getPath(), options);
//            copybitmap8 = BitmapFactory.decodeFile(file.getPath(), options);
//            copybitmap9 = BitmapFactory.decodeFile(file.getPath(), options);
//            copybitmap10 = BitmapFactory.decodeFile(file.getPath(), options);


            //  Bitmap을 Mat으로 만들어줌
            matInput = new Mat(bitmap.getWidth(), bitmap.getHeight(), CvType.CV_8UC1);
            Log.d(TAG, "OnCreate : matInput: " + matInput.toString());
            Utils.bitmapToMat(bitmap, matInput);  // bitmap => mat

            matResult = new Mat();

            CHANGEAUTUMN();//캐니
            CHANGESEPIA();//세비아효과
            CHANGEGRAY();//그레이
            CHANGEHSV();
            CHANGEYCrCb();
            CHANGELuv();
//
//            CHANGE12();
//            CHANGE13();
//            CHANGE14();
//            CHANGE15();

            imageView.setImageBitmap(bitmap);

        }//촬영했던 원본사진 setting 및 필터미리보기에 보여질 화면

        image1.setImageBitmap(bitmap);
        image2.setImageBitmap(copybitmap1);
        image3.setImageBitmap(copybitmap2);
        image4.setImageBitmap(copybitmap3);
        image5.setImageBitmap(copybitmap4);
        image6.setImageBitmap(copybitmap5);
        image7.setImageBitmap(copybitmap6);

//
//        image8.setImageBitmap(copybitmap7);
//        image9.setImageBitmap(copybitmap8);
//        image10.setImageBitmap(copybitmap9);
//        image11.setImageBitmap(copybitmap10);

        savebtn.setOnClickListener(new View.OnClickListener() {
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
                // 현재파일이 중복되지 않으면 디렉터리 만들어준다 저장될 디렉터리 만들어 주기 위해서 만듬
                if (!storageDir.exists()) {
                    storageDir.mkdirs();
                }

//                BitmapDrawable d = (BitmapDrawable)((ImageView) findViewById(R.id.imageView)).getDrawable();
//                Bitmap b = d.getBitmap();
                Drawable d = imageView.getDrawable();
                Bitmap b = ((BitmapDrawable)d).getBitmap();

                String where = "/storage/emulated/0/facePicture";
                SaveBitmapToFileCache(b, where, filename);
            }
        });
        image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView.setImageBitmap(bitmap);
            }
        });
        image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CHANGEAUTUMN();//캐니
            }
        });
        image3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CHANGESEPIA();//세비아효과
            }
        });
        image4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CHANGEGRAY();//그레이
            }
        });
        image5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CHANGEHSV();
            }
        });
        image6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CHANGEYCrCb();
            }
        });
        image7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CHANGELuv();
            }
        });
//
//        image8.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                CHANGE12();
//            }
//        });
//        image9.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                CHANGE13();
//            }
//        });
//        image10.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                CHANGE14();
//            }
//        });
//        image11.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                CHANGE15();
//            }
//        });

    }//////////////온크리에이트

    // 케니 효과
    public void CHANGEAUTUMN() {
        // 효과 주기  먼저 RGB =>  GRAY 로 변경한다음 Canny 입혀줘야해
        Imgproc.cvtColor(matInput, matResult, Imgproc.COLOR_RGB2GRAY);
        Imgproc.Canny(matResult, matResult, 80, 100);
        Imgproc.cvtColor(matResult, matResult, Imgproc.COLOR_GRAY2RGBA, 4);
        Utils.matToBitmap(matResult, copybitmap1);  // 캐니효과 입힌 bitmap 생성
        imageView.setImageBitmap(copybitmap1);
    }

    // 눈 효과
    public void CHANGESEPIA() {
        int width = copybitmap2.getWidth();
        int height = copybitmap2.getHeight();
        int[] pixels = new int[width * height];

        copybitmap2.getPixels(pixels, 0, width, 0, 0, width, height);

        Random random = new Random();

        int R, G, B, index = 0, thresHold = 50;

        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {

                index = y * width + x;

                R = Color.red(pixels[index]);
                G = Color.green(pixels[index]);
                B = Color.blue(pixels[index]);

                thresHold = random.nextInt(0xff);

                if (R > thresHold && G > thresHold && B > thresHold) {
                    pixels[index] = Color.rgb(0xff, 0xff, 0xff);
                }
            }
        }
        copybitmap2 = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        copybitmap2.setPixels(pixels, 0, width, 0, 0, width, height);

        imageView.setImageBitmap(copybitmap2);

    }

    //그레이효과
    public void CHANGEGRAY() {
        Log.d(TAG, "change : matInput: " + matInput.toString());
        Log.d(TAG, "change : matResult: " + matResult.toString());

        Imgproc.cvtColor(matInput, matResult, Imgproc.COLOR_RGB2GRAY);

        Log.d(TAG, "그래이 세팅후 멧결과 : matResult: " + matResult.toString());

        Utils.matToBitmap(matResult, copybitmap3);

        imageView.setImageBitmap(copybitmap3);
    }

    //효과
    public void CHANGEHSV() {
        Imgproc.cvtColor(matInput, matResult, Imgproc.COLOR_RGB2HSV);
        Utils.matToBitmap(matResult, copybitmap4);

        imageView.setImageBitmap(copybitmap4);
    }

    //효과
    public void CHANGEYCrCb() {
        Imgproc.cvtColor(matInput, matResult, Imgproc.COLOR_RGB2YCrCb);
        Utils.matToBitmap(matResult, copybitmap5);

        imageView.setImageBitmap(copybitmap5);
    }

    //효과
    public void CHANGELuv() {
        Imgproc.cvtColor(matInput, matResult, Imgproc.COLOR_RGB2Luv);
        Utils.matToBitmap(matResult, copybitmap6);

        imageView.setImageBitmap(copybitmap6);
    }

    // Bitmap to File
    public void SaveBitmapToFileCache(Bitmap bitmap, String strFilePath, String filename) {
        File file = new File(strFilePath);

        // If no folders
        if (!file.exists()) {
            file.mkdirs();
            // Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
        }
        File fileCacheItem = new File(strFilePath + filename);
        OutputStream out = null;
        try {
            fileCacheItem.createNewFile();

            out = new FileOutputStream(fileCacheItem);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //                out.close();
            Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
        }
    }//출처: http://jhrun.tistory.com/160 [JHRunning]

    //효과
    public void CHANGE12() {
        Imgproc.cvtColor(matInput, matResult, Imgproc.COLOR_BGR2BGR565);
        Utils.matToBitmap(matResult, copybitmap7);

        imageView.setImageBitmap(copybitmap7);
    }

    //효과
    public void CHANGE13() {
        Imgproc.cvtColor(matInput, matResult, Imgproc.COLOR_XYZ2BGR);
        Utils.matToBitmap(matResult, copybitmap8);

        imageView.setImageBitmap(copybitmap8);
    }

    //효과
    public void CHANGE14() {
        Imgproc.cvtColor(matInput, matResult, Imgproc.COLOR_Lab2RGB);
        Utils.matToBitmap(matResult, copybitmap9);

        imageView.setImageBitmap(copybitmap9);
    }

    //효과
    public void CHANGE15() {
        Imgproc.cvtColor(matInput, matResult, Imgproc.COLOR_YUV2BGR);
        Utils.matToBitmap(matResult, copybitmap10);

        imageView.setImageBitmap(copybitmap10);
    }
}
