package mao;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Project name(项目名称)：Redis_client
 * Package(包名): mao
 * Class(测试类名): RedisClientTest
 * Author(作者）: mao
 * Author QQ：1296193245
 * GitHub：https://github.com/maomao124/
 * Date(创建日期)： 2022/6/29
 * Time(创建时间)： 14:24
 * Version(版本): 1.0
 * Description(描述)： 测试类
 */

class RedisClientTest
{

    static RedisClient redisClient;


    @BeforeAll
    static void beforeAll()
    {
        redisClient = new RedisClient("127.0.0.1", 6379, "123456");
    }

    @AfterAll
    static void afterAll()
    {
        redisClient.close();
    }

    @Test
    void get()
    {
        System.out.println(redisClient.get("key12"));
    }

    @Test
    void set()
    {
        System.out.println(redisClient.set("key12", "1234"));
    }

    @Test
    void mget()
    {
        System.out.println(redisClient.mget("key1", "key11", "key12"));
    }

    @Test
    void delete()
    {
        System.out.println(redisClient.delete("key12"));
    }

    @Test
    void mset()
    {
        System.out.println(redisClient.mset("q1", "123", "q2", "456", "q3", "789"));
        System.out.println(redisClient.mget("q1", "q2", "q3"));
        System.out.println(redisClient.delete("q1"));
        System.out.println(redisClient.delete("q2"));
        System.out.println(redisClient.delete("q3"));
    }

    @Test
    void pexpire()
    {
        System.out.println(redisClient.pexpire("key12", 20000L));
    }

    @Test
    void ttl()
    {
        System.out.println(redisClient.ttl("key12"));
    }

    @Test
    void hset()
    {
        System.out.println(redisClient.hset("map14", "a", "1", "b", "2", "c", "3", "d", "4"));
    }

    @Test
    void hget()
    {
        System.out.println(redisClient.hget("map14", "a"));
        System.out.println(redisClient.hget("map14", "b"));
        System.out.println(redisClient.hget("map14", "c"));
        System.out.println(redisClient.hget("map14", "d"));
        System.out.println(redisClient.hget("map14", "e"));
    }

    @Test
    void hkeys()
    {
        System.out.println(redisClient.hkeys("map14"));
    }

    @Test
    void hvals()
    {
        System.out.println(redisClient.hvals("map14"));
    }

    @Test
    void hlen()
    {
        System.out.println(redisClient.hlen("map14"));
    }

    @Test
    void lpush()
    {
        System.out.println(redisClient.lpush("list3", "1", "2", "3", "8", "12", "15", "22", "29"));
    }

    @Test
    void llen()
    {
        System.out.println(redisClient.llen("list3"));
    }
}