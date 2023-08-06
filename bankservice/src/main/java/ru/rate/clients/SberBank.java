package ru.rate.clients;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.rate.model.BankRate;
import ru.rate.model.sber.Data;
import ru.rate.model.sber.SberResponse;

import java.util.List;

@Service
@Slf4j
public class SberBank implements Bank {
    @Autowired
    private SberClient sberClient;
    @Autowired
    private ObjectMapper objectMapper;


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
        return sberClient.getOfficesWithCurrency(city);
    }
}
