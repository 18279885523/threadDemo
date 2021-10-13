package com.example.threaddemo;

import lombok.SneakyThrows;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/***
 * 创建可容纳固定数量线程的池子，每个线程的存活时间是无限的，
 */
public class FixedThreadTest {
    public static void main(String[] args) {
        ExecutorService executorService = new newFixedThreadPool().newFixedThreadPool();

        for (int i = 0; i < 10; i++) {
            executorService.execute(new TestRunnable2());
        }
        executorService.shutdown();
    }

}

class TestRunnable2 implements Runnable {
    static int i = 1;

    @SneakyThrows
    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + "  线程被调用了。第" + getCount() + "次");
        Thread.sleep(100000);
    }

    public static int getCount() {
        return i++;
    }
}

class newFixedThreadPool {
    ExecutorService newFixedThreadPool() {
        /**
         * 拥有固定数量线程的线程池
         * 如果有更多的任务，存储在 LinkedBlockingQueue 里。通常跟底层处理器CPU支持的线程总数有关。
         */
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        return executorService;
    }
}
