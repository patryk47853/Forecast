FROM openjdk:21-jdk

WORKDIR /app

COPY target/forecast-0.0.1.jar /app/forecast.jar

EXPOSE 8080

CMD ["java", "-jar", "forecast.jar"]