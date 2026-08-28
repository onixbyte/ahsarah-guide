package com.onixbyte.deltaforceguide.config;

import com.onixbyte.deltaforceguide.shared.JacksonRedisSerialiser;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.time.Duration;

/**
 * Configuration class for Redis-based caching components.
 * <p>
 * This configuration class provides beans for Redis cache management and template operations
 * within the Helix application. It configures custom serialisation strategies using
 * {@link GenericJackson2JsonRedisSerializer} for values and string serialisation for keys,
 * ensuring optimal performance and compatibility with JSON-based data structures.
 * <p>
 * The configuration includes:
 * <ul>
 *   <li>Custom {@link RedisCacheManager} with JSON serialisation support</li>
 *   <li>Configured {@link RedisTemplate} for direct Redis operations</li>
 * </ul>
 *
 * @author zihluwang
 * @see RedisCacheManager
 * @see RedisTemplate
 * @see GenericJackson2JsonRedisSerializer
 * @since 1.0.0
 */
@Configuration
@EnableCaching
public class CacheConfig {

    /**
     * Creates a custom Redis cache manager with JSON serialisation support.
     * <p>
     * This method configures a {@link RedisCacheManager} that uses string serialisation for cache
     * keys and {@link GenericJackson2JsonRedisSerializer} for cache values. This setup ensures that
     * complex objects can be stored and retrieved from Redis cache whilst maintaining readability
     * and compatibility with JSON-based systems.
     *
     * @param connectionFactory the Redis connection factory used to establish connections
     * @return a configured {@link RedisCacheManager} with custom serialisation settings
     * @see RedisCacheManager
     * @see GenericJackson2JsonRedisSerializer
     * @see RedisSerializationContext
     */
    @Bean
    public RedisCacheManager cacheManager(
            RedisConnectionFactory connectionFactory
    ) {
        var _keySerializer = RedisSerializer.string();

        var cacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(_keySerializer))
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(JacksonRedisSerialiser.INSTANCE))
                .entryTtl(Duration.ofMinutes(90L));

        return RedisCacheManager.RedisCacheManagerBuilder
                .fromConnectionFactory(connectionFactory)
                .cacheDefaults(cacheConfiguration)
                .build();
    }

    /**
     * Creates a Redis template for direct Redis operations with custom serialisation.
     * <p>
     * This method configures a {@link RedisTemplate} that uses string serialisation for keys
     * and {@link GenericJackson2JsonRedisSerializer} for values. This template provides low-level
     * access to Redis operations whilst ensuring consistent serialisation strategies across
     * the application.
     * <p>
     * The template is fully configured and ready for use after bean creation.
     *
     * @param connectionFactory the Redis connection factory used to establish connections
     * @return a fully configured {@link RedisTemplate} for Redis operations
     * @see RedisTemplate
     * @see GenericJackson2JsonRedisSerializer
     * @see RedisSerializer
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(
            RedisConnectionFactory connectionFactory
    ) {
        var redisTemplate = new RedisTemplate<String, Object>();
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.setKeySerializer(RedisSerializer.string());
        redisTemplate.setValueSerializer(JacksonRedisSerialiser.INSTANCE);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
}

