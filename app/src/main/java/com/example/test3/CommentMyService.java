package com.example.test3;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import net.sf.json.JSONObject;


import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SQLQueryListener;
/*
                        ########################################
                        #           本Service类已弃用          #
                        ########################################
*/
public class CommentMyService extends Service {
    //public int pass_number = 0;    //0默认不通过，1说明通过


    public CommentMyService() {}

    //服务代理，使用服务中的方法
    class Music_Comment extends Binder {
        public void play_run(){
            Music_reping();

        }
    }

    //热评信息获取
    public void Music_reping(){


        //获取数据
        reping me = new reping();
        JSONObject message = new JSONObject();
        message = me.InitMessage();
        Log.e("message",""+message);
        String music_name = me.get_comment_name(message);
        String music_comment = me.get_comment_note(message);

        //将热评封装至消息对象中
        //Message msg = LoginFragment.handler.obtainMessage();
        Bundle bundle = new Bundle();
        bundle.putString("music_name",music_name);
        bundle.putString("music_comment",music_comment);
       // msg.setData(bundle);
        //将消息发送到主线程的消息队列
       // LoginFragment.handler.sendMessage(msg);
    }





    @Override
    public void onCreate() {
        Bmob.initialize(CommentMyService.this,"6f26b60e82795408fabfdf3ffa7d2cc6");
        super.onCreate();
    }

    //绑定服务
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return new Music_Comment();
    }

    //解除绑定服务
    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }
}
