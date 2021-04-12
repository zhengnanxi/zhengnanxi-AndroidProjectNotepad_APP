package com.example.test3.ui.home;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.List;

import cn.bmob.v3.BmobObject;

public class CardMessage extends BmobObject implements Serializable {
    public String image_theme_id;
    public String article_title;
    public String article_text;
    public String id;
    public String user_name;
    public String user_mail;
    //9张图片
    public String user_Picture_1;
    public String user_Picture_2;
    public String user_Picture_3;
    public String user_Picture_4;
    public String user_Picture_5;
    public String user_Picture_6;
    public String user_Picture_7;
    public String user_Picture_8;
    public String user_Picture_9;


    public String getImage_theme_id() {
        return image_theme_id;
    }

    public void setImage_theme_id(String image_theme_id) {
        this.image_theme_id = image_theme_id;
    }

    public String getArticle_title() {
        return article_title;
    }

    public void setArticle_title(String article_title) {
        this.article_title = article_title;
    }

    public String getArticle_text() {
        return article_text;
    }

    public void setArticle_text(String article_text) {
        this.article_text = article_text;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_mail() {
        return user_mail;
    }

    public void setUser_mail(String user_mail) {
        this.user_mail = user_mail;
    }

    public String getUser_Picture_1() {
        return user_Picture_1;
    }

    public void setUser_Picture_1(String user_Picture_1) {
        this.user_Picture_1 = user_Picture_1;
    }

    public String getUser_Picture_2() {
        return user_Picture_2;
    }

    public void setUser_Picture_2(String user_Picture_2) {
        this.user_Picture_2 = user_Picture_2;
    }

    public String getUser_Picture_3() {
        return user_Picture_3;
    }

    public void setUser_Picture_3(String user_Picture_3) {
        this.user_Picture_3 = user_Picture_3;
    }

    public String getUser_Picture_4() {
        return user_Picture_4;
    }

    public void setUser_Picture_4(String user_Picture_4) {
        this.user_Picture_4 = user_Picture_4;
    }

    public String getUser_Picture_5() {
        return user_Picture_5;
    }

    public void setUser_Picture_5(String user_Picture_5) {
        this.user_Picture_5 = user_Picture_5;
    }

    public String getUser_Picture_6() {
        return user_Picture_6;
    }

    public void setUser_Picture_6(String user_Picture_6) {
        this.user_Picture_6 = user_Picture_6;
    }

    public String getUser_Picture_7() {
        return user_Picture_7;
    }

    public void setUser_Picture_7(String user_Picture_7) {
        this.user_Picture_7 = user_Picture_7;
    }

    public String getUser_Picture_8() {
        return user_Picture_8;
    }

    public void setUser_Picture_8(String user_Picture_8) {
        this.user_Picture_8 = user_Picture_8;
    }

    public String getUser_Picture_9() {
        return user_Picture_9;
    }

    public void setUser_Picture_9(String user_Picture_9) {
        this.user_Picture_9 = user_Picture_9;
    }

    public CardMessage(){

    }


    public CardMessage(String image_theme_id, String article_title, String article_text, String id) {
        this.image_theme_id = image_theme_id;
        this.article_title = article_title;
        this.article_text = article_text;
        this.id = id;

    }

    @NonNull
    @Override
    public String toString() {
        return "GoodsEntity{" +
                "image_theme_id='" + image_theme_id + '\'' +
                ", article_title='" + article_title + '\'' +
                ", article_text='" + article_text + '\'' +
                '}';

    }
}
