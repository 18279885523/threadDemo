package com.example.threaddemo;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.ArrayList;
import java.util.List;



@Slf4j
public class SignalCommunication {
    /***
     *  volatile 实现线程共享变量
     */
    // 定义一个共享变量实现通信，volatile修饰，否则线程不能及时感知
    static volatile boolean notice = false;

//    public static void main(String[] args) {
//        List<String> list = new ArrayList<>();
//        // 实现线程A
//        Thread threadA = new Thread(() -> {
//            for (int i = 1; i <= 10; i++) {
//                list.add("abc");
//                log.info("线程A向list中添加一个元素，此时list中的元素个数为：" + list.size());
//                try {
//                    Thread.sleep(500);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                if (list.size() == 5)
//                    notice = true;
//            }
//        });
//        // 实现线程B
//        Thread threadB = new Thread(() -> {
//            while (true) {
//                if (notice) {
//                    log.info("线程B收到通知，开始执行处理...");
//                    break;
//                }
//            }
//        });
//        //　需要先启动线程B
//        threadB.start();
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        // 再启动线程A
//        threadA.start();
//    }

    /***
     *
     * wait() 实现通信
    */
//    public static void main(String[] args) {
//        // 定义一个锁对象
//        Object lock = new Object();
//        List<String>  list = new ArrayList<>();
//        // 实现线程A
//        Thread threadA = new Thread(() -> {
//            synchronized (lock) {
//                for (int i = 1; i <= 10; i++) {
//                    list.add("abc");
//                    log.info("线程A向list中添加一个元素，此时list中的元素个数为：" + list.size());
//                    try {
//                        Thread.sleep(500);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    if (list.size() == 5)
//                        // 唤醒B线程
//                        lock.notify();
//                }
//            }
//        });
//        // 实现线程B
//        Thread threadB = new Thread(() -> {
//            while (true) {
//                synchronized (lock) {
//                    if (list.size() != 5) {
//                        try {
//                            //让出同步锁等待其他线程执行
//                            lock.wait();
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    log.info("线程B收到通知，开始执行自己的业务...");
//                }
//            }
//        });
//        //　需要先启动线程B
//        threadB.start();
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        // 再启动线程A
//        threadA.start();
//    }


    /**
     * 管道流实现通信
     * */
    static class SendThread implements Runnable{
        private PipedOutputStream outputStream = new PipedOutputStream();
        @Override
        public void run() {
            try {
                outputStream.write("管道对接成功！！".getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        public PipedOutputStream getOutputStream() {
            return outputStream;
        }
        public void setOutputStream(PipedOutputStream outputStream) {
            this.outputStream = outputStream;
        }
    }

    static class RecieveThread implements Runnable{
        private PipedInputStream inputStream = new PipedInputStream();
        @Override
        public void run() {
            byte[] data = new byte[1024];
            try {
                int len = inputStream.read(data);
                log.info("对接情况："+new String(data,0,len));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public PipedInputStream getInputStream() {
            return inputStream;
        }

        public void setInputStream(PipedInputStream inputStream) {
            this.inputStream = inputStream;
        }
    }

    public static void main(String[] args) {
        SendThread sendThread = new SendThread();
        RecieveThread recieveThread = new RecieveThread();
        PipedOutputStream outputStream = sendThread.getOutputStream();
        try {
            outputStream.connect(recieveThread.getInputStream());
            new Thread(sendThread).start();
            new Thread(recieveThread).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

