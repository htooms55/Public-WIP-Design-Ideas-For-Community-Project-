package service;
import model.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

public class WeatherService {

    @SuppressWarnings("ConvertToTryWithResources")
    public static void updateWeatherFromApi(Garden garden) {
        String loc = garden.getLocation();

        if (loc == null || !loc.contains(",")) {
            System.out.println("Garden location must be in 'lat,lon' format, e.g., '39.0997,-94.5786'.");
            return;
        }

        String[] parts = loc.split(",");
        if (parts.length != 2) {
            System.out.println("Invalid location format.");
            return;
        }

        double lat, lon;
        try {
            lat = Double.parseDouble(parts[0].trim());
            lon = Double.parseDouble(parts[1].trim());
        } catch (NumberFormatException e) {
            System.out.println("Could not parse latitude/longitude.");
            return;
        }

        String urlString = "https://api.open-meteo.com/v1/forecast?latitude="
                + lat + "&longitude=" + lon
                + "&current_weather=true"
                + "&temperature_unit=fahrenheit";

        try {
            URI uri = new URI(urlString);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(uri.toURL().openStream())
            );

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
            in.close();

            String json = sb.toString();

            // Only look inside the "current_weather" section (B4 it was not lol)
            int cwIndex = json.indexOf("\"current_weather\"");
            String cwJson = (cwIndex != -1) ? json.substring(cwIndex) : json;

            Double temperature = extractDoubleField(cwJson, "\"temperature\"");
            Double windspeed   = extractDoubleField(cwJson, "\"windspeed\"");
            Double weathercode = extractDoubleField(cwJson, "\"weathercode\"");

            if (temperature == null) {
                System.out.println("Could not parse temperature from weather response.");
                return;
            }

            // Open-Meteo is now returning °F because of temperature_unit=fahrenheit
            String summary = String.format("Temp: %.1f°F", temperature);
            if (windspeed != null) {
                // Open-Meteo default windspeed unit is km/h unless you change it
                summary += String.format(", Wind: %.1f km/h", windspeed);
            }
            if (weathercode != null) {
                summary += String.format(", Code: %.0f", weathercode);
            }

            garden.setWeather(summary);
            System.out.println("Weather updated: " + summary);

        } catch (IOException | URISyntaxException e) {
            System.out.println("Error retrieving weather: " + e.getMessage());
        }
    }

    private static Double extractDoubleField(String json, String key) {
        int idx = json.indexOf(key);
        if (idx == -1) return null;

        int colon = json.indexOf(":", idx);
        if (colon == -1) return null;

        int start = colon + 1;

        while (start < json.length() && Character.isWhitespace(json.charAt(start))) {
            start++;
        }

        int end = start;
        while (end < json.length()) {
            char c = json.charAt(end);
            if (c == ',' || c == '}' || Character.isWhitespace(c)) break;
            end++;
        }

        try {
            return Double.valueOf(json.substring(start, end));
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
