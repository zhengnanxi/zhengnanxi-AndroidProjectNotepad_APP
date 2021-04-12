package com.example.test3.ui.share;


import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test3.LoginFragment;
import com.example.test3.R;
import com.example.test3.ui.home.CardMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SQLQueryListener;
import cn.bmob.v3.listener.UpdateListener;

public class CloudManage extends AppCompatActivity
{
    private List<CardMessage> cardMessagesList; //数据源
    private List<Map.Entry<String, String>> cardMessages;
    public ImageView btn_back;
    public RecyclerView lsv_side_slip_delete;
    public SideSlipAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_cloud_layout);
        Bmob.initialize(this,"你的比目后端云ID");

        //初始化控件
        control_init();
        //获得数据
        get_cloudMessage();
        //设置监听
        //setListeners();
        //设置适配器
        //setAdapter();


    }


    public void setAdapter()
    {
        adapter = new SideSlipAdapter(getApplicationContext(), cardMessages);
        lsv_side_slip_delete.setLayoutManager(new GridLayoutManager(getApplicationContext(),1,GridLayoutManager.VERTICAL,false));
        lsv_side_slip_delete.setAdapter(adapter);

        //adapter.notifyDataSetInvalidated();
    }



    private void setListeners()
    {
        // 注册监听器,回调用来刷新数据显示
        adapter.setDelItemListener(new SideSlipAdapter.OnItemClickListener() {
            @Override
            public void delete(View view, int position) {
                Log.e("测试","这里的位置是：" + position);
                String title = cardMessages.get(position).getKey();
                delete_cloudMessage(title,position);
                //cardMessages.remove(position);
                Log.e("标题", cardMessages.get(position).getKey() +"   " +  cardMessages.get(position).getValue());
                adapter.notifyDataSetChanged();

                //adapter.notifyDataSetChanged();
                //Toast.makeText(getApplicationContext(), "删除成功", Toast.LENGTH_LONG).show();

            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void setDatas()
    {
        //查询云端数据
        get_cloudMessage();

    }

    //开启子线程--删除云端数据
    public void delete_cloudMessage(String delete_title, int position)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //查询objectid--依据objectid进行删除数据
                    String sql = "select * from CardMessage where user_name = ? and user_mail = ? and article_title = ?";
                    Thread.sleep(500);
                    new BmobQuery<CardMessage>().doSQLQuery(sql, new SQLQueryListener<CardMessage>() {
                        @Override
                        public void done(BmobQueryResult<CardMessage> bmobQueryResult, BmobException e)
                        {
                            if (e == null)
                            {
                                List<CardMessage> deleteList;
                                String objectId;
                                deleteList = bmobQueryResult.getResults();
                                Log.e("康康", deleteList.get(0).getObjectId());
                                
                                //根据对象的objectid进行删除数据
                                objectId = deleteList.get(0).getObjectId();
                                deleteList.get(0).delete(objectId, new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        if (e == null)
                                        {
                                            Toast.makeText(CloudManage.this, "云端删除中....", Toast.LENGTH_SHORT).show();
                                            cardMessages.remove(position);
                                            adapter.notifyDataSetChanged();
                                            Toast.makeText(getApplicationContext(), "云端删除成功", Toast.LENGTH_SHORT).show();
                                        }
                                        else
                                        {
                                            Log.e("删除错误：", e.toString());
                                            Toast.makeText(CloudManage.this, "云端删除失败，请检查网络", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });

                            }else
                            {
                                Toast.makeText(getApplicationContext(), "云端删除失败，请检查网络状况", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Object[]{LoginFragment.Account_Name, LoginFragment.Account_Mail, delete_title});

                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //开启子线程--获得云端数据
    public void get_cloudMessage()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {

                try
                {
                    //存放到handle
                    Bundle bundle = new Bundle();
                    Message message = Message.obtain();
                    String sql = "select * from CardMessage where user_name = ? and user_mail = ?";
                    Thread.sleep(500);
                    new BmobQuery<CardMessage>().doSQLQuery(sql, new SQLQueryListener<CardMessage>() {
                        @Override
                        public void done(BmobQueryResult<CardMessage> bmobQueryResult, BmobException e) {
                            if (e == null)
                            {
                                int count = bmobQueryResult.getCount();
                                Log.e("云端总量：" ,count + "");
                                cardMessagesList = bmobQueryResult.getResults();
                                //cardMessages = new ArrayList<CardMessage>(cardMessagesList);
                                //cardMessages.remove(0);   测试用
                                //把标题和内容存储到Map数组里面
                                Map<String, String> cloudMessage = new HashMap<>();
                                for (int i = 0; i < cardMessagesList.size(); i++)
                                {
                                    CardMessage temporary = cardMessagesList.get(i);
                                    cloudMessage.put(temporary.getArticle_title(),temporary.getArticle_text());
                                }
                                //全部数据存入数据
                                cardMessages = new ArrayList<Map.Entry<String, String>>(cloudMessage.entrySet());

                                //启动intent用
                                bundle.putInt("sum",count);
                                message.setData(bundle);
                                message.what = 123;
                                handler.sendMessage(message);
                            }else
                            {
                                Log.e("smile", "错误码："+e.getErrorCode()+"，错误描述："+e.getMessage());
                            }
                        }
                    }, new Object[]{LoginFragment.Account_Name, LoginFragment.Account_Mail});

                }catch (InterruptedException e)
                {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    public Handler handler = new Handler()
    {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 123)
            {

                //设置适配器
                control_init();
                setAdapter();
                setListeners();
            }
        }
    };

    public void control_init()
    {
        btn_back = (ImageView)findViewById(R.id.back);
        lsv_side_slip_delete = findViewById(R.id.lsv_side_slip_delete);
    }


    @Override
    protected void onPostResume() {
        super.onPostResume();


    }
}

