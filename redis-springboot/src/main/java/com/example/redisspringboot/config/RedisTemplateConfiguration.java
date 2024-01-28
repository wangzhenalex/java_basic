package com.example.redisspringboot.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
public class RedisTemplateConfiguration {
    @Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        // 使用Jackson2JsonRedisSerialize 替换默认序列化
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        //忽略任何值为null的属性
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
        // 设置key和value的序列化规则
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        // 设置hashKey和hashValue的序列化规则
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
        //afterPropertiesSet和init-method之间的执行顺序是afterPropertiesSet 先执行，init-method 后执行。
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    @Bean
    public CacheManager cacheManager1m(LettuceConnectionFactory factory){
        //加载默认Spring Cache配置信息
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig();
        //设置有效期为1小时
        config = config.entryTtl(Duration.ofMinutes(1));
        //说明缓存Key使用单冒号进行分割
        config = config.computePrefixWith(cacheName -> cacheName + ":");
        //Redis Key采用String直接存储
        config = config.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()));
        //Redis Value则将对象采用JSON形式存储
        config = config.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
        //不缓存Null值对象
        config = config.disableCachingNullValues();

        //实例化CacheManger缓存管理器
        RedisCacheManager cacheManager = RedisCacheManager.RedisCacheManagerBuilder
                //绑定REDIS连接工厂
                .fromConnectionFactory(factory)
                //绑定配置对象
                .cacheDefaults(config)
                //与声明式事务注解@Transactional进行兼容
                .transactionAware()
                //完成对象构建
                .build();
        return cacheManager;

    }
}
