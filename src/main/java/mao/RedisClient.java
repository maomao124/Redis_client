package mao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Project name(项目名称)：Redis_client
 * Package(包名): mao
 * Class(类名): RedisClient
 * Author(作者）: mao
 * Author QQ：1296193245
 * GitHub：https://github.com/maomao124/
 * Date(创建日期)： 2022/6/29
 * Time(创建时间)： 13:07
 * Version(版本): 1.0
 * Description(描述)：
 * <p>
 * ## RESP协议
 * <p>
 * Redis是一个CS架构的软件，通信一般分两步（不包括pipeline和PubSub）
 * <p>
 * * 客户端（client）向服务端（server）发送一条命令
 * * 服务端解析并执行命令，返回响应结果给客户端
 * <p>
 * 因此客户端发送命令的格式、服务端响应结果的格式必须有一个规范，这个规范就是通信协议。
 * <p>
 * <p>
 * <p>
 * 在Redis中采用的是**RESP**（Redis Serialization Protocol）协议
 * <p>
 * <p>
 * <p>
 * * lRedis 1.2版本引入了RESP协议
 * * lRedis 2.0版本中成为与Redis服务端通信的标准，称为RESP2
 * * lRedis 6.0版本中，从RESP2升级到了RESP3协议，增加了更多数据类型并且支持6.0的新特性--客户端缓存
 * <p>
 * <p>
 * <p>
 * lRedis 2.0版本：
 * <p>
 * 在RESP中，通过首字节的字符来区分不同数据类型，常用的数据类型包括5种
 * <p>
 * * 单行字符串：首字节是 ‘**+**’ ，后面跟上单行字符串，以CRLF（ "**\r\n**" ）结尾。例如返回"OK"： "+OK\r\n"
 * <p>
 * * 错误（Errors）：首字节是 ‘**-**’ ，与单行字符串格式一样，只是字符串是异常信息，例如："-Error message\r\n"
 * <p>
 * * 数值：首字节是 ‘**:**’ ，后面跟上数字格式的字符串，以CRLF结尾。例如：":10\r\n"
 * <p>
 * * 多行字符串：首字节是 ‘**$**’ ，表示二进制安全的字符串，最大支持512MB。例如：$5\r\nhello***\r\n
 * <p>
 * * $5的数字5：字符串占用字节大小
 * * hello：真正的字符串数据
 * * 如果大小为0，则代表空字符串："$0\r\n\r\n"
 * * u如果大小为-1，则代表不存在："$-1\r\n"
 * <p>
 * * 数组：首字节是 ‘*’，后面跟上数组元素个数，再跟上元素，元素数据类型不限
 * <p>
 * ```sh
 * *3\r\n
 * :10\r\n
 * $5\r\nhello\r\n
 * ```
 */


public class RedisClient
{
    private Socket socket;
    private PrintWriter printWriter;
    private BufferedReader bufferedReader;


    /**
     * Instantiates a new Redis client.
     */
    public RedisClient()
    {
        try
        {
            //连接redis
            socket = new Socket(RedisInformation.getHost(), RedisInformation.getPort());
            //获取输入流
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            //获取输出流
            printWriter = new PrintWriter(socket.getOutputStream());
            //身份认证
            if (RedisInformation.getPassword() != null)
            {
                sendRequest("auth", RedisInformation.getPassword());
                Object response = getResponse();
                System.out.println("密码验证成功");
            }
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * Instantiates a new Redis client.
     *
     * @param host     the host
     * @param port     the port
     * @param password the password
     */
    public RedisClient(String host, int port, String password)
    {
        try
        {
            //连接redis
            socket = new Socket(host, port);
            //获取输入流
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            //获取输出流
            printWriter = new PrintWriter(socket.getOutputStream());
            //身份认证
            if (password != null)
            {
                sendRequest("auth", password);
                Object response = getResponse();
                System.out.println("密码验证成功：" + response);
            }
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * Instantiates a new Redis client.
     *
     * @param host the host
     * @param port the port
     */
    public RedisClient(String host, int port)
    {
        try
        {
            //连接redis
            socket = new Socket(host, port);
            //获取输入流
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            //获取输出流
            printWriter = new PrintWriter(socket.getOutputStream());
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }


    /**
     * 关闭redis客户端与redis服务端的连接
     */
    public void close()
    {
        try
        {
            if (printWriter != null)
            {
                printWriter.close();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        try
        {
            if (bufferedReader != null)
            {
                bufferedReader.close();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        try
        {
            if (socket != null)
            {
                socket.close();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }

    /**
     * 发送请求
     *
     * @param args 发起请求的参数，参数数量不一定
     */
    private void sendRequest(String... args)
    {
        //先写入元素个数，数组，换行
        printWriter.println("*" + args.length);
        //剩余的都是数组，遍历添加
        for (String arg : args)
        {
            //$为多行字符串，长度
            printWriter.println("$" + arg.getBytes(StandardCharsets.UTF_8).length);
            printWriter.println(arg);
        }
        //刷新
        printWriter.flush();
    }

    /**
     * 获取发送请求后的响应
     *
     * @return Object对象
     */
    private Object getResponse()
    {
        try
        {
            //获取当前前缀，因为要判断是什么类型
            char prefix = (char) bufferedReader.read();
            //判断是什么类型
            if (prefix == RedisInformation.SINGLE_LINE_STRING)
            {
                //单行字符串
                //直接读一行，读到换行符
                return bufferedReader.readLine();
            }
            if (prefix == RedisInformation.ERROR)
            {
                //错误
                //抛出运行时异常
                throw new RuntimeException(bufferedReader.readLine());
            }
            if (prefix == RedisInformation.NUMBER)
            {
                //数值
                //转数字
                return Integer.valueOf(bufferedReader.readLine());
            }
            if (prefix == RedisInformation.MULTILINE_STRING)
            {
                //多行字符串
                //先获取长度
                int length = Integer.parseInt(bufferedReader.readLine());
                //判断数组是否为空
                if (length == -1 || length == 0)
                {
                    //不存在或者数组为空
                    //返回空字符串
                    return "";
                }
                //不为空，读取
                return bufferedReader.readLine();
            }
            if (prefix == RedisInformation.ARRAY)
            {
                //数组
                return readBulkString();
            }
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
        return null;
    }

    /**
     * 读取数组
     *
     * @return List<Object>
     * @throws IOException IOException
     */
    private List<Object> readBulkString() throws IOException
    {
        //获取当前数组的大小
        int size = Integer.parseInt(bufferedReader.readLine());
        //判断数组大小
        if (size == 0 || size == -1)
        {
            //返回null
            return null;
        }
        //数组有值
        //构建集合
        List<Object> list = new ArrayList<>(3);
        //遍历获取
        for (int i = 0; i < size; i++)
        {
            try
            {
                //递归获取
                list.add(getResponse());
            }
            catch (Exception e)
            {
                //异常加入到集合
                list.add(e);
            }
        }
        //返回
        return list;
    }


    /**
     * redis的get命令
     *
     * @param key key
     * @return value
     */
    public Object get(String key)
    {
        if (key == null)
        {
            return null;
        }
        sendRequest("get", key);
        return getResponse();
    }

    /**
     * redis的set命令
     *
     * @param key   key
     * @param value value
     * @return Object
     */
    public Object set(String key, String value)
    {
        if (key == null)
        {
            return null;
        }
        sendRequest("set", key,value);
        return getResponse();
    }

    /**
     * redis的mget命令
     * @param key key
     * @return Object(list集合)
     */
    public Object mget(String key)
    {
        if (key == null)
        {
            return null;
        }
        sendRequest("mget", key);
        return getResponse();
    }


    /**
     * Test.
     */
    public void test()
    {
        /*sendRequest("get", "key11");
        Object response = getResponse();
        System.out.println(response);*/
        System.out.println(get("key11"));
        System.out.println(get("key11"));
        System.out.println(get("key1"));
        System.out.println(set("key12","125678656"));
        System.out.println(get("key12"));
    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args)
    {
        new RedisClient().test();
    }
}
