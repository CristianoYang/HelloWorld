public class DeadLockTest {

    //创建资源
    private static Object resourceA = new Object();
    private static Object resourceB = new Object();

    class A implements Runnable{
        @Override
        public void run() {
            synchronized (resourceA){
                System.out.println(Thread.currentThread() + " get ResourceA.");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread() + " waiting for ResourceB.");
                synchronized (resourceB){
                    System.out.println(Thread.currentThread() + " get ResourceB.");
                }
            }
        }
    }

    class B implements Runnable{
        @Override
        public void run() {
            synchronized (resourceB){
                System.out.println(Thread.currentThread() + " get ResourceB.");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread() + " waiting for ResourceA.");
                synchronized (resourceA){
                    System.out.println(Thread.currentThread() + " get ResourceA.");
                }
            }
        }
    }

    public static void main(String[] args) {

        DeadLockTest deadLockTest = new DeadLockTest();
        new Thread(deadLockTest.new A()).start();
        new Thread(deadLockTest.new B()).start();
    }
}
