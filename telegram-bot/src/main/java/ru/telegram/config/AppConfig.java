package ru.telegram.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.rate.model.BankRateResponse;
import ru.telegram.clients.TelegramClient;
import ru.telegram.services.OffsetUpdater;
import ru.telegram.services.TelegramService;
import ru.telegram.services.TelegramServiceImpl;
import ru.telegram.services.processors.MessageSender;

import java.time.LocalDate;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(CurrencyRateClientConfig.class)
@Slf4j
public class AppConfig {
    public static final String TELEGRAM_TOKEN_ID = System.getenv("tokenId");
    private final CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder().build(true);
    @Value("${telegram.delay:10000}")
    private long delay;
    @Value("${telegram.period:10000}")
    private long period;

    @Bean
    public TelegramClientConfig telegramClientConfig(@Value("${telegram.url}") String url) {
        return new TelegramClientConfig(url, TELEGRAM_TOKEN_ID);
    }

    @Bean
    public Cache<LocalDate, BankRateResponse> currencyRateCache(@Value("${cache.size}") int cacheSize) {
        return cacheManager.createCache("CurrencyRate-Cache",
                CacheConfigurationBuilder.newCacheConfigurationBuilder(LocalDate.class, BankRateResponse.class,
                                ResourcePoolsBuilder.heap(cacheSize))
                        .build());
    }

    @Bean
    public TelegramImporterScheduled telegramImporterScheduled(TelegramClient telegramClient,
                                                               MessageSender messageSender,
                                                               OffsetUpdater lastUpdateIdKeeper) {
        var telegramService = new TelegramServiceImpl(telegramClient, lastUpdateIdKeeper, messageSender);
        return new TelegramImporterScheduled(telegramService, delay, period);
    }

    public static class TelegramImporterScheduled {
        public TelegramImporterScheduled(TelegramService telegramService, long delay, long period) {
            ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
            executor.scheduleAtFixedRate(telegramService::process, delay, period, TimeUnit.MILLISECONDS);
        }
    }
}
