package ru.telegram.clients;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.telegram.config.TelegramClientConfig;
import ru.telegram.model.getUpdates.GetUpdatesRequest;
import ru.telegram.model.getUpdates.GetUpdatesResponse;
import ru.telegram.model.sendMessage.SendMessageRequest;
import ru.telegram.model.sendMessage.SendMessageResponse;


@Slf4j
@Service
@RequiredArgsConstructor
public class TelegramClientImpl implements TelegramClient {
    private final String GET_UPDATES = "getUpdates";
    private final String SEND_MESSAGE = "sendMessage";
    private final HttpClient httpClientJdk;
    private final ObjectMapper objectMapper;
    private final TelegramClientConfig clientConfig;

    @Override
    public GetUpdatesResponse getUpdates(GetUpdatesRequest request) {
        try {
            var body = objectMapper.writeValueAsString(request);
            log.info("getUpdates params:{}", body);

            var responseAsString = httpClientJdk.postRequest(makeUrl(GET_UPDATES), body);
            log.info("updatesAsString:{}", responseAsString);
            var response = objectMapper.readValue(responseAsString, GetUpdatesResponse.class);
            log.info("updatesResponse:{}", response);

            return response;
        } catch (JsonProcessingException ex) {
            log.error("request:{}", request, ex);
            throw new RuntimeException(ex);
        }
    }

    @Override
    public SendMessageResponse sendMessage(SendMessageRequest request) {
        try {
            var params = objectMapper.writeValueAsString(request);
            log.info("sendMessage params:{}", params);
            var responseAsString = httpClientJdk.postRequest(makeUrl(SEND_MESSAGE), params);
            log.info("sendMessageAsString:{}", responseAsString);
            var response = objectMapper.readValue(responseAsString, SendMessageResponse.class);
            log.info("sendMessageResponse:{}", response);

            return response;
        } catch (JsonProcessingException ex) {
            log.error("request:{}", request, ex);
            throw new RuntimeException(ex);
        }

    }

    private String makeUrl(String endpoint) {
        return String.format("%s/bot%s/%s", clientConfig.getUrl(), clientConfig.getToken(), endpoint);
    }
}
