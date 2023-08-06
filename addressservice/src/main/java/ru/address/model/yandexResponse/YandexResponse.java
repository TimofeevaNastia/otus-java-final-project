package ru.address.model.yandexResponse;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class YandexResponse {
    ResponseData response;
}
