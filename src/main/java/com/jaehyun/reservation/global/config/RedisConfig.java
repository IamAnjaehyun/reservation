package com.jaehyun.reservation.global.config;

import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class RedisConfig extends CachingConfigurerSupport {

  @Value("${spring.redis.port}")
  private int port;
  @Value("${spring.redis.host}")
  private String host;
  @Value("${spring.redis.timeout}")
  private Long timeout;
  @Bean
  public LettuceConnectionFactory lettuceConnectionFactory() {
    LettuceClientConfiguration lettuceClientConfiguration = LettuceClientConfiguration.builder()
        .commandTimeout(Duration.ofMinutes(1))
        .shutdownTimeout(Duration.ZERO)
        .build();
    RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(host, port);
    return new LettuceConnectionFactory(redisStandaloneConfiguration, lettuceClientConfiguration);
  }

  @Bean
  public RedisTemplate<?, ?> redisTemplate() {
    RedisTemplate<byte[], byte[]> template = new RedisTemplate<>();
    template.setConnectionFactory(lettuceConnectionFactory());
    return template;
  }

  @Bean
  @Override
  public CacheManager cacheManager() {
    RedisCacheManager.RedisCacheManagerBuilder builder = RedisCacheManager
        .RedisCacheManagerBuilder
        .fromConnectionFactory(lettuceConnectionFactory());
    RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig()
        .entryTtl(Duration.ofMinutes(timeout));
    builder.cacheDefaults(configuration);
    return builder.build();
  }
}
