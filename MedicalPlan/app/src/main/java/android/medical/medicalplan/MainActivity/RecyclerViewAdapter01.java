package android.medical.medicalplan.MainActivity;

import android.medical.medicalplan.R;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class RecyclerViewAdapter01 extends RecyclerView.Adapter<RecyclerViewAdapter01.CustomViewHolder> {

    private ArrayList<RecyclerViewItem01> mList;

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView id;
        protected TextView english;
        protected TextView korean;

        public CustomViewHolder(View view) {
            super(view);
            this.id = (TextView) view.findViewById(R.id.name);
            this.english = (TextView) view.findViewById(R.id.address);
            this.korean = (TextView) view.findViewById(R.id.time);
        }
    }

    public RecyclerViewAdapter01(ArrayList<RecyclerViewItem01> list) {
        this.mList = list;
    }

    // RecyclerView에 새로운 데이터를 보여주기 위해 필요한 ViewHolder를 생성해야 할 때 호출됩니다.
    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list, null);
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_main_recyclerview01, viewGroup, false);

        CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;
    }

    // Adapter의 특정 위치(position)에 있는 데이터를 보여줘야 할때 호출됩니다.
    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder viewholder, int position) {

        viewholder.id.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        viewholder.english.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        viewholder.korean.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

        viewholder.id.setGravity(Gravity.CENTER);
        viewholder.english.setGravity(Gravity.CENTER);
        viewholder.korean.setGravity(Gravity.CENTER);

        viewholder.id.setText(mList.get(position).getId());
        viewholder.english.setText(mList.get(position).getEnglish());
        viewholder.korean.setText(mList.get(position).getKorean());
    }

    @Override
    public int getItemCount() {
        return (null != mList ? mList.size() : 0);
    }

}