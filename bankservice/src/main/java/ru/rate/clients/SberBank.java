package ru.rate.clients;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.rate.model.BankRate;
import ru.rate.model.sber.Data;
import ru.rate.model.sber.SberResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class SberBank implements Bank {
    @Autowired
    private SberClient sberClient;
    @Autowired
    private ObjectMapper objectMapper;
    private String URL_GET_ADDRESSES = "https://%s.bankiros.ru/bank/sberbank/currency?all=1";

    @Override
    public BankRate getRate(String currency) {
        var body = sberClient.getRate(currency);
        BankRate bankRate = new BankRate()
                .setNameBank("Сбербанк")
                .setCodeBank("sber");
        try {
            List<Data> res = objectMapper.readValue(body, objectMapper.getTypeFactory().constructCollectionType(List.class, Data.class));
            var response = new SberResponse().setData(res);
            log.info("Получен ответ от Сбербанка: {}", response);

            var rateValue = response.getData().get(0)
                    .getRates();
            switch (currency) {
                case "EUR" -> {
                    bankRate.setSellByClientRate(rateValue.getEUR().getRateList().get(0).getRateBuy());
                    bankRate.setBuyByClientRate(rateValue.getEUR().getRateList().get(0).getRateSell());
                }
                case "USD" -> {
                    bankRate.setSellByClientRate(rateValue.getUSD().getRateList().get(0).getRateBuy());
                    bankRate.setBuyByClientRate(rateValue.getUSD().getRateList().get(0).getRateSell());
                }
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return bankRate;
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
