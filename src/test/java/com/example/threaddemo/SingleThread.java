package com.example.threaddemo;

import lombok.SneakyThrows;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/***
 * 只有一个线程的线程池，线程的存活时间是无限的
 */
public class SingleThread {
    public static void main(String[] args) {
        ExecutorService executorService = new newSingleThreadExecutor().newSingleThreadExecutor();

        for (int i = 0; i < 10; i++) {
            executorService.execute(new TestRunnable());
        }
        executorService.shutdown();
    }

}


class TestRunnable implements Runnable {
    static int i = 1;

    @SneakyThrows
    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + "  线程被调用了。第" + getCount() + "次");
//        throw new Exception("a");
    }

    public static int getCount() {
        return i++;

    }

}



class newSingleThreadExecutor {

    ExecutorService newSingleThreadExecutor() {
        /**
         * 此线程池 Executor 只有一个线程。用于FIFO形式执行任务。
         * 如果此线程在执行任务时因异常而挂掉，则创建一个新线程来替换此线程，后续任务将在新线程中执行。
         */
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        return executorService;
    }
}
