package com.example.threaddemo;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class SyncDemo implements Runnable {

    //非公平锁，如果要改为公平锁时传入 true
    //private ReentrantLock reentrantLock = new ReentrantLock();

    public synchronized void get() {
        log.info("2 enter thread name-->" + Thread.currentThread().getName());
        //reentrantLock.lock();
        log.info("3 get thread name-->" + Thread.currentThread().getName());
        set();
        //reentrantLock.unlock();
        log.info("5 leave run thread name-->" + Thread.currentThread().getName());
    }

    public synchronized void set() {
        //reentrantLock.lock();
        log.info("4 set thread name-->" + Thread.currentThread().getName());
        //reentrantLock.unlock();
    }

    @Override
    public void run() {
        log.info("1 run thread name-->" + Thread.currentThread().getName());
        get();
    }

    public static void main(String[] args) {
        SyncDemo test = new SyncDemo();
        for (int i = 0; i < 5; i++) {
            new Thread(test, "thread-" + i).start();
        }
    }
}

