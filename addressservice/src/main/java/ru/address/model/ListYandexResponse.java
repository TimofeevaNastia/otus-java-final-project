package ru.address.model;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.address.model.yandexResponse.YandexResponse;

import java.util.List;

@Data
@Accessors(chain = true)
public class ListYandexResponse {
    List<YandexResponse> responseList;
}
