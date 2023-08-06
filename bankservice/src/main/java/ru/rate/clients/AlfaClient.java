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
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class AlfaClient extends BaseRequest implements HttpBankClient {
    private final String CURRENCIES = "%s/api/v1/scrooge/currencies/alfa-rates?rateType.in=rateCass,makeCash&lastActualForDate.eq=true&clientType.eq=standardCC%s%s";
    private final String OFFICE_LIST = "%s/help/currency/office-list/";
    private String server;
    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss[XXX]";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);


    public AlfaClient(BankUrlConfig bankUrlConfig) {
        server = bankUrlConfig.getBank("alfa").getUrl();
    }

    @Override
    public String getRate(String currencyCode) {
        var dateNow = getActualDateTime(); //2023-07-09T13:57:43+03:00 %2B
        var url = String.format(CURRENCIES, server, "&currencyCode.in=" + currencyCode, "&date.lte=" + dateNow);
        var request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
        return doRequest(request);
    }

    @Override
    public List<String> getOfficesWithCurrency() {
        var url = String.format(OFFICE_LIST, server);
        var result = new ArrayList<String>();
        try {
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/115.0.0.0 Safari/537.36")
                    .get();
            Elements listNews = doc.select("[data-test-id='dollar-euro'] li");
            for (Element element : listNews) {
                var data = "";
                if (element.text().contains("Барвиха")) {
                    data = element.text().substring(element.text().indexOf("д."));
                } else {
                    data = element.text().substring(element.text().indexOf("г."));
                }
                result.add(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private String getActualDateTime() {
        ZonedDateTime time = ZonedDateTime.now(ZoneId.systemDefault());
        return time.format(DATE_FORMATTER);
    }
}
