package com.ling.kotlin.wallet.utils;

import android.content.Context;
import androidx.core.content.ContextCompat;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.ling.kotlin.R;
import com.ling.kotlin.wallet.bean.ChartEntity;
import com.ling.kotlin.wallet.bean.CurrentMonth;
import com.ling.kotlin.wallet.bean.LastMonth;

import java.util.ArrayList;
import java.util.List;

public class ChartManager {
    private static String lineName = null;
    private static String lineName1 = null;

    public static void initDataStyle(float axisMaximum, float axisMinimum, LineChart chart) {

        chart.getDescription().setEnabled(false);
        chart.setAutoScaleMinMaxEnabled(false);
        //设置触控手势
        chart.setTouchEnabled(false);
        //设置缩放
        chart.setDragEnabled(false);
        //设置推动
        chart.setScaleEnabled(false);
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        YAxis yAxis = chart.getAxisLeft();
        chart.getAxisRight().setEnabled(false);
        yAxis.setEnabled(false);
        // axis range
        yAxis.setAxisMaximum(axisMaximum);
        yAxis.setAxisMinimum(axisMinimum);
        chart.animateY(1500,Easing.EaseInOutCubic);
        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setForm(Legend.LegendForm.LINE);
    }


    public static void setChartData(Context context, LineChart chart, final ChartEntity bean) {
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(getLastMonthsLineDataSet(context, bean)); // add the data sets
        dataSets.add(getCurrentMonthsLineDataSet(context, bean)); // add the data sets
        LineData data = new LineData(dataSets);
        ValueFormatter valueFormatter = new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return bean.getCurrentMonths().get((int) value).getDate();
            }
        };
        chart.getXAxis().setTextColor(ContextCompat.getColor(context, R.color.app_text_three_normal_color));
        chart.getXAxis().setValueFormatter(valueFormatter);
        chart.setData(data);
    }


    private static LineDataSet getLastMonthsLineDataSet(Context context, ChartEntity bean) {
        ArrayList<Entry> values = new ArrayList<>();
        List<LastMonth> lastMoths = bean.getLastMonths();

        for (int i = 0; i < lastMoths.size(); i++) {
            LastMonth lastMonth = lastMoths.get(i);
            values.add(new Entry(i, lastMonth.getLabel()));
        }

        LineDataSet set1 = new LineDataSet(values, lineName);
        set1.setColor(ContextCompat.getColor(context, R.color.app_b3bfc8_color));
        set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set1.setDrawCircles(false);
        set1.setDrawValues(false);
        set1.setLineWidth(1.5f);
        return set1;
    }

    private static LineDataSet getCurrentMonthsLineDataSet(Context context, ChartEntity bean) {
        ArrayList<Entry> values = new ArrayList<>();
        List<CurrentMonth> lastMoths = bean.getCurrentMonths();
        float total = 0.0f;
        for (int i = 0; i < lastMoths.size(); i++) {
            CurrentMonth currentMonth = lastMoths.get(i);
            float label = currentMonth.getLabel();
            total = total + label;
            values.add(new Entry(i, currentMonth.getLabel()));
        }
        bean.setTotal(total);
        LineDataSet set1 = new LineDataSet(values, lineName1);
        set1.setColor(ContextCompat.getColor(context, R.color.app_218cdc_color));
        set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set1.setValueTextSize(10f);
        set1.setDrawCircles(false);
        set1.setDrawValues(false);
        set1.setLineWidth(1.5f);
        return set1;
    }

    /**
     * @param name
     * @Description:设置折线的名称
     */
    public static void setLineName(String name) {
        lineName = name;
    }

    /**
     * @param name
     * @Description:设置另一条折线的名称
     */
    public static void setLineName1(String name) {
        lineName1 = name;
    }
}
