package com.nowcoder.community;

//一个文件只有一个类是public
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class BlockingQueueTests {

    public static void main(String[] args) {
        BlockingQueue<Integer> blockingQueue = new ArrayBlockingQueue<>(10);
        new Thread(new Producer(blockingQueue)).start();
        new Thread(new Consume(blockingQueue)).start();
        new Thread(new Consume(blockingQueue)).start();
        new Thread(new Consume(blockingQueue)).start();
    }

}


class Producer implements Runnable{
    private BlockingQueue<Integer> blockingQueue;

    public Producer(BlockingQueue<Integer> blockingQueue) {
        this.blockingQueue = blockingQueue;
    }

    @Override
    public void run() {

        try {
            for (int i = 0; i < 100; i++) {
                Thread.sleep(20);
                //存
                blockingQueue.put(i);
                System.out.println(Thread.currentThread().getName() + "produce" + blockingQueue.size());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

class Consume implements Runnable{
    private BlockingQueue<Integer> blockingQueue;

    public Consume(BlockingQueue<Integer> blockingQueue) {
        this.blockingQueue = blockingQueue;
    }

    @Override
    public void run() {


        try {
            while (true) {
                Thread.sleep(new Random().nextInt(1000));
                blockingQueue.take();
                System.out.println(Thread.currentThread().getName() + "consume" + blockingQueue.size());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }



    }
}



//class Producer implements Runnable {
//
//    private BlockingQueue<Integer> queue;
//
//    public Producer(BlockingQueue<Integer> queue) {
//        this.queue = queue;
//    }
//
//    @Override
//    public void run() {
//        try {
//            for (int i = 0; i < 100; i++) {
//                Thread.sleep(20);
//                queue.put(i);
//                System.out.println(Thread.currentThread().getName() + "生产:" + queue.size());
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//}
//
//class Consumer implements Runnable {
//
//    private BlockingQueue<Integer> queue;
//
//    public Consumer(BlockingQueue<Integer> queue) {
//        this.queue = queue;
//    }
//
//    @Override
//    public void run() {
//        try {
//            while (true) {
//                Thread.sleep(new Random().nextInt(1000));
//                queue.take();
//                System.out.println(Thread.currentThread().getName() + "消费:" + queue.size());
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}
//
