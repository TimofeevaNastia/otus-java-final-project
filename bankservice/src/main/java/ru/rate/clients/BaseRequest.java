package ru.rate.clients;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.ConnectException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Slf4j
public class BaseRequest {

    public String doRequest(HttpRequest request) {
        try {
            var client = java.net.http.HttpClient.newHttpClient();
            var response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (Exception ex) {
            if (ex instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            throw new HttpClientException(ex.getMessage());
        }
    }

    public String performRequest(String url, String params) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(params))
                .build();
        return doRequest(url, request);
    }
    private String doRequest(String url, HttpRequest request) {
        try {
            var client = java.net.http.HttpClient.newHttpClient();
            var response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        }
        catch (Exception ex) {
            if (ex instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            log.error("Http request error, url:{}", url, ex);
            throw new HttpClientException(ex.getMessage());
        }
    }

}
