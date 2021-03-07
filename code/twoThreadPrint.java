
public class twoThreadPrint {
    private static volatile boolean flag = false;
    private static Object resource = new Object();
    private static volatile int num = 1;
    private static volatile char letter = 'A';

    class A implements Runnable{
        @Override
        public void run(){
            for(int i = 0 ; i < 26 ; i++){
                synchronized (resource){
                    while(flag){
                        try {
                            resource.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    System.out.println(num);
                    num++;
                    flag = true;
                    resource.notifyAll();
                }
            }
        }
    }

    class B implements Runnable{
        @Override
        public void run(){
            for(int i = 0 ; i < 26 ; i++){
                synchronized (resource){
                    while(!flag){
                        try {
                            resource.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    System.out.println(letter);
                    letter = (char) (letter+1);
                    flag = false;
                    resource.notifyAll();
                }
            }
        }
    }

    public static void main(String[] args) {
        twoThreadPrint twoThreadPrint = new twoThreadPrint();
        Thread threadA = new Thread(twoThreadPrint.new A());
        Thread threadB = new Thread(twoThreadPrint.new B());

        threadA.start();
        threadB.start();
    }

}
