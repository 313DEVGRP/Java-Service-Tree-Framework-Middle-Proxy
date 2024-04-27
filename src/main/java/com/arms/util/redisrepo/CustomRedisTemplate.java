package com.arms.util.redisrepo;

import java.util.List;

public interface CustomRedisTemplate {
   List<String> scan(String id);
}
