package com.example.konote.photo2;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by KONOTE on 2017-11-24.
 */
public class UploadFile extends AsyncTask<String, String, String> {

    //doInBackground : AsyncTask 클래스를 사용하는 목적은, 메인 쓰레드(UI THREAD)이외의 개인 쓰레드를 사용하면서 다른 작업들(네트워크, 파일 저장 등)을 하기 위함이었는데, 이 작업을 하기 위한 공간이다.
    // 즉, 사용자 지정 쓰레드와 같은 개념이면서.. 이  UploadFile이란 클래스를 객체로 만들어 사용할 변수의 execute()메소드로 호출되면, 그 execute()의 ()안 매개 변수 값이 전달된다.
    // 내 예제에서는 서버에 올릴 것이므로 서버 URL을 전달받을 것이다. 이 함수에서는 UI접근이 불가능하다.

    //onPreExecute : execute()함수를 통해 호출된 doInBackground보다 이전에 호출되는 메소드이다.
    // 이 메소드에서는 UI에 대한 접근이 가능하다.
    // 즉, doInBackground는 사용자 지정쓰레드이기에 메인쓰레드처럼 UI접근이 안되나,
    // onPreExecute안에 UI접근 내용을 작성하면 메인쓰레드에서 이 부분에 있는 것을 인식하고, UI 작업을 가능하게 한다.

    //onPostExecute : Post가 들어간 것으로 보니, execute 이후에 작업이 호출된다는 뜻이다.
    // 이 곳은 doInBackground 메소드가 종료된 후 호출되는데, 만약 doInBackground가 정상적이지 않게 종료된 경우, 이 파라미터로 null 값이 전달된다.
    // 그러므로 null이 들어왔을 때 처리하는 구문 등을 활용하면, 오류 예외 처리가 가능하다. 이 곳도 UI접근이 가능하다.

    //onProgressUpdate : Async는 쓰레드기능과 핸들러기능을 동시에 수행할 수 있다고 했다.
    // 근데 onPreExecute와 onPostExecute는 doInBackground  이 전, 이 후에만 UI접근이 가능한 것인데..
    // 만약 doInBackground 중간에 UI를 편집하고 싶다면 어떻게 해야할까. 그 작업을 이 메소드에서 수행한다.
    // 이 메소드를 호출하려면 doInBackground 메소드내에 publishProgress(progress)를 호출하면 이 곳으로 온다! 이 곳도 UI접근이 가능하다.


    Context context; // 생성자 호출 시
    ProgressDialog mProgressDialog; // 진행 상태 다이얼로그
    String fileName; // 파일 위치

    HttpURLConnection conn = null; // 네트워크 연결 객체
    DataOutputStream dos = null; // 서버 전송 시 데이터 작성한 뒤 전송

    String lineEnd = "\r\n"; // 구분자
    String twoHyphens = "--";
    String boundary = "*****";

    int bytesRead, bytesAvailable, bufferSize;
    byte[] buffer;
    int maxBufferSize = 1024;
    File sourceFile;
    int serverResponseCode;
    String TAG = "FileUpload";

    public UploadFile(Context context) {
        this.context = context;
    }

    public void setPath(String uploadFilePath){
        this.fileName = uploadFilePath;
        this.sourceFile = new File(uploadFilePath);
        Log.d("파일이름", this.fileName);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
//        mProgressDialog = new ProgressDialog(context);
//        mProgressDialog.setTitle("Loading...");
//        mProgressDialog.setMessage("Image uploading...");
//        mProgressDialog.setCanceledOnTouchOutside(false);
//        mProgressDialog.setIndeterminate(false);
//        mProgressDialog.show();
    }
    @Override
    protected String doInBackground(String... strings) {
        if(!sourceFile.isFile()){ // 해당 위치의 파일이 있는지 검사
            Log.e(TAG, "sourceFile(" + fileName + ") is Not A File");
            return null;
        }else {
            String success = "Success";
            Log.i(TAG, "sourceFile(" + fileName + ") is A File");
            try {
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL(strings[0]);
                Log.i("strings[0]", strings[0]); // 내 서버 photo URL 출력

                // Open a HTTP  connection to  the URL
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST"); // 전송 방식
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary); // boundary 기준으로 인자를 구분함
                conn.setRequestProperty("uploaded_file", fileName);
                Log.i(TAG, "fileName: " + fileName);

                // dataoutput은 outputstream이란 클래스를 가져오며, outputStream는 FileOutputStream의 하위 클래스이다.
                // output은 쓰기, input은 읽기, 데이터를 전송할 때 전송할 내용을 적는 것으로 이해할 것
                dos = new DataOutputStream(conn.getOutputStream());

                // 사용자 이름으로 폴더를 생성하기 위해 사용자 이름을 서버로 전송한다. 하나의 인자 전달 data1 = newImage
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"data1\"" + lineEnd); // name의 \ \ 안 인자가 php의 key
                dos.writeBytes(lineEnd);
                dos.writeBytes("newImage"); // newImage라는 값을 넘김
                dos.writeBytes(lineEnd);


                // 이미지 전송, 데이터 전달 uploadded_file라는 php key값에 저장되는 내용은 fileName
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\"; filename=\"" + fileName + "\"" + lineEnd);
                dos.writeBytes(lineEnd);

                // create a buffer of  maximum size
                bytesAvailable = fileInputStream.available();

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {
                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                }

                // send multipart form data necesssary after file data..., 마지막에 two~~ lineEnd로 마무리 (인자 나열이 끝났음을 알림)
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                // Responses from the server (code and message)
                serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn.getResponseMessage();

                Log.i(TAG, "[UploadImageToServer] HTTP Response is : " + serverResponseMessage + ": " + serverResponseCode);

                if (serverResponseCode == 200) {

                }

                // 결과 확인
                BufferedReader rd = null;

                rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                String line = null;
                while ((line = rd.readLine()) != null) {
                    Log.i("Upload State", line);
                }

                //close the streams //
                fileInputStream.close();
                dos.flush();
                dos.close();
            } catch(Exception e){
                Log.e(TAG + " Error", e.toString());
            }
            return success;
        }
    }
    //- AsyncTask를 상속받은 이유, 쓰레드 & 핸들러 대신 사용한 이유.. 네트워크를 하기 위함이었다.

    //- 먼저 해당 파일의 경로를 넘겨받은 setPath()메소드에서 new File로 해당 경로로 파일을 생성해보고, 생성이 되는지 확인한다.
    // 만약 생성이 된다면 파일이 생성된다 보고, 이 파일은 Input, 받아오기 위한 파일이 된다.

    //- doInBackground(String... strings){}에서 strings는 이 클래스를 사용하는 클래스 변수에서 execute()를 통해 호출 할때, ()안에 들어가는 매개변수의 값이다.
    // 미리 말하면, 저 안에는 파일을 업로드할 php로 연결하였으며, 이 php에서는 파일을 받고, 해당 파일을 특정 위치에 저장하도록 하는 기능을 작성했다.

    //- 그러므로 URL변수에 해당 string값을 넣는데, 여기서 strings는 배열이다. execute로는 하나의 url(php)만 전달할 것이므로 [0]을 넣은 것이다.

    //- HttpUrlConnection 변수 conn으로 openConnection()을 통해 연결을 시작하며, 기타 설정등을 포함한다.

    //- FileInputStream은 InputStream을 상속받는 클래스이며, 파일을 읽기 위한 클래스이다.
    // 즉, 안드로이드 스튜디오와 sourceFile(해당 경로의 파일)을 연결 짓는 통로(Stream)라 생각하면 된다.
    // InputStream은 바이트 단위로 데이터를 읽어 들이는 모든 입력 스트림이 상속하는 최상위 클래스이다. 이 외에도 Buffered, Data Input Stream 등이 있는데..
    // 이 것은 각각 데이터를 어떻게 받아오기 위함인 듯하다. 간단히 정리하면, 해당 파일의 경로를 불러와 해당 파일(사진 이미지)을 읽고(FileInputStream), 전송할 데이터를 작성(While부분, DataOutputStream)하여 보내는 것이다.

    //- 그럼 이 데이터를 받는 부분, 즉 php쪽에서는 어떻게 처리해야할까

}