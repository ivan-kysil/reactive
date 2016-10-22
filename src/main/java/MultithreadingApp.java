import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class MultithreadingApp {

    public static int counter = 0;
    public static AtomicInteger atomicCounter = new AtomicInteger(0);
    public static Random rnd = new Random();

    public static void main(String[] args) {
        Supplier supplier = () -> counter;
        Consumer<Integer> consumer = (newCounter) -> {
            System.out.println(Thread.currentThread().getName() + ":" + newCounter);
            counter = newCounter;
        };
        Semaphore semaphore = new Semaphore(1);

        MyThread t1 = new MyThread(supplier, consumer, semaphore);
        MyThread t2 = new MyThread(supplier, consumer, semaphore);
        MyThread t3 = new MyThread(supplier, consumer, semaphore);
        MyThread t4 = new MyThread(supplier, consumer, semaphore);
        MyThread t5 = new MyThread(supplier, consumer, semaphore);

        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();
    }

    static class MyThread extends Thread {
        Supplier<Integer> supplier;
        Consumer<Integer> consumer;
        Semaphore semaphore;

        public MyThread(Supplier<Integer> supplier, Consumer<Integer> consumer, Semaphore semaphore) {
            this.supplier = supplier;
            this.consumer = consumer;
            this.semaphore = semaphore;
        }

        @Override
        public void run() {
            while (true) {
                int incr = rnd.nextInt(10);
                try {
                    Thread.sleep(rnd.nextInt(1000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                try {
                    semaphore.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Integer counter = supplier.get();
                try {
                    Thread.sleep(rnd.nextInt(1000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                consumer.accept(counter + incr);
                if (atomicCounter.addAndGet(incr) != supplier.get()) {
                    throw new IllegalStateException(atomicCounter.get() + " != " + supplier.get());
                };
                semaphore.release();
            }
        }
    }

}
