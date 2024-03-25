package org.example;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * Created with IntelliJ IDEA.
 *
 * @author： zhen wang
 * @date： 2024/3/23
 * @description：
 * @version: 1.0
 */
public class DownloadsSample {
    //同时模拟的并发访问用户数量
    public static int users = 10;
    //用户下载的真实数量
    public static int downTotal = 50000;
    //计数器
    public static int count = 0;

    //模拟重现问题
    public static void main(String[] args) throws InterruptedException {
        //调度器
        ExecutorService executorService = Executors.newCachedThreadPool();
        //信号量，用于模拟并发的人数
        final Semaphore semaphore = new Semaphore(users);
        for (int i = 0; i < downTotal; i++) {
            executorService.execute(() -> {
                //模拟并发的人数
                try {
                    //获取信号量
                    semaphore.acquire();
                    add();
                    //释放信号量
                    semaphore.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        Thread.sleep(3000);
        executorService.shutdown();
        System.out.println("下载总数：" + count);
    }


    public static void add() {
        count++;
    }
}
