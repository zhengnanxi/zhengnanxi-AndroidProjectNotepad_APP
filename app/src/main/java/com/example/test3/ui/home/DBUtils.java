package com.example.test3.ui.home;

import java.sql.Blob;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DBUtils {
    //数据库名
    public static final String DATABASE_NAME = "Notepad";
    //表名
    public static final String DATABASE_TABLE = "Note";
    //数据库版本
    public static final int DATABASE_VERSION = 1;

    //表的列名
    public static final String NOTEPAD_ID = "id";           //ID值
    //public static final String NOTEPAD_THEME_ID = "theme_id";       //图片信息
    public static final String NOTEPAD_TITLE = "title";             //记事本标题
    public static final String NOTEPAD_CONTENT = "content";         //记事本内容
    //public static final String NOTEPAD_TIME = "notetime";          //记录时间---取消
    public static final String NOTEPAD_User_Name = "user_name";     //用户昵称
    public static final String NOTEPAD_User_Mail = "user_mail";     //用户邮箱
    //图片数据-9列
    public static final String NOTEPAD_Picture_ID_1 = "Picture_1";
    public static final String NOTEPAD_Picture_ID_2 = "Picture_2";
    public static final String NOTEPAD_Picture_ID_3 = "Picture_3";
    public static final String NOTEPAD_Picture_ID_4 = "Picture_4";
    public static final String NOTEPAD_Picture_ID_5 = "Picture_5";
    public static final String NOTEPAD_Picture_ID_6 = "Picture_6";
    public static final String NOTEPAD_Picture_ID_7 = "Picture_7";
    public static final String NOTEPAD_Picture_ID_8 = "Picture_8";
    public static final String NOTEPAD_Picture_ID_9 = "Picture_9";

    //获取当前日期
    public static final String getTime(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(date);
    }

}
