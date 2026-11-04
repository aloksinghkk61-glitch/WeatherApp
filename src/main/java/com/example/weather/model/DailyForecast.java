package com.example.weather.model;

public record DailyForecast(
        String date,
        double maxTemp,
        double minTemp,
        int precipitationChance,
        String condition,
        String icon) {
}
