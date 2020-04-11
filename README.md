# Resilience4j Workbench

The purpose of the workbench is to experiment with Spring Boot using Resilience4J.

## Run the workbench

```
./gradlew bootRun
```

## Query the service

The demon service is returning the sum of two numbers. There are two distinct services and one cache.

Using the tool [HTTPie](https://httpie.org/)

```
http PUT localhost:8080/sum numberA:=1 numberB:=2 
```

```
http PUT localhost:8080/sum-spring-retry numberA:=1 numberB:=2 
```

## Metrics

When the SApring profile `metrics-infrastructure` is activated Prometheus and Grafana will be started using Docker.

The URLs to access both services will be logged during startup.

Please note that the initial username and password of Grafana is `admin` / `admin`.
