package com.example.test3.ui.home;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Base64DataException;
import android.util.Log;


import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class StringAndBitmap {
    //图片与String之间的转换，便于将图片存储在数据库中
    private Bitmap bitmap;
    private String string;
    public Bitmap stringToBitmap(String string){
        //数据库中的String--Base64类型转换成Bitmap
        try {
            byte[] bytes= Base64.decode(string,Base64.DEFAULT);
            bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
            return bitmap;
        }
        catch (Exception e){
            Log.e("StrinigAndBitmap1",e.getMessage());
            return null;
        }
    }
    public String bitmapToString(Bitmap bitmap){
        //用户在活动中上传的图片转换成String进行存储--Base64
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            stream.flush();
            stream.close();
            byte[] bytes = stream.toByteArray();// 转为byte数组
            string=Base64.encodeToString(bytes,Base64.DEFAULT);
        }
        catch (Exception e){
            Log.e("StrinigAndBitmap2",e.getMessage());
            return null;
        }finally {
            try {
                if (stream != null){
                    stream.flush();
                    stream.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        return string;
    }

}
