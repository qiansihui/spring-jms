package spring.redis.publisher;

import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Component;
import spring.redis.publisher.RedisManager;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by root on 10/5/15.
 */
public class RedisPublisher {

    RedisManager redisManager;

    public void setRedisManager(RedisManager redisManager) {
        this.redisManager = redisManager;
    }

    public void send() {
        Map<String, String> map = new HashMap<>();
        map.put("type", "log");
        map.put("time", String.valueOf(new Date()));
        map.put("event", "sun qq login");
        redisManager.publish("log", JSON.toJSONString(map));
    }
}
