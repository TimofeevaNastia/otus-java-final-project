package ru.telegram.clients;

public interface HttpClient {

    String postRequest(String url, String body);
    String getRequest(String url);
}
