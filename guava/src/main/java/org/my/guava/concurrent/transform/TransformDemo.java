package org.my.guava.concurrent.transform;

import com.google.common.base.Function;
import com.google.common.util.concurrent.*;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

/**
 * @author 李杰  210242
 * @description:
 * @date 2021/8/12 14:32
 */
public class TransformDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ListeningExecutorService service = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10));

        ListenableFuture future1 = service.submit(new Callable<Integer>() {
            public Integer call() throws InterruptedException {
                Thread.sleep(5000);
                System.out.println("call future 1.");
                return 1;
            }
        });

        ListenableFuture future2 = service.submit(new Callable<Integer>() {
            public Integer call() throws InterruptedException {
                Thread.sleep(1000);
                System.out.println("call future 2.");
                //    throw new RuntimeException("----call future 2.");
                return 2;
            }
        });

        final ListenableFuture allFutures = Futures.allAsList(future1, future2);

        final ListenableFuture transform = Futures.transform(allFutures, new Function<List<Integer>, String>() {
            @Override
            public String apply(List<Integer> results)  {
                return String.format("success future:%d", results.size());
            }
        });

        System.out.println(transform.get());
        service.shutdown();
    }

}
