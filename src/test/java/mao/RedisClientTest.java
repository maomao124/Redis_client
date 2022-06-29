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
        System.out.println(redisClient.mget("key1","key11","key12"));
    }

    @Test
    void delete()
    {
        System.out.println(redisClient.delete("key12"));
    }
}