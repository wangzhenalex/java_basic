import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMultiLock;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.sleep;

/**
 * @ClassName Ticket12306
 * @Description
 * @Author zhenwang
 * @Date 2024/1/12
 * @Version 1.0
 */
public class Ticket12306 implements Runnable {
    private InterProcessMultiLock lock;

    public Ticket12306() {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        //方式-2
        CuratorFramework client2 = CuratorFrameworkFactory.builder().connectString("localhost:2181")
                .namespace("base")
                .retryPolicy(retryPolicy)
                .build();
        client2.start();
        lock = new InterProcessMultiLock(client2, Collections.singletonList("/lock"));
    }

    Integer ticketNums = 10;

    @Override
    public void run() {
        while (true) {
            try {
                lock.acquire(3, TimeUnit.SECONDS);
                if (ticketNums > 0) {
                    System.out.println(Thread.currentThread().getName() + "拿到了第" + ticketNums-- + "票");
                    sleep(100);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    lock.release();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }
}
