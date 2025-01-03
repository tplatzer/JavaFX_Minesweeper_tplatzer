package htl.steyr.javafx_minesweeper_tplatzer;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LeaderboardClient
{
    private static final String SERVER_URL = "http://api.timplatzer.com:9000/leaderboard";

    public Map<String, List<Map<String, Object>>> fetchLeaderboard() throws Exception
    {
        try (HttpClient client = HttpClient.newHttpClient())
        {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(SERVER_URL))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200)
            {
                throw new Exception("Failed to fetch leaderboard: " + response.body());
            }

            System.out.println(response.body());
            return parseJsonToMap(response.body());
        }
    }

    public void submitBestTime(String username, int time, String mode) throws Exception
    {
        try (HttpClient client = HttpClient.newHttpClient())
        {
            String requestBody = String.format("{\"username\":\"%s\", \"time\":%d, \"mode\":\"%s\"}", username, time, mode);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(SERVER_URL))
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .header("Content-Type", "application/json")
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 201)
            {
                throw new Exception("Failed to submit best time: " + response.body());
            }
        }
    }

    private Map<String, List<Map<String, Object>>> parseJsonToMap(String json)
    {
        Map<String, List<Map<String, Object>>> result = new HashMap<>();

        json = json.trim();
        if (!json.startsWith("{") || !json.endsWith("}"))
        {
            throw new IllegalArgumentException("Invalid JSON string: " + json);
        }

        json = json.substring(1, json.length() - 1).trim();

        String[] entries = json.split(",(?=\"[^\"]+\":\\[)");
        for (String entry : entries)
        {
            int colonIndex = entry.indexOf(':');
            if (colonIndex > 0)
            {
                String key = entry.substring(0, colonIndex).trim().replaceAll("^\"|\"$", "");
                String value = entry.substring(colonIndex + 1).trim();

                if (value.startsWith("[") && value.endsWith("]"))
                {
                    try
                    {
                        result.put(key, parseJsonArray(value));
                    } catch (Exception e)
                    {
                        System.err.println("Failed to parse array for key: " + key + ", value: " + value);
                        System.err.println(e.getMessage());
                    }
                } else
                {
                    System.err.println("Skipping non-array value for key: " + key + ", value: " + value);
                }
            }
        }

        return result;
    }

    private List<Map<String, Object>> parseJsonArray(String jsonArray)
    {
        List<Map<String, Object>> list = new ArrayList<>();

        jsonArray = jsonArray.trim();
        if (!jsonArray.startsWith("[") || !jsonArray.endsWith("]"))
        {
            throw new IllegalArgumentException("Invalid JSON array: " + jsonArray);
        }

        jsonArray = jsonArray.substring(1, jsonArray.length() - 1).trim();

        int braceCount = 0;
        StringBuilder currentObject = new StringBuilder();

        for (char c : jsonArray.toCharArray())
        {
            if (c == '{')
            {
                braceCount++;
            } else if (c == '}')
            {
                braceCount--;
            }

            currentObject.append(c);

            if (braceCount == 0 && !currentObject.isEmpty())
            {
                String object = currentObject.toString().trim();
                currentObject.setLength(0);

                try
                {
                    if (object.startsWith("{") && object.endsWith("}"))
                    {
                        list.add(parseJsonObject(object));
                    }
                } catch (Exception e)
                {
                    System.err.println("Skipping invalid JSON object: " + object);
                    System.err.println(e.getMessage());
                }
            }
        }

        if (braceCount != 0)
        {
            throw new IllegalArgumentException("Mismatched braces in JSON array: " + jsonArray);
        }

        return list;
    }

    private Map<String, Object> parseJsonObject(String jsonObject)
    {
        Map<String, Object> map = new HashMap<>();

        jsonObject = jsonObject.trim();
        if (!jsonObject.startsWith("{") || !jsonObject.endsWith("}"))
        {
            throw new IllegalArgumentException("Invalid JSON object: " + jsonObject);
        }

        jsonObject = jsonObject.substring(1, jsonObject.length() - 1).trim();

        String[] entries = jsonObject.split(",(?=\"[^\"]+\":)");
        for (String entry : entries)
        {
            String[] keyValue = entry.split(":", 2);
            if (keyValue.length == 2)
            {
                String key = keyValue[0].trim().replaceAll("^\"|\"$", "");
                String value = keyValue[1].trim();

                if (value.startsWith("\"") && value.endsWith("\""))
                {
                    map.put(key, value.replaceAll("^\"|\"$", ""));
                } else if (value.matches("-?\\d+(\\.\\d+)?"))
                {
                    if (value.contains("."))
                    {
                        map.put(key, Double.parseDouble(value));
                    } else
                    {
                        map.put(key, Integer.parseInt(value));
                    }
                } else
                {
                    System.err.println("Skipping invalid JSON value: " + value);
                }
            } else
            {
                System.err.println("Skipping invalid JSON entry: " + entry);
            }
        }

        return map;
    }
}
