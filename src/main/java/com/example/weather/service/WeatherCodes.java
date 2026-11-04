package com.example.weather.service;

import java.util.Map;

/** Maps WMO weather codes (used by Open-Meteo) to a label and an emoji icon. */
final class WeatherCodes {

    record Info(String label, String icon) {}

    private static final Map<Integer, Info> CODES = Map.ofEntries(
            Map.entry(0, new Info("Clear sky", "☀️")),
            Map.entry(1, new Info("Mainly clear", "🌤️")),
            Map.entry(2, new Info("Partly cloudy", "⛅")),
            Map.entry(3, new Info("Overcast", "☁️")),
            Map.entry(45, new Info("Fog", "🌫️")),
            Map.entry(48, new Info("Rime fog", "🌫️")),
            Map.entry(51, new Info("Light drizzle", "🌦️")),
            Map.entry(53, new Info("Drizzle", "🌦️")),
            Map.entry(55, new Info("Heavy drizzle", "🌧️")),
            Map.entry(56, new Info("Freezing drizzle", "🌧️")),
            Map.entry(57, new Info("Freezing drizzle", "🌧️")),
            Map.entry(61, new Info("Light rain", "🌦️")),
            Map.entry(63, new Info("Rain", "🌧️")),
            Map.entry(65, new Info("Heavy rain", "🌧️")),
            Map.entry(66, new Info("Freezing rain", "🌧️")),
            Map.entry(67, new Info("Freezing rain", "🌧️")),
            Map.entry(71, new Info("Light snow", "🌨️")),
            Map.entry(73, new Info("Snow", "🌨️")),
            Map.entry(75, new Info("Heavy snow", "❄️")),
            Map.entry(77, new Info("Snow grains", "❄️")),
            Map.entry(80, new Info("Rain showers", "🌦️")),
            Map.entry(81, new Info("Rain showers", "🌧️")),
            Map.entry(82, new Info("Violent showers", "⛈️")),
            Map.entry(85, new Info("Snow showers", "🌨️")),
            Map.entry(86, new Info("Heavy snow showers", "❄️")),
            Map.entry(95, new Info("Thunderstorm", "⛈️")),
            Map.entry(96, new Info("Thunderstorm with hail", "⛈️")),
            Map.entry(99, new Info("Thunderstorm with hail", "⛈️")));

    private static final Info UNKNOWN = new Info("Unknown", "🌡️");

    static Info of(int code) {
        return CODES.getOrDefault(code, UNKNOWN);
    }

    private WeatherCodes() {}
}
