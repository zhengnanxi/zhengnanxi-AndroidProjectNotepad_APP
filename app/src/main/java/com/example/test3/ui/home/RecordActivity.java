package com.example.test3.ui.home;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.test3.LoginFragment;
import com.example.test3.R;
import com.example.test3.ui.glidetools.GlideEngine;
import com.example.test3.ui.glidetools.SelectPlotAdapter;
import com.example.test3.ui.glidetools.Tools;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class RecordActivity extends AppCompatActivity implements View.OnClickListener {
    public ImageView note_back;
    //public ImageView note_image;
    public EditText note_title;
    public EditText content;
    public FloatingActionButton save;
    public FloatingActionButton delete;
    public SQLiteHelper mSQLiteHepler;
    public TextView note_name;
    public String id;
    public StringAndBitmap stringAndBitmap;
    public String Picture_Path;
    public static final int REQUEST_CODE = 1;
    private SelectPlotAdapter adapter;
    private ArrayList<String> allSelectList;    //所有的图片集合
    private ArrayList<String> categoryLists;    //查看图片集合
    private List<LocalMedia> selectList = new ArrayList<>();
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        stringAndBitmap = new StringAndBitmap();
        note_back = (ImageView) findViewById(R.id.note_back);
        note_title = (EditText) findViewById(R.id.tv_title);
        content = (EditText) findViewById(R.id.note_content);
        save = (FloatingActionButton) findViewById(R.id.fab_save);
        delete = (FloatingActionButton) findViewById(R.id.fab_delete);
        note_name = (TextView) findViewById(R.id.note_name);
        //note_image = (ImageView) findViewById(R.id.tv_image);     //旧版本的图片选择
        recyclerView = (RecyclerView) findViewById(R.id.recycler);

        note_back.setOnClickListener(this);
        note_title.setOnClickListener(this);
        content.setOnClickListener(this);
        save.setOnClickListener(this);
        delete.setOnClickListener(this);
        //note_image.setOnClickListener(this);      //旧版本的图片选择

        if (allSelectList == null)
        {
            allSelectList = new ArrayList<>();
        }
        if (categoryLists == null)
        {
            categoryLists = new ArrayList<>();
        }

        Tools.requestPermissions(this);
        initData();
        //initAdapter();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            //后退事件
            case R.id.note_back:
                finish();
                break;

            //清空事件
            case R.id.fab_delete:
                content.setText("");
                note_title.setText("");
                break;

            //图片设置----取消该功能
            case R.id.tv_image:
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                //intent.setType("image/*");
                //intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,REQUEST_CODE);
                break;

                //保存内容
            case R.id.fab_save:
                //获取内容

                //Bitmap bitmap = ((BitmapDrawable) note_image.getDrawable()).getBitmap();        //获取图片的Bitmap内容
                //String bitmap_string = stringAndBitmap.bitmapToString(bitmap);
                String noteContent = content.getText().toString().trim();
                String noteTitle = note_title.getText().toString().trim();
                //保存数据到数据库
                if (id != null){
                    if (noteContent.length() > 0){
                        //判断是否小于9张，是则补为空。
                        if (allSelectList.size() < 9)
                        {
                            int size = allSelectList.size();
                            for (int i = size; i < 9; i++)
                            {
                                allSelectList.add("留空");
                            }
                        }
                        if (mSQLiteHepler.updateData(LoginFragment.Account_Name, LoginFragment.Account_Mail, id,
                                noteTitle, noteContent, allSelectList.get(0),
                                allSelectList.get(1), allSelectList.get(2),
                                allSelectList.get(3), allSelectList.get(4),
                                allSelectList.get(5), allSelectList.get(6),
                                allSelectList.get(7), allSelectList.get(8))){
                            Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
                            setResult(2);
                            finish();
                        }else {
                            Toast.makeText(this, "保存失败", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(this, "内容不能为空", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    if (noteContent.length() > 0){
                        if (allSelectList.size() < 9)
                        {
                            int size = allSelectList.size();
                            for (int i = size; i < 9; i++)
                            {
                                allSelectList.add("留空");
                            }
                        }
                        if (mSQLiteHepler.insertData(LoginFragment.Account_Name, LoginFragment.Account_Mail,
                                noteTitle, noteContent, allSelectList.get(0),
                                allSelectList.get(1), allSelectList.get(2),
                                allSelectList.get(3), allSelectList.get(4),
                                allSelectList.get(5), allSelectList.get(6),
                                allSelectList.get(7), allSelectList.get(8))){
                            Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
                            setResult(2);
                            finish();
                        }else {
                            Toast.makeText(this, "保存失败", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(this, "内容不能为空", Toast.LENGTH_SHORT).show();
                    }
                }


                break;
        }

    }

    public void initData(){
        mSQLiteHepler = new SQLiteHelper(this);
        note_name.setText("添加记录");
        Intent intent = getIntent();
        if (intent != null){
            id = intent.getStringExtra("id");
            if (id != null){
                note_name.setText("修改笔记");
                note_title.setText(intent.getStringExtra("title"));
                content.setText(intent.getStringExtra("content"));
                //note_image.setImageBitmap(BitmapFactory.decodeFile(intent.getStringExtra("themid")));
                ArrayList<String> picturelist = new ArrayList<>();
                picturelist = intent.getStringArrayListExtra("picturelist");
                Log.e("数组大小", picturelist.size() + "");
                Log.e("数组内容", picturelist.toString());
                //ArrayList<String> newlist = new ArrayList<String>();
                allSelectList.clear();
                for (int i = 0; i < picturelist.size(); i++)
                {
                    if (picturelist.get(i).length() > 10)
                    {
                        allSelectList.add(picturelist.get(i));
                    }

                }
                Log.e("数组后", allSelectList.toString());
                //最多9张图片
                adapter = new SelectPlotAdapter(this, 9);
                recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
                adapter.setImageList(allSelectList);
                recyclerView.setAdapter(adapter);
                adapter.setListener(new SelectPlotAdapter.CallbackListener() {
                    @Override
                    public void add() {
                        //可添加的最大张数=9-当前已选的张数
                        int size = 9 - allSelectList.size();
                        Tools.galleryPictures(RecordActivity.this, size);
                    }

                    @Override
                    public void delete(int position) {
                        allSelectList.remove(position);
                        categoryLists.remove(position);
                        adapter.setImageList(allSelectList);
                    }

                    @Override
                    public void item(int position, List<String> mList) {

                        selectList.clear();
                        for (int i = 0; i < allSelectList.size(); i++) {
                            LocalMedia localMedia = new LocalMedia();
                            localMedia.setPath(allSelectList.get(i));
                            selectList.add(localMedia);
                        }
                        //①、图片选择器自带预览
                        PictureSelector.create(RecordActivity.this)
                                .themeStyle(R.style.picture_default_style)
                                .isNotPreviewDownload(true)//是否显示保存弹框
                                .imageEngine(GlideEngine.createGlideEngine()) // 选择器展示不出图片则添加
                                .openExternalPreview(position, selectList);
                        //②:自定义布局预览
                        //Tools.startPhotoViewActivity(MainActivity.this, categoryLists, position);
                    }
                });

            }
            else
            {
                initAdapter();
            }
        }

    }

    //调用相册返回结果处理---取消该调用
    /*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.e("requestCode","返回请求代码："+requestCode);

        try {
            if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
                Uri selectedImage = data.getData();
                Log.e("Uri","Uri为：" + selectedImage);
                String[] filePathColumns = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePathColumns[0]);
                String imagePath = c.getString(columnIndex);
                //showImage(imagePath);
                Log.e("imagepath",imagePath);
                Bitmap bm = BitmapFactory.decodeFile(imagePath);
                Log.e("bm","bm为："+bm);
                //note_image.setImageBitmap(bm);
                Picture_Path = imagePath;
                c.close();
                //获取图片
                //Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
            }
        } catch (Exception e) {
            Log.e("Exception", e.getMessage(), e);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    */
    private void initAdapter()
    {
        //最多9张图片
        adapter = new SelectPlotAdapter(this, 9);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        adapter.setImageList(allSelectList);
        recyclerView.setAdapter(adapter);
        adapter.setListener(new SelectPlotAdapter.CallbackListener() {
            @Override
            public void add() {
                //可添加的最大张数=9-当前已选的张数
                int size = 9 - allSelectList.size();
                Tools.galleryPictures(RecordActivity.this, size);
            }

            @Override
            public void delete(int position) {
                allSelectList.remove(position);
                categoryLists.remove(position);
                adapter.setImageList(allSelectList);
            }

            @Override
            public void item(int position, List<String> mList) {

                selectList.clear();
                for (int i = 0; i < allSelectList.size(); i++) {
                    LocalMedia localMedia = new LocalMedia();
                    localMedia.setPath(allSelectList.get(i));
                    selectList.add(localMedia);
                }
                //①、图片选择器自带预览
                PictureSelector.create(RecordActivity.this)
                        .themeStyle(R.style.picture_default_style)
                        .isNotPreviewDownload(true)//是否显示保存弹框
                        .imageEngine(GlideEngine.createGlideEngine()) // 选择器展示不出图片则添加
                        .openExternalPreview(position, selectList);
                //②:自定义布局预览
                //Tools.startPhotoViewActivity(MainActivity.this, categoryLists, position);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            // 结果回调
            List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
            showSelectPic(selectList);
        }
    }

    private void showSelectPic(List<LocalMedia> result) {
        for (int i = 0; i < result.size(); i++) {
            String path;
            //判断是否10.0以上
            if (Build.VERSION.SDK_INT >= 29) {
                path = result.get(i).getAndroidQToPath();
            } else {
                path = result.get(i).getPath();
            }
            allSelectList.add(path);
            categoryLists.add(path);
            Log.e("图片选择信息", "图片链接: " + path);
        }
        adapter.setImageList(allSelectList);
    }

}
