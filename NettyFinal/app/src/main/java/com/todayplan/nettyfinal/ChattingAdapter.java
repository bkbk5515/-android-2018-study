package com.todayplan.nettyfinal;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;


public class ChattingAdapter extends BaseAdapter {

    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    public ArrayList<ChattingItem> listViewItemList = new ArrayList<ChattingItem>();

    // ListViewAdapter의 생성자
    public ChattingAdapter() {
    }

    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return listViewItemList.size();
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final int pos = position;
        final Context context = parent.getContext();

        /**
         *
         */
        CustomHolder holder = null;

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.room_chatting_listview_item, parent, false);

            TextView l_id = null;
            TextView l_msg = null;
            TextView r_id = null;
            TextView r_msg = null;

            // 홀더 생성 및 Tag로 등록
            holder = new CustomHolder();
            holder.l_id = (TextView) convertView.findViewById(R.id.l_id);
            holder.l_msg = (TextView) convertView.findViewById(R.id.l_msg);
            holder.r_id = (TextView) convertView.findViewById(R.id.r_id);
            holder.r_msg = (TextView) convertView.findViewById(R.id.r_msg);
            convertView.setTag(holder);
        } else {
            holder = (CustomHolder) convertView.getTag();

        }
        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        ChattingItem listViewItem = listViewItemList.get(position);

        if (listViewItem.getID().equals(LoginActivity.mId)) {//내 메세지라면
            Log.d("bkbk5515", "listViewItem.getID() + if " +listViewItem.getID());
            Log.d("bkbk5515", "LoginActivity.mId if " + LoginActivity.mId);

            holder.l_id.setVisibility(View.GONE);
            holder.l_msg.setVisibility(View.GONE);
            holder.r_id.setVisibility(View.VISIBLE);
            holder.r_msg.setVisibility(View.VISIBLE);

            holder.r_id.setText(listViewItem.getID());
            holder.r_msg.setText(listViewItem.getMSG());

        } else if(!listViewItem.getID().equals(LoginActivity.mId)) {// 내 메세지가 아니라면
            Log.d("bkbk5515", "listViewItem.getID() + else if " +listViewItem.getID());
            Log.d("bkbk5515", "LoginActivity.mId else if " + LoginActivity.mId);


            holder.r_id.setVisibility(View.GONE);
            holder.r_msg.setVisibility(View.GONE);
            holder.l_id.setVisibility(View.VISIBLE);
            holder.l_msg.setVisibility(View.VISIBLE);

            holder.l_id.setText(listViewItem.getID());
            holder.l_msg.setText(listViewItem.getMSG());

        }

        return convertView;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position);
    }

    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(String id, String msg) {
        ChattingItem item = new ChattingItem();
        item.setID(id);
        item.setMSG(msg);
        listViewItemList.add(item);
    }

    private class CustomHolder {
        TextView l_id;
        TextView l_msg;
        TextView r_id;
        TextView r_msg;
        RelativeLayout layout;
    }
}