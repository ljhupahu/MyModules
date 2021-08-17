package org.my.guava.concurrent.listenableFuture;

import com.google.common.util.concurrent.*;

import java.util.concurrent.*;

/**
 * 此方法主要是想说明ListenableFuture与Future的区别
 */
public class ListenableFutureAndFuture {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        Future<Integer> submit = executorService.submit(() -> {
            Thread.sleep(1000);
            return 1;
        });
        System.out.println("Future取得结果：" + submit.get());
        System.out.println("submit.get()会产生阻塞， 这句话会在submit.get()之后才执行。======================");

        ListeningExecutorService service = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10));
        ListenableFuture future1 = service.submit(new Callable<Integer>() {
            public Integer call() throws InterruptedException {
                Thread.sleep(1000);
                System.out.println("call future 1.");
                return 1;
            }
        });

        Futures.addCallback(future1, new FutureCallback<Object>() {
            @Override
            public void onSuccess(Object result) {
                System.out.printf("success with: %s%n", result);
            }

            @Override
            public void onFailure(Throwable t) {
                System.out.printf("=====Failed==");
            }
        });
        System.out.println("=====这句话会在callBack之前执行，当ListenableFuture成功执行后，产生的结果会到onSuccess()方法中=================");

        executorService.shutdown();
        service.shutdown();
    }
}
