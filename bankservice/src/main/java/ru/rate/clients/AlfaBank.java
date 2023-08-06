package ru.rate.clients;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.rate.model.BankRate;
import ru.rate.model.alfa.AlfaResponse;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class AlfaBank implements Bank {
    @Autowired
    private AlfaClient alfaClient;

    @Autowired
    private ObjectMapper objectMapper;

    public BankRate getRate(String currency) {
        BankRate bankRate = new BankRate()
                .setNameBank("Альфа банк")
                .setCodeBank("alfa");
        try {
            var body = alfaClient.getRate(currency);
            var response = objectMapper.readValue(body, AlfaResponse.class);
            log.info("Получен ответ от Aльфа банка: {}", response);
            var rateValue = response.getData().get(0).getRateByClientType().get(0).getRatesByType().get(0).getLastActualRate();
            bankRate.setSellByClientRate(rateValue.getBuy().getOriginalValue());
            bankRate.setBuyByClientRate(rateValue.getSell().getOriginalValue());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return bankRate;
    }

//    public List<String> getOfficesWithCurrency() {
//        return alfaClient.getOfficesWithCurrency();
//    }

    @Override
    public List<String> getOfficesWithCurrency(String city) {
        var offices = alfaClient.getOfficesWithCurrency();
        var result = new ArrayList<String>();
        offices.forEach(x -> {
            String[] data = x.split(",");
            var cityAddr = data[0];
            if (StringUtils.containsIgnoreCase(cityAddr, city) || StringUtils.containsIgnoreCase(city, cityAddr)) {
                result.add(x);
            }
        });
        return result;
    }
}
