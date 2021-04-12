package com.example.test3;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.google.android.material.button.MaterialButton;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


@SuppressLint("AppCompatCustomView")
public class TimeButton extends Button implements OnClickListener
{
    private long lenght = 60 * 1000;// 倒计时长度,这里给了默认60秒
    private String textafter = "秒后重新获取~";
    private String textbefore = "点击获取验证码~";
    private int colorafter;
    private int colorbefore;
    private final String TIME = "time";
    private final String CTIME = "ctime";
    private OnClickListener mOnclickListener;
    private Timer t;
    private TimerTask tt;
    private long time;
    Map<String, Long> map = new HashMap<String, Long>();

    public TimeButton(Context context) {
        super(context);
        setOnClickListener(this);

    }

    public TimeButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnClickListener(this);
    }

    @SuppressLint("HandlerLeak")
    Handler han = new Handler() {
        public void handleMessage(android.os.Message msg) {
            TimeButton.this.setText(time / 1000 + textafter);
            time -= 1000;
            if (time < 0) {
                TimeButton.this.setEnabled(true);
                TimeButton.this.setText(textbefore);
                clearTimer();
            }
        };
    };

    private void initTimer() {
        time = lenght;
        t = new Timer();
        tt = new TimerTask() {

            @Override
            public void run() {
                Log.e("xuboyu", time / 1000 + "");
                han.sendEmptyMessage(0x01);//十六进制的数字1
            }
        };
    }

    private void clearTimer() {
        if (tt != null) {
            tt.cancel();
            tt = null;
        }
        if (t != null)
            t.cancel();
        t = null;
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        if (l instanceof TimeButton) {
            super.setOnClickListener(l);
        } else
            this.mOnclickListener = l;
    }

    @Override
    public void onClick(View v) {
        if (mOnclickListener != null)
            mOnclickListener.onClick(v);
        initTimer();
        this.setText(time / 1000 + textafter);
        this.setEnabled(false);
        t.schedule(tt, 0, 1000);
        // t.scheduleAtFixedRate(task, delay, period);
    }

    /**
     * 和activity的onDestroy()方法同步
     */
    public void onDestroy() {
        if (App.map == null)
            App.map = new HashMap<String, Long>();
        App.map.put(TIME, time);
        App.map.put(CTIME, System.currentTimeMillis());
        clearTimer();
        Log.e("xuboyu", "onDestroy");
    }

    /**
     * 和activity的onCreate()方法同步
     */
    public void onCreate(Bundle bundle) {
        Log.e("xuboyu:倒计时相关", App.map + "");
        if (App.map == null)
            return;
        if (App.map.size() <= 0)// 这里表示没有上次未完成的计时
            return;
        long time = System.currentTimeMillis() - App.map.get(CTIME)
                - App.map.get(TIME);
        App.map.clear();
        if (time > 0)
            return;
        else {
            initTimer();
            this.time = Math.abs(time);
            t.schedule(tt, 0, 1000);
            this.setText(time + textafter);
            this.setEnabled(false);
        }
    }

    /** * 设置计时时候显示的文本 */
    public TimeButton setTextAfter(String text1) {
        this.textafter = text1;
        return this;
    }

    /** * 设置点击之前的文本 */
    public TimeButton setTextBefore(String text0) {
        this.textbefore = text0;
        this.setText(textbefore);
        return this;
    }

    /**
     * 设置到计时长度
     * @param lenght
     * 时间 默认毫秒
     * @return
     */
    public TimeButton setLenght(long lenght) {
        this.lenght = lenght;
        return this;
    }

    /*
    public static void main(String[] args) {
        SendEmail sendEmail = new SendEmail();
        //设置要发送的邮箱
        sendEmail.setReceiveMailAccount("1376311674@qq.com");
        //创建10位发验证码
        Random random=new Random();
        String str="";
        for(int i=0;i<5;i++) {
            int n=random.nextInt(10);
            str+=n;
        }
        sendEmail.setInfo(str);
        try {
            sendEmail.Send();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

}
class App extends Application
{
    public static Map<String, Long> map;
}
