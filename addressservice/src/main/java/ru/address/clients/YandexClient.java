package ru.address.clients;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.address.config.YandexConfig;

import java.net.URI;
import java.net.http.HttpRequest;

@Service
@Slf4j
public class YandexClient implements HttpClient {
    private final String API_KEY = System.getenv("apiKey");
    private final String URL = "%s/?apikey=%s&geocode=%s&lang=ru_RU&format=json";
    private String server;

    public YandexClient(YandexConfig config) {
        server = config.getUrl();
    }

    @Override
    public String getAddressData(String address) {
        log.info("запрос к Yandex API на получение данных по адресу, входные данные: {}", address);
        var url = String.format(URL, server, API_KEY, address);
        var request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
        return doRequest(request);
    }

}
