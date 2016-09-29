package com.jszf.txsystem.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.jszf.txsystem.MyApplication;
import com.jszf.txsystem.bean.ShiftBean;
import com.jszf.txsystem.service_receiver.BluetoothService;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2016/8/12.
 */
public class PrintUtils {

    private static Handler mHandler = new Handler();

    /**
     * 打印
     *
     * @param message
     */
    public static void sendMessage(Context mContext, String message) {
        // Check that we're actually connected before trying anything
        if (MyApplication.mService.getState() != BluetoothService.STATE_CONNECTED) {
            Toast.makeText(mContext, "蓝牙没有连接", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check that there's actually something to send
        if (message.length() > 0) {
            // Get the message bytes and tell the BluetoothService to write
            byte[] send;
            try {
                send = message.getBytes("GB2312");
            } catch (UnsupportedEncodingException e) {
                send = message.getBytes();
            }

            MyApplication.mService.write(send);

        }
    }


    public static void sendMessage(Context mContext, Bitmap bitmap) {
        // Check that we're actually connected before trying anything
        if (MyApplication.mService.getState() != BluetoothService.STATE_CONNECTED) {
            Toast.makeText(mContext, "蓝牙没有连接", Toast.LENGTH_SHORT).show();
            return;
        }
		// 发送打印图片前导指令
		byte[] start = { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x1B,
				0x40, 0x1B, 0x33, 0x00 };
        MyApplication.mService.write(start);

        /**获取打印图片的数据**/
//		byte[] send = getReadBitMapBytes(bitmap);

        MyApplication.mService.printCenter();
        byte[] draw2PxPoint = PrintPic.draw2PxPointPic(bitmap);

        MyApplication.mService.write(draw2PxPoint);
        // 发送结束指令
        byte[] end = {0x1d, 0x4c, 0x1f, 0x00};
        MyApplication.mService.write(end);
    }


    public static void printContentText(final Context mContext, final HashMap<String, String> map) {
        Log.d("PrintUtils", "打印");
        int timeSpace = getSharePrintData(mContext).getInt("time", 0);
        int printType = getSharePrintData(mContext).getInt("print_type", 0);
//        String outlet  = getSharePrintData(mContext).getString("outlet","");
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        final String printdate = sDateFormat.format(new Date());    //收银时间
        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
        switch (printType) {
            case 1:
                if (printCustomer(mContext, map, printdate)) return;
                break;
            case 2:
                printFinance(mContext, map, printdate);
                break;
            case 3:
                if (printCustomer(mContext, map, printdate)) return;
                mHandler.postDelayed(() -> printFinance(mContext, map, printdate), timeSpace * 1000);

                break;
        }

    }

    public static void printShitf(Context context,ShiftBean shiftBean){
//        if (judgeBluetoothState(context)) return;
        final StringBuffer shiftBuffer = new StringBuffer();
        ShiftBean.ShiftModel shiftModel = shiftBean.get_ShiftModel();
        List<ShiftBean.ShiftModel.Infos> infos = shiftModel.getInfos();
        Log.d("ShiftActivity", "infos.size():" + infos.size());
        ArrayList<String> contentList = new ArrayList<>();
        String str1 = shiftModel.getEndTime();
        String str2 = shiftModel.getStartTime();
        String preString1 = str1.substring(6, str1.length() - 2);
        String preString2 = str2.substring(6, str2.length() - 2);
        String shiftTime = timeStampDate(preString1, "yyyy/MM/dd HH:mm:ss");
        Log.d("BluetoothService", "--->"+str1+","+str2);
        Log.d("BluetoothService", "1-------->"+shiftTime);
        String startTime = timeStampDate(preString2, "yyyy/MM/dd HH:mm:ss");
        Log.d("BluetoothService", "2-------->"+startTime);
        contentList.add(shiftTime);
        contentList.add(startTime);
        contentList.add(shiftModel.getUserName());
        for (ShiftBean.ShiftModel.Infos info : infos) {
            contentList.add(info.getValue() + "");
        }
//        shiftBuffer.append("\n\n\n");
        shiftBuffer.append("        交班扎帐信息      \n");
        shiftBuffer.append("交班时间:" + contentList.get(0)+"\n");
        shiftBuffer.append("开始时间:" +contentList.get(1)+"\n");
        shiftBuffer.append("交班人:" + contentList.get(2) +"\n");
        shiftBuffer.append("订单金额:¥" + contentList.get(3)+ "\n");
        shiftBuffer.append("订单数目:" + (int)Double.parseDouble(contentList.get(4))+"\n");
        shiftBuffer.append("成功金额:¥" + contentList.get(5)+"\n");
        shiftBuffer.append("成功笔数:" + (int)Double.parseDouble(contentList.get(6))+"\n");
        shiftBuffer.append("失败金额:¥" + contentList.get(7)+"\n");
        shiftBuffer.append("失败笔数:" + (int)Double.parseDouble(contentList.get(8))+"\n");
        shiftBuffer.append("待核实金额:¥" + contentList.get(9)+"\n");
        shiftBuffer.append("待核实笔数:" + (int)Double.parseDouble(contentList.get(10))+"\n");
        shiftBuffer.append("退款失败金额:¥" + contentList.get(11)+"\n");
        shiftBuffer.append("退款失败笔数:" + (int)Double.parseDouble(contentList.get(12))+"\n");
        shiftBuffer.append("退款中金额:¥" + contentList.get(13)+"\n");
        shiftBuffer.append("退款中笔数:" + (int)Double.parseDouble(contentList.get(14))+"\n");
        shiftBuffer.append("已退款金额:¥" + contentList.get(15)+"\n");
        shiftBuffer.append("已退款笔数:" + (int)Double.parseDouble(contentList.get(16))+"\n");
        shiftBuffer.append("--------------------------------\n\n\n\n\n");
        byte[] mByte = new byte[1024];
        try {
            mByte = shiftBuffer.toString().getBytes("UTF-8");
            MyApplication.mService.write(mByte);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        Log.d("PrintUtils", "打印数据："+shiftBuffer.toString());

    }

    /**
     * 时间戳转换成日期格式字符串
     * @param seconds 精确到秒的字符串
     * @param format
     * @return
     */
    public static String timeStampDate(String seconds,String format) {
        if(seconds == null || seconds.isEmpty() || seconds.equals("null")){
            return "";
        }
        if(format == null || format.isEmpty()) format = "yyyy/MM/dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(Long.valueOf(seconds)));
    }
    private static void printFinance(Context mContext, HashMap<String, String> map, String mPrintdate) {
        if (judgeBluetoothState(mContext)) return;
        final StringBuffer sb2 = new StringBuffer();
        sb2.append("-------------财务联-------------\n");
        sb2.append("收银时间    " + mPrintdate + "\n");
        sb2.append("流水单号    " + map.get("orderNo") + "\n");
        sb2.append("支付金额    " + map.get("orderAmount") + "元\n");
        sb2.append("付款方式    " + map.get("payType") + "\n");
        sb2.append("收款单位    " + map.get("outlet") + "\n");
        sb2.append("--------------------------------\n\n\n\n\n");
        byte[] mByte = new byte[1024];
        try {
            mByte = sb2.toString().getBytes("GBK");
            MyApplication.mService.write(mByte);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean judgeBluetoothState(Context mContext) {
        if (MyApplication.getInstance().mBluetoothAdapter == null) {
            Toast.makeText(mContext, "未开启蓝牙", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            if (MyApplication.getInstance().mService == null) {
                return true;
            }else {
                if (MyApplication.getInstance().mService.getState() != BluetoothService.STATE_CONNECTED) {
                    Toast.makeText(mContext, "未连接打印机蓝牙", Toast.LENGTH_SHORT).show();
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean printCustomer(Context mContext, HashMap<String, String> map, String mPrintdate) {
        if (MyApplication.getInstance().mService == null) {
            Toast.makeText(mContext, "未开启蓝牙", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (MyApplication.getInstance().mService.getState() != BluetoothService.STATE_CONNECTED) {
            Toast.makeText(mContext, "未连接打印机蓝牙", Toast.LENGTH_SHORT).show();
            return true;
        }
        final StringBuffer sb = new StringBuffer();
        final String amount = "132.00"; //金额
        final String payMethod = "支付宝支付";    //支付方式
        final String outlet = "高新金融城店";    //门店
        sb.append("-------------顾客联-------------\n");
        sb.append("收银时间     " + mPrintdate + "\n");
        sb.append("订单编号     " + map.get("orderNo") + "\n");
        sb.append("支付金额     " + map.get("orderAmount") + "元\n");
        sb.append("付款方式     " + map.get("payType") + "\n");
        sb.append("收款单位     " + map.get("outlet") + "\n");
        sb.append("--------------------------------");
        sb.append("             谢谢惠顾,欢迎下次光临!\n\n\n\n\n");
        byte[] mBytes = new byte[1024];
        try {
            mBytes = sb.toString().getBytes("GBK");
            MyApplication.mService.write(mBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

//	---------------顾客联-------------
//	收银时间:{收银时间}
//	订单号:{流水号}
//	{条形码}
//	金额:{金额}
//	付款方式:{付款方式}
//	门店:{门店}
//	{二维码}
//	------------------------------------
//	{分页}
//	--------------财务联-------------
//
//	收银时间:{收银时间}
//	流水号:{流水号}
//	金额:{金额}
//	付款方式:{付款方式}
//	门店:{门店}
//	------------------------------------


//		printer.escWHEnlarge(PrinterEscCmd.ENLARGE_MODE.ENLARGE_1.getValue());//设置字体放大一倍
//		printer.escAlign(PrinterEscCmd.ALIGN_MODE.ALIGN_Mid.getValue());//设置对齐方式:居中
//		printer.escPrintText("--------------财务联-------------");//执行水平制表，定位第一个位置
//		printer.escPrintText("\n\n");
//
//		printer.escWHEnlarge(PrinterEscCmd.ENLARGE_MODE.ENLARGE_N.getValue());//设置字体不放大
//		printer.escBold(false);//设置字体不加粗
//		printer.escPrintText("流水号:" + tradeNo);
//		printer.escPrintText("\n");
//
//		printer.escWHEnlarge(PrinterEscCmd.ENLARGE_MODE.ENLARGE_N.getValue());//设置字体不放大
//		printer.escPrintText("金额:" + amount);
//		printer.escWHEnlarge(PrinterEscCmd.ENLARGE_MODE.ENLARGE_N.getValue());//设置字体不放大
//
//		printer.escPrintText("付款方式:" + payMethod);
//
//		printer.escPrintText("门店:" + outlet);
//		printer.escPrintText("- - - - - - - - - - - - - - - - \n");


//		if (cb_cutpaper.isChecked()) {
//			printer.escCuter(PrinterEscCmd.Cuter_TYPE.FullCut.getValue(), (byte) 10);//切纸
//		}

    /**
     * 打印条形码
     *
     * @param content 条码的内容
     */
    public static void print1DCodeBy2(String content) {
        byte[] bytes = content.getBytes();
        byte[] cmd = new byte[bytes.length + 4];
        // 打印条码的指令
        cmd[0] = 0x1D;// 29	//起始
        cmd[1] = 0x6B;// 107
        cmd[2] = 73;// 条码的类型 code39:69 code128:73 具体参考文档
        cmd[3] = (byte) bytes.length;// 条码数据的字节数
        for (int i = 0; i < bytes.length; i++) {
            cmd[4 + i] = bytes[i];
        }

        initPrinter();// 一定要初始化，不然条码打不出来

        // 设置对齐方式
        byte[] alignByte = {0x1B, 0x61, 1};// 0：左对齐；1：居中；2：右对齐
        MyApplication.mService.write(alignByte);

        // 设置条码的高度(注：高度与宽度要么全设置，要么全不设置)
        byte[] setCodeHeigthByte = {0x1D, 0x68, (byte) 100};
        MyApplication.mService.write(setCodeHeigthByte);
        // 设置条码的宽度
        byte[] setCodeWidthByte = {0x1D, 0x77, 50};
        MyApplication.mService.write(setCodeWidthByte);

//		// 设置字符打印在条码下方
//		byte[] codeStrByte = { 0x1D, 0x48, 2 };
//		mBluetoothService.write(codeStrByte);

        // 打印条码
        MyApplication.mService.write(cmd);

        // 设置换行
        byte[] LF = {0x0A, 0x0A, 0x0A, 0x0A};// 四行
        MyApplication.mService.write(LF);
    }

    /**
     * 初始化打印机
     */
    private static void initPrinter() {
        byte[] initPrinter = {0x1B, 0x40};// 初始化
        MyApplication.mService.write(initPrinter);
    }

    public static void printQRcode(Bitmap bitmap){
        byte[] mBytes = new byte[5*1024*1024];
        mBytes= getByteByBitmap(bitmap);
        MyApplication.mService.write(mBytes);

    }

    /**
     * 获取位图的字节数组
     *
     * @param bitmap
     * @return
     */
    private static byte[] getByteByBitmap(Bitmap bitmap) {
        PrintPic printPic = PrintPic.getInstance();
        printPic.initCanvas(800);
        printPic.initPaint();
        printPic.drawImage(0, 0, bitmap);
        return printPic.printDraw();
    }

    public static boolean isOpenDevice() {
        if (MyApplication.mBluetoothAdapter != null) {
            boolean isOpenDevice = MyApplication.mBluetoothAdapter.isEnabled();
            return isOpenDevice;
        }
        return false;
    }

    public static void CodePrint128(String data) {
        byte m = 73;
        byte mIndex = 0;
        byte[] cmd = new byte[1024];
        int var6 = mIndex + 1;
        cmd[mIndex] = 29;
        cmd[var6++] = 107;
        cmd[var6++] = (byte) m;
        cmd[var6++] = (byte) data.length();

        for (int i = 0; i < data.length(); ++i) {
            cmd[var6++] = (byte) data.charAt(i);
        }
        try {
            MyApplication.mService.write(cmd);
        } catch (Exception e) {
            Log.d("BluetoothPrinterActivity", e.getMessage());
        }
    }

    public static SharedPreferences getSharePrintData(Context mContext) {
        SharedPreferences sp = mContext.getSharedPreferences("print_set", mContext.MODE_PRIVATE);
        return sp;
    }
}
