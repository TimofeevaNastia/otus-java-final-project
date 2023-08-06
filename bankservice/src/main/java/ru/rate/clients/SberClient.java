package ru.rate.clients;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import ru.rate.config.BankUrlConfig;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class SberClient extends BaseRequest implements HttpBankClient {
    private BankUrlConfig bankUrlConfig;
    private final String URL = "%s/proxy/services/rates/public/v2/branches?t=10477,5331,10477,5331&z=14&rateType=ERNP-1%s";
    private String URL_GET_ADDRESSES = "https://%s.bankiros.ru/bank/sberbank/currency?all=1";
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
    public List<String> getOfficesWithCurrency(String city) {
        var result = new ArrayList<String>();
        var url = URL_GET_ADDRESSES.formatted(City.getValue(city));
        try {
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/115.0.0.0 Safari/537.36")
                    .get();
            Elements listNews = doc.select("[data-key] .xxx-tbl-row__grid>div:first-child span");
            for (Element element : listNews) {
                var data = element.text().substring(element.text().indexOf("г."));
                result.add(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //TODO: Приходит много отделений, берем только первые 10 чтобы не отправлять в яндекс запросы
        int maxSize = Math.min(result.size(), 10);
        return result.subList(0, maxSize);
    }
}
