package io.pivotal.samples.dashboard.config.data;

import io.pivotal.samples.dashboard.domain.Client;
import io.pivotal.samples.dashboard.repositories.redis.RedisClientRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@Profile("redis")
public class RedisConfig {

    @Bean
    public RedisClientRepository redisRepository(RedisTemplate<String, Client> redisTemplate) {
        return new RedisClientRepository(redisTemplate);
    }

    @Bean
    public RedisTemplate<String, Client> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Client> template = new RedisTemplate<>();

        template.setConnectionFactory(redisConnectionFactory);

        RedisSerializer<String> stringSerializer = new StringRedisSerializer();
        RedisSerializer<Client> clientSerializer = new Jackson2JsonRedisSerializer<>(Client.class);

        template.setKeySerializer(stringSerializer);
        template.setValueSerializer(clientSerializer);
        template.setHashKeySerializer(stringSerializer);
        template.setHashValueSerializer(clientSerializer);

        return template;
    }

}
