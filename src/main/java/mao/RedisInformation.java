package mao;

import java.io.InputStream;
import java.util.Properties;

/**
 * Project name(项目名称)：Redis_client
 * Package(包名): mao
 * Class(类名): RedisInformation
 * Author(作者）: mao
 * Author QQ：1296193245
 * GitHub：https://github.com/maomao124/
 * Date(创建日期)： 2022/6/29
 * Time(创建时间)： 13:08
 * Version(版本): 1.0
 * Description(描述)： 无
 */

public class RedisInformation
{

    /**
     * ip
     */
    private static final String host;
    /**
     * 端口号
     */
    private static final int port;
    /**
     * 密码
     */
    private static final String password;

    /**
     * 单行字符串
     */
    public static final char SINGLE_LINE_STRING = '+';
    /**
     * 异常或者错误
     */
    public static final char ERROR = '-';
    /**
     * 数值
     */
    public static final char NUMBER = ':';
    /**
     * 多行字符串
     */
    public static final char MULTILINE_STRING = '$';
    /**
     * 数组
     */
    public static final char ARRAY = '*';


    static
    {
        try
        {
            //从类路径里获取配置信息
            InputStream inputStream = RedisInformation.class.getClassLoader().getResourceAsStream("redis.properties");
            Properties properties = new Properties();
            //加载配置到properties
            properties.load(inputStream);
            //ip地址
            host = properties.getProperty("redis.host");
            //端口号
            port = Integer.parseInt(properties.getProperty("redis.port"));
            //密码
            password = properties.getProperty("redis.password");
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }


    public static String getHost()
    {
        return host;
    }

    public static int getPort()
    {
        return port;
    }

    public static String getPassword()
    {
        return password;
    }
}
