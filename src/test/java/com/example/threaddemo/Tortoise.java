package com.example.threaddemo;

public class Tortoise extends Animal {

    public Tortoise() {
        setName("乌龟");
    }

    // 重写running方法

    public void running() {
        // 跑的距离
        double dis = 0.3;
        length -= dis;
        if (length <= 0) {
            length = 0;
            System.out.println("乌龟获得了胜利");
            // 让兔子不要在跑了
            if (calltoback != null) {
                calltoback.win();
            }
        }
        System.out.println("乌龟跑了" + dis + "米，距离终点还有" + (int) length + "米");
        try {
            sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}