package com.example.threaddemo;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;
import java.util.logging.SimpleFormatter;

public class DelayQueueTest {
    private static final DelayQueue<MyDelayed> delayQueue = new DelayQueue<>();

    private static final ExecutorService service = Executors.newCachedThreadPool();

    public static void main(String[] args) throws InterruptedException {

        ThreadPoolExecutor pool = new ThreadPoolExecutor(
                1,
                2,
                60,
                TimeUnit.SECONDS, new SynchronousQueue(),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.DiscardOldestPolicy()
        );
        service.submit(() -> {
            delayQueue.put(new MyDelayed("A-Task",5000));
            delayQueue.put(new MyDelayed("B-Task",4000));
            delayQueue.put(new MyDelayed("C-Task",3000));
            delayQueue.put(new MyDelayed("D-Task",2000));
            delayQueue.put(new MyDelayed("E-Task",1000));
        });
        while (true){
            System.out.println(delayQueue.take());
        }
    }


//    public static void main(String[] args) {
//        try {
//            ExecutorService es = Executors.newCachedThreadPool();
//            DelayQueue<DelayTask> queue = new DelayQueue<DelayTask>();
//            Random rand = new Random();
//            // add 20's DelayTask to DelayQueue
//            for (int i = 0; i < 20; i++) {
//                queue.add(new DelayTask(rand.nextInt(500)));
//            }
//            DelayTask delayTask = new DelayTask(501);
//            queue.add(delayTask);
//            es.execute(new DelayConsumer(queue));
//            TimeUnit.SECONDS.sleep(1);
//            es.shutdownNow();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
}

class DelayConsumer implements Runnable {
    private DelayQueue<DelayTask> queue;

    public DelayConsumer(DelayQueue<DelayTask> queue) {
        this.queue = queue;
    }

    public void run() {
        try {
            System.out.println("############print order by DelayQueue###############");
            // from DelayQueue get DelayTask and execute it
            while (!Thread.interrupted()) {
                queue.take().run();
            }
        } catch (InterruptedException e) {
            System.out.println("Exit Consumer");
        }
    }
}

class DelayTask implements Runnable, Delayed {
    protected static List<DelayTask> tasks = new ArrayList<DelayTask>();
    public long time = 0;
    public long delay;
    private static int count = 0;
    private int id = count++;

    private final long nowTime = System.currentTimeMillis();

    public DelayTask(long delay) {
        this.delay = delay;
        time = System.nanoTime() + TimeUnit.NANOSECONDS.convert(this.delay, TimeUnit.MICROSECONDS);
        tasks.add(this);
    }

    /**
     * 用于延迟队列内部比较排序
     * 当前时间的延迟时间 - 比较对象的延迟时间
     *
     * @param o 比较对象
     * @return 结果
     */
    @Override
    public int compareTo(Delayed o) {
        long result = ((DelayTask) o).getTime() - this.getTime();
        if (result > 0) {
            return 1;
        }
        if (result < 0) {
            return -1;
        }
        return 0;
    }

    /**
     * 获得延迟时间
     * <p>
     * 用过期时间-当前时间
     *
     * @param unit 时间单位
     * @return 延迟时间
     */
    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(time - System.nanoTime(), TimeUnit.NANOSECONDS);
    }

    @Override
    public void run() {
        System.out.println(this);
    }

    public String toString() {
        SimpleFormatter simpleFormatter = new SimpleFormatter();

        return "Task " + id + ":" +
                ", nowTime=" + nowTime +
                ", expireTime=" + time;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }


}
