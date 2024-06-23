package pl.patrykjava.forecast;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.patrykjava.forecast.exception.CityNotFoundException;
import pl.patrykjava.forecast.exception.ForecastNotFoundException;
import pl.patrykjava.forecast.model.CityWeather;
import pl.patrykjava.forecast.model.Forecast;
import pl.patrykjava.forecast.client.WeatherClient;
import pl.patrykjava.forecast.service.WeatherService;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WeatherServiceTest {

    @Mock
    private WeatherClient weatherClient;

    @InjectMocks
    private WeatherService weatherService;

    private CityWeather sampleCityWeather;

    @BeforeEach
    void setUp() {
        Forecast forecast1 = new Forecast("2024-06-21", 27.7, 12.8, 20.8, 15.1, 1.35, 0.0, 74.0, 10.0, 8.0);
        Forecast forecast2 = new Forecast("2024-06-22", 28.0, 13.0, 21.0, 16.0, 1.5, 0.0, 75.0, 10.0, 8.0);
        Forecast forecast3 = new Forecast("2024-06-23", 29.0, 14.0, 22.0, 17.0, 1.7, 0.0, 76.0, 10.0, 8.0);
        sampleCityWeather = new CityWeather("Wroclaw", List.of(forecast1, forecast2, forecast3));
    }

    @Test
    void getWeatherForCities_ShouldReturnWeatherForAllCities() {
        // Given
        when(weatherClient.getWeatherData(anyList())).thenReturn(List.of(sampleCityWeather));

        // When
        List<CityWeather> result = weatherService.getWeatherForCities();
        CityWeather cityWeather = result.get(0);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());

        assertEquals("Wroclaw", cityWeather.getCity());
        assertEquals(3, cityWeather.getForecasts().size());
        verify(weatherClient, times(1)).getWeatherData(anyList());
    }

    @Test
    void getThreeDayForecast_ShouldReturnForecastForCity() {
        // Given
        when(weatherClient.getWeatherData(anyList())).thenReturn(List.of(sampleCityWeather));

        // When
        CityWeather result = weatherService.getThreeDayForecast("Wroclaw");
        Forecast forecast = result.getForecasts().get(0);

        // Then
        assertNotNull(result);
        assertEquals("Wroclaw", result.getCity());
        assertEquals(3, result.getForecasts().size());

        assertEquals("2024-06-21", forecast.getDate());
        assertEquals(27.7, forecast.getMaxTempC());
        assertEquals(12.8, forecast.getMinTempC());
        assertEquals(20.8, forecast.getAvgTempC());
        assertEquals(15.1, forecast.getMaxWindKph());
        assertEquals(1.35, forecast.getTotalPrecipMm());
        assertEquals(0.0, forecast.getTotalSnowCm());
        assertEquals(74.0, forecast.getAvgHumidity());
        assertEquals(10.0, forecast.getAvgVisKm());
        assertEquals(8.0, forecast.getUv());
        verify(weatherClient, times(1)).getWeatherData(eq(List.of("Wroclaw")));
    }

    @Test
    void getThreeDayForecast_ShouldThrowCityNotFoundExceptionForInvalidCity() {
        // When & Then
        assertThrows(CityNotFoundException.class, () -> weatherService.getThreeDayForecast("InvalidCity"));
        verify(weatherClient, times(0)).getWeatherData(anyList());
    }

    @Test
    void getThreeDayForecast_ShouldThrowCityNotFoundExceptionWhenCityNotFoundInRepository() {
        // Given
        when(weatherClient.getWeatherData(anyList())).thenReturn(Collections.emptyList());

        // When & Then
        assertThrows(CityNotFoundException.class, () -> weatherService.getThreeDayForecast("Wroclaw"));
        verify(weatherClient, times(1)).getWeatherData(eq(List.of("Wroclaw")));
    }

    @Test
    void getForecastForDay_ShouldReturnForecastForGivenDayIndex() {
        // Given
        when(weatherClient.getWeatherData(anyList())).thenReturn(List.of(sampleCityWeather));

        // When
        Forecast result = weatherService.getForecastForDay("Wroclaw", 1);

        // Then
        assertNotNull(result);
        assertEquals("2024-06-22", result.getDate());
        assertEquals(28.0, result.getMaxTempC());
        assertEquals(13.0, result.getMinTempC());
        assertEquals(21.0, result.getAvgTempC());
        assertEquals(16.0, result.getMaxWindKph());
        assertEquals(1.5, result.getTotalPrecipMm());
        assertEquals(0.0, result.getTotalSnowCm());
        assertEquals(75.0, result.getAvgHumidity());
        assertEquals(10.0, result.getAvgVisKm());
        assertEquals(8.0, result.getUv());
        verify(weatherClient, times(1)).getWeatherData(eq(List.of("Wroclaw")));
    }

    @Test
    void getForecastForDay_ShouldThrowForecastNotFoundExceptionForInvalidDayIndex() {
        // Given
        when(weatherClient.getWeatherData(anyList())).thenReturn(List.of(sampleCityWeather));

        // When & Then
        assertThrows(ForecastNotFoundException.class, () -> weatherService.getForecastForDay("Wroclaw", 5));
        verify(weatherClient, times(1)).getWeatherData(eq(List.of("Wroclaw")));
    }

    @Test
    void getForecastForDay_ShouldThrowCityNotFoundExceptionForInvalidCity() {
        // When & Then
        assertThrows(CityNotFoundException.class, () -> weatherService.getForecastForDay("InvalidCity", 1));
        verify(weatherClient, times(0)).getWeatherData(anyList());
    }

    @Test
    void getThreeDayForecast_ShouldReturnForecastWithAllProperties() {
        // Given
        when(weatherClient.getWeatherData(anyList())).thenReturn(List.of(sampleCityWeather));

        // When
        CityWeather result = weatherService.getThreeDayForecast("Wroclaw");

        // Then
        assertNotNull(result);
        assertEquals("Wroclaw", result.getCity());
        assertEquals(3, result.getForecasts().size());

        Forecast forecast = result.getForecasts().get(0);
        assertAll("Forecast properties: ",
                () -> assertEquals("2024-06-21", forecast.getDate()),
                () -> assertEquals(27.7, forecast.getMaxTempC()),
                () -> assertEquals(12.8, forecast.getMinTempC()),
                () -> assertEquals(20.8, forecast.getAvgTempC()),
                () -> assertEquals(15.1, forecast.getMaxWindKph()),
                () -> assertEquals(1.35, forecast.getTotalPrecipMm()),
                () -> assertEquals(0.0, forecast.getTotalSnowCm()),
                () -> assertEquals(74.0, forecast.getAvgHumidity()),
                () -> assertEquals(10.0, forecast.getAvgVisKm()),
                () -> assertEquals(8.0, forecast.getUv())
        );

        verify(weatherClient, times(1)).getWeatherData(eq(List.of("Wroclaw")));
    }
}
