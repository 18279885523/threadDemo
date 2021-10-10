package com.example.threaddemo;

import lombok.extern.slf4j.Slf4j;

import java.util.Calendar;
import java.util.Date;

@Slf4j
public class SingleDemo {

//    public static void main(String[] args) throws Exception {
//        eat();
//        drink();
//    }

    private static void eat() throws Exception {
        log.info("开始吃饭...\t" + new Date());
        Thread.sleep(5000);
        log.info("我吃完了...\t" + new Date());
    }

    private static void drink() throws Exception {
        log.info("开始喝酒...\t" + new Date());
        Thread.sleep(5000);
        log.info("喝醉了...\t" + new Date());
    }


    //    public static void main(String[] args){
//
//        EatThread eatThread = new EatThread();
//        DrinkThread drinkThread = new DrinkThread();
//        Thread t1 = new Thread(eatThread);
//        Thread t2 = new Thread(drinkThread);
//
//        t1.start();
//        t2.start();
//
//
////        t1.run();
////        t2.run();
//    }
    static class EatThread implements Runnable {
        @Override
        public void run() {
            log.info("开始吃饭...\t" + new Date());
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("我吃完了...\t" + new Date());
        }
    }

    static class DrinkThread implements Runnable {
        @Override
        public void run() {
            log.info("开始喝酒...\t" + new Date());
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("喝醉了...\t" + new Date());
        }
    }


    public static class MyThread extends Thread {
        String value;

        public MyThread(String value) {
            this.value = value;
        }

        @Override
        public void run() {
            try {
                synchronized (value) {
                    log.info(value);
                    log.info(getName() + "开始等待;当前时间秒数：" + Calendar.getInstance().get(Calendar.SECOND));
                    //让当前线程进行wait
                    value.wait(10000);
                    value.notify();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        String value = "123456789";
        MyThread myThread1 = new MyThread(value);
        MyThread myThread2 = new MyThread(value);
        MyThread myThread3 = new MyThread(value);
        myThread1.start();
        myThread2.start();
        myThread3.start();
    }
}
