package com.example.threaddemo;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

@Slf4j
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
                    log.info(num + "add10");
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        Thread add20 = new Thread(new Runnable() {
            public void run() {
                try {
                    num += 20;
                    log.info(num + "add20");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        Thread multi3 = new Thread(new Runnable() {
            public void run() {
                try {
                    num *= 3;
                    log.info(num + "multi3");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        Thread devide4 = new Thread(new Runnable() {
            public void run() {
                try {
                    num /= 4;
                    log.info(num + "devide4");
                } catch (Exception e) {
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
        log.info(num + "");
    }
}