package com.example.test3.ui.Weather;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class tianqi {
    public static JSONObject result(){

        //String requestUrl = "https://tianqiapi.com/free/week";
        //接口地址
        String requestUrl = "https://tianqiapi.com/api";
        //params用于存储要请求的参数
        Map params = new HashMap();
        //必要的KEY之类的
        params.put("appid",你的id);
        params.put("appsecret","你的密钥");
        //params.put("city",city_user);//只能输入市！！！
        //调用httpRequest方法，这个方法主要用于请求地址，并加上请求参数
        String str = httpRequest(requestUrl,params);
        //处理返回的JSON数据并返回
        JSONObject jsonObject = new JSONObject();
        JSONObject pageBean = jsonObject.fromObject(str);
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
            httpUrlConn.setRequestMethod("GET");
            httpUrlConn.connect();

            //获得输入
            InputStream inputStream = httpUrlConn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
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

    public static JSONObject InitArray(){
        JSONObject message = new JSONObject();
        message = result();
        //JSONArray jsonArray = message.getJSONArray("data");
        return message;
    }



    public static void main(String[] args){
        JSONObject message = new JSONObject();
        //message = result("北京");

        JSONArray jsonArray = message.getJSONArray("data");

        for (int i =0; i < jsonArray.size(); i++){
            JSONObject obj = JSONObject.fromObject(jsonArray.get(i));
            String date = obj.getString("date");
            String wea = obj.getString("wea");
            String now = obj.getString("tem");
            String hot = obj.getString("tem1");
            String low = obj.getString("tem2");
            if (i == 0){
                String kongqi = obj.getString("air_level");
                System.out.println("日期:" + date + "\n" +
                        "天气状况:" + wea + "\n" +
                        "实时温度" + now + "\n" +
                        "最高温度" + hot + "\n" +
                        "最低温度" + low + "\n" +
                        "空气质量"  + kongqi + "\n");
            }
            System.out.println("日期:" + date + "\n" +
                    "天气状况:" + wea + "\n" +
                    "实时温度" + now + "\n" +
                     "最高温度" + hot + "\n" +
                    "最低温度" + low + "\n" );
        }

    }
}
