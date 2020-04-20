package com.example.konote.view;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by KONOTE on 2017-11-10.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    List<Chat> mChat;

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(List<Chat> mChat) {
        this.mChat = mChat;
//        this.username = _username;
//        this.time = _time;
    }
    @Override
    public int getItemViewType(int position) {
        if(mChat.get(position).getType() == 1111){
            return 1;
        }
        else{
            return 2;
        }
    }
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        if(viewType == 1){
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.r_text_view, parent, false);
        }else{
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_text_view, parent, false);
        }
        ViewHolder vh = new ViewHolder(v, viewType);
        return vh;
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;
        public TextView mTextView1;
        public TextView mUserName;

        public ViewHolder(View itemView, int _type) {
            super(itemView);
            int type = _type;
            if(type == 1) {
                mTextView1 = (TextView) itemView.findViewById(R.id.mTextView);
            }
            else{
                mTextView = (TextView) itemView.findViewById(R.id.mTextView);
                mUserName = (TextView) itemView.findViewById(R.id.mUserName);
//                mTime = (TextView) itemView.findViewById(R.id.mTime);
            }
        }
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        int type = mChat.get(position).getType();

        if(type==1111) {
            holder.mTextView1.setText(mChat.get(position).getMsg());
        }else{
            holder.mUserName.setText(mChat.get(position).getUsername());
            holder.mTextView.setText(mChat.get(position).getMsg());

            Log.d("이름", mChat.get(position).getUsername());
            Log.d("메세지", mChat.get(position).getMsg());
        }

    }
    @Override
    public int getItemCount() {
        return mChat.size();
    }
}