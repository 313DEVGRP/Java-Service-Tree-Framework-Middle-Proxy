package com.arms.util.redisrepo;

import com.arms.util.redisrepo.util.KeyName;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Component;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class CustomRedisTemplateImpl implements CustomRedisTemplate{

    private final RedisTemplate<String,Object> redisTemplate;


    @Override
    public List<String> scan(String id) {
        RedisConnectionFactory connectionFactory = redisTemplate.getConnectionFactory();
        if (connectionFactory == null) {
            throw new IllegalStateException("RedisConnectionFactory is not available");
        }

        try (RedisConnection connection = connectionFactory.getConnection()) {
            return connection.scan(ScanOptions.scanOptions().match(id).count(200).build())
                    .stream()
                    .map(a -> new KeyName(new String(a, StandardCharsets.UTF_8)).get())
                    .collect(Collectors.toList());
        } catch (Exception e) {
            // 예외 처리: 로그를 남기거나 다른 방식으로 처리할 수 있습니다.
            throw new RuntimeException("Error during scanning Redis keys", e);
        }
    }

}
