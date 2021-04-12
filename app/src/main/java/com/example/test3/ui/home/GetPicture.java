package com.example.test3.ui.home;

import java.io.File;

public class GetPicture {
    public static final String PICTURE_PATH = new String("mnt/sdcard/DCIM/");
    void getAllPicture(){
        File home = new File(PICTURE_PATH);
        PictureFilter pictureFilter = new PictureFilter();
        if (home.listFiles(pictureFilter).length > 0){
            for (File file : home.listFiles(pictureFilter)){

            }
        }
    }
}
