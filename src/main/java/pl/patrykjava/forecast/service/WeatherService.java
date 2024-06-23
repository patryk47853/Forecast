package pl.patrykjava.forecast.service;

import org.springframework.stereotype.Service;
import pl.patrykjava.forecast.exception.CityNotFoundException;
import pl.patrykjava.forecast.exception.ForecastNotFoundException;
import pl.patrykjava.forecast.model.CityWeather;
import pl.patrykjava.forecast.model.Forecast;
import pl.patrykjava.forecast.client.WeatherClient;

import java.util.List;

@Service
public class WeatherService {

    private final WeatherClient weatherClient;
    private final static List<String> cities = List.of("Wroclaw", "Cracow", "Lodz", "Warsaw", "Poznan");

    public WeatherService(WeatherClient weatherClient) {
        this.weatherClient = weatherClient;
    }

    public List<CityWeather> getWeatherForCities() {
        return weatherClient.getWeatherData(cities);
    }

    public CityWeather getThreeDayForecast(String city) {
        if (!cities.contains(city)) {
            throw new CityNotFoundException("Weather data not available for city: " + city);
        }

        List<CityWeather> cityWeatherList = weatherClient.getWeatherData(List.of(city));

        if (cityWeatherList.isEmpty()) {
            throw new CityNotFoundException("City not found: " + city);
        }

        return cityWeatherList.get(0);
    }

    public Forecast getForecastForDay(String city, int dayIndex) {
        CityWeather cityWeather = getThreeDayForecast(city);

        if (cityWeather != null && cityWeather.getForecasts().size() > dayIndex) {
            return cityWeather.getForecasts().get(dayIndex);
        }

        throw new ForecastNotFoundException("Forecast not found for city: " + city + " and day index: " + dayIndex);
    }
}
