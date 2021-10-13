package com.example.threaddemo;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class MyDelayed implements Delayed {
    private final String taskName;
    private final long nowTime = System.currentTimeMillis();
    private final long expireTime;

    public MyDelayed(String taskName, long expireTime) {
        this.taskName = taskName;
        this.expireTime = expireTime;
    }
    /**
     * 获得延迟时间
     *
     * 用过期时间-当前时间
     * @param unit 时间单位
     * @return 延迟时间
     */
    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert((nowTime + expireTime) - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
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
        return (int) (this.getDelay(TimeUnit.MILLISECONDS) - o.getDelay(TimeUnit.MILLISECONDS));
    }

    @Override
    public String toString() {
        return "MyDelayed{" +
                "taskName='" + taskName + '\'' +
                ", nowTime=" + nowTime +
                ", expireTime=" + expireTime +
                '}';
    }
}
