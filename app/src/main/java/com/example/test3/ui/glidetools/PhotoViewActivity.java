package com.example.test3.ui.glidetools;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.test3.R;

import java.util.ArrayList;
import java.util.List;

public class PhotoViewActivity extends AppCompatActivity
{
    private ViewPager viewPager;
    private TextView mTvImageCount;
    private int currentPosition;
    private List<String> urlLists;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_view);
        viewPager = (ViewPager)findViewById(R.id.viewpager);
        mTvImageCount = (TextView) findViewById(R.id.mTvImageCount);


    }

    private void initData()
    {
        if (urlLists == null)
        {
            urlLists = new ArrayList<>();
        }
        //获得点击位置
        currentPosition = getIntent().getIntExtra("position", 0);
        //图片集合
        urlLists = getIntent().getStringArrayListExtra("list");


    }
}
