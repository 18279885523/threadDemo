package com.example.threaddemo;

public abstract class Animal extends Thread {

    //比赛长度
    public double length = 20;

    //抽象方法
    public abstract void running();

    //在父类重写run方法，在子类只要重写running方法就可以了
    @Override
    public void run() {
        super.run();
        while (length > 0) {
            running();
        }
    }

    //回调数据
    public static interface Calltoback {
        public void win();
    }

    //创建接口对象
    public Calltoback calltoback;

}

