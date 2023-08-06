package ru.telegram.clients;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.ehcache.Cache;
import org.springframework.stereotype.Service;
import ru.rate.model.BankAddressRequest;
import ru.rate.model.BankRateResponse;
import ru.rate.model.BankServiceResponse;
import ru.telegram.config.CurrencyRateClientConfig;
import ru.telegram.services.TelegramException;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class RateServiceClientImpl implements RateServiceClient {
    private final HttpClient httpClient;
    private final Cache<LocalDate, BankRateResponse> currencyRateCache;
    private final ObjectMapper objectMapper;
    private final CurrencyRateClientConfig currencyRateClientConfig;

    @Override
    public BankRateResponse getActualRate(String currencyCode) {
        BankRateResponse bankRate = currencyRateCache.get(LocalDate.now());
        if (bankRate == null)
            try {
                bankRate = objectMapper.readValue(httpClient.getRequest(currencyRateClientConfig.getCurrency() + "/" + currencyCode),
                        BankRateResponse.class);
                currencyRateCache.put(LocalDate.now(), bankRate);
            } catch (JsonProcessingException e) {
                throw new TelegramException(e);
            }
        return bankRate;
    }

    @Override
    public BankServiceResponse getBankAddressesWithCurrency(BankAddressRequest request) {
        try {
            var params = objectMapper.writeValueAsString(request);
            return objectMapper.readValue(httpClient.postRequest(currencyRateClientConfig.getAddresses(), params),
                    BankServiceResponse.class);
        } catch (JsonProcessingException e) {
            throw new TelegramException(e);
        }
    }
}
