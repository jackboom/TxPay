package com.jszf.txsystem.util;

import android.content.Context;
import android.graphics.Color;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ValueFormatter;
import com.jszf.txsystem.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2016/7/27.
 */
public class LineChartUtils2 {
    private static List<Float> dataList;

    public static void showChart(Context mContext,LineChart lineChart, List<Float> list, int color) {
        lineChart.setDrawBorders(false); //在折线图上添加边框
        lineChart.setDescription(""); //数据描述
        lineChart.setNoDataTextDescription("You need to provide data for the chart.");

        lineChart.setDrawGridBackground(false); //表格颜色
        lineChart.setGridBackgroundColor(Color.WHITE & 0x70FFFFFF); //表格的颜色，设置一个透明度

        lineChart.setTouchEnabled(false); //可点击

        lineChart.setDragEnabled(true);  //可拖拽
        lineChart.setScaleEnabled(false);  //可缩放

        lineChart.setPinchZoom(false);

//        lineChart.setBackgroundColor(color); //设置背景颜色
        lineChart.setDrawGridBackground(false);

        Legend mLegend = lineChart.getLegend(); //设置标示，就是那个一组y的value的

        mLegend.setForm(Legend.LegendForm.CIRCLE); //样式
        mLegend.setEnabled(false);
        mLegend.setFormSize(6f); //字体
        mLegend.setTextColor(Color.WHITE); //颜色
        float max = Collections.max(list);
        lineChart.setTranslationY(-60);
//        lineChart.setTranslationX(10);
//        lineChart.offsetLeftAndRight(10);
//        lineChart.setVisibleXRange(6);   //x轴可显示的坐标范围
//        lineChart.setMaxVisibleValueCount(7);
        XAxis xAxis = lineChart.getXAxis();  //x轴的标示
        xAxis.setEnabled(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); //x轴位置
        xAxis.setTextColor(Color.WHITE);    //字体的颜色
        xAxis.setTextSize(10f); //字体大小
        xAxis.setGridColor(Color.WHITE);//网格线颜色
        xAxis.setDrawGridLines(true); //不显示网格线

        YAxis axisLeft = lineChart.getAxisLeft(); //y轴左边标示
        axisLeft.setEnabled(false);
        axisLeft.setAxisMaxValue(max * 5 / 4);
        YAxis axisRight = lineChart.getAxisRight(); //y轴右边标示
        axisRight.setEnabled(false);
        axisLeft.setTextColor(Color.WHITE); //字体颜色
        axisLeft.setTextSize(10f); //字体大小
//        axisLeft.setAxisMaxValue(1000f); //最大值

//        axisLeft.setAxisMaxValue(max *3/2); //最大值
//        axisLeft.setAxisMinValue(-max/10);   //最小值
//        axisLeft.setAxisMinValue(-max/3);
        axisLeft.setGridColor(Color.WHITE); //网格线颜色

        axisRight.setDrawAxisLine(false);
        axisRight.setDrawGridLines(false);
        axisRight.setDrawLabels(false);

        // 加载数据
        LineData data = getLineData(mContext, list.size(), list);
        lineChart.setData(data);
//        lineChart.invalidate();//填充数据

//        lineChart.animateX(500);
        lineChart.animateY(100);   //从Y轴进入的动画
        lineChart.animateXY(100, 100);    //从XY轴一起进入的动画
        lineChart.animateX(100);  //立即执行动画

        lineChart.invalidate();
    }

    public static LineData getLineData(Context mContext, int count, List<Float> list) {
        float max = Collections.max(list);
        ArrayList xValues = new ArrayList();
        for (int i = 0; i < list.size(); i++) {
            // x轴显示的数据，这里默认使用数字下标显示
            xValues.add("" + list.get(i));
        }

        // y轴的数据
        ArrayList yValues = new ArrayList();
        for (int i = 0; i < list.size(); i++) {
//            float value = (int) (Math.random() * 100);
            float value = list.get(i);
            yValues.add(new Entry(value, i));
        }

        // y轴的数据集合
//        yValues.add(new Entry(max/10,0));
//        yValues.add(new Entry(max/10,list.size()-1));
        LineDataSet lineDataSet = new LineDataSet(yValues, "");
        lineDataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float v) {
                DecimalFormat df = new DecimalFormat("0.00");
                String value = df.format(v);
                return value;
            }
        });
//        ArrayList lineDataSets = new ArrayList();
//        lineDataSets.add(lineDataSet); // 添加数据集合

        //用y轴的集合来设置参数
        lineDataSet.setDrawCubic(false);  //设置曲线为圆滑的线
        lineDataSet.setCubicIntensity(0.2f);
        lineDataSet.setLineWidth(3f); // 线宽
        lineDataSet.setCircleSize(5f);// 显示的圆形大

        lineDataSet.setFillAlpha(20);
        lineDataSet.setDrawFilled(true);
        lineDataSet.setFillColor(mContext.getResources().getColor(R.color.statusBar_bg));
        lineDataSet.setColor(mContext.getResources().getColor(R.color.analyse_text_paytype));// 显示颜色
        lineDataSet.setCircleColor(mContext.getResources().getColor(R.color.analyse_text_paytype));// 圆形的颜色
        lineDataSet.setDrawCircleHole(false);   //实心圆
        lineDataSet.setHighLightColor(mContext.getResources().getColor(R.color.analyse_text_paytype)); // 高亮的线的颜色
        lineDataSet.setValueTextColor(mContext.getResources().getColor(R.color.analyse_text_paytype)); //数值显示的颜色
        lineDataSet.setValueTextSize(12f);     //数值显示的大小



        //创建lineData
        LineData lineData = new LineData(xValues, lineDataSet);

        return lineData;
    }
}
