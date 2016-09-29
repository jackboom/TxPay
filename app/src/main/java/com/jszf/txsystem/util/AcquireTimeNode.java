package com.jszf.txsystem.util;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2016/4/19.
 */
public class AcquireTimeNode {
    /**
     * 获取到时间节点
     *
     * @param mTime
     * @return
     */
    public static String getTimePoint(int mTime) {
        Date date = new Date();//当前日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");//格式化对象
        Calendar calendar = Calendar.getInstance();//日历对象
        Calendar calendar1 = Calendar.getInstance();
        calendar.setTime(date);//设置当前日期
//        calendar.add(Calendar.MONTH, -1);//月份减一
        calendar.add(Calendar.DATE, -mTime);
        calendar1.add(Calendar.DAY_OF_MONTH, -mTime);
        int week = calendar1.get(Calendar.DAY_OF_WEEK);
        Date mDate = calendar.getTime();
        String beforeTimes = sdf.format(mDate);
//        long beforeTime = calendar.getTimeInMillis();
        return beforeTimes + "," + week;
    }

    public static double getCurrentTime() {
        Date date = new Date();//当前日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");//格式化对象
        String beforeTimes = sdf.format(date);
        return Double.parseDouble(beforeTimes);
    }
    public static String getCurrentTimeToString() {
        Date date = new Date();//当前日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//格式化对象
        String beforeTimes = sdf.format(date);
        return beforeTimes;
    }

    /**
     * 通过给出的月份获取时间数据
     *
     * @param month
     * @return
     */
    public static List<String> setTimeDataByMonth(String month) {
        Calendar calendar = Calendar.getInstance();//日历对象
        int currentMonth = calendar.get(Calendar.MONTH) + 1;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");//格式化对象
        List<String> list = new ArrayList<>();
        if (!month.equals(currentMonth + "")) {
            calendar.set(Calendar.MONTH, (Integer.parseInt(month) - 1));
            int day = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            calendar.set(Calendar.DAY_OF_MONTH, day);
            String endTime = sdf.format(calendar.getTime());
            int dayLength = 0;
            String startTime = "";
            if (day % 7 == 0) {
                dayLength = day;
                calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
                startTime = sdf.format(calendar.getTime());
                for (int i = 0; i < dayLength; i++) {
                    StringBuffer sb = new StringBuffer();
                    String str = (calendar.get(Calendar.MONTH)) + "";
                    if (str.length() == 1) {
                        sb.append("0");
                    }
                    sb.append(str);
                    StringBuffer sb2 = new StringBuffer();
                    if (i < 9) {
                        sb2.append("0" + (i + 1));
                    } else {
                        sb2.append((i + 1));
                    }
                    list.add(calendar.get(Calendar.YEAR) + "-" + sb.toString() + "-" + sb2.toString());
                }
            } else {
                int offset = 7 - day % 7;
                dayLength = day + offset;
                calendar.add(Calendar.MONTH, 0);
                int mmm = calendar.get(Calendar.MONTH);
                int day2 = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                calendar.set(Calendar.DAY_OF_MONTH, day2 - offset + 1);

                for (int i = 0; i < offset; i++) {
                    StringBuffer sb = new StringBuffer();
                    String str = (calendar.get(Calendar.MONTH)) + "";
                    if (str.length() == 1) {
                        sb.append("0");
                    }
                    sb.append(str);
                    StringBuffer sb2 = new StringBuffer();
                    int beforeDay = (calendar.get(Calendar.DAY_OF_MONTH) + i);
                    if (beforeDay < 9) {
                        sb2.append("0" + beforeDay);
                    } else {
                        sb2.append(beforeDay);
                    }
                    list.add(calendar.get(Calendar.YEAR) + "-" + sb.toString() + "-" +
                            sb2.toString());
                }
                for (int j = 0; j < day; j++) {
                    calendar.set(Calendar.MONTH, Integer.parseInt(month));
                    StringBuffer sb = new StringBuffer();
                    String str = (calendar.get(Calendar.MONTH)) + "";
                    if (str.length() == 1) {
                        sb.append("0");
                    }
                    sb.append(str);
                    StringBuffer sb2 = new StringBuffer();
                    if (j < 9) {
                        sb2.append("0" + (j + 1));
                    } else {
                        sb2.append((j + 1));
                    }
                    list.add(calendar.get(calendar.YEAR) + "-" + sb.toString() + "-" + sb2.toString());
                }
                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMddHHmmss");//格式化对象
                startTime = sdf.format(calendar.getTime());
            }
            return list;
        } else {
            return getTimeInitData();
        }
    }

    public static List<String> setBarTimeDataByMonth(String month) {
        Calendar calendar = Calendar.getInstance();//日历对象
        int currentMonth = calendar.get(Calendar.MONTH) + 1;
        Log.d("AcquireTimeNode", "currentMonth:" + currentMonth);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");//格式化对象
        List<String> list = new ArrayList<>();
        if (!month.equals(currentMonth + "")) {
            calendar.set(Calendar.MONTH, (Integer.parseInt(month)));
            int day = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            Log.d("AcquireTimeNode", "day:" + day);
            calendar.set(Calendar.DAY_OF_MONTH, day);
            Log.d("AcquireTimeNode", "calendar.get(Calendar.MONTH):" + calendar.get(Calendar.MONTH));
            String endTime = sdf.format(calendar.getTime());
            int dayLength = 0;
            String startTime = "";
            dayLength = day;
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
            startTime = sdf.format(calendar.getTime());
            for (int i = 0; i < dayLength; i++) {
                StringBuffer sb = new StringBuffer();
                String str = (calendar.get(Calendar.MONTH)) + "";
                if (str.length() == 1) {
                    sb.append("0");
                }
                sb.append(str);
                StringBuffer sb2 = new StringBuffer();
                if (i < 9) {
                    sb2.append("0" + (i + 1));
                } else {
                    sb2.append((i + 1));
                }
                list.add(calendar.get(Calendar.YEAR) + "-" + sb.toString() + "-" + sb2.toString());
            }
            return list;
        } else {
            return getBarTimeInitData();
        }
    }

    /**
     * 获取初始化情况的时间数据
     *
     * @return
     */
    public static List<String> getTimeInitData() {
        Date date = new Date();//当前日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");//格式化对象
        Calendar calendar = Calendar.getInstance();//日历对象
        calendar.setTime(date);//设置当前日期
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        Date mDate = calendar.getTime();
        String endTime = sdf.format(mDate);
        HashMap<String, String> map = new HashMap<>();
        List<String> list = new ArrayList<>();
        map.put("endTime", endTime);
        int dayLength = 0;
        String startTime = "";
        if (day % 7 == 0) {
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
            startTime = sdf.format(calendar.getTime());
            dayLength = day;
            for (int i = 0; i < dayLength; i++) {
                StringBuffer sb = new StringBuffer();
                String str = (calendar.get(Calendar.MONTH) + 1) + "";
                if (str.length() == 1) {
                    sb.append("0");
                }
                sb.append(str);
                StringBuffer sb2 = new StringBuffer();
                if (i < 9) {
                    sb2.append("0" + (i + 1));
                } else {
                    sb2.append((i + 1));
                }
                list.add(calendar.get(Calendar.YEAR) + "-" + (sb.toString()) +
                        "-" + sb2.toString());
            }
        } else {
            int offset = 7 - day % 7;
            calendar.add(Calendar.MONTH, -1);
            int day2 = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            calendar.set(Calendar.DAY_OF_MONTH, day2 - offset + 1);
            for (int i = 0; i < offset; i++) {
                StringBuffer sb = new StringBuffer();
                String str = (calendar.get(Calendar.MONTH) + 1) + "";
                if (str.length() == 1) {
                    sb.append("0");
                }
                sb.append(str);
                StringBuffer sb2 = new StringBuffer();
                int beforeDay = (calendar.get(Calendar.DAY_OF_MONTH) + i);
                if (beforeDay < 9) {
                    sb2.append("0" + beforeDay);
                }
                sb2.append(beforeDay);
                list.add(calendar.get(Calendar.YEAR) + "-" + sb.toString() + "-" +
                        sb2.toString());
            }
            for (int j = 0; j < day; j++) {
                calendar.setTime(mDate);
                StringBuffer sb = new StringBuffer();
                String str = (calendar.get(Calendar.MONTH) + 1) + "";
                if (str.length() == 1) {
                    sb.append("0");
                }
                sb.append(str);
                StringBuffer sb2 = new StringBuffer();
                if (j < 9) {
                    sb2.append("0" + (j + 1));
                } else {
                    sb2.append((j + 1));
                }
                list.add(calendar.get(calendar.YEAR) + "-" + sb.toString() + "-" + (sb2.toString()));
            }
        }
        return list;
    }

    public static List<String> getBarTimeInitData() {
        Date date = new Date();//当前日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");//格式化对象
        Calendar calendar = Calendar.getInstance();//日历对象
        calendar.setTime(date);//设置当前日期
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        Date mDate = calendar.getTime();
        String endTime = sdf.format(mDate);
        HashMap<String, String> map = new HashMap<>();
        List<String> list = new ArrayList<>();
        map.put("endTime", endTime);
        int dayLength = 0;
        String startTime = "";
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        startTime = sdf.format(calendar.getTime());
        dayLength = day;
        for (int i = 0; i < dayLength; i++) {
            StringBuffer sb = new StringBuffer();
            String str = (calendar.get(Calendar.MONTH) + 1) + "";
            if (str.length() == 1) {
                sb.append("0");
            }
            sb.append(str);
            StringBuffer sb2 = new StringBuffer();
            if (i < 9) {
                sb2.append("0" + (i + 1));
            } else {
                sb2.append((i + 1));
            }
            list.add(calendar.get(Calendar.YEAR) + "-" + (sb.toString()) +
                    "-" + sb2.toString());
        }
        return list;
    }

    public static long getTimeLong(int mTime) {
        Calendar calendar = Calendar.getInstance();//日历对象
//        calendar.add(Calendar.MONTH, -1);//月份减一
        calendar.add(Calendar.DATE, -mTime);
        long mDate = calendar.getTimeInMillis();
        return mDate;
    }

    public static String getTimeDate(String time) {
        time.substring(8, 10);
        return time;
    }
}
