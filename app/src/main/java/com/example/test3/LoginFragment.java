package com.example.test3;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.Editable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import net.sf.json.JSONObject;

import java.sql.Time;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SQLQueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import static android.content.Context.BIND_AUTO_CREATE;

/**
 * Fragment representing the login screen
 */
public class LoginFragment extends Fragment {
    public static int Account_Pass = 0;     //默认账号认证失败
    public static int Account_Pass_Change = 0;      //默认修改账号认证失败
    public static boolean Password_pass = false;    //默认密码认证失败
    public static String Account_Name = "";         //全局用户昵称
    public static String Account_Mail = "";         //全局用户邮箱

    //public CommentMyService.Music_Comment music_comment;        //创建服务
    //public MyConn myConn;
    //public Intent intent;
    public TextView Comment_name;
    public TextView Comment_message;

    private String SecurityCodeSave = "";

    public Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 666){
                Bundle bundle = msg.getData();
                String name = bundle.getString("name");
                String comment = bundle.getString("comment");
                Comment_name.setText("歌名: " + name);
                Comment_message.setText("评论: " + comment);
            }
        }
    };



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bmob.initialize(getActivity(),"6f26b60e82795408fabfdf3ffa7d2cc6");


        //服务的链接---
        //myConn = new MyConn();
        //intent = new Intent(getActivity(), CommentMyService.class);
        //getActivity().bindService(intent, myConn, BIND_AUTO_CREATE);
        //music_comment.play_run();

    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.shr_login_fragment, container, false);

        // Snippet from "Navigate to the next Fragment" section goes here.
        final TextInputLayout passwordTextInput = view.findViewById(R.id.password_text_input);
        final TextInputEditText passwordEditeText = view.findViewById(R.id.password_edit_text);
        final TextInputEditText user_name = view.findViewById(R.id.account);

        //按钮监控
        MaterialButton nextButton = view.findViewById(R.id.next_button);
        MaterialButton registerBbutton = view.findViewById(R.id.register_button);
        MaterialButton changepasswordButton = view.findViewById(R.id.change_password_button);

        ImageView trydoit = (ImageView) view.findViewById(R.id.imageView3);     //彩蛋功能
        Comment_name = (TextView) view.findViewById(R.id.Comment_name);         //歌名
        Comment_message = (TextView) view.findViewById(R.id.Comment_message);   //评论内容





        //设置密码少于6位数字则出错--登录按钮
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Account_Pass = myBinder.call_Confirm_result(passwordEditeText.getText().toString(), user_name.getText().toString());
                //判断账号
                if (user_name.getText().toString() == null){
                    //密码位数判断
                    if (!isPasswordValid(passwordEditeText.getText())){
                        passwordTextInput.setError("账户密码至少6位");
                    }
                }
                else {
                    passwordTextInput.setError(null);       //清除错误
                    //比目云获取数据--进行判断
                    String sql = "select * from RegisterMessage where Password = ? and UserName = ? ";
                    new BmobQuery<RegisterMessage>().doSQLQuery(sql, new SQLQueryListener<RegisterMessage>() {
                        @Override
                        public void done(BmobQueryResult<RegisterMessage> bmobQueryResult, BmobException e) {
                            if (e == null){
                                List<RegisterMessage> list = (List<RegisterMessage>)  bmobQueryResult.getResults();
                                if (list!=null && list.size() > 0){
                                    //测试数据用
                                    Log.e("密码：",list.get(0).getPassword());
                                    Log.e("账号名：",list.get(0).getUserName());
                                    Log.e("邮箱：", list.get(0).getUserMail());
                                    Account_Pass = 1;           //权限通过，设置1
                                }
                                //Log.e("smile","WDNMD");-----因为多次查询失败所写的o(╯□╰)o
                                if (Account_Pass == 1){
                                    Intent intent = new Intent();
                                    intent.setClass(getActivity(), MainActivity.class);
                                    intent.putExtra("zhanghao",list.get(0).getUserName());
                                    //保存目前使用的用户昵称和邮箱
                                    Account_Name = list.get(0).getUserName();
                                    Account_Mail = list.get(0).getUserMail();
                                    startActivity(intent);
                                    Toast.makeText(getActivity(), "登陆成功(￣▽￣)~*", Toast.LENGTH_LONG).show();
                                    Account_Pass = 0;           //跳转成功后，权限恢复为0



                                } else {
                                    Toast.makeText(getActivity(), "登陆失败！请检查密码或账号！", Toast.LENGTH_LONG).show();
                                }
                            }else {
                                Log.e("smile", "错误码："+e.getErrorCode()+"，错误描述："+e.getMessage());
                            }
                        }
                    },passwordEditeText.getText().toString(),user_name.getText().toString());
                }
            }
        });

        //注册
        registerBbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterUI();
            }
        });

        //忘记密码功能
        changepasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangePassword();
            }
        });

        //密码输入监听
        passwordEditeText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (isPasswordValid(passwordEditeText.getText())){
                    passwordTextInput.setError(null);
                }
                return false;
            }
        });

        //彩蛋输出--热评消息-------该功能暂无法实现点击---涉及线程销毁和重启---目前无法实现
        trydoit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getjson();
            }
        });
        return view;
    }

    public void ChangePassword()
    {
        //弹出密码修改框
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        AlertDialog dialog = builder.create();
        View dialogView = View.inflate(getActivity(),R.layout.change_password_layout,null);
        dialog.setView(dialogView);
        dialog.show();

        //初始化控件
        final TextInputEditText changeusername = dialogView.findViewById(R.id.change_user_name);        //用户名
        final TextInputEditText changeusermail = dialogView.findViewById(R.id.change_user_Mail);        //用户邮箱
        final TextInputEditText firstpassword = dialogView.findViewById(R.id.change_password_text);        //初始密码
        final TextInputEditText securitycode = dialogView.findViewById(R.id.security_code);             //填写验证码用

        final TextInputEditText againpassword = dialogView.findViewById(R.id.change_password_text_again);        //再次输入密码
        final TextInputLayout firstpasswrodinput = dialogView.findViewById(R.id.change_password_input_text);        //第一次提示错误用
        final TextInputLayout againpasswordinput = dialogView.findViewById(R.id.change_password_text_input_again);             //再次输入密码提示错误用
        MaterialButton sure_change = dialogView.findViewById(R.id.sure_change_button);            //提交注册信息
        TimeButton securitycodesend = dialogView.findViewById(R.id.security_code_send_button);      //发送验证码
        //RegisterMessage rm = new RegisterMessage();     //实例化数据表

        securitycodesend.setTextAfter("秒后重新验证").setTextBefore("点击发送验证码").setLenght(10 * 1000);

        //发送验证码
        securitycodesend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String pattern =  "\\w[-\\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\\.)+[A-Za-z]{2,14}";
                Pattern r = Pattern.compile(pattern);
                Matcher m = r.matcher(changeusermail.getText().toString());
                Log.e("邮箱判断",changeusermail.getText().toString());
                Log.e("邮箱判断", m.matches() +  "");
                if (m.matches())
                {
                    send(changeusermail.getText().toString());
                    /*
                    SendEmail sendEmail = new SendEmail();
                    //设置要发送的邮箱
                    sendEmail.setReceiveMailAccount(changeusermail.getText().toString() + "");
                    //创建5位发验证码
                    Random random=new Random();
                    String str="";
                    for(int i=0;i<5;i++) {
                        int n=random.nextInt(10);
                        str+=n;
                    }
                    SecurityCodeSave = str; //全局验证码变量
                    sendEmail.setInfo(str);
                    try {
                        sendEmail.Send();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                     */
                }
                else
                {
                    Toast.makeText(getActivity(), "邮箱格式错误！", Toast.LENGTH_SHORT).show();
                }

            }
        });

        sure_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (changeusername.getText().toString() != null
                        && changeusermail.getText().toString() != null
                        && securitycode.getText().toString().equals(SecurityCodeSave))
                {
                    firstpasswrodinput.setError(null);       //清除错误
                    //比目云获取数据--进行判断
                    String sql = "select * from RegisterMessage where UserMail = ? and UserName = ? ";
                    new BmobQuery<RegisterMessage>().doSQLQuery(sql, new SQLQueryListener<RegisterMessage>() {
                        @Override
                        public void done(BmobQueryResult<RegisterMessage> bmobQueryResult, BmobException e) {
                            if (e == null){
                                List<RegisterMessage> list = (List<RegisterMessage>)  bmobQueryResult.getResults();//判断数据获取是否为空
                                if (list!=null && list.size() >= 0){
                                    Log.e("jieguo",list.get(0).getPassword());
                                    Log.e("jieguo",list.get(0).getUserName());
                                    Log.e("jieguo",list.get(0).getUserMail());
                                    Log.e("jieguo",list.get(0).getObjectId());
                                    Account_Pass_Change = 1;           //权限通过，设置1
                                }
                                //Log.e("smile","WDNMD");-----因为多次查询失败所写的o(╯□╰)o
                                if (Account_Pass_Change == 1){
                                    //数据库密码修改
                                    RegisterMessage rm = new RegisterMessage();     //实例化数据表
                                    rm.setPassword(againpassword.getText().toString());
                                    rm.update(list.get(0).getObjectId(), new UpdateListener() {
                                        @Override
                                        public void done(BmobException e) {
                                            if(e==null){
                                                Toast.makeText(getActivity(), "修改密码成功！", Toast.LENGTH_SHORT).show();
                                                dialog.dismiss();
                                            }else{
                                                Toast.makeText(getActivity(), "修改密码失败！" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                  //  Toast.makeText(getActivity(), "登陆成功(￣▽￣)~*", Toast.LENGTH_LONG).show();
                                    Account_Pass_Change = 0;           //跳转成功后，权限恢复为0



                                } else {
                                    Toast.makeText(getActivity(), "登陆失败！请检查密码或账号！", Toast.LENGTH_LONG).show();
                                }
                            }else {
                                Log.e("smile", "错误码："+e.getErrorCode()+"，错误描述："+e.getMessage());
                            }
                        }
                    },changeusermail.getText().toString(), changeusername.getText().toString());

                }
                else
                {
                    //密码位数判断
                    if (!isPasswordValid(firstpassword.getText())){
                        firstpasswrodinput.setError("账户密码至少6位");
                    }
                    Toast.makeText(getActivity(), "验证失败！请检查密码或账号！", Toast.LENGTH_LONG).show();
                }
            }
        });




    }

    //判断密码位数是否大于6位
    private boolean isPasswordValid(@Nullable Editable text) {
        return text != null && text.length() >= 6;
    }

    //调用注册界面
    public void RegisterUI (){
        //弹出注册框
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        AlertDialog dialog = builder.create();
        View dialogView = View.inflate(getActivity(),R.layout.register_layout,null);
        dialog.setView(dialogView);
        dialog.show();

        //初始化控件
        final TextInputEditText username = dialogView.findViewById(R.id.User_name);        //用户名
        final TextInputEditText firstpassword = dialogView.findViewById(R.id.password_edit_text_register);        //初始密码
        final TextInputEditText againpassword = dialogView.findViewById(R.id.password_edit_again);        //再次输入密码
        final TextInputLayout firstpasswrodinput = dialogView.findViewById(R.id.password_text_input_register);        //第一次提示错误用
        final TextInputLayout againpasswordinput = dialogView.findViewById(R.id.password_text_again);             //再次输入密码提示错误用
        final TextInputEditText usermail = dialogView.findViewById(R.id.User_Mail);                 //用户电子邮箱
        MaterialButton sure_register = dialogView.findViewById(R.id.sure_register_button);            //提交注册信息
        RegisterMessage rm = new RegisterMessage();     //实例化数据表


        //注册按钮监听
        sure_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pattern =  "\\w[-\\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\\.)+[A-Za-z]{2,14}";
                Pattern r = Pattern.compile(pattern);
                Matcher m = r.matcher(usermail.getText().toString());
               if (firstpassword.getText().toString().equals(againpassword.getText().toString()))
               {
                   if (!isPasswordValid(firstpassword.getText()))
                   {
                       firstpasswrodinput.setError("密码少于6位数");
                   }else if (!isPasswordValid(againpassword.getText()))
                   {
                       againpasswordinput.setError("密码少于6位数");
                   }else if (m.matches())
                   {
                       firstpasswrodinput.setError(null);
                       againpasswordinput.setError(null);
                       rm.setUserName(username.getText().toString());
                       rm.setPassword(firstpassword.getText().toString());
                       rm.setUserMail(usermail.getText().toString());
                       rm.save(new SaveListener<String>() {
                           @Override
                           public void done(String s, BmobException e) {
                               if (e == null){
                                   Toast.makeText(getActivity(), "注册账号成功(￣▽￣)~*", Toast.LENGTH_LONG).show();
                                   //数据测试用
                                   Log.e("注册账号：" , username.getText().toString());
                                   Log.e("注册邮箱：", usermail.getText().toString());
                                   Log.e("注册密码：", firstpassword.getText().toString());
                                   dialog.dismiss();
                               }else {
                                   Toast.makeText(getActivity(), "注册失败", Toast.LENGTH_SHORT).show();
                               }
                           }
                       });
                   }
                   else {
                       Toast.makeText(getContext(), "密码不一致或邮箱格式错误", Toast.LENGTH_LONG).show();
                   }
               } else {
                   Toast.makeText(getContext(), "密码不一致或邮箱格式错误", Toast.LENGTH_LONG).show();
               }
            }
        });
    }

    //子线程编写----用于获取热评消息用------
    public void getjson(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500); //线程延迟0.5秒
                    JSONObject message = reping.InitMessage();
                    String Music_name = reping.get_comment_name(message);
                    String Music_comment = reping.get_comment_note(message);
                    //Bundle机制
                    Bundle bundle = new Bundle();
                    bundle.putString("name",Music_name);
                    bundle.putString("comment",Music_comment);
                    Message message1 = Message.obtain();
                    message1.setData(bundle);
                    message1.what = 666;
                    handler.sendMessage(message1);

                }catch (InterruptedException e){
                    e.printStackTrace();
                }

            }
        }).start();

    }

    //子线程编写----用于发送邮件用------
    public void send(String sendtomail)
    {
        new Thread(new Runnable() {
            @Override
            public void run()
            {
                try
                {
                    Thread.sleep(500);

                    SendEmail sendEmail = new SendEmail();
                    //设置要发送的邮箱
                    sendEmail.setReceiveMailAccount(sendtomail);
                    //创建5位发验证码
                    Random random=new Random();
                    String str="";
                    for(int i=0;i<5;i++) {
                        int n=random.nextInt(10);
                        str+=n;
                    }
                    SecurityCodeSave = str; //全局验证码变量
                    sendEmail.setInfo(str);
                    try {
                        sendEmail.Send();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        }).start();
    }




    /*-------弃用------
    //热评消息的获取--handler
    @SuppressLint("HandlerLeak")
    public static Handler handler = new Handler(){
        //在主线程中处理从子线程发送过来的消息

        @Override
        public void handleMessage(@NonNull Message msg) {
            Bundle bundle = msg.getData();
            String name = bundle.getString("music_name");
            String comment = bundle.getString("music_comment");

            Log.e("name",name);
            Log.e("comment",comment);
        }
    };
     *///--------弃用------


    /*--------弃用------
    //创建MyConn类，实现服务连接
    public class MyConn implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            music_comment = (CommentMyService.Music_Comment) service;
            Log.e("Login","服务绑定成功");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }
     *///--------弃用------



    /*-----------------弃用-------------------(线程的创建)----改用上面的方法
    class MyThread extends Thread{
        @Override
        public void run() {
            super.run();
            JSONObject message = reping.InitMessage();
            String Music_name = reping.get_comment_name(message);
            String Music_comment = reping.get_comment_note(message);
            Comment_name.setText("歌名: " + Music_name);
            Comment_message.setText("评论: " + Music_comment);
        }
    }
     */
    /*
    class MyThread implements Runnable{
        @Override
        public void run() {
            JSONObject message = reping.InitMessage();
            String Music_name = reping.get_comment_name(message);
            String Music_comment = reping.get_comment_note(message);
            Comment_name.setText("歌名: " + Music_name);
            Comment_message.setText("评论: " + Music_comment);
        }
    }

     *///-----------------弃用-------------------





}
