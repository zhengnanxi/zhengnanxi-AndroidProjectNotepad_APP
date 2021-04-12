package com.example.test3.ui.glidetools;

import android.content.Context;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.hjq.permissions.OnPermission;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;

import java.util.List;

public class Tools
{
    //请求权限--相册与相机
    public static void requestPermissions(final AppCompatActivity activity)
    {
        XXPermissions.with(activity)
                .permission(Permission.Group.STORAGE)   //相册
                .permission(Permission.CAMERA)          //相机
                .request(new OnPermission() {
                    @Override
                    public void hasPermission(List<String> granted, boolean all)
                    {

                    }

                    @Override
                    public void noPermission(List<String> denied, boolean quick)
                    {

                    }
                });
    }

    //打开图库+拍照按钮
    public static void galleryPictures(AppCompatActivity activity, int maxSize)
    {
        PictureSelector.create(activity)
                .openGallery(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                //.theme()//主题样式(不设置为默认样式) 也可参考demo values/styles下 例如：R.style.picture.white.style
                .maxSelectNum(maxSize)// 最大图片选择数量 int
                .minSelectNum(1)// 最小选择数量 int
                .imageEngine(GlideEngine.createGlideEngine())
                .imageSpanCount(3)// 每行显示个数 int
                .isCamera(true)// 是否显示拍照按钮 true or false
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                .isEnableCrop(false)// 不进行裁剪
                .isCompress(true)// 压缩图片
                .minimumCompressSize(100)// 小于100kb的图片不压缩
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
    }

    //加载圆角图片
    public static void showGlide(Context context, ImageView view, String url) {
        //设置圆形图片-设置参数
        RequestOptions options = new RequestOptions()
                //.error(R.mipmap.ic_add)
                .transform(new GlideRoundTransform(context,5));
        Glide.with(context)
                .load(url)
                .apply(options)
                .into(view);
    }

}
