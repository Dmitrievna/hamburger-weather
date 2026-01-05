**Weather route backend service**
This project is a backend-only prototype that provides a weather overview for a shortest path along the way between starting and destination address. 

The application aggregates data from mulipe third-party services in order to:
- convert addresses to geographic coordinates
- find a shortest path between converted coordinates
- retrieve weather forecast along the route
- summarize result into a single report

The main goal of this project is to explore reactive programming using Java and Spring Boot WebFlux as well as integration of multiple external APIs.

External APIs used:
- Geocoding API – address-to-coordinate conversion
- OSRM API – route calculation
- Open-Meteo API - for weather forecasts


This application is intended for learning and architectural exploration purposes and does not include a frontend.
