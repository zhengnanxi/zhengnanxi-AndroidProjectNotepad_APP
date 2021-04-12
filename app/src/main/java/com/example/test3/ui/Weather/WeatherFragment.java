package com.example.test3.ui.Weather;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import com.example.test3.R;
import com.google.gson.JsonObject;


import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WeatherFragment extends Fragment {

    private GalleryViewModel galleryViewModel;
    public List<Weather_Message> weather_list = new ArrayList<Weather_Message>();
    public ListView listView;
    public TextView tem_now;
    public TextView tem_now_zhuankuan;
    public TextView tem_now_air;
    public ImageView tem_now_pc;
    public TextView tem_city;

    public Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 123){
                Bundle bundle = msg.getData();
                String[] data = bundle.getStringArray("data");     //日期
                String[] wea_img = bundle.getStringArray("wea_img");   //天气图标
                String[] tem = bundle.getStringArray("tem");     //实时温度
                String[] tem1 = bundle.getStringArray("tem1");      //高温
                String[] tem2 = bundle.getStringArray("tem2");      //低温
                String[] wea = bundle.getStringArray("wea");       //天气状况
                String air = bundle.getString("air");     //空气质量--只有当天一天而已
                String city = bundle.getString("city");

                //首页设置---当天天气信息
                tem_now.setText(tem[0]);
                tem_now_air.setText("空气质量:" + air);
                tem_now_zhuankuan.setText(wea[0]);
                tem_now_pc.setImageResource(switch_img(wea_img[0]));
                tem_city.setText(city);

                //其余3天天气
                //当天
                Weather_Message today = new Weather_Message("今天",switch_img(wea_img[0]),wea[0],tem1[0],tem2[0]);
                weather_list.add(today);
                //明天
                Weather_Message tomorrow = new Weather_Message(data[1],switch_img(wea_img[1]),wea[1],tem1[1],tem2[1]);
                weather_list.add(tomorrow);
                //后天------别吐槽命名了。。。太长不好写，都是单词多头字母
                Weather_Message t_d_a_t = new Weather_Message(data[2],switch_img(wea_img[2]),wea[2],tem1[2],tem2[2]);
                weather_list.add(t_d_a_t);
                //大后天
                Weather_Message t_d_f_n = new Weather_Message(data[3],switch_img(wea_img[3]),wea[3],tem1[3],tem2[3]);
                weather_list.add(t_d_f_n);

                //启动适配器
                WeatherAdapter adapter = new WeatherAdapter(getContext(),R.layout.weather_item_layout,weather_list);
                listView.setAdapter(adapter);
            }
        }
    };

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel = ViewModelProviders.of(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_weather, container, false);

        listView = (ListView) root.findViewById(R.id.weather_list);        //列表控件
        tem_now = (TextView) root.findViewById(R.id.tempera_now);          //实时温度
        tem_now_zhuankuan = (TextView) root.findViewById(R.id.weather_tianqizhuangkuan);   //目前天气状况信息
        tem_now_air = (TextView) root.findViewById(R.id.weather_air);        //空气质量
        tem_now_pc = (ImageView) root.findViewById(R.id.weather_image);      //目前天气图片
        tem_city = (TextView) root.findViewById(R.id.city_name);        //城市



        get_weather_json();


        return root;
    }

    public void initMessage(){


    }

    //获取天气数据---3天
    public void get_weather_json(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String[] data = new String[4];      //日期
                    String[] wea_img = new String[4];   //天气图标
                    String[] tem = new String[4];     //实时温度
                    String[] tem1 = new String[4];      //高温
                    String[] tem2 = new String[4];      //低温
                    String[] wea = new String[4];       //天气状况
                    String air = null;     //空气质量--只有当天一天而已
                    String city = null;

                    Thread.sleep(500);
                    JSONObject message1 = tianqi.InitArray();
                    city = message1.getString("city");
                    JSONArray jsonArray = message1.getJSONArray("data");
                    for (int i =0; i < 4; i++){
                        JSONObject obj = JSONObject.fromObject(jsonArray.get(i));
                        data[i] = obj.getString("date").substring(8);             //日期
                        wea_img[i] = obj.getString("wea_img");        //天气图标
                        wea[i] = obj.getString("wea");         //天气状况
                        tem[i] = obj.getString("tem");          //实时温度
                        tem1[i] = obj.getString("tem1");          //高温
                        tem2[i] = obj.getString("tem2");         //低温
                        if (i == 0){
                            air = obj.getString("air_level");       //空气质量--只有当天一天
                        }
                    }

                    //存放到handle
                    Bundle bundle = new Bundle();
                    Message message = Message.obtain();
                    bundle.putStringArray("data",data);
                    bundle.putStringArray("wea_img",wea_img);
                    bundle.putStringArray("tem",tem);
                    bundle.putStringArray("tem1",tem1);
                    bundle.putStringArray("tem2",tem2);
                    bundle.putStringArray("wea",wea);
                    bundle.putString("air",air);
                    bundle.putString("city",city);
                    message.setData(bundle);
                    message.what = 123;
                    handler.sendMessage(message);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public int switch_img(String img){
        int id = 0;

        switch (img){
            case "qing":        //晴天
                id = R.drawable.ic_stat_weather_picture;
                break;
            case "xue":         //雪
                id = R.drawable.weather_new_snow;
                break;
            case "lei":
                id = R.drawable.weather_lei_new;
                break;
            case "shachen":
                id = R.drawable.weather_shachen;
                break;
            case "wu":
                id = R.drawable.weather_wu;
                break;
            case "yun":
                id = R.drawable.weather_duoyun_new;
                break;
            case "yu":
                id = R.drawable.weather_rain;
                break;
            case "yin":
                id = R.drawable.yin;
                break;
        }
        return id;
    }

}