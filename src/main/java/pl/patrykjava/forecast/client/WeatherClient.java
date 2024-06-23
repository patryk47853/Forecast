package pl.patrykjava.forecast.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import pl.patrykjava.forecast.model.CityWeather;
import pl.patrykjava.forecast.model.Forecast;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class WeatherClient {

    @Value("${weather.api.key}")
    private String apiKey;

    private final WebClient webClient;
    private static final Logger logger = LoggerFactory.getLogger(WeatherClient.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    public WeatherClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://api.weatherapi.com/v1").build();
    }

    public List<CityWeather> getWeatherData(List<String> cities) {
        return cities.stream()
                .map(this::fetchWeatherForCity)
                .map(Mono::block)
                .collect(Collectors.toList());
    }

    private Mono<CityWeather> fetchWeatherForCity(String city) {
        String url = buildUrl(city, apiKey);

        logger.info("Fetching weather data for city: {}", city);
        logger.info("URL: {}", url);

        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(String.class)
                .doOnNext(response -> logger.info("Response: {}", response))
                .map(response -> {
                    List<Forecast> forecasts = parseForecast(response);
                    return new CityWeather(city, forecasts);
                });
    }

    private List<Forecast> parseForecast(String response) {
        List<Forecast> forecasts = new ArrayList<>();

        try {
            JsonNode root = objectMapper.readTree(response);
            JsonNode forecastDays = root.path("forecast").path("forecastday");
            for (JsonNode day : forecastDays) {
                JsonNode dayData = day.path("day");
                Forecast forecast = objectMapper.treeToValue(dayData, Forecast.class);
                forecast.setDate(day.path("date").asText());
                forecasts.add(forecast);
            }
        } catch (Exception e) {
            logger.error("Error parsing forecast: ", e);
        }

        return forecasts;
    }

    private static String buildUrl(String city, String apiKey) {
        return String.format("/forecast.json?key=%s&q=%s&days=3", apiKey, city);
    }
}
