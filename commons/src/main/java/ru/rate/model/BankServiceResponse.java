package ru.rate.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class BankServiceResponse {
    private ru.rate.model.Data data;
    private ErrorBody error;
}
