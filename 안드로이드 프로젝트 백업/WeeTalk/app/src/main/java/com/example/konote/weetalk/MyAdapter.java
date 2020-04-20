package com.example.konote.weetalk;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by KONOTE on 2017-11-10.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    List<Chat> mChat;
    Context context;

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(List<Chat> mChat, Context com) {
        this.mChat = mChat;
        this.context = com;
    }
    @Override
    public int getItemViewType(int position) {
        if(mChat.get(position).getType() == 1111){
            return 1;
        }else if(mChat.get(position).getType() == 0000){
            return 2;
        }else if (mChat.get(position).getType() == 3333){
            return 3;
        }else if (mChat.get(position).getType() == 2222){
            return 4;
        }else if(mChat.get(position).getType() == 5555){
            return 5;
        }else{
            return 6;
        }
    }
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        if(viewType == 1){
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.r_text_view, parent, false);
        }else if(viewType == 2){
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_text_view, parent, false);
        }else if(viewType == 3){
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.r_image_view, parent, false);
        }else if (viewType == 4){
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_image_view, parent, false);
        }else if (viewType == 5){
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.r_map_view, parent, false);
        }else{
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_map_view, parent, false);
        }
        ViewHolder vh = new ViewHolder(v, viewType);



        return vh;
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView mUserName, mUserName2, mUserName3, mTextView, mTextView1, mTime,mTime2,mTime3,mTime4,mTime5,mTime6;
        public ImageView mImageView, mImageView2;
        public Button mMapbtn1, mMapbtn2;


        public ViewHolder(View itemView, int _type) {
            super(itemView);
            int type = _type;
            if(type == 1) {
                mTextView1 = (TextView) itemView.findViewById(R.id.mTextView);
                mTime3 = (TextView) itemView.findViewById(R.id.mTime);
            }
            else if(type == 2){
                mTextView = (TextView) itemView.findViewById(R.id.mTextView);
                mUserName = (TextView) itemView.findViewById(R.id.mUserName);
                mTime = (TextView) itemView.findViewById(R.id.mTime);
            }
            else if(type == 3){
                mImageView = (ImageView) itemView.findViewById(R.id.mImageView);
                mTime4 = (TextView) itemView.findViewById(R.id.mTime);
            }
            else if (type == 4){
                mUserName2 = (TextView) itemView.findViewById(R.id.mTextView2);
                mImageView2 = (ImageView) itemView.findViewById(R.id.mImageView2);
                mTime2 = (TextView) itemView.findViewById(R.id.mTime2);
            }
            else if (type == 5){
                mMapbtn1 = (Button) itemView.findViewById(R.id.r_mapbtn);
                mTime5 = (TextView) itemView.findViewById(R.id.r_mTime);
            }
            else if (type == 6){
                mUserName3 = (TextView) itemView.findViewById(R.id.l_mapname);
                mMapbtn2 = (Button) itemView.findViewById(R.id.l_mapbtn);
                mTime6 = (TextView) itemView.findViewById(R.id.l_mTime);
            }
        }
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        int type = mChat.get(position).getType();

        if(type==1111) {
            holder.mTextView1.setText(mChat.get(position).getMsg());
            holder.mTime3.setText(mChat.get(position).getTime());
        }else if(type == 0000){
            holder.mUserName.setText(mChat.get(position).getUsername());
            holder.mTextView.setText(mChat.get(position).getMsg());
            holder.mTime.setText(mChat.get(position).getTime());
        }else if(type == 3333) {

            String image = mChat.get(position).getMsg();
            Log.d("셋 할 이미지", image);
            try {
                //야매
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);

//                URL url = new URL("http://bkbk55152.vps.phps.kr/newImage/" + image);
//                InputStream is = url.openStream();
//                final Bitmap bm = BitmapFactory.decodeStream(is);
//                holder.mImageView.setImageBitmap(bm);
                Picasso.with(context)
                        .load("http://bkbk55152.vps.phps.kr/newImage/" + image)
                        .placeholder(R.drawable.xxx)
                        .into(holder.mImageView);
                holder.mTime4.setText(mChat.get(position).getTime());

            } catch(Exception e){
                Log.d("케치1 : ", e.toString());
            }
        }
        else if (type == 2222){

            final String image = mChat.get(position).getMsg();

            try {
                Log.d("사진수신 들어옴", "----");
                //야매
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);

                Log.d("셋 할 이미지/받은 메세지", image);
                Log.d("셋 할 유저타입/", String.valueOf(mChat.get(position).getType()));
                Log.d("셋 할 유저이름/", mChat.get(position).getUsername());
                Log.d("셋 할 시간", mChat.get(position).getTime());

                holder.mUserName2.setText(mChat.get(position).getUsername());

                Picasso.with(context)
                        .load("http://bkbk55152.vps.phps.kr/newImage/" + image)
                        .placeholder(R.drawable.xxx)
                        .into(holder.mImageView2);

                holder.mTime2.setText(mChat.get(position).getTime());

            } catch(Exception e){
                Log.d("케치2 : ", e.toString());
            }
        }
        else if(type == 5555){
//            holder.mUserName3.setText(mChat.get(position).getUsername());
            holder.mMapbtn1.setText("지도 보기");
            holder.mTime5.setText(mChat.get(position).getTime());
        }
        else if(type == 4444){
            holder.mUserName3.setText(mChat.get(position).getUsername());
            holder.mMapbtn2.setText("지도 보기");
            holder.mTime6.setText(mChat.get(position).getTime());
        }
    }
    @Override
    public int getItemCount() {
        return mChat.size();
    }
}