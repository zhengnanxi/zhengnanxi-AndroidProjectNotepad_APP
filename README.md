# Android Trip Journal-安卓旅行日志

🚀嗨！这是本人大学安卓课程设计的作品！虽然期间经历过很多事情，但还是如愿的进行开源出来。

---------------

​	由于第一次写Readme文档，不太会写，所以我尽量的把整个项目的说明和重要部分解释清楚。😀

本项目用到很多第三方开源库，特此感谢这些大大开源的库，同时也感谢csdn许多博客的启发。🚀

由于使用了Google的`Mater Design `大家在导入依赖库可能需要科学上网来进行下载依赖库。🚀

简单的项目功能介绍：

1.  用户注册：邮箱填写、邮箱填写、密码填写、 用户登录、用户忘记密码

2. 创建记事本：编写记事本、修改记事本、删除记事本、上传记事本

3. 数据管理： 通过云服务器找回被删除的数据、本地笔记上传到云端、选择删除云端数据

4.  天气预报：获取用户当前位置的3天以内的天气情况

5.  网易云热评：获取歌曲的热门评论

6.  用户密码：修改或忘记密码

   初略的功能模块图介绍

   ![image-20210409221014574](https://raw.githubusercontent.com/zhengnanxi/zhengnanxi-AndroidProjectNotepad_APP/master/image-20210409221014574.png)

​																		**🚀<a href="https://www.bilibili.com/video/BV1m54y1b73J">项目演示视频</a>🚀**





### 项目启动注意

项目使用AndroidStudio V3.5.2 进行开发！！！

由于项目用到`比目后端云`作为后台数据库使用，我会把自己的ID清空，需要的同学，可以自行填入。除此之外，还有很多要自行填入的，我会在这里说明🚀

>1. 修改比目后端云ID
>
>  1. java\com\example\test3\ui\home\HomeFragment.java
>
>  2. java\com\example\test3\ui\share\CloudManage.java
>  3. java\com\example\test3\ui\share\ShareFragment.java
>  4. java\com\example\test3\LoginFragment.java
>  5. java\com\example\test3\Main2Activity.java
>
>  ```java
>  Bmob.initialize(getActivity(),"你的比目后端云ID");
>  ```
>
>22
>
>2. 天气API接口
>
>  本人使用的是http://www.tianqiapi.com/ 该网站的API接口
>
>​	java\com\example\test3\ui\Weather
>
>  ```java
>  //必要的KEY之类的
>  params.put("appid",你的id);
>  params.put("appsecret","你的密钥");
>  ```
>
>3. 网易云热评API说明
>
>  歌单ID是我本人的，嘿嘿嘿🚀
>
>  ```java
>  //必要的KEY之类的
>  params.put("mid",430657150);//选定特定的歌单ID--来着我喜欢的音乐(～￣▽￣)～
>  ```
>
>4. 邮箱发送方说明与设置
>
>  具体如何开启邮件服务器，请自行百度(很简单的)
>
>  java\com\example\test3\SendEmail.java
>
>  ```java
>  private final String myEmailAccount = "发送方邮箱";  //发送方
>  private final String myEmailPassword = "邮箱密钥";  //秘钥
>  ```



#### 数据库表结构说明：

项目使用安卓自带的Sqlite数据库，大家不用担心，源码自己查看就行。

其次是比目后端云的表结构

1. 用户数据表---->表名：RegisterMessage

![image-20210409225145205](https://raw.githubusercontent.com/zhengnanxi/zhengnanxi-AndroidProjectNotepad_APP/master/image-20210409225145205.png)

2. 存储用户日志表---->由于太多列，所以大家查看源码里面的sql数据自行创建

   具体位置：java\com\example\test3\ui\home\DBUtils.java





#### 写到最后

如果你喜欢本项目的话，麻烦点个关注 真的很感谢。最后，感谢这些开源项目🚀🚀🚀

>```xml
>//Glide依赖
>com.github.bumptech.glide:glide:4.8.0
>//仿QQ滑动删除依赖
>com.github.mcxtzhang:SwipeDelMenuLayout:V1.3.0'
>//图片选择器
>com.github.LuckSiege.PictureSelector:picture_library:v2.5.8
>//权限请求
>com.hjq:xxpermissions:6.5
>com.jayway.android.robotium:robotium-solo:5.6.3
>//邮件依赖
>com.sun.mail:android-mail:1.6.0
>com.sun.mail:android-activation:1.6.0
>//浮动按钮依赖
>com.getbase:floatingactionbutton:1.10.1
>```
>

