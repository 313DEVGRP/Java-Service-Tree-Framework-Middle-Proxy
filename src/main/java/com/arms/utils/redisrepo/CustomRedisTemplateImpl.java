package com.arms.utils.redisrepo;

import com.arms.utils.redisrepo.util.KeyName;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class CustomRedisTemplateImpl implements CustomRedisTemplate{

    private final RedisTemplate<String,Object> redisTemplate;
    @Override
    public List<String> scan(String id) {
        return redisTemplate.getConnectionFactory().getConnection()
                .scan(ScanOptions.scanOptions().match(id).count(200).build())
                .stream()
                .map(a -> new KeyName(new String(a, StandardCharsets.UTF_8)).get())
                .collect(Collectors.toList());
    }

}
