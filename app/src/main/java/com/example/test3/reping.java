package com.example.test3;


import android.util.Log;

import net.sf.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class reping {
    public static JSONObject result(){

        //String requestUrl = "https://api.66mz8.com/api/music.163.php";        //--目前不能用--2020/6/21
        //接口地址
        String requestUrl = "https://api.uomg.com/api/comments.163";            //暂时--2020/6/21
        //params用于存储要请求的参数
        Map params = new HashMap();
        //必要的KEY之类的
        params.put("mid",430657150);        //选定特定的歌单ID--来着我喜欢的音乐(～￣▽￣)～
        params.put("format","JSON");        //返回json格式
        //params.put("appsecret","xV3Ok6Cx");
        //params.put("city",city_user);//只能输入市！！！
        //调用httpRequest方法，这个方法主要用于请求地址，并加上请求参数
        String str = httpRequest(requestUrl,params);
        //处理返回的JSON数据并返回
        //JSONArray jsonArray = JSONArray.fromObject(str);  --返回json格式是数组时用的
        //JSONObject jsonObject = new JSONObject();         --可有可无
        JSONObject pageBean = JSONObject.fromObject(str.trim());
        return pageBean;
    }
    private static String httpRequest(String requestUrl, Map params){
        StringBuffer buffer = new StringBuffer();
        try {
            //建立URL，把请求地址给补全，其中urlencode（）方法用于把params里的参数给取出来
            URL url = new URL(requestUrl+"?"+urlencode(params));
            //打开http连接
            HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();
            httpUrlConn.setDoInput(true);
            httpUrlConn.setRequestMethod("GET");        //GET方法请求，post我不弄。。
            httpUrlConn.connect();

            //获得输入
            InputStream inputStream = httpUrlConn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            //将bufferReader的值给放到buffer里
            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            //关闭bufferReader和输入流
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
            inputStream = null;
            //断开连接
            httpUrlConn.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
        //返回字符串
        return buffer.toString();

    }

    //请求连接完善
    public static String urlencode(Map<String,Object>data) {
        //将map里的参数变成像 showapi_appid=###&showapi_sign=###&的样子
        StringBuilder sb = new StringBuilder();
        for (Map.Entry i : data.entrySet()) {
            try {
                sb.append(i.getKey()).append("=").append(URLEncoder.encode(i.getValue()+"","UTF-8")).append("&");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }


    public static JSONObject test(){
        JSONObject message = new JSONObject();
        message = result();
        System.out.println(message);
        JSONObject jsonArray = message.getJSONObject("data");
        String name = jsonArray.getString("name");
        String comment = jsonArray.getString("content");
        System.out.println("歌名为：" + name);
        System.out.println("评论：" + comment);
        return jsonArray;

    }


    //先执行API获得数据
    public static JSONObject InitMessage(){
        JSONObject message = new JSONObject();
        message = result();
        JSONObject jsonArray = message.getJSONObject("data");
        Log.e("json",""+jsonArray);
        return jsonArray;
    }

    //--------------弃用------------------

    //提取歌名
    public static String get_comment_name (JSONObject jsonArray){
        String name = jsonArray.getString("name");
        return name;
    }

    //提取评论
    public static String get_comment_note (JSONObject jsonArray){
        String comment = jsonArray.getString("content");
        return comment;
    }

    public static void main(String[] args)
    {
        //test();
    }

    //--------------弃用------------------
}
