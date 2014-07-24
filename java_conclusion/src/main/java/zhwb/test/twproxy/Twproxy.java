package zhwb.test.twproxy;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Date: 2014/7/24
 * Time: 13:45
 *
 * @author jack.zhang
 */
public class Twproxy {
    public static void main(String[] args) throws InterruptedException {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxIdle(20);
        poolConfig.setMaxActive(1000);
        poolConfig.setMaxWait(2000);
        poolConfig.setTestOnBorrow(true);
        final JedisPool jedisPool = new JedisPool(poolConfig, "192.168.44.16", 11111, 2000);

        final String fuck = "qunye";
        Jedis resource = jedisPool.getResource();
        resource.del(fuck);
        jedisPool.returnResource(resource);
        ExecutorService executorService = Executors.newCachedThreadPool();
        final CountDownLatch begin = new CountDownLatch(1);
        int count = 10000;
        final CountDownLatch latch = new CountDownLatch(count);
        long start = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    Jedis resource = null;
                    try {
                        begin.await();
                        resource = jedisPool.getResource();
                        System.out.println("---------running----------" + resource.incr(fuck));
                    } catch (Exception e) {
                        System.out.println("---------error----------" + getStackTrace(e));
                    } finally {
                        jedisPool.returnResource(resource);
                        latch.countDown();
                    }
                }
            });
        }
        begin.countDown();
        latch.await();
        System.out.println("total time" + String.valueOf(System.currentTimeMillis() - start) + "ms");
    }

    private static String getStackTrace(Throwable t) {
        final Writer writer = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(writer);
        t.printStackTrace(printWriter);
        printWriter.close();
        return writer.toString();
    }
}