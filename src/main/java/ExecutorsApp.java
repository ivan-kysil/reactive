import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ExecutorsApp {
    private static int counter = 0;
    public static void main(String[] args) throws InterruptedException {
        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        long t1 = System.currentTimeMillis();

        Runnable runnable = () -> {
            System.out.println("Hello:" + (++counter) + "; Time:" + (System.currentTimeMillis() - t1));
        };
        Random random = new Random();

        for (int i = 0; i < 200000; i++) {
            scheduledExecutorService.schedule(runnable, random.nextInt(1000), TimeUnit.MILLISECONDS);
        }

        scheduledExecutorService.shutdown();
    }

}
