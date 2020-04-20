package android.medical.recyclerview;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;


//기본 아이템은 공백이 없는데 마지막이 아닌 경우 공백을 30을 준다. (px값이니까 나중에는 dp값 계산해야겠지)
public class MyListDecoration extends RecyclerView.ItemDecoration {



    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        if (parent.getChildAdapterPosition(view) != parent.getAdapter().getItemCount() - 1) {
//            outRect.top = 30;
//            outRect.bottom = 30;
//            outRect.left = 30;
            outRect.right = 30;
        }
    }
}