package PCModel;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 使用BlockingQueue实现生产者消费者模型
 */

public class BQ {
    private static volatile int count = 0;
    private static final int capacity = 10;
    private static BlockingQueue<String> queue =  new LinkedBlockingQueue<>(capacity);



    class Producer implements Runnable{
        @Override
        public void run(){
            for(int i = 0 ; i < 20 ; i++){
                String ele = "ele" + String.valueOf(++count);
                try {
                    queue.put(ele);
                    System.out.println(Thread.currentThread().getName()
                            + "生产者生产，目前总共有" + count);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    class Consumer implements Runnable{
        @Override
        public void run(){
            for(int i = 0 ; i < 20 ; i++){
                try {
                    String ele = queue.take();
                    count--;
                    System.out.println(Thread.currentThread().getName()
                            + "消费者消费，目前总共有" + count);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    }
    public static void main(String[] args) {
        BQ bq = new BQ();
        new Thread(bq.new Producer()).start();
        new Thread(bq.new Consumer()).start();
        new Thread(bq.new Producer()).start();
        new Thread(bq.new Consumer()).start();
        new Thread(bq.new Producer()).start();
        new Thread(bq.new Consumer()).start();
        new Thread(bq.new Producer()).start();
        new Thread(bq.new Consumer()).start();
    }
}
