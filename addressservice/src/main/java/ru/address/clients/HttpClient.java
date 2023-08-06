package ru.address.clients;


import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;


public interface HttpClient {

    String getAddressData(String currency);


    default String doRequest(HttpRequest request) {
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
}
