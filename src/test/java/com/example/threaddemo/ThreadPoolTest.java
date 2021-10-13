package com.example.threaddemo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.*;

public class ThreadPoolTest {
    public static void main(String[] args) {
        //线程池维护线程的最少数量
        int corePoolSize = 2;
        //线程池维护线程的最大数量
        int maxPoolSize = 3;
        //线程池维护线程所允许的空闲时间（当线程池的数量超过corePoolSize时，多余的空闲线程的存活时间。）
        long keepAliveTime = 2;
        //缓存队列可以放多少个线程
        BlockingDeque<Runnable> queue = new LinkedBlockingDeque<>(3);
        //超出时抛出异常
//        RejectedExecutionHandler rejectedExecutionHandler = new ThreadPoolExecutor.AbortPolicy();
        //3个任务之间丢弃，而且不抛错误
        // RejectedExecutionHandler rejectedExecutionHandler = new ThreadPoolExecutor.DiscardPolicy();
        //调用这个线程的主线程执行
        //RejectedExecutionHandler rejectedExecutionHandler = new ThreadPoolExecutor.CallerRunsPolicy();
        // 最早进入工作队列的任务丢弃，然后把新任务加入到工作队列
        RejectedExecutionHandler rejectedExecutionHandler = new ThreadPoolExecutor.DiscardOldestPolicy();
        ThreadFactory factory = (Runnable r) -> {
            //创建一个线程
            Thread t = new Thread(r);
            //给创建的线程设置UncaughtExceptionHandler对象 里面实现异常的默认逻辑
            t.setDefaultUncaughtExceptionHandler((Thread thread1, Throwable e) -> {
                System.out.println("factory的exceptionHandler捕捉到异常--->>> \n" + e.getMessage());
            });
            return t;
        };
        //创建一个线程池
        ThreadPoolExecutor executor = new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveTime,
                TimeUnit.SECONDS, queue, factory, rejectedExecutionHandler);
//        runnable(executor);
        /***
         *
         * corePoolSize为2，maxMumPoolSize为3，阻塞队列大小为3。前面2（0和1）个任务被线程直接执行，
         * 接下来三个（2、3、4）任务加入了队列中。此时阻塞队列满了。所以再创建1（maxMumPoolSize-corePoolSize）个线程执行任务5
         * 。再然后，继续提交6，7，8，9，因为队列已满，所以执行拒绝策略，删除队列中等待时间最久的任务。
         * 加入6，删除2，队列中3，4，6。加入7，删除3，队列中4，6，7。加入8，删除4，队列中6，7，8。加入9，删除6，
         * 此时队列中剩下7,8和9。线程池中有空闲线程时，队列中的任务被执行，所以7,8和9任务被执行
         *
         */
        for (int i = 0; i < 10; i++) {
            Runnable myrun = new MyRunnable("task-"+i);
            executor.execute(myrun);
        }
        // 关闭线程池
        executor.shutdown();

    }

    static void runnable(ThreadPoolExecutor executor) {
        TestRunnable testRunnable = new TestRunnable(executor);
        for (int i = 0; i < 10; i++) {
            executor.execute(testRunnable);
        }
        executor.shutdown();
    }

    static class TestRunnable implements Runnable {
        static int i = 1;
        ThreadPoolExecutor executor;

        public TestRunnable(ThreadPoolExecutor executor) {
            this.executor = executor;
        }

        public static int getCount() {
            return i++;
        }

        @Override
        public void run() {
            synchronized (this) {
                System.out.print(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                        .format(new Date()) + "---》》》ActiveCount:"
                        + executor.getActiveCount() + "，CompletedTaskCount:" + executor.getCompletedTaskCount()
                        + "，Queue:" + executor.getQueue().remainingCapacity() + "，taskCount:" + executor.getTaskCount()
                );
                System.out.println("   " + Thread.currentThread().getName() + "  线程被调用了。第" + getCount() + "次");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }


    static class MyRunnable implements Runnable {
        private String name;

        public MyRunnable(String name) {
            this.name = name;
        }

        public void run() {
            try {
                System.out.println(this.name + " is running.");
                Thread.sleep(200);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}