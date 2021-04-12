package com.example.test3.ui.Weather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.test3.R;

import java.util.List;

public class WeatherAdapter extends ArrayAdapter {
    private final int resourceId;
    public WeatherAdapter(@NonNull Context context, int resource, @NonNull List objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Weather_Message weather_message = (Weather_Message) getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId,null);

        //控件设置
        TextView Weather_day = (TextView) view.findViewById(R.id.riqi);
        ImageView Weather_pc = (ImageView) view.findViewById(R.id.tianqitupiao);
        TextView Weather_zhuankuan = (TextView) view.findViewById(R.id.tianqizhuangkuan);
        TextView Weather_hotandlow = (TextView) view.findViewById(R.id.qiwen);

        //设置资源
        Weather_day.setText(weather_message.getDay());
        Weather_pc.setImageResource(weather_message.getWeather_picture());
        Weather_zhuankuan.setText(weather_message.getWeather_message());
        Weather_hotandlow.setText(weather_message.getWeather_hight() + "-" + weather_message.getWeather_low());
        return view;
    }
}
