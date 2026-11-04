package com.example.weather.model;

import java.util.List;

public record WeatherReport(
        String city,
        String region,
        String country,
        String tempUnit,
        String windUnit,
        CurrentWeather current,
        List<DailyForecast> forecast) {
}
