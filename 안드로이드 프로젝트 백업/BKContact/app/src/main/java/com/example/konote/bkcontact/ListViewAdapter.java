package com.example.konote.bkcontact;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class ListViewAdapter extends BaseAdapter implements Filterable {

    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    public ArrayList<ListViewItem> listViewItemList = new ArrayList<ListViewItem>();

    // 필터링된 결과 데이터를 저장하기 위한 ArrayList. 최초에는 전체 리스트 보유.
    private ArrayList<ListViewItem> filteredItemList = listViewItemList;

    Filter listFilter;

    // ListViewAdapter의 생성자
    public ListViewAdapter() {
    }

    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return filteredItemList.size();
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        ImageView iconImageView = (ImageView) convertView.findViewById(R.id.imageView1);
        TextView titleTextView = (TextView) convertView.findViewById(R.id.textView1);
        TextView descTextView = (TextView) convertView.findViewById(R.id.textView2);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        ListViewItem listViewItem = filteredItemList.get(position);

//        Uri uri = Uri.parse(listViewItem.getIcon());
//        File imgfile = new File(uri.toString());
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        Bitmap bm= BitmapFactory.decodeFile(imgfile.getPath(),options);
//        options.inSampleSize = 1;
//
//        Log.d("realPath2", uri.toString());

        // 아이템 내 각 위젯에 데이터 반영
        iconImageView.setImageBitmap(listViewItem.getIcon());
        titleTextView.setText(listViewItem.getName());
        descTextView.setText(listViewItem.getNumber1());

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
        return filteredItemList.get(position);
    }

    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(Bitmap icon, String name, String number1, String number2, String mail, String memo) {
        ListViewItem item = new ListViewItem();

        item.setIcon(icon);
        item.setName(name);
        item.setNumber1(number1);
        item.setNumber2(number2);
        item.setMail(mail);
        item.setMemo(memo);

        listViewItemList.add(item);
    }

    @Override
    public Filter getFilter() {
        if (listFilter == null) {
            listFilter = new ListFilter();
        }
        return listFilter;
    }


    private class ListFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (constraint == null || constraint.length() == 0) { // 만약 입력된게 단 하나도 없다면 그냥 원래있던 리스트를 다 보여줌
                results.values = listViewItemList;
                results.count = listViewItemList.size();
            } else { // 그게 아니라면 검색된것만 보여줌
                ArrayList<ListViewItem> itemList = new ArrayList<ListViewItem>();
                for (ListViewItem item : listViewItemList) {
//                    if (item.getName().toUpperCase().contains(constraint.toString().toUpperCase()) || item.getNumber1().toUpperCase().contains(constraint.toString().toUpperCase())) {
                    if (item.getName().contains(constraint) || item.getNumber1().contains(constraint)) {
                        itemList.add(item);
                    }
                }
                results.values = itemList;
                results.count = itemList.size();
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            filteredItemList = (ArrayList<ListViewItem>) filterResults.values;
            if (filterResults.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }
}
