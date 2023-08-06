package ru.telegram.clients;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
@Slf4j
public class HttpClientJdk implements HttpClient {

    @Override
    public String postRequest(String url, String body) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
        return doRequest(url, request);
    }

    @Override
    public String getRequest(String url) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .GET()
                .build();
        return doRequest(url, request);
    }

    private String doRequest(String url, HttpRequest request) {
        try {
            var client = java.net.http.HttpClient.newHttpClient();
            var response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (Exception ex) {
            if (ex instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            log.error("Http request error, url:{}", url, ex);
            throw new HttpClientException(ex.getMessage());
        }
    }
}
