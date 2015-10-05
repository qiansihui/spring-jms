package spring.redis.listener;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

/**
 * Created by vincent on 15-9-8.
 */
public class RedisManager {

    private JedisPool jedisPool;

    private JedisPubSub jedisPubSub;

    public void init(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Jedis jedis = jedisPool.getResource();
                try {
                    jedis.psubscribe(jedisPubSub,"log");
                }finally {
                    jedis.close();
                }
            }
        }).start();
    }

    public JedisPool getJedisPool() {
        return jedisPool;
    }

    public void setJedisPool(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    public JedisPubSub getJedisPubSub() {
        return jedisPubSub;
    }

    public void setJedisPubSub(JedisPubSub jedisPubSub) {
        this.jedisPubSub = jedisPubSub;
    }
}
