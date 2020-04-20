package com.example.konote.bkcontact;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by KONOTE on 2017-07-16.
 */

public class AddContact extends AppCompatActivity implements View.OnClickListener {

    private Button call;
    private Button sns;
    private Button mail;
    private Button btn_cap;
    private Button savebutton;

    private EditText name;
    private EditText number1;
    private EditText number2;
    private EditText mailad;
    private EditText memo;
    private ImageView imageView;

    private String num;
    private String mailput;
    private String filePath;
    Uri uri;
    Uri uri1;

    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;
    private static final int CROP_FROM_iMAGE = 2;

    private Uri mImageCaptureUri;
    private ImageView iv_UserPhoto;
    private String absoultePath;

    Bitmap photo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        Toast.makeText(getApplicationContext(), "AddContact가 시작되었습니다.(AddContact의 onCreate)", Toast.LENGTH_SHORT).show();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.addcontact);

//        btn_cap = (Button) findViewById(R.id.btn_cap);
        savebutton = (Button) findViewById(R.id.savebutton);

        imageView = (ImageView) findViewById(R.id.imageView1);

        name = (EditText) findViewById(R.id.name1);
        number1 = (EditText) findViewById(R.id.number1);
        number2 = (EditText) findViewById(R.id.number2);
        mailad = (EditText) findViewById(R.id.mail1);
        memo = (EditText) findViewById(R.id.memo1);

        iv_UserPhoto = (ImageView) this.findViewById(R.id.imageView1); // 추가한것~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        Button btn_agreeJoin = (Button) this.findViewById(R.id.delbutton);// 추가한것~~~~~~~~~~~~~~~~~~~~~~~~~~~~~이미지버튼으로 바꿔보기???????????????????????????????
        btn_agreeJoin.setOnClickListener(this);// 추가한것~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//        imageView.setOnClickListener(this);


        //저장버튼으로 리스트뷰에서 사용할 텍스트들+이미지 값 반환환
        savebutton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent saveintent = new Intent();

                saveintent.putExtra("icon", photo);
                saveintent.putExtra("name", name.getText().toString());
                saveintent.putExtra("number1", number1.getText().toString());
                saveintent.putExtra("number2", number2.getText().toString());
                saveintent.putExtra("mail", mailad.getText().toString());
                saveintent.putExtra("memo", memo.getText().toString());

                setResult(RESULT_OK, saveintent);
                finish();

                overridePendingTransition(R.anim.activity_u, R.anim.activity_d);
            }
        });

//        //캡쳐부분//
//        final RelativeLayout container = (RelativeLayout) findViewById(R.id.Rel);
//        btn_cap.setOnClickListener(new Button.OnClickListener() {
//            public void onClick(View v) {
//
//                // TODO Auto-generated method stub
//
//                String folder = "Test_Directory"; // 폴더 이름
//
//                try {
//                    // 현재 날짜로 파일을 저장하기
//                    SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
//                    // 년월일시분초
//                    Date currentTime_1 = new Date();
//                    String dateString = formatter.format(currentTime_1);
//                    File sdCardPath = Environment.getExternalStorageDirectory();
//                    File dirs = new File(Environment.getExternalStorageDirectory(), folder);
//
//                    if (!dirs.exists()) { // 원하는 경로에 폴더가 있는지 확인
//                        dirs.mkdirs(); // Test 폴더 생성
//                        Log.d("CAMERA_TEST", "Directory Created");
//                    }
//                    container.buildDrawingCache();
//                    Bitmap captureView = container.getDrawingCache();
//                    FileOutputStream fos;
//                    String save;
//
//                    try {
//                        save = sdCardPath.getPath() + "/" + folder + "/" + dateString + ".jpg";
//                        // 저장 경로
//                        fos = new FileOutputStream(save);
//                        captureView.compress(Bitmap.CompressFormat.JPEG, 100, fos); // 캡쳐
//
//                        // 미디어 스캐너를 통해 모든 미디어 리스트를 갱신시킨다.
//                        sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
//                                Uri.parse("file://" + Environment.getExternalStorageDirectory())));
//                    } catch (FileNotFoundException e) {
//                        e.printStackTrace();
//                    }
//                    Toast.makeText(getApplicationContext(), dateString + ".jpg 저장",
//                            Toast.LENGTH_LONG).show();
//                } catch (Exception e) {
//                    // TODO: handle exception
//                    Log.e("Screen", "" + e.toString());
//                }
//
//            }
//
//        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_u, R.anim.activity_d);
    }

    //
//    @Override
//    protected void onStart() {
//        super.onStart();
//        //Toast.makeText(getApplicationContext(), "화면 잠금 해제시에도 토스트 메세지 발생.(AddContact의 onStart)", Toast.LENGTH_SHORT).show();
//
//    }
//
    @Override
    protected void onRestart() {
        super.onRestart();
//        Toast.makeText(getApplicationContext(), "AddContact의 onRestart", Toast.LENGTH_SHORT).show();
        CheckTypesTask task = new CheckTypesTask();
        task.execute();
    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        Toast.makeText(getApplicationContext(), "화면이 전면에 보여집니다.(AddContact의 onResume)", Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
////        Toast.makeText(getApplicationContext(), "다른 액티비티가 전면에 보임.(AddContact의 onResume)", Toast.LENGTH_SHORT).show();
//
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        Toast.makeText(getApplicationContext(), "AddContact에서 다른 화면으로 이동 합니다.(AddContact의 onStop)", Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        Toast.makeText(getApplicationContext(), "AddContact의 onDestroy)", Toast.LENGTH_SHORT).show();
//    }


    ////////////////////////////////////////////====================================================================================
//            /**
//             * 카메라에서 사진 촬영
//             */
//            public void doTakePhotoAction() // 카메라 촬영 후 이미지 가져오기
//            {
//                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//
//                // 임시로 사용할 파일의 경로를 생성
//                String url = "tmp_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
//                mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), url));
//
//                intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
//                startActivityForResult(intent, PICK_FROM_CAMERA);
//            }

    /**
     * 앨범에서 이미지 가져오기
     */
    public void doTakeAlbumAction() // 앨범에서 이미지 가져오기
    {
        // 앨범 호출
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK)
            return;

        switch (requestCode) {
            case PICK_FROM_ALBUM: {
                // 이후의 처리가 카메라와 같으므로 일단  break없이 진행합니다.
                // 실제 코드에서는 좀더 합리적인 방법을 선택하시기 바랍니다.
                mImageCaptureUri = data.getData();
                Log.d("SmartWheel", mImageCaptureUri.getPath().toString());
            }

            case PICK_FROM_CAMERA: {
                // 이미지를 가져온 이후의 리사이즈할 이미지 크기를 결정합니다.
                // 이후에 이미지 크롭 어플리케이션을 호출하게 됩니다.
                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setDataAndType(mImageCaptureUri, "image/*");

                // CROP할 이미지를 200*200 크기로 저장
                intent.putExtra("outputX", 200); // CROP한 이미지의 x축 크기
                intent.putExtra("outputY", 200); // CROP한 이미지의 y축 크기
                intent.putExtra("aspectX", 1); // CROP 박스의 X축 비율
                intent.putExtra("aspectY", 1); // CROP 박스의 Y축 비율
                intent.putExtra("scale", true);
                intent.putExtra("return-data", true);
                startActivityForResult(intent, CROP_FROM_iMAGE); // CROP_FROM_CAMERA case문 이동
                break;
            }
            case CROP_FROM_iMAGE: {
                // 크롭이 된 이후의 이미지를 넘겨 받습니다.
                // 이미지뷰에 이미지를 보여준다거나 부가적인 작업 이후에
                // 임시 파일을 삭제합니다.
                if (resultCode != RESULT_OK) {
                    return;
                }

                final Bundle extras = data.getExtras();

                // CROP된 이미지를 저장하기 위한 FILE 경로
                filePath = Environment.getExternalStorageDirectory().getAbsolutePath() +
                        "/SmartWheel/" + System.currentTimeMillis() + ".jpg";

                if (extras != null) {
                    photo = extras.getParcelable("data"); // CROP된 BITMAP
                    iv_UserPhoto.setImageBitmap(photo); // 레이아웃의 이미지칸에 CROP된 BITMAP을 보여줌 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~여기 2줄이 비트맵 이미지를 정의 후 이미지뷰에 이미지를 set해주는 과정

                    storeCropImage(photo, filePath); // CROP된 이미지를 외부저장소, 앨범에 저장한다.
                    absoultePath = filePath;
                    break;
                }
                // 임시 파일 삭제
                File f = new File(mImageCaptureUri.getPath());
                if (f.exists()) {
                    f.delete();
                }
            }
        }
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.delbutton) {

            DialogInterface.OnClickListener albumListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    doTakeAlbumAction();
                }
            };

            DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            };

            new android.app.AlertDialog.Builder(this)
                    .setTitle("이미지 업로드")
//                    .setPositiveButton("사진촬영", cameraListener)
                    .setNeutralButton("앨범선택", albumListener)
                    .setNegativeButton("취소", cancelListener)
                    .show();
        }
    }

    /*
     * Bitmap을 저장하는 부분
     */
    private void storeCropImage(Bitmap bitmap, String filePath) {
        // SmartWheel 폴더를 생성하여 이미지를 저장하는 방식이다.
        String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/SmartWheel";
        File directory_SmartWheel = new File(dirPath);

        if (!directory_SmartWheel.exists()) // SmartWheel 디렉터리에 폴더가 없다면 (새로 이미지를 저장할 경우에 속한다.)
            directory_SmartWheel.mkdir();

        File copyFile = new File(filePath);
        BufferedOutputStream out = null;

        try {
            copyFile.createNewFile();
            out = new BufferedOutputStream(new FileOutputStream(copyFile));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

            // sendBroadcast를 통해 Crop된 사진을 앨범에 보이도록 갱신한다.
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(copyFile)));

            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //    // 실제경로로 바꿔주는곳
//    public String getPath(Uri uri){
//        String[] projection = {MediaStore.Images.Media.DATA};
//        Cursor cursor = managedQuery(uri, projection, null, null, null);
//        startManagingCursor(cursor);
//        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//        cursor.moveToFirst();
//        return cursor.getString(columnIndex);
//    }
    private class CheckTypesTask extends AsyncTask<Void, Void, Void> {

        ProgressDialog asyncDialog = new ProgressDialog(AddContact.this);

        @Override
        protected void onPreExecute() {
            asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            asyncDialog.setMessage("사진 불러오는중...");

            // show dialog
            asyncDialog.show();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                for (int i = 0; i < 1; i++) {
                    //asyncDialog.setProgress(i * 30);
                    Thread.sleep(500);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            asyncDialog.dismiss();
            super.onPostExecute(result);
        }
    }
}