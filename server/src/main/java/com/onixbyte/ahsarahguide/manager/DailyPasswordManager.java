package com.onixbyte.ahsarahguide.manager;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.onixbyte.ahsarahguide.domain.dto.DailyPasswordResponse;
import com.onixbyte.ahsarahguide.exeption.InternalServerErrorException;
import com.onixbyte.ahsarahguide.shared.JacksonModules;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Manager for daily password data access and caching coordination.
 *
 * @author zihluwang
 */
@Component
public class DailyPasswordManager {

    private static final String CACHE_KEY_PREFIX = "daily-password:";

    private final RestClient restClient;
    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public DailyPasswordManager(
            RestClient.Builder restClientBuilder,
            RedisTemplate<String, Object> redisTemplate
    ) {
        var snakeCaseMapper = new ObjectMapper();
        snakeCaseMapper.setPropertyNamingStrategy(
                PropertyNamingStrategies.SnakeCaseStrategy.INSTANCE);
        snakeCaseMapper.configure(
                DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        snakeCaseMapper.registerModule(JacksonModules.DATE_TIME_MODULE);

        this.restClient = restClientBuilder
                .baseUrl("https://tmini.net/api")
                .messageConverters(converters -> {
                    converters.removeIf(
                            MappingJackson2HttpMessageConverter.class::isInstance);
                    converters.add(
                            new MappingJackson2HttpMessageConverter(snakeCaseMapper));
                })
                .build();
        this.redisTemplate = redisTemplate;
    }

    /**
     * Retrieves the daily password from cache or generates a new one.
     * @return the daily password response
     */
    public DailyPasswordResponse getDailyPassword() {
        var key = CACHE_KEY_PREFIX + LocalDate.now();

        var cached = redisTemplate.opsForValue().get(key);
        if (cached != null) {
            return (DailyPasswordResponse) cached;
        }

        var response = restClient.get()
                .uri((uriBuilder) -> uriBuilder
                        .path("/sjzmm")
                        .queryParam("ckey", "")
                        .queryParam("type", "json")
                        .build())
                .retrieve()
                .body(DailyPasswordResponse.class);

        if (Objects.isNull(response)) {
            throw new InternalServerErrorException("暂无每日密码数据。");
        }

        redisTemplate.opsForValue().set(key, response, Duration.ofDays(1L));
        return response;
    }
}
