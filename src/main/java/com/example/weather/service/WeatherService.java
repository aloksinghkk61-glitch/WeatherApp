package com.example.weather.service;

import com.example.weather.exception.CityNotFoundException;
import com.example.weather.model.CurrentWeather;
import com.example.weather.model.DailyForecast;
import com.example.weather.model.WeatherReport;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;

@Service
public class WeatherService {

    private static final String CURRENT_FIELDS =
            "temperature_2m,relative_humidity_2m,apparent_temperature,wind_speed_10m,weather_code";
    private static final String DAILY_FIELDS =
            "weather_code,temperature_2m_max,temperature_2m_min,precipitation_probability_max";

    private final RestClient geocodingClient;
    private final RestClient forecastClient;

    public WeatherService(RestClient.Builder builder,
                          @Value("${weather.geocoding-url}") String geocodingUrl,
                          @Value("${weather.forecast-url}") String forecastUrl) {
        this.geocodingClient = builder.clone().baseUrl(geocodingUrl).build();
        this.forecastClient = builder.clone().baseUrl(forecastUrl).build();
    }

    public WeatherReport getWeather(String city, boolean imperial) {
        if (city == null || city.isBlank()) {
            throw new IllegalArgumentException("Enter a city name.");
        }

        // 1) City name -> coordinates
        JsonNode geo = geocodingClient.get()
                .uri(u -> u.path("/v1/search")
                        .queryParam("name", "{name}")
                        .queryParam("count", 1)
                        .queryParam("language", "en")
                        .build(city.trim()))
                .retrieve()
                .body(JsonNode.class);

        JsonNode place = (geo == null) ? null : geo.path("results").path(0);
        if (place == null || place.isMissingNode() || place.isNull()) {
            throw new CityNotFoundException(city.trim());
        }

        double lat = place.path("latitude").asDouble();
        double lon = place.path("longitude").asDouble();

        // 2) Coordinates -> current weather + 5-day forecast
        JsonNode data = forecastClient.get()
                .uri(u -> {
                    var b = u.path("/v1/forecast")
                            .queryParam("latitude", lat)
                            .queryParam("longitude", lon)
                            .queryParam("current", CURRENT_FIELDS)
                            .queryParam("daily", DAILY_FIELDS)
                            .queryParam("forecast_days", 5)
                            .queryParam("timezone", "auto");
                    if (imperial) {
                        b = b.queryParam("temperature_unit", "fahrenheit")
                             .queryParam("wind_speed_unit", "mph");
                    }
                    return b.build();
                })
                .retrieve()
                .body(JsonNode.class);

        return toReport(place, data, imperial);
    }

    private WeatherReport toReport(JsonNode place, JsonNode data, boolean imperial) {
        JsonNode cur = data.path("current");
        WeatherCodes.Info curInfo = WeatherCodes.of(cur.path("weather_code").asInt());

        CurrentWeather current = new CurrentWeather(
                cur.path("temperature_2m").asDouble(),
                cur.path("apparent_temperature").asDouble(),
                cur.path("relative_humidity_2m").asInt(),
                cur.path("wind_speed_10m").asDouble(),
                curInfo.label(),
                curInfo.icon());

        JsonNode daily = data.path("daily");
        JsonNode dates = daily.path("time");
        List<DailyForecast> forecast = new ArrayList<>();
        for (int i = 0; i < dates.size(); i++) {
            WeatherCodes.Info info = WeatherCodes.of(daily.path("weather_code").path(i).asInt());
            forecast.add(new DailyForecast(
                    dates.path(i).asText(),
                    daily.path("temperature_2m_max").path(i).asDouble(),
                    daily.path("temperature_2m_min").path(i).asDouble(),
                    daily.path("precipitation_probability_max").path(i).asInt(),
                    info.label(),
                    info.icon()));
        }

        return new WeatherReport(
                place.path("name").asText(),
                place.path("admin1").asText(""),
                place.path("country").asText(""),
                imperial ? "°F" : "°C",
                imperial ? "mph" : "km/h",
                current,
                forecast);
    }
}
