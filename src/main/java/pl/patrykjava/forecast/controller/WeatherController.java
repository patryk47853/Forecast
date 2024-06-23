package pl.patrykjava.forecast.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.patrykjava.forecast.model.CityWeather;
import pl.patrykjava.forecast.model.Forecast;
import pl.patrykjava.forecast.service.WeatherService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/weather")
@Tag(name = "Weather Forecast")
public class WeatherController {
    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @Operation(
            description = "Get three day forecast for five biggest cities in Poland: Wroclaw, Cracow, Lodz, Warsaw, Poznan",
            summary = "Get three day forecast for five biggest cities in Poland"
    )
    @GetMapping()
    public List<CityWeather> getWeatherForecast() {
        return weatherService.getWeatherForCities();
    }

    @Operation(
            description = "Get three day weather forecast for a specified city. Available cities: Wroclaw, Cracow, Lodz, Warsaw, Poznan",
            summary = "Get three day forecast for a city"
    )
    @GetMapping("/{city}")
    public CityWeather getThreeDayForecast(@PathVariable String city) {
        return weatherService.getThreeDayForecast(city);
    }

    @Operation(
            description = "Get today's weather forecast for a specified city. Available cities: Wroclaw, Cracow, Lodz, Warsaw, Poznan",
            summary = "Get today's forecast for a city"
    )
    @GetMapping("/{city}/today")
    public Forecast getTodayForecast(@PathVariable String city) {
        return weatherService.getForecastForDay(city, 0);
    }

    @Operation(
            description = "Get tomorrow's weather forecast for a specified city. Available cities: Wroclaw, Cracow, Lodz, Warsaw, Poznan",
            summary = "Get tomorrow's forecast for a city"
    )
    @GetMapping("/{city}/tomorrow")
    public Forecast getTomorrowForecast(@PathVariable String city) {
        return weatherService.getForecastForDay(city, 1);
    }

    @Operation(
            description = "Get the day after tomorrow's weather forecast for a specified city. Available cities: Wroclaw, Cracow, Lodz, Warsaw, Poznan",
            summary = "Get day after tomorrow's forecast for a city"
    )
    @GetMapping("/{city}/dayafter")
    public Forecast getDayAfterTomorrowForecast(@PathVariable String city) {
        return weatherService.getForecastForDay(city, 2);
    }
}
