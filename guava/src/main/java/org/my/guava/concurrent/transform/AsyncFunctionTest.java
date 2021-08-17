package org.my.guava.concurrent.transform;

import com.google.common.util.concurrent.*;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

/**
 * 主要是想说明 Difference between Function and AsyncFunction
 * Function类是对多个方法的迭代， 而AshycFuntion将ListenableFuture 转移到另一个ListenableFuture.
 * Reference: https://stackoverflow.com/questions/25633159/what-is-the-difference-between-function-and-asyncfunction-in-google-guava
 * @author 李杰  210242
 * @description:
 * @date 2021/8/12 15:15
 */
public class AsyncFunctionTest {
    private static ListeningExecutorService listeningExecutorService = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(1));
    private static ListeningExecutorService listeningExecutorService1 = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(1));
    static AsyncFunction<String, String> asyncFunction = new AsyncFunction<String, String>() {
        @Override
        public ListenableFuture<String> apply(final String input) throws Exception {
            return listeningExecutorService1.submit(new Callable<String>() {
                @Override
                public String call() throws Exception {
                    Thread.sleep(3000);
                    return input + "101";
                }
            });
        }
    };


    static ListenableFuture<String> lis = listeningExecutorService.submit(new Callable<String>() {
        @Override
        public String call() throws Exception {
            return "张三";
        }
    });

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ListenableFuture<String> lf = Futures.transformAsync(lis, asyncFunction);
        System.out.println(lf.get());
        listeningExecutorService.shutdown();
    }
}
