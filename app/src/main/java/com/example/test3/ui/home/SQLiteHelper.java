package com.example.test3.ui.home;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;

import com.example.test3.LoginFragment;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class SQLiteHelper extends SQLiteOpenHelper {
    private SQLiteDatabase sqLiteDatabase;

    public SQLiteHelper(Context context){
        super(context, DBUtils.DATABASE_NAME, null, DBUtils.DATABASE_VERSION);
        sqLiteDatabase = this.getWritableDatabase();
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String table = DBUtils.DATABASE_TABLE;
        String  id = DBUtils.NOTEPAD_ID;
        //String themeid = DBUtils.NOTEPAD_THEME_ID;
        String title = DBUtils.NOTEPAD_TITLE;
        String content = DBUtils.NOTEPAD_CONTENT;
        //String time = DBUtils.NOTEPAD_TIME;
        String picture_1 = DBUtils.NOTEPAD_Picture_ID_1;
        String picture_2 = DBUtils.NOTEPAD_Picture_ID_2;
        String picture_3 = DBUtils.NOTEPAD_Picture_ID_3;
        String picture_4 = DBUtils.NOTEPAD_Picture_ID_4;
        String picture_5 = DBUtils.NOTEPAD_Picture_ID_5;
        String picture_6 = DBUtils.NOTEPAD_Picture_ID_6;
        String picture_7 = DBUtils.NOTEPAD_Picture_ID_7;
        String picture_8 = DBUtils.NOTEPAD_Picture_ID_8;
        String picture_9 = DBUtils.NOTEPAD_Picture_ID_9;

        db.execSQL("create table " + DBUtils.DATABASE_TABLE
                + "(" + DBUtils.NOTEPAD_ID + " integer primary key autoincrement,"
                + DBUtils.NOTEPAD_User_Name + " text,"
                + DBUtils.NOTEPAD_User_Mail + " text,"
                //+ DBUtils.NOTEPAD_THEME_ID + " varch(8000),"
                + DBUtils.NOTEPAD_TITLE + " text,"
                + DBUtils.NOTEPAD_CONTENT + " text,"
                + DBUtils.NOTEPAD_Picture_ID_1 + " text,"
                + DBUtils.NOTEPAD_Picture_ID_2 + " text,"
                + DBUtils.NOTEPAD_Picture_ID_3 + " text,"
                + DBUtils.NOTEPAD_Picture_ID_4 + " text,"
                + DBUtils.NOTEPAD_Picture_ID_5 + " text,"
                + DBUtils.NOTEPAD_Picture_ID_6 + " text,"
                + DBUtils.NOTEPAD_Picture_ID_7 + " text,"
                + DBUtils.NOTEPAD_Picture_ID_8 + " text,"
                + DBUtils.NOTEPAD_Picture_ID_9 + " text)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
    //添加数据
    public boolean insertData (String userName, String userMail, String userTitle, String userContene,
                               String userPicture_1, String userPicture_2, String userPicture_3,
                               String userPicture_4, String userPicture_5, String userPicture_6,
                               String userPicture_7, String userPicture_8, String userPicture_9){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBUtils.NOTEPAD_User_Name, userName);
        contentValues.put(DBUtils.NOTEPAD_User_Mail, userMail);
        //contentValues.put(DBUtils.NOTEPAD_THEME_ID, userThemeID);
        contentValues.put(DBUtils.NOTEPAD_TITLE, userTitle);
        contentValues.put(DBUtils.NOTEPAD_CONTENT, userContene);
        //contentValues.put(DBUtils.NOTEPAD_TIME, userTime);
        contentValues.put(DBUtils.NOTEPAD_Picture_ID_1, userPicture_1);
        contentValues.put(DBUtils.NOTEPAD_Picture_ID_2, userPicture_2);
        contentValues.put(DBUtils.NOTEPAD_Picture_ID_3, userPicture_3);
        contentValues.put(DBUtils.NOTEPAD_Picture_ID_4, userPicture_4);
        contentValues.put(DBUtils.NOTEPAD_Picture_ID_5, userPicture_5);
        contentValues.put(DBUtils.NOTEPAD_Picture_ID_6, userPicture_6);
        contentValues.put(DBUtils.NOTEPAD_Picture_ID_7, userPicture_7);
        contentValues.put(DBUtils.NOTEPAD_Picture_ID_8, userPicture_8);
        contentValues.put(DBUtils.NOTEPAD_Picture_ID_9, userPicture_9);

        return sqLiteDatabase.insert(DBUtils.DATABASE_TABLE,null,contentValues) > 0;
    }

    //删除数据
    public boolean deleteData (String id){
        String sql = DBUtils.NOTEPAD_ID + "=?";
        String[] contentValuesArray = new String[]{String.valueOf(id)};
        return sqLiteDatabase.delete(DBUtils.DATABASE_TABLE,sql,contentValuesArray) > 0;
    }

    //修改数据
    public  boolean updateData(String user_name, String user_mail,String id,
                               String title, String content,
                               String userPicture_1, String userPicture_2, String userPicture_3,
                               String userPicture_4, String userPicture_5, String userPicture_6,
                               String userPicture_7, String userPicture_8, String userPicture_9){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBUtils.NOTEPAD_User_Name, user_name);
        contentValues.put(DBUtils.NOTEPAD_User_Mail, user_mail);
        //contentValues.put(DBUtils.NOTEPAD_THEME_ID, imageid);
        contentValues.put(DBUtils.NOTEPAD_TITLE, title);
        contentValues.put(DBUtils.NOTEPAD_CONTENT, content);
        //contentValues.put(DBUtils.NOTEPAD_TIME, userYear);
        contentValues.put(DBUtils.NOTEPAD_Picture_ID_1, userPicture_1);
        contentValues.put(DBUtils.NOTEPAD_Picture_ID_2, userPicture_2);
        contentValues.put(DBUtils.NOTEPAD_Picture_ID_3, userPicture_3);
        contentValues.put(DBUtils.NOTEPAD_Picture_ID_4, userPicture_4);
        contentValues.put(DBUtils.NOTEPAD_Picture_ID_5, userPicture_5);
        contentValues.put(DBUtils.NOTEPAD_Picture_ID_6, userPicture_6);
        contentValues.put(DBUtils.NOTEPAD_Picture_ID_7, userPicture_7);
        contentValues.put(DBUtils.NOTEPAD_Picture_ID_8, userPicture_8);
        contentValues.put(DBUtils.NOTEPAD_Picture_ID_9, userPicture_9);
        String sql = DBUtils.NOTEPAD_ID + "=?";
        String[] strings = new String[]{id};
        return sqLiteDatabase.update(DBUtils.DATABASE_TABLE,contentValues,sql,strings) > 0;
    }

    //查询数据
    public List<CardMessage> query(){
        List<CardMessage> list = new ArrayList<CardMessage>();
        Cursor cursor = sqLiteDatabase.query(
                DBUtils.DATABASE_TABLE,
                null,
                "user_name='" + LoginFragment.Account_Name +"'" + " and user_mail=" + "'" + LoginFragment.Account_Mail + "'"/*+ " and " + "user_mail=" + LoginFragment.Account_Mail*/,
                null,
                null,
                null,
                DBUtils.NOTEPAD_ID + " desc");
        if (cursor != null){
            while (cursor.moveToNext()){
                CardMessage cardInfo = new CardMessage();
                //9张图片存储
                /*
                String[] picturelist = new String[9];
                for (int i = 1; i < 10; i++)
                {
                    picturelist[i] = "Picture_" + String.valueOf(i);
                    System.out.println(picturelist[i]);
                }
*/
                String id = String.valueOf(cursor.getInt(cursor.getColumnIndex(DBUtils.NOTEPAD_ID)));
                //String themeid = cursor.getString(cursor.getColumnIndex(DBUtils.NOTEPAD_THEME_ID));
                String title = cursor.getString(cursor.getColumnIndex(DBUtils.NOTEPAD_TITLE));
                String content = cursor.getString(cursor.getColumnIndex(DBUtils.NOTEPAD_CONTENT));

                cardInfo.setId(id);
                //cardInfo.setImage_theme_id(themeid);
                cardInfo.setArticle_title(title);
                cardInfo.setArticle_text(content);
                cardInfo.setUser_name(LoginFragment.Account_Name);
                cardInfo.setUser_mail(LoginFragment.Account_Mail);
                //添加9张图片
                cardInfo.setUser_Picture_1(cursor.getString(cursor.getColumnIndex(DBUtils.NOTEPAD_Picture_ID_1)));
                cardInfo.setUser_Picture_2(cursor.getString(cursor.getColumnIndex(DBUtils.NOTEPAD_Picture_ID_2)));
                cardInfo.setUser_Picture_3(cursor.getString(cursor.getColumnIndex(DBUtils.NOTEPAD_Picture_ID_3)));
                cardInfo.setUser_Picture_4(cursor.getString(cursor.getColumnIndex(DBUtils.NOTEPAD_Picture_ID_4)));
                cardInfo.setUser_Picture_5(cursor.getString(cursor.getColumnIndex(DBUtils.NOTEPAD_Picture_ID_5)));
                cardInfo.setUser_Picture_6(cursor.getString(cursor.getColumnIndex(DBUtils.NOTEPAD_Picture_ID_6)));
                cardInfo.setUser_Picture_7(cursor.getString(cursor.getColumnIndex(DBUtils.NOTEPAD_Picture_ID_7)));
                cardInfo.setUser_Picture_8(cursor.getString(cursor.getColumnIndex(DBUtils.NOTEPAD_Picture_ID_8)));
                cardInfo.setUser_Picture_9(cursor.getString(cursor.getColumnIndex(DBUtils.NOTEPAD_Picture_ID_9)));

                list.add(cardInfo);

            }
            cursor.close();
        }
        return list;

    }


}
