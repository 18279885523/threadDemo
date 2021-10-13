package com.example.threaddemo;

import java.util.concurrent.*;

public class SynchronousQueueTest {

    private static final SynchronousQueue<String> synchronousQueue = new SynchronousQueue<>();

    private static final ExecutorService service = Executors.newCachedThreadPool();

    public static class MyTask implements Runnable {
        static int i = 1;

        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + "  线程被调用了。第" + getCount() + "次");
        }

        public static int getCount() {
            return i++;
        }
    }

    public static void main(String[] args) {
        ThreadPoolExecutor pool = new ThreadPoolExecutor(
                1,
                2,
                60,
                TimeUnit.SECONDS, new SynchronousQueue(),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.DiscardOldestPolicy()
        );
        for (int i = 0; i < 10; i++) {
            pool.execute(new MyTask());
        }
        pool.shutdown();
//        service.submit(() -> {
//            try {
//                synchronousQueue.put("liu");
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//            System.out.println("Consumer finished spending");
//        });
//
//
//        service.submit(() ->{
//            try {
//                synchronousQueue.take();
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//            System.out.println("take over");
//        });
    }
}
