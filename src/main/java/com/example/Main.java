package com.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public class Main {
    public Main() {
    }

    public static void main(String[] args) {
        Main app = new Main();
        app.run();
    }

    public void run() {
        readFile();
        makeHttpRequest();
    }

    private void readFile() {
        // Updated for containerization: Use environment variable for file path
        String filePath = System.getenv("FILE_PATH");
        if (filePath == null) {
            System.err.println("Environment variable FILE_PATH is not set.");
            return;
        }
        try (InputStream inputStream = Main.class.getClassLoader().getResourceAsStream(filePath);
             BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void makeHttpRequest() {
        // Updated for containerization: Use environment variable for API URL and non-blocking HTTP call
        String apiUrl = System.getenv("API_URL");
        if (apiUrl == null) {
            System.err.println("Environment variable API_URL is not set.");
            return;
        }
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(apiUrl))
                    .GET()
                    .build();

            CompletableFuture<HttpResponse<String>> response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
            response.thenAccept(res -> {
                if (res.statusCode() == 200) {
                    System.out.println("Response from API: " + res.body());
                } else {
                    System.err.println("Failed to connect to API with response code " + res.statusCode());
                }
            }).exceptionally(e -> {
                e.printStackTrace();
                return null;
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
