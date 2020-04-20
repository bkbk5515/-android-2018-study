package my.com.kakaoblog;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;


public class KakaoCafeListViewAdapter extends BaseAdapter {

    Context context_activity;

    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    public ArrayList<KakaoCafeListViewItem> listViewItemList = new ArrayList<KakaoCafeListViewItem>();

    // ListViewAdapter의 생성자
    public KakaoCafeListViewAdapter(Context context) {
        this.context_activity = context;
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

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.custom_listview_item, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        ImageView imageview = (ImageView) convertView.findViewById(R.id.imageView1);
        TextView titleTextView = (TextView) convertView.findViewById(R.id.textView1);
        TextView descTextView = (TextView) convertView.findViewById(R.id.textView2);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        KakaoCafeListViewItem listViewItem = listViewItemList.get(position);


        // 아이템 내 각 위젯에 데이터 반영
        Picasso.with(context_activity)//원래 this였음.
                //https://icon2.kisspng.com/20180623/gil/kisspng-lg-g4-android-marshmallow-android-nougat-android-o-this-nox-5b2ee3d31564b2.7066279915297996350876.jpg
                .load(listViewItem.getThumbnail().toString())
                //.transform(PicassoTransformations.resizeTransformation)
                .resize(100, 100)
//                .load(listViewItem.getThumbnail())
                .placeholder(R.drawable.androidpng)
                .error(R.drawable.androidpng)
                .into(imageview);
        titleTextView.setText(listViewItem.getTitle());
        descTextView.setText(listViewItem.getContents());


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
    public void addItem(String thumbnail, String url, String title, String contents) {
        KakaoCafeListViewItem item = new KakaoCafeListViewItem();

        //live일때 live이미지
        item.setThumbnail(thumbnail);
        item.setUrl(url);
        item.setTitle(title);
        item.setContents(contents);

        listViewItemList.add(item);

    }
//    public class PicassoTransformations {
//
//        public int targetWidth = 200;
//
//        public Transformation resizeTransformation = new Transformation() {
//            @Override
//            public Bitmap transform(Bitmap source) {
//                double aspectRatio = (double) source.getHeight() / (double) source.getWidth();
//                int targetHeight = (int) (targetWidth * aspectRatio);
//                Bitmap result = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false);
//                if (result != source) {
//                    source.recycle();
//                }
//                return result;
//            }
//
//            @Override
//            public String key() {
//                return "resizeTransformation#" + System.currentTimeMillis();
//            }
//        };
//    }//  출처: http://dwfox.tistory.com/33 [DWFOX]


}