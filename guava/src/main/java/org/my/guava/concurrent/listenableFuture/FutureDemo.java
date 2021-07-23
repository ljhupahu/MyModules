package org.my.guava.concurrent.listenableFuture;

import com.google.common.util.concurrent.*;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

/**
 * reference  https://www.jb51.net/article/92655.htm
 * @author 李杰  210242
 * @description:
 * @date 2021/7/8 10:14
 */
public class FutureDemo {
    public static void main(String[] args) throws Exception {
        FutureDemo futureDemo = new FutureDemo();
        futureDemo.should_test_furture();
    }


    public void should_test_furture() throws Exception {
        ListeningExecutorService service = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10));

        ListenableFuture future1 = service.submit(new Callable<Integer>() {
            public Integer call() throws InterruptedException {
                Thread.sleep(1000);
                System.out.println("call future 1.");
                return 1;
            }
        });

        ListenableFuture future2 = service.submit(new Callable<Integer>() {
            public Integer call() throws InterruptedException {
                Thread.sleep(5000);
                System.out.println("call future 2.");
                //    throw new RuntimeException("----call future 2.");
                return 2;
            }
        });

        final ListenableFuture allFutures = Futures.allAsList(future1, future2);

        ListenableFuture<Integer> transform = Futures.transformAsync(allFutures, input -> Futures.immediateFuture(input));



        Futures.addCallback(transform, new FutureCallback<Object>() {

            public void onSuccess(Object result) {
                System.out.println(result.getClass());
                System.out.printf("success with: %s%n", result);
            }

            public void onFailure(Throwable thrown) {
                System.out.printf("onFailure%s%n", thrown.getMessage());
            }
        });

        System.out.println(transform.get());
        service.shutdown();

    }
}
