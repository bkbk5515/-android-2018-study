package com.example.konote.bkcontact;

import android.app.SearchManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class Detail extends AppCompatActivity {

    static final int REQ_ADD_CONTACT3 = 3;
    private Button call;
    private Button sns;
    private Button mail;
    private Button savebutton;
    private Button sharebtn;
    private Button search;
    private Button map;
    private Button shopping;
    private Button anim;

    private ImageView imageView;
    private EditText name;
    private EditText number1;
    private EditText number2;
    private EditText mail1;
    private EditText memo;

    String num;
    String memosearch;

    Bitmap iconset;

    Uri uri;
    Uri uri1;

    Handler handler = new Handler();
    Toastthread thread1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail);

        call = (Button) findViewById(R.id.call);
        sns = (Button) findViewById(R.id.sns);
        mail = (Button) findViewById(R.id.mail);
        savebutton = (Button) findViewById(R.id.savebutton);
        sharebtn = (Button) findViewById(R.id.sharebtn);
        search = (Button) findViewById(R.id.search);
        map = (Button) findViewById(R.id.map);
        shopping = (Button) findViewById(R.id.shopping);
        anim = (Button) findViewById(R.id.animbtn);

        imageView = (ImageView) findViewById(R.id.imageView1);
        name = (EditText) findViewById(R.id.name1);
        number1 = (EditText) findViewById(R.id.number1);
        number2 = (EditText) findViewById(R.id.number2);
        mail1 = (EditText) findViewById(R.id.mail1);
        memo = (EditText) findViewById(R.id.memo1);

        final Intent intent = getIntent();

        iconset = (Bitmap) intent.getExtras().get("Icon");
        final String nameset = intent.getExtras().getString("Name");
        final String number1set = intent.getExtras().getString("Number1");
        final String number2set = intent.getExtras().getString("Number2");
        final String mailset = intent.getExtras().getString("Mail");
        String memoset = intent.getExtras().getString("Memo");

        imageView.setImageBitmap(iconset);
        name.setText(nameset);
        number1.setText(number1set);
        number2.setText(number2set);
        mail1.setText(mailset);
        memo.setText(memoset);

        //랜덤 int 0~9까지 발생시켜 애니메이션 랜덤 실행
        int random = (int) (Math.random() * 6);
//        int random = 5;

        if (random == 0) { // 한바퀴 회전
            //애니메이션
            final Animation random1 = AnimationUtils.loadAnimation(this, R.anim.detail_image_anim1);
            imageView.startAnimation(random1);
        } else if (random == 1) { // 투명상태에서 또렷해짐
            final Animation random1 = AnimationUtils.loadAnimation(this, R.anim.detail_image_anim2);
            imageView.startAnimation(random1);
        } else if (random == 2) { // 크기가 커졌다가 원위치로
            final Animation random1 = AnimationUtils.loadAnimation(this, R.anim.detail_image_anim3);
            imageView.startAnimation(random1);
        } else if (random == 3) { // 투명한상태에서 회전하며 또렷해짐
            final Animation random1 = AnimationUtils.loadAnimation(this, R.anim.detail_image_anim4);
            imageView.startAnimation(random1);
        } else if (random == 4) { // 밑에서 원위치로 이동하며 투명하던게 또렷해짐
            final Animation random1 = AnimationUtils.loadAnimation(this, R.anim.detail_image_anim5);
            imageView.startAnimation(random1);
        } else if (random == 5) { //왼쪽 밑에서 원위치로 이동 + 회전하면서 이동 + 투명상태에서 점점 또렷해짐 + 2.5배의 크기에서 점점 원위치로 돌아옴
            final Animation random1 = AnimationUtils.loadAnimation(this, R.anim.detail_image_anim6);
            imageView.startAnimation(random1);
        }

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                num = number1.getText().toString();
                String tel = "tel:" + num;

                Toast.makeText(getApplicationContext(), "전화걸기", Toast.LENGTH_SHORT).show();

                startActivity(new Intent("android.intent.action.DIAL", Uri.parse(tel)));

//                Intent callIntent = new Intent(Intent.ACTION_CALL);
//                callIntent.setData(Uri.parse("tel:" + num));
//                startActivity(callIntent);
            }
        });

        sns.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {

                num = number1.getText().toString();
                uri = Uri.parse("smsto:" + num); //sms 문자와 관련된 Data는 'smsto:'로 시작. 이후는 문자를 받는 사람의 전화번호
                Intent i1 = new Intent(Intent.ACTION_SENDTO, uri); //시스템 액티비티인 SMS문자보내기 Activity의 action값
                //i.putExtra("sms_body", "Hello...");  //보낼 문자내용을 추가로 전송, key값은 반드시 'sms_body'
                Toast.makeText(getApplicationContext(), "메세지 보내기", Toast.LENGTH_SHORT).show();

                startActivity(i1);//액티비티 실행
            }
        });

        mail.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {

                uri1 = Uri.parse("mailto:" + mail1.getText().toString()); //이메일과 관련된 Data는 'mailto:'으로 시작. 이후는 이메일 주소
                Intent i2 = new Intent(Intent.ACTION_SENDTO, uri1); //시스템 액티비티인 Dial Activity의 action값
                Toast.makeText(getApplicationContext(), "메일 보내기", Toast.LENGTH_SHORT).show();

                startActivity(i2);//액티비티 실행
            }
        });

        sharebtn.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                String nameshare = nameset;
                String number1share = number1set;
                String number2share = number2set;
                String mail1share = mailset;

                String smsBody = "이름 : " + nameshare + "\n" + "전화번호1 : " + number1share + "\n" + "전화번호2 : " + number2share + "\n" + "이메일 : " + mail1share;

                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.setType("text/plain");
//                intent.putExtra(Intent.EXTRA_SUBJECT, title);
                intent.putExtra(Intent.EXTRA_TEXT, smsBody);
                Toast.makeText(getApplicationContext(), "공유", Toast.LENGTH_SHORT).show();

                Intent chooser = Intent.createChooser(intent, "공유");
                startActivity(chooser);

            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "검색", Toast.LENGTH_SHORT).show();

                memosearch = memo.getText().toString();
                Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                intent.putExtra(SearchManager.QUERY, memosearch);
                startActivity(intent);
            }
        });

        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "구글맵", Toast.LENGTH_SHORT).show();

                Uri uri = Uri.parse("geo:37.566535,126.977969");                  // 좌표값 설정.
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri); // 인텐트 생성.

                startActivity(intent);
            }
        });

        shopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "쇼핑몰", Toast.LENGTH_SHORT).show();

                Uri uri = Uri.parse("http://www.musinsa.com");
                Intent it = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(it);
            }
        });

        savebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                intent.putExtra("icon", iconset);
                intent.putExtra("name", name.getText().toString());
                intent.putExtra("number1", number1.getText().toString());
                intent.putExtra("number2", number2.getText().toString());
                intent.putExtra("mail1", mail1.getText().toString());
                intent.putExtra("memo", memo.getText().toString());

                setResult(RESULT_OK, intent);
                finish();
                overridePendingTransition(R.anim.activity_start2, R.anim.activity_end2);
            }
        });

        anim.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Detail.this, Anim.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_start2, R.anim.activity_end2);
    }

    @Override
    protected void onStart() {
        super.onStart();
        thread1 = new Toastthread();
        thread1.start();
    }

    class Toastthread extends Thread {
        boolean flag = true;

        @Override
        public void run() {
            while (flag) {
                try {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Dialog1();
                        }
                    }, 7000);
                    thread1.setFlag(false);
                    Thread.sleep(10000);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public void setFlag(boolean f) {
            this.flag = f;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        thread1.setFlag(false);
    }

    public void Dialog1(){
        DialogInterface.OnClickListener callbutton = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                thread1.setFlag(false);

                num = number1.getText().toString();
                String tel = "tel:" + num;

                startActivity(new Intent("android.intent.action.DIAL", Uri.parse(tel)));
            }
        };
        DialogInterface.OnClickListener delitembutton = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                thread1.setFlag(false);

                num = number1.getText().toString();
                uri = Uri.parse("smsto:" + num); //sms 문자와 관련된 Data는 'smsto:'로 시작. 이후는 문자를 받는 사람의 전화번호
                Intent i1 = new Intent(Intent.ACTION_SENDTO, uri); //시스템 액티비티인 SMS문자보내기 Activity의 action값

                startActivity(i1);//액티비티 실행
            }
        };

        DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                thread1.setFlag(false);

                dialog.dismiss();
            }
        };

        new android.app.AlertDialog.Builder(Detail.this)
                .setTitle("연락 한번 해볼까요?")
                .setPositiveButton("전화", callbutton)
                .setNeutralButton("취소", cancelListener)
                .setNegativeButton("메세지", delitembutton)
                .show();
//        return true;
//                                            return false;

    }
}