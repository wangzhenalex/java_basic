package org.example.thread;

import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 *
 * @author： zhen wang
 * @date： 2024/3/25
 * @description：Runable
 * @version: 1.0
 */
public class Match2 {
    public static void main(String[] args) {
        //创建一个新的线程
        Runner2 liuxiang = new Runner2();
        Thread thread1 = new Thread(liuxiang);
        //设置线程名称
        thread1.setName("刘翔");
        Runner2 laoqi = new Runner2();
        Thread thread2 = new Thread(laoqi);
        thread2.setName("老齐");
        Runner2 op = new Runner2();
        Thread thread3 = new Thread(op);
        thread3.setName("路飞");
        //启动线程
        thread1.start();
        thread2.start();
        thread3.start();

    }
}

class Runner2 implements Runnable {
    @Override
    public void run() {
        Integer speed = new Random().nextInt(100);
        for (int i = 1; i <= 100; i++) {
            try {
                Thread.sleep(1000);
                //当前线程休眠1秒
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName()+ "已前进" + (i * speed) + "米（" + speed + "米/秒)");
        }
    }
}