package com.example.threaddemo;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class CaluteOneNum {
    private volatile static int num = 0;

    public static void main(String[] args) {
        final CyclicBarrier ot2 = new CyclicBarrier(2);
        final CyclicBarrier ot3 = new CyclicBarrier(2);
        final CyclicBarrier ot4 = new CyclicBarrier(2);
        Thread add10 = new Thread(new Runnable() {
            public void run() {
                try {
                    num += 10;
                    System.out.println(num);
                    ot2.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }

            }
        });

        Thread add20 = new Thread(new Runnable() {
            public void run() {
                try {
                    ot2.await();
                    num += 20;
                    System.out.println(num);
                    ot3.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread multi3 = new Thread(new Runnable() {
            public void run() {
                try {
                    ot3.await();
                    num *= 3;
                    System.out.println(num);
                    ot4.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread devide4 = new Thread(new Runnable() {
            public void run() {
                try {
                    ot4.await();
                    num /= 4;
                    System.out.println(num);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }
        });


        devide4.start();
        multi3.start();
        add20.start();
        add10.start();
        try {
            devide4.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(num);
    }
}