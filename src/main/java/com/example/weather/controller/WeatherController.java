package com.example.weather.controller;

import com.example.weather.model.WeatherReport;
import com.example.weather.service.WeatherService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    /** GET /api/weather?city=Bhopal&units=metric|imperial */
    @GetMapping("/weather")
    public WeatherReport weather(@RequestParam String city,
                                 @RequestParam(defaultValue = "metric") String units) {
        return weatherService.getWeather(city, "imperial".equalsIgnoreCase(units));
    }
}
