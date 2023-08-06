package ru.rate.clients;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.rate.config.BankUrlConfig;

import java.net.URI;
import java.net.http.HttpRequest;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class SberClient extends BaseRequest implements HttpBankClient {
    private BankUrlConfig bankUrlConfig;
    private final String URL = "%s/proxy/services/rates/public/v2/branches?t=10477,5331,10477,5331&z=14&rateType=ERNP-1%s";
    private String server;

    public SberClient(BankUrlConfig bankUrlConfig) {
        server = bankUrlConfig.getBank("sber").getUrl();
    }

    @Override
    public String getRate(String currencyCode) {
        var url = String.format(URL, server, "&isoCodes[]=" + currencyCode);
        var request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept-Language", "ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7")
                .header("Content-Type", "application/json")
                .header("Connection", "keep-alive")
                .header("Host", "www.sberbank.ru")
                .header("Referer", "http://www.sberbank.ru/ru/quotes/currencies?currency=" + currencyCode)
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36")
                .GET()
                .build();
        return doRequest(request);
    }

    @Override
    public List<String> getOfficesWithCurrency() {
        var res = new ArrayList<String>();
        res.add("Москва, Пушкина, 45");
        return res;
    }
}
