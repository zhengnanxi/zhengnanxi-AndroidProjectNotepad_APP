package com.example.test3;

import cn.bmob.v3.BmobObject;

public class RegisterMessage extends BmobObject {
    private String UserName;
    private String Password;
    private String UserMail;



    public RegisterMessage(String userName, String password, String userMail) {
        UserName = userName;
        Password = password;
        UserMail = userMail;
    }

    public RegisterMessage() {

    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
    public String getUserMail() {
        return UserMail;
    }

    public void setUserMail(String userMail) {
        UserMail = userMail;
    }
}
