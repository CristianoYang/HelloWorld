package PCModel;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 使用ReentrantLock实现生产者消费者模型
 */

public class reentrantlock {
    //创建不可重入锁和对应的两个条件变量
    private static ReentrantLock lock = new ReentrantLock();
    private static Condition notFull = lock.newCondition();
    private static Condition notEmpty = lock.newCondition();
    //定义一个并行的队列和队列的大小（仓库的大小）
    private static Queue<String> queue = new LinkedList<>();
    private static final int queueSize = 10;
    private static volatile int id = 0;

    class Producer implements Runnable{
        @Override
        public void run(){
            for(int i = 0 ; i < 50 ; i++) {
                lock.lock();    //获取独占锁
                try {

                    //如果队列满则等待，这里循环条件判断避免虚假唤醒
                    while (queue.size() == queueSize) {
                        System.out.println("当前元素已满等待中...");
                        notFull.await();           //条件变量调用await使当前线程阻塞挂起
                    }
                    String ele = "ele" + String.valueOf(id++);
                    System.out.println("生产出一个元素：" + ele);
                    //生产一个元素到队列
                    queue.add(ele);
                    //队列中已经有元素可供消费了，所以用notFull来唤醒消费者线程
                    notEmpty.signalAll();

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();  //释放锁
                }
            }
        }
    }

    class Consumer implements Runnable{
        @Override
        public void run(){
            for(int i = 0 ; i < 50 ; i++) {
                lock.lock();    //获取独占锁
                try {

                    //如果队列为空则等待
                    while (queue.size() == 0) {
                        System.out.println("当前没有元素等待中...");
                        notEmpty.await();
                    }

                    //消费一个元素
                    String ele = queue.poll();
                    System.out.println("消费了一个元素: " + ele);
                    //唤醒生产者线程
                    notFull.signalAll();

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();  //释放锁
                }
            }
        }
    }

    public static void main(String[] args) {
        reentrantlock rl = new reentrantlock();
        new Thread(rl.new Producer()).start();
        new Thread(rl.new Consumer()).start();

    }
}
