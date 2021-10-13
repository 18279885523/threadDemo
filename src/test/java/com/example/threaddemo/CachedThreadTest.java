package com.example.threaddemo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CachedThreadTest {
    public static void main(String[] args) {

        ExecutorService executorService = new newCachedThreadPoolTest().newCachedThreadPoolTest();

        for (int i = 0; i < 10; i++) {
            executorService.execute(new TestRunnable());
        }
        executorService.shutdown();
    }

    static class TestRunnable implements Runnable {
        static int i = 1;

        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + "  线程被调用了。第" + getCount() + "次");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        public static int getCount() {
            return i++;
        }
    }

    static class newCachedThreadPoolTest {
        ExecutorService newCachedThreadPoolTest() {
            /**
             * 此线程池的线程数不受限制。如果所有的线程都在执行任务并且又有新的任务到来，这个线程池将创建一个新的线程并将其提交到 Executor。
             * 只要其中一个线程变为空闲，它就会执行新的任务。 如果一个线程有 60 秒的时间都是空闲的，它们将被结束生命周期并从缓存中删除。
             */
            ExecutorService executorService = Executors.newCachedThreadPool();
            return executorService;
        }
    }
}
