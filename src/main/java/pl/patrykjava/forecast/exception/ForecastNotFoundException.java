package pl.patrykjava.forecast.exception;

public class ForecastNotFoundException extends RuntimeException {
    public ForecastNotFoundException(String message) {
        super(message);
    }
}
