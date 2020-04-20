//package android.medical.recyclerview;
//
//import android.content.Context;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import java.util.ArrayList;
//
//
///**
// * ViewHolder는 BaseAdapter에서와 달리 RecyclerView.Adapter에서는 강제로 만들어 져야 한다.
// * onCreateViewHolder을 통해서 생성되면, onBindViewHolder에서 해당 holder의 View에 데이터를 노출을 정의하면 된다.
// * RecyclerView는 ViewHolder을 재사용할 수 있도록 설계되어 있으므로, ViewType이 한번 생성된 이후로는 onBindViewHolder만 호출되게 된다.
// */
//public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
//
//    private ArrayList<String> itemList;
//    private Context context;
//    private View.OnClickListener onClickItem;
//
//    public MyAdapter(Context context, ArrayList<String> itemList, View.OnClickListener onClickItem) {
//        this.context = context;
//        this.itemList = itemList;
//        this.onClickItem = onClickItem;
//    }
//
//    @Override
//    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//
//        // context 와 parent.getContext() 는 같다.
//        View view = LayoutInflater.from(context).inflate(R.layout.item_tv, parent, false);
//
//        return new ViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(ViewHolder holder, int position) {
//        String item = itemList.get(position);
//
//        holder.textview.setText(item);
//        holder.textview.setTag(item);
//        holder.textview.setOnClickListener(onClickItem);
//    }
//
//    @Override
//    public int getItemCount() {
//        return itemList.size();
//    }
//
//    public class ViewHolder extends RecyclerView.ViewHolder {
//
//        public TextView textview;
//
//        public ViewHolder(View itemView) {
//            super(itemView);
//
//            textview = itemView.findViewById(R.id.item_textview);
//        }
//    }
//}