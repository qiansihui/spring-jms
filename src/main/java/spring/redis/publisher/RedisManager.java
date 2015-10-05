package spring.redis.publisher;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by vincent on 15-9-8.
 */

public class RedisManager {

    private JedisPool jedisPool;

    public void publish(String key,String val){
        Jedis jedis = jedisPool.getResource();
        try {
             jedis.publish(key,val);
        } finally {
            jedis.close();
        }
    }


    public JedisPool getJedisPool() {
        return jedisPool;
    }

    public void setJedisPool(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }
}
