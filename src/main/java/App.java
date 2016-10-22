import rx.Observable;
import rx.schedulers.Schedulers;

import java.util.concurrent.*;
import java.util.function.Consumer;

public class App {

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        System.out.println("1");

        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("supplyAsync");
            return "asdf";
        });

        CompletableFuture<Void> completableFuture1 = CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("runAsync");
        });

        completableFuture.thenAccept(System.out::println);
        completableFuture1.thenAccept(System.out::println);
        System.out.println("end");
        Thread.sleep(5000);
    }

    public static<T> void then(final Future<T> future, final Consumer<T> action) {
        Observable<T> observable = Observable.from(future, Schedulers.newThread());
        System.out.println("2");

        observable.subscribe( action::accept );
    }

}
