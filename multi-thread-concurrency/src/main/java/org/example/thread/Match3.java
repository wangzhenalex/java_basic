package org.example.thread;

import java.util.Random;
import java.util.concurrent.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @author： zhen wang
 * @date： 2024/3/25
 * @description：Callable
 * @version: 1.0
 */
public class Match3 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executorService  = Executors.newFixedThreadPool(3);
        //创建一个新的线程
        Runner3 liuxiang = new Runner3();
        liuxiang.setName("刘翔");
        Runner3 laoqi = new Runner3();
        laoqi.setName("老齐");
        Runner3 op = new Runner3();
        op.setName("路飞");

        //将这个对象扔到线程池中，线程池自动分配一个线程来运行liuxiang这个对象的call方法
        //Future用于接受线程内部call方法的返回值
        Future<Integer> result1 =  executorService.submit(liuxiang);
        Future<Integer> result2 =  executorService.submit(laoqi);
        Future<Integer> result3 =  executorService.submit(op);

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executorService.shutdown();//关闭线程池释放所有资源
        System.out.println("刘翔累计跑了" + result1.get() + "米" );
        System.out.println("老齐累计跑了" + result2.get() + "米" );
        System.out.println("路飞累计跑了" + result3.get() + "米" );

    }
}

class Runner3 implements Callable<Integer> {
    private String name ;
    public void setName(String name){
        this.name = name;
    }
    @Override
    public Integer call() throws Exception {
        Integer speed = new Random().nextInt(100);
        Integer distance = 0;
        for (int i = 1; i <= 100; i++) {
            try {
                //当前线程休眠1秒
                Thread.sleep(1000);
                distance = i * speed;
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println(this.name + "已前进" + distance + "米(" + speed + "米/秒)");
        }
        return distance * speed;
    }
}