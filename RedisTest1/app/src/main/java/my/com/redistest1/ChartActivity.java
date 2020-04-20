package my.com.redistest1;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class ChartActivity  extends AppCompatActivity {
    PieChart pieChart;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chart);

        pieChart = (PieChart) findViewById(R.id.piechart);

        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5,10,5,5);

        pieChart.setDragDecelerationFrictionCoef(0.95f);

        //false로 바꾸면 가운데 흰 공백 없이 꽉참
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleRadius(61f);
        ArrayList<PieEntry> values = new ArrayList<>();

        values.add(new PieEntry(50f, "남자"));
        values.add(new PieEntry(10f, "여자"));
        values.add(new PieEntry(10f, "중성"));
        values.add(new PieEntry(10, "남자"));
        values.add(new PieEntry(10, "여자"));
        values.add(new PieEntry(10, "거세"));

        pieChart.animateY(1000, Easing.EasingOption.EaseInOutCubic);

        //하단 색상별 구분 안내 우측에 멘트 달 수 있음
        PieDataSet pieDataSet = new PieDataSet(values, "");
        //1f로 바꾸면
        pieDataSet.setSliceSpace(3f);
        pieDataSet.setSelectionShift(5f);
        pieDataSet.setColors(ColorTemplate.JOYFUL_COLORS);

        PieData data = new PieData((pieDataSet));
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.YELLOW);

        pieChart.setData(data);
    }
}
