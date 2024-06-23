package pl.patrykjava.forecast.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Forecast {
    @JsonProperty("date")
    private String date;

    @JsonProperty("maxtemp_c")
    private double maxTempC;

    @JsonProperty("mintemp_c")
    private double minTempC;

    @JsonProperty("avgtemp_c")
    private double avgTempC;

    @JsonProperty("maxwind_kph")
    private double maxWindKph;

    @JsonProperty("totalprecip_mm")
    private double totalPrecipMm;

    @JsonProperty("totalsnow_cm")
    private double totalSnowCm;

    @JsonProperty("avghumidity")
    private double avgHumidity;

    @JsonProperty("avgvis_km")
    private double avgVisKm;

    @JsonProperty("uv")
    private double uv;
}
