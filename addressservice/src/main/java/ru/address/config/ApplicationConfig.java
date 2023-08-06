package ru.address.config;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.address.model.ListYandexResponse;
import ru.address.model.yandexResponse.YandexResponse;
import ru.rate.model.BankRateResponse;

import java.time.LocalDate;
import java.util.List;

@Configuration
@EnableConfigurationProperties(YandexConfig.class)
public class ApplicationConfig {
    private final CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder().build(true);

    @Bean
    public Cache<String, String> cityCache(@Value("${cache.size}") int cacheSize) {
        return cacheManager.createCache("CurrencyRate-Cache",
                CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, String.class,
                                ResourcePoolsBuilder.heap(cacheSize))
                        .build());
    }
}
