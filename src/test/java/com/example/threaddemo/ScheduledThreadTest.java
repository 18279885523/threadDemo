package com.example.threaddemo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScheduledThreadTest {

    ScheduledExecutorService ScheduledThreadPoolExecutorTest() {
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(2);

        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        //延迟2秒后启动，之后每间隔1秒执行一次
        executorService.scheduleAtFixedRate(new TestRunnable(), 2, 1, TimeUnit.SECONDS);
        return executorService;
    }
    public static void main(String[] args) {
        new ScheduledThreadTest().ScheduledThreadPoolExecutorTest();

    }

    static class TestRunnable implements Runnable {
        static int i = 1;

        @Override
        public void run() {
            System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            System.out.println(Thread.currentThread().getName() + "  线程被调用了。第" + getCount() + "次");
        }

        public static int getCount() {
            return i++;
        }
    }
}
