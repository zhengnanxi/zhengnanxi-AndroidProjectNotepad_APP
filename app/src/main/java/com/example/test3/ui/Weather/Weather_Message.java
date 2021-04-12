package com.example.test3.ui.Weather;

public class Weather_Message {
    private String day;
    private int weather_picture;
    private String weather_message;
    private String weather_hight;
    private String weather_low;

    public Weather_Message(String day, int weather_picture, String weather_message, String weather_hight, String weather_low) {
        this.day = day;
        this.weather_picture = weather_picture;
        this.weather_message = weather_message;
        this.weather_hight = weather_hight;
        this.weather_low = weather_low;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public int getWeather_picture() {
        return weather_picture;
    }

    public void setWeather_picture(int weather_picture) {
        this.weather_picture = weather_picture;
    }

    public String getWeather_message() {
        return weather_message;
    }

    public void setWeather_message(String weather_message) {
        this.weather_message = weather_message;
    }

    public String getWeather_hight() {
        return weather_hight;
    }

    public void setWeather_hight(String weather_hight) {
        this.weather_hight = weather_hight;
    }

    public String getWeather_low() {
        return weather_low;
    }

    public void setWeather_low(String weather_low) {
        this.weather_low = weather_low;
    }
}
