package org.my.guava.listening;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

import java.util.concurrent.ExecutionException;

/**
 * 如果需要对返回值做处理，可以使用Futures.transform方法，它是同步方法，另外还有一个异步方法Futures.transformAsync
 */
public class ListeningResultTransform {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // 创建一个由invoke线程执行的线程池
        ListeningExecutorService executorService = MoreExecutors.newDirectExecutorService();
        ListenableFuture<String> listenableFuture = executorService.submit(() -> "hello, future");

        // 同步转换
        ListenableFuture<Integer> future5 = Futures.transform(listenableFuture, String::length, executorService);
        System.out.println(future5.get());

    }
}
