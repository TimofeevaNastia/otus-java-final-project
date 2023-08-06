package ru.telegram.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.Serial;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class JsonConfig {
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss[XXX]";

    @Bean
    public ObjectMapper objectMapper() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
        var objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        JavaTimeModule module = new JavaTimeModule();
        module.addSerializer(new StdSerializer<>(ZonedDateTime.class) {
            @Serial
            private static final long serialVersionUID = 7267001379918924374L;

            @Override
            public void serialize(ZonedDateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                gen.writeString(value.format(dateTimeFormatter));
            }
        });
        module.addDeserializer(ZonedDateTime.class, new StdDeserializer<>(ZonedDateTime.class) {
            @Serial
            private static final long serialVersionUID = 154543596456722486L;

            @Override
            public ZonedDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return ZonedDateTime.parse(p.getText(), dateTimeFormatter.withZone(ZoneId.systemDefault()));
            }
        });
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.registerModule(module);
        return objectMapper;
    }

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer zonedDateTimeCustomizer() {

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);

        return builder -> {
            builder.serializers(new StdSerializer<>(ZonedDateTime.class) {
                @Serial
                private static final long serialVersionUID = 7267001379918924374L;

                @Override
                public void serialize(ZonedDateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                    gen.writeString(value.format(dateTimeFormatter));
                }
            });
            builder.deserializers(new StdDeserializer<>(ZonedDateTime.class) {
                @Serial
                private static final long serialVersionUID = 154543596456722486L;

                @Override
                public ZonedDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                    return ZonedDateTime.parse(p.getText(), dateTimeFormatter.withZone(ZoneId.systemDefault()));
                }
            });
        };
    }
}
