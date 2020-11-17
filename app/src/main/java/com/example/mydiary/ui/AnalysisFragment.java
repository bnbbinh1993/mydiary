package com.example.mydiary.ui;
import android.graphics.Color;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.mydiary.R;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import java.util.ArrayList;
import java.util.List;

public class AnalysisFragment extends Fragment implements OnChartValueSelectedListener {
    private CombinedChart mChart;

    public static AnalysisFragment newInstance() {
        AnalysisFragment fragment = new AnalysisFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_analysis, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        mChart = (CombinedChart) view.findViewById(R.id.combinedChart);
        mChart.getDescription().setEnabled(false);
        mChart.setBackgroundColor(Color.WHITE);
        mChart.setDrawGridBackground(false);
        mChart.setDrawBarShadow(false);
        mChart.setHighlightFullBarEnabled(false);
        mChart.setOnChartValueSelectedListener(this);
        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setAxisMinimum(0f);
        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setAxisMinimum(0f);
        final List<String> xLabel = new ArrayList<>();
        for (int i = 1; i < 31; i++) {
            xLabel.add(String.valueOf(i));
        }
        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMinimum(0f);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return xLabel.get((int) value % xLabel.size());
            }
        });
        CombinedData data = new CombinedData();
        LineData lineDatas = new LineData();
        lineDatas.addDataSet((ILineDataSet) dataChart());
        data.setData(lineDatas);
        xAxis.setAxisMaximum(data.getXMax() + 0.25f);
        mChart.setData(data);
        mChart.invalidate();
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
//        Toast.makeText(getContext(), "Value: "
//                + e.getY()
//                + ", index: "
//                + h.getX()
//                + ", DataSet index: "
//                + h.getDataSetIndex(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected() {
    }

    private static DataSet dataChart() {
        LineData d = new LineData();
        int[] data = new int[]{30, 29, 28, 27, 26, 25, 24, 20, 30, 23, 35, 34, 32, 31, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30};
        ArrayList<Entry> entries = new ArrayList<Entry>();
        for (int index = 0; index < data.length; index++) {
            entries.add(new Entry(index, data[index]));
        }
        LineDataSet set = new LineDataSet(entries, "Theo dõi chỉ số cảm xúc");
        set.setColor(Color.GREEN);
        set.setLineWidth(2.5f);
        set.setCircleColor(Color.GREEN);
        set.setCircleRadius(3f);
        set.setFillColor(Color.GREEN);
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setDrawValues(true);
        set.setValueTextSize(9f);
        set.setValueTextColor(Color.RED);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        d.addDataSet(set);
        return set;
    }

}