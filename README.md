# Weather Forecast App

Spring Boot 3 (Java 17) app that fetches live weather from the free
[Open-Meteo](https://open-meteo.com) API (no API key needed), parses the JSON,
and serves both a REST endpoint and a small web UI.

Shows: current temperature, humidity, feels-like, wind, and a 5-day forecast.

## Run locally

Requires JDK 17+ and Maven 3.9+.

    mvn spring-boot:run

Open http://localhost:8080

## REST API

    GET /api/weather?city=Bhopal&units=metric     (units: metric | imperial)

Errors come back as JSON problem details (404 city not found, 502 provider down).

## How it works

1. `WeatherController`  - exposes `/api/weather`
2. `WeatherService`     - calls Open-Meteo geocoding (city -> lat/lon), then the forecast
                          endpoint, and parses the JSON with Jackson `JsonNode`
3. `WeatherCodes`       - turns WMO weather codes into labels and icons
4. `static/index.html`  - the website (served by Spring Boot, calls `/api/weather`)

## Deploy (Render, free tier)

1. Push this folder to a GitHub repo.
2. On https://render.com: New > Web Service > connect the repo.
3. Runtime: **Docker** (it detects the Dockerfile; `render.yaml` is included too).
4. Deploy. You get a public URL like https://weather-app.onrender.com

Other hosts that work with the same Dockerfile: Railway, Fly.io, Google Cloud Run,
Koyeb. The app reads the `PORT` environment variable automatically.

Note: Open-Meteo's free tier is for non-commercial use.
