package htl.steyr.javafx_minesweeper_tplatzer.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A client for interacting with the leaderboard server.
 * <p>
 * Provides functionality for fetching the leaderboard and submitting best times via HTTP.
 */
public class LeaderboardClient
{
    /**
     * The base URL of the leaderboard server.
     * <p>
     * Used for sending and retrieving leaderboard data via HTTP requests.
     */
    private static final String SERVER_URL = "http://api.timplatzer.com:9000/leaderboard";


    /**
     * Default constructor for the LeaderboardClient class.
     * <p>
     * This constructor is required to ensure that an explicit constructor is present
     * for documentation purposes. It does not perform any specific initialization
     * but allows for potential future extensions.
     */
    public LeaderboardClient() {}

    /**
     * Fetches the leaderboard data from the server.
     * <p>
     * Sends a GET request to the server to retrieve the leaderboard data in JSON format.
     * If the server responds with a non-200 status code, an exception is thrown.
     * The response is parsed into a map structure for further processing.
     *
     * @return A map containing the leaderboard data, where each entry corresponds to a difficulty level (e.g., "beginner", "advanced", "pro"),
     * and each entry is a list of player data (username and best time).
     * @throws Exception if the request fails or if the response status code is not 200.
     */
    public Map<String, List<Map<String, Object>>> fetchLeaderboard() throws Exception
    {
        try (HttpClient client = HttpClient.newHttpClient()) // Initializes the HTTP client for the request
        {
            // Builds a GET request to the server using the SERVER_URL constant
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(SERVER_URL)) // Creates a URI from the SERVER_URL
                    .GET() // Specifies the HTTP GET method
                    .build();

            // Sends the request and captures the response body as a string
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Checks if the response status code is 200 (OK), otherwise throws an exception
            if (response.statusCode() != 200)
            {
                throw new Exception("Failed to fetch leaderboard: " + response.body());
            }

            // Parses the response body (JSON) into a map and returns it
            return parseJsonToMap(response.body());
        }
    }

    /**
     * Submits the player's best time to the leaderboard server.
     * <p>
     * Sends a POST request to the server with the player's username, best time, and the difficulty mode.
     * If the server responds with a non-201 status code, an exception is thrown.
     *
     * @param username The username of the player whose best time is being submitted.
     * @param time     The best time achieved by the player (in seconds).
     * @param mode     The difficulty mode of the game (e.g., "beginner", "advanced", "pro").
     * @throws Exception if the request fails or if the response status code is not 201 (Created).
     */
    public void submitBestTime(String username, int time, String mode) throws Exception
    {
        try (HttpClient client = HttpClient.newHttpClient()) // Initializes the HTTP client for the request
        {
            // Formats the request body with the provided username, time, and mode
            String requestBody = String.format("{\"username\":\"%s\", \"time\":%d, \"mode\":\"%s\"}", username, time, mode);

            // Builds a POST request to submit the best time to the server
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(SERVER_URL)) // Specifies the server URL
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody)) // Sets the request body with JSON data
                    .header("Content-Type", "application/json") // Specifies that the request body is JSON
                    .build();

            // Sends the request and captures the response
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Checks if the response status code is 201 (Created), otherwise throws an exception
            if (response.statusCode() != 201)
            {
                throw new Exception("Failed to submit best time: " + response.body());
            }
        }
    }

    /**
     * Parses a JSON string into a map of leaderboard data.
     * <p>
     * This method processes a JSON string representing the leaderboard data and converts it into a
     * {@link Map} where the key is a string (difficulty level) and the value is a list of player data
     * (represented as a map of key-value pairs).
     * The method only processes entries that are arrays (list of player data), skipping non-array entries.
     *
     * @param json The JSON string representing the leaderboard data.
     * @return A map where each key is a difficulty level, and each value is a list of player data.
     * @throws IllegalArgumentException if the JSON string is malformed or does not follow the expected structure.
     */
    private Map<String, List<Map<String, Object>>> parseJsonToMap(String json)
    {
        Map<String, List<Map<String, Object>>> result = new HashMap<>(); // Initializes the result map.

        json = json.trim(); // Removes leading and trailing whitespace.
        if (!json.startsWith("{") || !json.endsWith("}"))
        {
            throw new IllegalArgumentException("Invalid JSON string: " + json); // Throws an exception if the JSON structure is invalid.
        }

        json = json.substring(1, json.length() - 1).trim(); // Removes the curly braces from the string.

        String[] entries = json.split(",(?=\"[^\"]+\":\\[)"); // Splits the JSON string into entries based on a comma, keeping array values intact.
        for (String entry : entries)
        {
            int colonIndex = entry.indexOf(':'); // Finds the colon that separates keys and values.
            if (colonIndex > 0)
            {
                String key = entry.substring(0, colonIndex).trim().replaceAll("^\"|\"$", ""); // Extracts and cleans up the key.
                String value = entry.substring(colonIndex + 1).trim(); // Extracts the value.

                if (value.startsWith("[") && value.endsWith("]")) // Checks if the value is an array.
                {
                    try
                    {
                        result.put(key, parseJsonArray(value)); // Parses the JSON array and adds it to the result map.
                    } catch (Exception e)
                    {
                        System.err.println("Failed to parse array for key: " + key + ", value: " + value);
                        System.err.println(e.getMessage());
                    }
                } else
                {
                    System.err.println("Skipping non-array value for key: " + key + ", value: " + value); // Skips non-array values.
                }
            }
        }

        return result; // Returns the parsed map.
    }

    /**
     * Parses a JSON array string into a list of maps representing the objects in the array.
     * <p>
     * This method processes a JSON array string, extracting each object within the array and converting
     * it into a map of key-value pairs. Each object is assumed to be enclosed in curly braces and parsed
     * individually.
     *
     * @param jsonArray The JSON array string to parse.
     * @return A list of maps, where each map represents an individual JSON object within the array.
     * @throws IllegalArgumentException if the JSON array is malformed or contains mismatched braces.
     */
    private List<Map<String, Object>> parseJsonArray(String jsonArray)
    {
        List<Map<String, Object>> list = new ArrayList<>(); // Initializes the list to hold parsed JSON objects.

        jsonArray = jsonArray.trim(); // Removes leading and trailing whitespace.
        if (!jsonArray.startsWith("[") || !jsonArray.endsWith("]"))
        {
            throw new IllegalArgumentException("Invalid JSON array: " + jsonArray); // Throws exception if the array is not properly enclosed in brackets.
        }

        jsonArray = jsonArray.substring(1, jsonArray.length() - 1).trim(); // Removes the square brackets from the array string.

        int braceCount = 0; // Tracks the number of opening and closing braces to identify objects.
        StringBuilder currentObject = new StringBuilder(); // Used to accumulate characters for each JSON object.

        // Loops through the characters of the JSON array string.
        for (char c : jsonArray.toCharArray())
        {
            if (c == '{')
            {
                braceCount++; // Increments brace count for opening brace.
            } else if (c == '}')
            {
                braceCount--; // Decrements brace count for closing brace.
            }

            currentObject.append(c); // Adds the character to the current object being parsed.

            // When the number of braces is balanced, it indicates the end of an object.
            if (braceCount == 0 && !currentObject.isEmpty())
            {
                String object = currentObject.toString().trim(); // Extracts the object and trims it.
                currentObject.setLength(0); // Resets the StringBuilder for the next object.

                try
                {
                    if (object.startsWith("{") && object.endsWith("}"))
                    {
                        list.add(parseJsonObject(object)); // Parses the individual JSON object and adds it to the list.
                    }
                } catch (Exception e)
                {
                    System.err.println("Skipping invalid JSON object: " + object); // Prints an error if parsing fails.
                    System.err.println(e.getMessage());
                }
            }
        }

        if (braceCount != 0)
        {
            throw new IllegalArgumentException("Mismatched braces in JSON array: " + jsonArray); // Throws exception if braces are unbalanced.
        }

        return list; // Returns the list of parsed JSON objects.
    }

    /**
     * Parses a JSON object string into a map of key-value pairs.
     * <p>
     * This method processes a JSON object string by splitting it into individual key-value pairs and then parsing
     * each pair. It handles values that are strings or numbers, and logs any invalid entries.
     *
     * @param jsonObject The JSON object string to parse.
     * @return A map representing the parsed key-value pairs from the JSON object.
     * @throws IllegalArgumentException if the JSON object is malformed or does not start and end with curly braces.
     */
    private Map<String, Object> parseJsonObject(String jsonObject)
    {
        Map<String, Object> map = new HashMap<>(); // Initializes the map to hold parsed key-value pairs.

        jsonObject = jsonObject.trim(); // Trims any leading or trailing whitespace.
        if (!jsonObject.startsWith("{") || !jsonObject.endsWith("}"))
        {
            throw new IllegalArgumentException("Invalid JSON object: " + jsonObject); // Throws exception if the object isn't properly enclosed in curly braces.
        }

        jsonObject = jsonObject.substring(1, jsonObject.length() - 1).trim(); // Removes the curly braces from the object string.

        String[] entries = jsonObject.split(",(?=\"[^\"]+\":)"); // Splits the object string into entries by commas, ignoring commas within strings.
        for (String entry : entries)
        {
            String[] keyValue = entry.split(":", 2); // Splits each entry into key-value pair based on the first colon.
            if (keyValue.length == 2)
            {
                String key = keyValue[0].trim().replaceAll("^\"|\"$", ""); // Cleans up the key by removing any surrounding quotes.
                String value = keyValue[1].trim(); // Cleans up the value.

                if (value.startsWith("\"") && value.endsWith("\""))
                {
                    map.put(key, value.replaceAll("^\"|\"$", "")); // If the value is a string, remove the surrounding quotes and add to the map.
                } else if (value.matches("-?\\d+(\\.\\d+)?"))
                {
                    if (value.contains("."))
                    {
                        map.put(key, Double.parseDouble(value)); // If the value is a decimal number, parse as Double.
                    } else
                    {
                        map.put(key, Integer.parseInt(value)); // If the value is an integer, parse as Integer.
                    }
                } else
                {
                    System.err.println("Skipping invalid JSON value: " + value); // Logs invalid values.
                }
            } else
            {
                System.err.println("Skipping invalid JSON entry: " + entry); // Logs invalid entries.
            }
        }

        return map; // Returns the map containing parsed key-value pairs.
    }
}
