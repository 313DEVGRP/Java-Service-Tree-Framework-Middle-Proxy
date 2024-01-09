package com.arms.utils.redisrepo;

import java.util.List;

public interface CustomRedisTemplate {
   List<String> scan(String id);
}
