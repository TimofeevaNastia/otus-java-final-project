package ru.rate.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ErrorBody {
    String text;
    String description;
}
