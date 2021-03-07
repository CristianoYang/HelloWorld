package PCModel;

/**
 * 用wait/notify实现生产者消费者模型
 * 生产者之间是阻塞的
 */

public class waitNotify {
    private static volatile int count = 0;
    private static final int capacity = 10;
    private static Object resource = new Object();

    class Producer implements Runnable{
        @Override
        public void run(){
            for(int i = 0 ; i < 20 ; i++){
                synchronized (resource){
                    while(count==capacity){
                        try {
                            resource.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    count++;
                    System.out.println(Thread.currentThread().getName()
                            + "生产者生产，目前总共有" + count);
                    resource.notifyAll();
                }
            }
        }
    }

    class Consumer implements Runnable{
        @Override
        public void run(){
            for(int i = 0 ; i < 20 ; i++){
                synchronized (resource){
                    while(count==0){
                        try {
                            resource.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    count--;
                    System.out.println(Thread.currentThread().getName()
                            + "消费者消费，目前总共有" + count);
                    resource.notifyAll();
                }
            }
        }
    }

    public static void main(String[] args) {
        waitNotify twoThreadPrint = new waitNotify();
        Thread threadA = new Thread(twoThreadPrint.new Producer());
        Thread threadB = new Thread(twoThreadPrint.new Consumer());

        threadA.start();
        threadB.start();
    }

}
