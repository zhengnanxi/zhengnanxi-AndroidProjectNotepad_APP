package com.example.test3.ui.home;

import java.io.File;
import java.io.FilenameFilter;

public class PictureFilter implements FilenameFilter {
    @Override
    //通过像音乐播放列表一样获取图片------已弃用！！！------
    public boolean accept(File dir, String name) {
        return (name.endsWith(".jpg"));
    }
}
