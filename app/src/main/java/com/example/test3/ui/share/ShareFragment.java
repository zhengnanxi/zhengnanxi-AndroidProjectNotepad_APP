package com.example.test3.ui.share;

import android.app.AlertDialog;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.test3.LoginFragment;
import com.example.test3.R;
import com.example.test3.RegisterMessage;
import com.example.test3.SendEmail;
import com.example.test3.TimeButton;
import com.example.test3.ui.home.CardMessage;
import com.example.test3.ui.home.SQLiteHelper;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SQLQueryListener;
import cn.bmob.v3.listener.UpdateListener;

public class ShareFragment extends Fragment {

    private ShareViewModel shareViewModel;

    private List<CardMessage> cardMessagesList; //数据源
    public TextView account_my;
    public TextView mail_my;
    public TextView create_sum;
    public TextView cloud_sum;
    public ImageView cloud_data_delete; //跳转云端管理
    public SQLiteHelper mSQLiteHelper;
    public ImageView password_change;   //修改密码
    public static int Account_Pass = 0;     //默认账号认证失败
    public static int Account_Pass_Change = 0;      //默认修改账号认证失败
    private String SecurityCodeSave = "";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Bmob.initialize(getActivity(),"你的比目后端云ID");

    }


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_share, container, false);

        account_my = (TextView) root.findViewById(R.id.account_my);
        mail_my = (TextView) root.findViewById(R.id.mail_my);
        create_sum = (TextView) root.findViewById(R.id.create_sum);
        cloud_sum = (TextView) root.findViewById(R.id.cloud_sum);
        cloud_data_delete = (ImageView)root.findViewById(R.id.cloud_data_delete);
        password_change = (ImageView) root.findViewById(R.id.changepassword);

        //创建数据库
        mSQLiteHelper = new SQLiteHelper(getActivity());
        //查询数据库--本地
        cardMessagesList = mSQLiteHelper.query();

        account_my.setText("ID: " + LoginFragment.Account_Name);
        String mail = LoginFragment.Account_Mail;
        mail_my.setText("Mail: " + mail);

        //监听实现
        cloud_data_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), CloudManage.class);
                startActivity(intent);
            }
        });

        password_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //修改密码
                ChangePassword();
            }
        });


        //获取我的界面数据
        set_message();

        return root;
    }

    //获取云端数据
    public void set_message()
    {
        get_cloudSum();
    }
    //开启子线程
    public void get_cloudSum()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    //存放到handle
                    Bundle bundle = new Bundle();
                    Message message = Message.obtain();
                    String sql = "select count(*) from CardMessage where user_name = ? and user_mail = ?";
                    Thread.sleep(500);
                    new BmobQuery<CardMessage>().doSQLQuery(sql, new SQLQueryListener<CardMessage>() {
                        @Override
                        public void done(BmobQueryResult<CardMessage> bmobQueryResult, BmobException e) {
                            if (e == null)
                            {
                                int count = bmobQueryResult.getCount();
                                Log.e("云端总量：" ,count + "");
                                List<CardMessage> list = bmobQueryResult.getResults();
                                bundle.putInt("sum",count);
                                message.setData(bundle);
                                message.what = 111;
                                handler.sendMessage(message);
                            }else
                            {
                                Log.e("smile", "错误码："+e.getErrorCode()+"，错误描述："+e.getMessage());
                            }
                        }
                    }, LoginFragment.Account_Name,LoginFragment.Account_Mail);

                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public Handler handler = new Handler()
    {
      @Override
      public void handleMessage (@NonNull Message msg)
      {
          super.handleMessage(msg);
          if (msg.what == 111)
          {
              Bundle bundle = msg.getData();
              int cloudsum = bundle.getInt("sum");

              create_sum.setText(cardMessagesList.size() + "");
              cloud_sum.setText(cloudsum + "");

          }
      }
    };


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

        securitycodesend.setTextAfter("秒后重新验证").setTextBefore("点击发送验证码").setLenght(10 * 1000);

        //发送验证码
        securitycodesend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String pattern =  "\\w[-\\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\\.)+[A-Za-z]{2,14}";
                Pattern r = Pattern.compile(pattern);
                Matcher m = r.matcher(changeusermail.getText().toString());
                if (m.matches())
                {
                    send(changeusermail.getText().toString());
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

    //判断密码位数是否大于6位
    private boolean isPasswordValid(@Nullable Editable text) {
        return text != null && text.length() >= 6;
    }

}