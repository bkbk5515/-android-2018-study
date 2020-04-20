package com.example.konote.bkcontact;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {

    static final int REQ_ADD_CONTACT = 1;
    static final int REQ_ADD_CONTACT2 = 2;
    ListView listview = null;
    ListViewAdapter adapter;
    EditText editTextFilter;
    Bitmap icon;

    SharedPreferences listviewsave;
    SharedPreferences.Editor editor;

    Handler handler = new Handler();
    Toastthread thread1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Toast.makeText(getApplicationContext(), "어플이 처음 실행되거나 재 시작 되었습니다.(Main의 onCreate)", Toast.LENGTH_SHORT).show();

        adapter = new ListViewAdapter();
        listview = (ListView) findViewById(R.id.listview1);
        listview.setAdapter(adapter);
        editTextFilter = (EditText) findViewById(R.id.editTextFilter);

        listviewsave = getSharedPreferences("MyData", MODE_PRIVATE);
        editor = listviewsave.edit();

        //저장된 리스트뷰 불러오기
        shared();
//        handler_test();
        //아이템 추가(리스트뷰에 set은 onActivityResult에서 처리)
        Button buttonAddContact = (Button) findViewById(R.id.addbutton1);
        buttonAddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO : create intent and start activity.
                Intent intent = new Intent(MainActivity.this, AddContact.class);
                startActivityForResult(intent, REQ_ADD_CONTACT);
                //애니메이션
                overridePendingTransition(R.anim.activity_u, R.anim.activity_d);
            }
        });

        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
                Toast.makeText(getApplicationContext(), "롱클릭 실행됨", Toast.LENGTH_SHORT).show();

                final ListViewItem item = (ListViewItem) adapterView.getItemAtPosition(i);

                DialogInterface.OnClickListener callbutton = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        Toast.makeText(getApplicationContext(), "전화걸기", Toast.LENGTH_SHORT).show();
                        String nameshare = item.getName();
                        String number1share = item.getNumber1();
                        String number2share = item.getNumber2();
                        String mail1share = item.getMail();

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
                };
                DialogInterface.OnClickListener delitembutton = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int count, checked;
                        count = adapter.getCount();

                        if (count > 0) {
                            // 현재 선택된 아이템의 position 획득.
//                                checked = listview.getCheckedItemPosition();
                            checked = i;
                            if (checked > -1 && checked < count) {
                                // 아이템 삭제
                                ArrayList<ListViewItem> listViewItemList = adapter.listViewItemList;
                                listViewItemList.remove(i);

                                editor.remove("namesave" + i);
                                editor.remove("number1save" + i);
                                editor.remove("number2save" + i);
                                editor.remove("mailsave1" + i);
                                editor.remove("memosave" + i);
                                editor.commit();

                                // listview 선택 초기화.
                                listview.clearChoices();

                                Toast.makeText(getApplicationContext(), "선택한 아이템 삭제됨", Toast.LENGTH_SHORT).show();

                                // listview 갱신.
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }
                };

                DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                };

                new android.app.AlertDialog.Builder(MainActivity.this)
                        .setTitle("메뉴")
                        .setPositiveButton("공유", callbutton)
                        .setNeutralButton("취소", cancelListener)
                        .setNegativeButton("삭제", delitembutton)
                        .show();

//                return false;
                return true;
            }
        });

        ////////////아이템 클릭 이벤트
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                // get item
                ListViewItem item = (ListViewItem) parent.getItemAtPosition(position);

                //Toast.makeText(getApplicationContext(), "일반클릭 실행됨", Toast.LENGTH_SHORT).show();

                Bitmap iconDrawable = item.getIcon();
                String nameStr = item.getName();
                String number1Str = item.getNumber1();
                String number2Str = item.getNumber2();
                String mailStr = item.getMail();
                String memoStr = item.getMemo();

                Intent intent2 = new Intent(MainActivity.this, Detail.class);
                intent2.putExtra("Icon", iconDrawable);
                intent2.putExtra("Name", nameStr);
                intent2.putExtra("Number1", number1Str);
                intent2.putExtra("Number2", number2Str);
                intent2.putExtra("Mail", mailStr);
                intent2.putExtra("Memo", memoStr);

                startActivityForResult(intent2, REQ_ADD_CONTACT2);
                // TODO : use item data.

                //애니메이션
                overridePendingTransition(R.anim.activity_start1, R.anim.activity_end1);
            }
        });
        //스와이프!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        SwipeDismissListViewTouchListener touchListener =
                new SwipeDismissListViewTouchListener(listview, new SwipeDismissListViewTouchListener.DismissCallbacks() {
                    @Override
                    public boolean canDismiss(int position) {
                        return true;
                    }

                    @Override
                    public void onDismiss(ListView listView, int[] reverseSortedPositions) {
                        // 전화걸기는 인텐트 액션 다이얼과 다르게 권한을 직접 설정해 줘야함.
                        // 마시멜로우 이상부터 적용된 기능이라고 함.
                        for (int position : reverseSortedPositions) {
                            int permissionCheck = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE);
                            if (permissionCheck == PackageManager.PERMISSION_DENIED) {
                                // 권한 없음
                                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 0);
                                //Toast.makeText(getApplicationContext(),"권한없음",Toast.LENGTH_SHORT).show();
                            } else {
                                //권한 있음
                                ListViewItem item = (ListViewItem) adapter.getItem(position);
                                String callnum = item.getNumber1();
                                Intent i1 = new Intent(Intent.ACTION_CALL);
                                i1.setData(Uri.parse("tel:" + callnum));
                                startActivity(i1);
                            }
//                                    Toast.makeText(getApplicationContext(), "스와이프!!", Toast.LENGTH_SHORT).show();

//                                    ListViewItem item = (ListViewItem) adapter.getItem(position);
//                                    String callnum = item.getNumber1();
//                                    startActivity(new Intent("android.intent.action.CALL", Uri.parse("tel:" + callnum)));
//                                    Intent i1 = new Intent(Intent.ACTION_CALL);
//                                    i1.setData(Uri.parse("tel:" + callnum));
//                                    startActivity(i1);

//                                    listview.remove(listview.getItem(position));
                        }
//                                listview.notifyDataSetChanged();
                    }
                });
        listview.setOnTouchListener(touchListener);
        listview.setOnScrollListener(touchListener.makeScrollListener());

        editTextFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable edit) {
                String filterText = edit.toString();
                if (filterText.length() > 0) {
                    listview.setFilterText(filterText);
                } else {
                    listview.clearTextFilter();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

    }//크리에이트 끝

    //핸들러를 이용해 주기적으로 토스트 메세지 띄우기

    @Override
    public void finish() {
        super.finish();
        this.overridePendingTransition(R.anim.activity_end1, R.anim.activity_end2);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("onStart 들어옴", "~~~~~~~~~~~~~~~~~~");
        thread1 = new Toastthread();
        thread1.start();
    }

    class Toastthread extends Thread{
        boolean flag = true;

        @Override
        public void run() {
            while(flag) {
                try {
                    Log.d("트라이 들어옴?", "00000000000000000000000000");
                    int count = listviewsave.getInt("count", Integer.parseInt("0"));
                    Log.d("스레드 count", String.valueOf(count));
                    int random = (int)(Math.random()*count);
                    final String hdrtoast;

                    hdrtoast = new String(listviewsave.getString("namesave" + random, ""));
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), hdrtoast +"님께서 연락을 기다립니다.", Toast.LENGTH_SHORT).show();
                        }
                    });
                    Thread.sleep(8000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        public void setFlag(boolean f){
            this.flag = f;
        }
    }


//    @Override
//    protected void onRestart() {
//        super.onRestart();
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        Toast.makeText(getApplicationContext(), "화면이 전면에 보여집니다.(Main의 onResume)", Toast.LENGTH_SHORT).show();
//    }

    @Override
    protected void onPause() {
        super.onPause();

        thread1.setFlag(false);

        Log.d("onPause 안으로 들어왔니?", "okokokokok");

        listviewsave = getSharedPreferences("MyData", MODE_PRIVATE);
        editor = listviewsave.edit();

        int count, check;
        count = adapter.getCount();

        if (count > 0) {
            Log.d("1번째 if문 들어옴", "if(count >0)");
            check = listview.getCheckedItemPosition();

            for (check = 0; check < count; check++) {
                Log.d("3번째 for문안으로 들어옴", "for(i = 0; i < count; i++)");

//                editor.remove("iconsave" + check);
//                editor.remove("namesave" + check);
//                editor.remove("number1save" + check);
//                editor.remove("number2save" + check);
//                editor.remove("mailsave1" + check);
//                editor.remove("memosave" + check);
//                editor.remove("count");
//                editor.commit();

                String iconsave = BitMapToString(adapter.listViewItemList.get(check).getIcon());
                String namesave = adapter.listViewItemList.get(check).getName();
                String number1save = adapter.listViewItemList.get(check).getNumber1();
                String number2save = adapter.listViewItemList.get(check).getNumber2();
                String mailsave1 = adapter.listViewItemList.get(check).getMail();
                String memosave = adapter.listViewItemList.get(check).getMemo();

                editor.putString("iconsave" + check, iconsave);
                editor.putString("namesave" + check, namesave);
                editor.putString("number1save" + check, number1save);
                editor.putString("number2save" + check, number2save);
                editor.putString("mailsave1" + check, mailsave1);
                editor.putString("memosave" + check, memosave);

                editor.putInt("count", count);
                editor.putInt("check", check);

                Log.d("파우즈에서의 count", String.valueOf(count));
                Log.d("파우즈에서의 check", String.valueOf(check));

                editor.commit();

                Log.d("1", listviewsave.getString("namesave" + check, "값 없음"));
                Log.d("2", listviewsave.getString("number1save" + check, "값 없음"));
                Log.d("3", listviewsave.getString("number2save" + check, "값 없음"));
                Log.d("4", listviewsave.getString("mailsave1" + check, "값 없음"));
                Log.d("5", listviewsave.getString("memosave" + check, "값 없음"));
            }
        }
    }

//    @Override
//    protected void onStop() {
//        super.onStop();
//        Toast.makeText(getApplicationContext(), "MainActivity에서 다른 화면으로 이동합니다.(Main의 onStop)", Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        Toast.makeText(getApplicationContext(), "생명주기가 파괴됩니다.(Main의 Destroy)", Toast.LENGTH_SHORT).show();
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent returnintent) {

        ListViewItem listViewItem = new ListViewItem();
        int count, checked;
        count = adapter.getCount();

//      추가입니다
        if (requestCode == REQ_ADD_CONTACT) {// 새 연락처 등록
            if (resultCode == RESULT_OK) {

                //String icon = returnintent.getStringExtra("icon");
                icon = (Bitmap) returnintent.getExtras().get("icon");
                final String name = returnintent.getStringExtra("name");
                String number1 = returnintent.getStringExtra("number1");
                String number2 = returnintent.getStringExtra("number2");
                String mail = returnintent.getStringExtra("mail");
                String memo = returnintent.getStringExtra("memo");

//                Log.d("realPath1", icon);
//                poto = BitmapFactory.decodeFile(icon);

                if (icon != null)
                    listViewItem.setIcon(icon);
                if (name != null)
                    listViewItem.setName(name);
                if (number1 != null)
                    listViewItem.setNumber1(number1);
                if (number1 != null)
                    listViewItem.setNumber2(number2);
                if (number1 != null)
                    listViewItem.setMail(mail);
                if (number1 != null)
                    listViewItem.setMemo(memo);

                adapter.listViewItemList.add(listViewItem);


                Comparator<ListViewItem> textAsc = new Comparator<ListViewItem>() {// 가나다순으로 자동 정렬하는 코드
                    @Override
                    public int compare(ListViewItem item1, ListViewItem item2) {
                        return item1.getName().compareTo(item2.getName());
                    }
                };
                Collections.sort(adapter.listViewItemList, textAsc);

                Toast.makeText(getApplicationContext(), "새로운 연락처가 등록됨", Toast.LENGTH_SHORT).show();

                // listview 갱신
                adapter.notifyDataSetChanged();
            }
        }

        if (requestCode == REQ_ADD_CONTACT2) {// 수정
            if (resultCode == RESULT_OK) {
                if (count > 0) {
                    checked = listview.getCheckedItemPosition();
                    if (checked > -1 && checked < count) {
                        Bitmap icon = (Bitmap) returnintent.getExtras().get("icon");
                        String name = returnintent.getStringExtra("name");
                        String number1 = returnintent.getStringExtra("number1");
                        String number2 = returnintent.getStringExtra("number2");
                        String mail = returnintent.getStringExtra("mail1");
                        String memo = returnintent.getStringExtra("memo");

                        listViewItem.setIcon(icon);
                        listViewItem.setName(name);
                        listViewItem.setNumber1(number1);
                        listViewItem.setNumber2(number2);
                        listViewItem.setMail(mail);
                        listViewItem.setMemo(memo);

                        adapter.listViewItemList.set(checked, listViewItem);

                        Toast.makeText(getApplicationContext(), "연락처가 수정됨", Toast.LENGTH_SHORT).show();

                        adapter.notifyDataSetChanged();
                    }
                }
            }
        }
    }

    private void shared() {
        int count, newcount, check;

        count = listviewsave.getInt("count", Integer.parseInt("0"));
        newcount = adapter.getCount();
        check = listview.getCheckedItemPosition();

        if (newcount < 1) {
            for (int i = 0; i < count; i++) {

                String icon = new String(listviewsave.getString("iconsave" + i, ""));
                Bitmap bitmapicon = StringToBitMap(icon);

                String name = new String(listviewsave.getString("namesave" + i, ""));
                String number1 = new String(listviewsave.getString("number1save" + i, ""));
                String number2 = new String(listviewsave.getString("number2save" + i, ""));
                String mail = new String(listviewsave.getString("mailsave1" + i, ""));
                String memo = new String(listviewsave.getString("memosave" + i, ""));

                Log.d("첫번째 for문에 들어와서 count값", String.valueOf(count));

                Log.d("~~~~~~~~~count = ", String.valueOf(i));

                // 밑에꺼가 안된 이유 = call by reference문제 <http://trypsr.tistory.com/74>참고
                // 해결방법 = 리스트뷰 아이템에 name~memo까지 직접 넣어줘서 아이템안에서 직접 set해줌
                ListViewItem listViewItem = new ListViewItem(bitmapicon, name, number1, number2, mail, memo);

//                listViewItem.setName(name);
//                listViewItem.setNumber1(number1);
//                listViewItem.setNumber2(number2);
//                listViewItem.setMail(mail);
//                listViewItem.setMemo(memo);

                adapter.listViewItemList.add(listViewItem);
//                adapter.notifyDataSetChanged();
//                adapter.listViewItemList.set(check, listViewItem);
            }
        } else if (newcount == count) {
            for (int i = 0; i < count; i++) {
                String icon = new String(listviewsave.getString("iconsave" + i, ""));
                Bitmap bitmapicon = StringToBitMap(icon);
                String name = new String(listviewsave.getString("namesave" + i, ""));
                String number1 = new String(listviewsave.getString("number1save" + i, ""));
                String number2 = new String(listviewsave.getString("number2save" + i, ""));
                String mail = new String(listviewsave.getString("mailsave1" + i, ""));
                String memo = new String(listviewsave.getString("memosave" + i, ""));

                Log.d("두번째 for문에 들어와서 count값", String.valueOf(count));

                Log.d("~~~~~~~~~count = ", String.valueOf(i));

                // 밑에꺼가 안된 이유 = call by reference문제 <http://trypsr.tistory.com/74>참고
                // 해결방법 = 리스트뷰 아이템에 icon~memo까지 직접 넣어줘서 아이템안에서 직접 set해줌
                ListViewItem listViewItem = new ListViewItem(bitmapicon, name, number1, number2, mail, memo);

//                listViewItem.setName(name);
//                listViewItem.setNumber1(number1);
//                listViewItem.setNumber2(number2);
//                listViewItem.setMail(mail);
//                listViewItem.setMemo(memo);

//                adapter.listViewItemList.add(listViewItem);
//                adapter.notifyDataSetChanged();
                adapter.listViewItemList.set(check, listViewItem);
            }
        }
    }

    //아래 두 함수는 Bitmap을 String으로 ---> String을 Bitmap으로 변환해준다.
    public String BitMapToString(Bitmap icon) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        icon.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    public Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    private class CheckTypesTask extends AsyncTask<Void, Void, Void> {

        ProgressDialog asyncDialog = new ProgressDialog(MainActivity.this);

        @Override
        protected void onPreExecute() {
            asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            asyncDialog.setMessage("연락처를 최신상태로 갱신중...");
            // show dialog
            asyncDialog.show();
            super.onPreExecute();
        }

//        @Override
//        protected void onProgressUpdate(Void... values) {
//            super.onProgressUpdate(values);
//        }

        @Override
        protected Void doInBackground(Void... arg0) {
            int count;
            int newcount;

            try {
                for (int i = 0; i < 2; i++) {
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

    @Override // 전화걸기위해 승인여부 확인 결과.
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults[0] == 0) {
                Toast.makeText(this, "전화걸기 권한이 승인됨", Toast.LENGTH_SHORT).show();
            } else {
                //권한 거절된 경우
                Toast.makeText(this, "전화걸기 권한이 거절 되었습니다.전화걸기를 이용하려면 권한을 승낙하여야 합니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}