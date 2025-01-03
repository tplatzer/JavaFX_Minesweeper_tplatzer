package htl.steyr.javafx_minesweeper_tplatzer;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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

            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(response.body(), new TypeReference<>() {});
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
}
