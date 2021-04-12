package com.example.test3.ui.home;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;


import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;


import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.test3.LoginFragment;
import com.example.test3.MainActivity;
import com.example.test3.R;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.io.FileNotFoundException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SQLQueryListener;
import cn.bmob.v3.listener.SaveListener;

import static android.app.Activity.RESULT_OK;


public class HomeFragment extends Fragment {
    private List<CardMessage> cardMessagesList; //数据源
    private CarAdapter mcaradapter;     //适配器
    public RecyclerView recyclerView;
    public FloatingActionButton create;
    public FloatingActionButton cloud_up;
    public FloatingActionButton cloud_down;
    public FloatingActionButton item_search;
    public SQLiteHelper mSQLiteHelper;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Bmob.initialize(getActivity(),"你的比目后端云ID");



    }

    public View onCreateView(
            @NonNull LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        //获取RecyclerView
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        //创建数据库
        mSQLiteHelper = new SQLiteHelper(getActivity());
        cardMessagesList = mSQLiteHelper.query();
        showQueryData();

        return view;

        /*---------------------测试----------------------
        //获取RecyclerView
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        //创建adapte
        mcaradapter = new CarAdapter(getActivity(),cardMessagesList);
        //给RecyclerView设置adapter
        recyclerView.setAdapter(mcaradapter);

        //设置layoutManager
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),1,GridLayoutManager.VERTICAL,false));
        //recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        int largePadding = getResources().getDimensionPixelSize(R.dimen.shr_product_grid_spacing);
        int smallPadding = getResources().getDimensionPixelSize(R.dimen.shr_product_grid_spacing_small);
        recyclerView.addItemDecoration(new CardItemDecoration(largePadding,smallPadding));

         */

        //CardMessage cardMessage = new CardMessage();
        //cardMessage.setImage_theme_id(R.id.search);
        //cardMessage.setArticle_title("测试标题");
        //cardMessage.setArticle_text("这是第一篇出行记录随笔");
        //cardMessage.setId("0");
        //cardMessagesList.add(cardMessage);
        //mSQLiteHelper.insertData(cardMessage.getArticle_title(),cardMessage.getArticle_text());
        //cardMessagesList = mSQLiteHelper.query();

        /*
        mcaradapter = new CarAdapter(getActivity(),cardMessagesList);
        //给RecyclerView设置adapter
        recyclerView.setAdapter(mcaradapter);
        //设置layoutManager
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),1,GridLayoutManager.VERTICAL,false));
        //recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        int largePadding = getResources().getDimensionPixelSize(R.dimen.shr_product_grid_spacing);
        int smallPadding = getResources().getDimensionPixelSize(R.dimen.shr_product_grid_spacing_small);
        recyclerView.addItemDecoration(new CardItemDecoration(largePadding,smallPadding));
        *///---------------------测试----------------------


    }

    //悬浮按钮实现
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //浮动按钮的监听
        create = (FloatingActionButton) getActivity().findViewById(R.id.fab_create);
        cloud_up = (FloatingActionButton) getActivity().findViewById(R.id.fab_cloudup);
        cloud_down = (FloatingActionButton) getActivity().findViewById(R.id.fab_clouddown);
        item_search = (FloatingActionButton) getActivity().findViewById(R.id.fab_search);

        //创建笔记
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "您点击了新建笔记", Toast.LENGTH_SHORT).show();
                //跳到新建笔记控制类
                Intent intent = new Intent();
                intent.setClass(getActivity(),RecordActivity.class);
                //startActivityForResult(intent,1);
                startActivityForResult(intent,1);

            }
        });
        //数据上传云空间
        cloud_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "您点击了上传云空间", Toast.LENGTH_SHORT).show();
                cardMessagesList = mSQLiteHelper.query();
                for (int i = 0; i < cardMessagesList.size(); i++){
                    CardMessage cm = cardMessagesList.get(i);
                    CardMessage bc = new CardMessage();
                    //数据保存的云数据库表里
                    bc.setUser_name(LoginFragment.Account_Name);
                    bc.setUser_mail(LoginFragment.Account_Mail);
                    bc.setImage_theme_id(cm.getImage_theme_id());
                    bc.setArticle_title(cm.getArticle_title());
                    bc.setArticle_text(cm.getArticle_text());
                    bc.setUser_Picture_1(cm.getUser_Picture_1());
                    bc.setUser_Picture_2(cm.getUser_Picture_2());
                    bc.setUser_Picture_3(cm.getUser_Picture_3());
                    bc.setUser_Picture_4(cm.getUser_Picture_4());
                    bc.setUser_Picture_5(cm.getUser_Picture_5());
                    bc.setUser_Picture_6(cm.getUser_Picture_6());
                    bc.setUser_Picture_7(cm.getUser_Picture_7());
                    bc.setUser_Picture_8(cm.getUser_Picture_8());
                    bc.setUser_Picture_9(cm.getUser_Picture_9());
                    bc.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if(e==null){
                                Toast.makeText(getActivity(), "添加数据成功，返回objectId为：" + s, Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getActivity(), "数据已经同步过了   PS:注意标题不能重复(￣▽￣)~*", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
                mcaradapter.notifyDataSetChanged();
                //Item设置出来

                if (cardMessagesList != null){
                    cardMessagesList.clear();
                }
                cardMessagesList = mSQLiteHelper.query();
                mcaradapter = new CarAdapter(getActivity(),cardMessagesList);
                //给RecyclerView设置adapter
                recyclerView.setAdapter(mcaradapter);

                //Item监听
                mcaradapter.setOnItemClickListener(new CarAdapter.OnItemClickListener() {
                    @Override
                    public void OnItemClick(View view, int position) {
                        CardMessage cardMessage = cardMessagesList.get(position);
                        ArrayList<String> p = new ArrayList<String>();
                        p.add(cardMessage.getUser_Picture_1());
                        p.add(cardMessage.getUser_Picture_2());
                        p.add(cardMessage.getUser_Picture_3());
                        p.add(cardMessage.getUser_Picture_4());
                        p.add(cardMessage.getUser_Picture_5());
                        p.add(cardMessage.getUser_Picture_6());
                        p.add(cardMessage.getUser_Picture_7());
                        p.add(cardMessage.getUser_Picture_8());
                        p.add(cardMessage.getUser_Picture_9());

                        Intent intent = new Intent(getActivity(),RecordActivity.class);
                        intent.putExtra("themid",cardMessage.getImage_theme_id());
                        intent.putExtra("id",cardMessage.getId());
                        intent.putExtra("title",cardMessage.getArticle_title());
                        intent.putExtra("content",cardMessage.getArticle_text());

                        intent.putStringArrayListExtra("picturelist", p);
                        startActivityForResult(intent,1);
                    }

                    @Override
                    public void OnItemLongClick(View view, final int position) {
                        Toast.makeText(getActivity(), "被长按" + position, Toast.LENGTH_SHORT).show();
                        mcaradapter.notifyDataSetChanged();
                        //弹出删除框
                        AlertDialog dialog;
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()).setMessage("是否确认删除此笔记？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                CardMessage cardMessage = cardMessagesList.get(position);
                                if (mSQLiteHelper.deleteData(cardMessage.getId())){
                                    cardMessagesList.remove(position);
                                    mcaradapter.notifyDataSetChanged();
                                    Toast.makeText(getActivity(), "成功删除该笔记(*^▽^*)", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Toast.makeText(getActivity(), "已关闭删除对话框", Toast.LENGTH_SHORT).show();
                            }
                        });
                        dialog = builder.create();
                        dialog.show();

                    }
                });

            }
        });

        /*
            ###########################################
            #   暂时无法做到数据恢复 ---补个位在这里  #
            ###########################################

        */
        //数据恢复---想不到吧，我准备做了(*^▽^*)
        cloud_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "准备查询云空间！", Toast.LENGTH_SHORT).show();
                //查询数据库
                String sql = "select * from CardMessage where user_name = ? and user_mail = ?";
                new BmobQuery<CardMessage>().doSQLQuery(sql, new SQLQueryListener<CardMessage>() {
                    @Override
                    public void done(BmobQueryResult<CardMessage> bmobQueryResult, BmobException e) {
                        if (e == null){
                            List<CardMessage> list = (List<CardMessage>) bmobQueryResult.getResults();
                            List<CardMessage> old_message = mSQLiteHelper.query();
                            Log.e("list: ", list.toString());
                            Log.e("old_message:", old_message.toString());
                            SQLiteHelper mSQLiteHelper_test = new SQLiteHelper(getActivity());
                            //判断数据库是否为空
                            if (list != null && list.size() > 0){
                                //如果本地数据库空的--直接导入全部云空间数据
                                if (old_message.size() == 0){
                                    for (int j = 0; j < list.size(); j++){
                                        mSQLiteHelper.insertData(
                                                LoginFragment.Account_Name,
                                                LoginFragment.Account_Mail,
                                                list.get(j).getArticle_title(),
                                                list.get(j).getArticle_text(),
                                                list.get(j).getUser_Picture_1(),
                                                list.get(j).getUser_Picture_2(),
                                                list.get(j).getUser_Picture_3(),
                                                list.get(j).getUser_Picture_4(),
                                                list.get(j).getUser_Picture_5(),
                                                list.get(j).getUser_Picture_6(),
                                                list.get(j).getUser_Picture_7(),
                                                list.get(j).getUser_Picture_8(),
                                                list.get(j).getUser_Picture_9());
                                    }
                                } else {
                                    //循环整个数据库数据----
                                    // PS：算法忘了差不多，只能用穷举法--太难了，兄弟！建议好好学习算法！！
                                    Log.e("test","不知道会不会运行");
                                    List<CardMessage> make = new ArrayList<CardMessage>();
                                    int a = 0;
                                    for (int i = list.size()-1; i >= 0; i--){
                                        Log.e("list","" + list.get(i));
                                        Log.e("old","" + old_message.get(a));
                                        if (a <= old_message.size() &&
                                                (list.get(i).getArticle_title().equals(old_message.get(a).getArticle_title())) &&
                                                (list.get(i).getUser_name().equals(old_message.get(a).getUser_name()))){
                                            a++;
                                        } else {
                                            make.add(list.get(i));
                                            a++;
                                        }
                                    }
                                    if (make.size() == 0)
                                    {
                                        Toast.makeText(getActivity(), "本地数据与云端一致！！！", Toast.LENGTH_SHORT).show();
                                    }
                                    for (int s = 0; s < make.size(); s++){
                                        //原先数据添加
                                        Log.e("make","" + make.get(s));
                                        mSQLiteHelper.insertData(
                                                LoginFragment.Account_Name,
                                                LoginFragment.Account_Mail,
                                                make.get(s).getArticle_title(),
                                                make.get(s).getArticle_text(),
                                                make.get(s).getUser_Picture_1(),
                                                make.get(s).getUser_Picture_2(),
                                                make.get(s).getUser_Picture_3(),
                                                make.get(s).getUser_Picture_4(),
                                                make.get(s).getUser_Picture_5(),
                                                make.get(s).getUser_Picture_6(),
                                                make.get(s).getUser_Picture_7(),
                                                make.get(s).getUser_Picture_8(),
                                                make.get(s).getUser_Picture_9());
                                        //暂时添加列表用，避免重复item出现
                                        //mSQLiteHelper_test.insertData(make.get(s).getImage_theme_id(),
                                          //      make.get(s).getArticle_title(),
                                            //    make.get(s).getArticle_text());
                                    }

                                    /*------------上个版本的匹配算法(问题很大，已弃用)---------------//
                                    for (int i = list.size(); i >= 0; i--){
                                        int z = 0;
                                        Log.e("list","" + list.get(i));
                                        Log.e("oldmessage","" + old_message.get(z));

                                        //与本地数据库匹配----如果本地有数据，则逐个匹配
                                        if (list.get(i).getArticle_title() != old_message.get(z).getArticle_title()){
                                            //插入本地数据库
                                            mSQLiteHelper.insertData(list.get(i).getImage_theme_id(),
                                                    list.get(i).getArticle_title(),
                                                    list.get(i).getArticle_text());
                                            }
                                        z++;
                                    }
                                    *///------------上个版本的匹配算法(问题很大，已弃用)---------------//
                                }

                                mcaradapter.notifyDataSetChanged();
                                //Item设置出来

                                if (cardMessagesList != null){
                                    cardMessagesList.clear();
                                }
                                cardMessagesList = mSQLiteHelper.query();
                                mcaradapter = new CarAdapter(getActivity(),cardMessagesList);
                                //给RecyclerView设置adapter
                                recyclerView.setAdapter(mcaradapter);

                                //Item监听
                                mcaradapter.setOnItemClickListener(new CarAdapter.OnItemClickListener() {
                                    @Override
                                    public void OnItemClick(View view, int position) {
                                        CardMessage cardMessage = cardMessagesList.get(position);
                                        ArrayList<String> p = new ArrayList<String>();
                                        p.add(cardMessage.getUser_Picture_1());
                                        p.add(cardMessage.getUser_Picture_2());
                                        p.add(cardMessage.getUser_Picture_3());
                                        p.add(cardMessage.getUser_Picture_4());
                                        p.add(cardMessage.getUser_Picture_5());
                                        p.add(cardMessage.getUser_Picture_6());
                                        p.add(cardMessage.getUser_Picture_7());
                                        p.add(cardMessage.getUser_Picture_8());
                                        p.add(cardMessage.getUser_Picture_9());

                                        Intent intent = new Intent(getActivity(),RecordActivity.class);
                                        intent.putExtra("themid",cardMessage.getImage_theme_id());
                                        intent.putExtra("id",cardMessage.getId());
                                        intent.putExtra("title",cardMessage.getArticle_title());
                                        intent.putExtra("content",cardMessage.getArticle_text());

                                        intent.putStringArrayListExtra("picturelist", p);
                                        startActivityForResult(intent,1);
                                    }

                                    @Override
                                    public void OnItemLongClick(View view, final int position) {
                                        Toast.makeText(getActivity(), "被长按" + position, Toast.LENGTH_SHORT).show();
                                        mcaradapter.notifyDataSetChanged();
                                        //弹出删除框
                                        AlertDialog dialog;
                                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()).setMessage("是否确认删除此笔记？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                CardMessage cardMessage = cardMessagesList.get(position);
                                                if (mSQLiteHelper.deleteData(cardMessage.getId())){
                                                    cardMessagesList.remove(position);
                                                    mcaradapter.notifyDataSetChanged();
                                                    Toast.makeText(getActivity(), "成功删除该笔记(*^▽^*)", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                                Toast.makeText(getActivity(), "已关闭删除对话框", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                        dialog = builder.create();
                                        dialog.show();

                                    }
                                });
                                /*
                                //添加完后，更新整个适配器----用的是暂时用数据表---取消该方法---
                                if (cardMessagesList != null){
                                    cardMessagesList.clear();
                                }

                                cardMessagesList = mSQLiteHelper_test.query();
                                mcaradapter = new CarAdapter(getActivity(),cardMessagesList);

                                //给RecyclerView设置adapter
                                recyclerView.setAdapter(mcaradapter);
                                //Item监听
                                mcaradapter.setOnItemClickListener(new CarAdapter.OnItemClickListener() {
                                    @Override
                                    public void OnItemClick(View view, int position) {
                                        CardMessage cardMessage = cardMessagesList.get(position);
                                        Intent intent = new Intent(getActivity(),RecordActivity.class);
                                        intent.putExtra("themid",cardMessage.getImage_theme_id());
                                        intent.putExtra("id",cardMessage.getId());
                                        intent.putExtra("title",cardMessage.getArticle_title());
                                        intent.putExtra("content",cardMessage.getArticle_text());
                                        startActivityForResult(intent,1);
                                    }

                                    @Override
                                    public void OnItemLongClick(View view, final int position) {
                                        Toast.makeText(getActivity(), "被长按" + position, Toast.LENGTH_SHORT).show();
                                        mcaradapter.notifyDataSetChanged();
                                        //弹出删除框
                                        AlertDialog dialog;
                                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()).setMessage("是否确认删除此笔记？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                CardMessage cardMessage = cardMessagesList.get(position);
                                                if (mSQLiteHelper.deleteData(cardMessage.getId())){
                                                    cardMessagesList.remove(position);
                                                    mcaradapter.notifyDataSetChanged();
                                                    Toast.makeText(getActivity(), "成功删除该笔记(*^▽^*)", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                                Toast.makeText(getActivity(), "已关闭删除对话框", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                        dialog = builder.create();
                                        dialog.show();
                                    }
                                });*/
                            }
                            else
                            {
                                Toast.makeText(getActivity(), "云端数据库为空！！", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Log.e("smile", "错误码："+e.getErrorCode()+"，错误描述："+e.getMessage());
                        }
                    }
                },LoginFragment.Account_Name, LoginFragment.Account_Mail);
            }
        });

        //笔记搜索
        item_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                AlertDialog dialog;
                View dialogview = View.inflate(getActivity(), R.layout.search_item_layout, null);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        TextInputEditText search_title_request = (TextInputEditText) dialogview.findViewById(R.id.search_title);
                        String title = search_title_request.getText().toString();
                        mcaradapter.getFilter().filter(title);
                        dialog.dismiss();

                    }
                });


                dialog = builder.create();
                dialog.setView(dialogview);
                dialog.show();

            }
        });



        //Item监听
        mcaradapter.setOnItemClickListener(new CarAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                CardMessage cardMessage = cardMessagesList.get(position);
                ArrayList<String> p = new ArrayList<String>();
                p.add(cardMessage.getUser_Picture_1());
                p.add(cardMessage.getUser_Picture_2());
                p.add(cardMessage.getUser_Picture_3());
                p.add(cardMessage.getUser_Picture_4());
                p.add(cardMessage.getUser_Picture_5());
                p.add(cardMessage.getUser_Picture_6());
                p.add(cardMessage.getUser_Picture_7());
                p.add(cardMessage.getUser_Picture_8());
                p.add(cardMessage.getUser_Picture_9());
                Intent intent = new Intent(getActivity(),RecordActivity.class);
                intent.putExtra("themid",cardMessage.getImage_theme_id());
                intent.putExtra("id",cardMessage.getId());
                intent.putExtra("title",cardMessage.getArticle_title());
                intent.putExtra("content",cardMessage.getArticle_text());
                intent.putStringArrayListExtra("picturelist", p);
                startActivityForResult(intent,1);


            }

            @Override
            public void OnItemLongClick(View view, int position) {
                Toast.makeText(getActivity(), "被长按" + position, Toast.LENGTH_SHORT).show();
                mcaradapter.notifyDataSetChanged();
            }

        });

        //Item的监听-------------旧的---------
         /*mcaradapter.setOnItemClickListener(new CarAdapter.OnItemClickListener() {


            @Override
            public void onClick(int position) {
                Toast.makeText(getActivity(), "被点击" + position, Toast.LENGTH_SHORT).show();

                CardMessage cardMessage = cardMessagesList.get(position);
                Intent intent = new Intent(getActivity(),RecordActivity.class);
                intent.putExtra("id",cardMessage.getId());
                intent.putExtra("title",cardMessage.getArticle_title());
                intent.putExtra("content",cardMessage.getArticle_text());
                startActivityForResult(intent,1);

            }
        });*/
        /*mcaradapter.setOnItemLongClickListener(new CarAdapter.OnItemLongClickListener() {
            @Override
            public void onClick(int position) {
                Toast.makeText(getActivity(), "被长按" + position, Toast.LENGTH_SHORT).show();
            }
        });*///-------------旧的---------

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mcaradapter.notifyDataSetChanged();
        //Item设置出来
        if (requestCode == 1 && resultCode == 2){
            if (cardMessagesList != null){
                cardMessagesList.clear();
            }
            cardMessagesList = mSQLiteHelper.query();
            mcaradapter = new CarAdapter(getActivity(),cardMessagesList);
            //给RecyclerView设置adapter
            recyclerView.setAdapter(mcaradapter);
        }
    }

    public void showQueryData(){
        if (cardMessagesList != null){
            cardMessagesList.clear();

        }
        cardMessagesList = mSQLiteHelper.query();
        mcaradapter = new CarAdapter(getActivity(),cardMessagesList);

        //给RecyclerView设置adapter
        recyclerView.setAdapter(mcaradapter);

        //设置layoutManager
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),1,GridLayoutManager.VERTICAL,false));
        //recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        int largePadding = getResources().getDimensionPixelSize(R.dimen.shr_product_grid_spacing);
        int smallPadding = getResources().getDimensionPixelSize(R.dimen.shr_product_grid_spacing_small);
        recyclerView.addItemDecoration(new CardItemDecoration(largePadding,smallPadding));



    }


    //该fragment重新启动时，解决适配器问题
    @Override
    public void onResume() {

        super.onResume();
        if (cardMessagesList != null){
            cardMessagesList.clear();
        }
        cardMessagesList = mSQLiteHelper.query();
        mcaradapter = new CarAdapter(getActivity(),cardMessagesList);

        //给RecyclerView设置adapter
        recyclerView.setAdapter(mcaradapter);
        //Item监听
        mcaradapter.setOnItemClickListener(new CarAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                CardMessage cardMessage = cardMessagesList.get(position);
                ArrayList<String> p = new ArrayList<String>();
                p.add(cardMessage.getUser_Picture_1());
                p.add(cardMessage.getUser_Picture_2());
                p.add(cardMessage.getUser_Picture_3());
                p.add(cardMessage.getUser_Picture_4());
                p.add(cardMessage.getUser_Picture_5());
                p.add(cardMessage.getUser_Picture_6());
                p.add(cardMessage.getUser_Picture_7());
                p.add(cardMessage.getUser_Picture_8());
                p.add(cardMessage.getUser_Picture_9());
                Intent intent = new Intent(getActivity(),RecordActivity.class);
                intent.putExtra("themid",cardMessage.getImage_theme_id());
                intent.putExtra("id",cardMessage.getId());
                intent.putExtra("title",cardMessage.getArticle_title());
                intent.putExtra("content",cardMessage.getArticle_text());
                intent.putStringArrayListExtra("picturelist", p);
                startActivityForResult(intent,1);
            }

            @Override
            public void OnItemLongClick(View view, final int position) {
                Toast.makeText(getActivity(), "被长按" + position, Toast.LENGTH_SHORT).show();
                mcaradapter.notifyDataSetChanged();
                //弹出删除框
                AlertDialog dialog;
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()).setMessage("是否确认删除此笔记？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CardMessage cardMessage = cardMessagesList.get(position);
                        if (mSQLiteHelper.deleteData(cardMessage.getId())){
                            cardMessagesList.remove(position);
                            mcaradapter.notifyDataSetChanged();
                            Toast.makeText(getActivity(), "成功删除该笔记(*^▽^*)", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Toast.makeText(getActivity(), "已关闭删除对话框", Toast.LENGTH_SHORT).show();
                    }
                });
                dialog = builder.create();
                dialog.show();

            }
        });



    }
}