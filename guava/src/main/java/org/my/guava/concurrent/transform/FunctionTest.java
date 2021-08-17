package org.my.guava.concurrent.transform;

import com.google.common.base.Function;
import com.google.common.util.concurrent.*;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

/**
 *  * 主要是想说明 Difference between Function and AsyncFunction
 *  * Function类是对多个方法的迭代， 而AshycFuntion将ListenableFuture 转移到另一个ListenableFuture.
 *  Reference: https://stackoverflow.com/questions/25633159/what-is-the-difference-between-function-and-asyncfunction-in-google-guava
 * @author
 * @description:
 * @date 2021/8/12 15:15
 */
public class FunctionTest {
    private static ListeningExecutorService listeningExecutorService = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(1));
    private static ListeningExecutorService listeningExecutorService1 = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(1));
    static Function<String, String> function = new Function<String, String>() {
        @Override
        public String apply(final String input)  {
            return input + "101";
            };
        };

    static ListenableFuture<String> lis = listeningExecutorService.submit(new Callable<String>() {
        @Override
        public String call() throws Exception {
            return "张三";
        }
    });

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ListenableFuture<String> lf = Futures.transform(lis, function);

        System.out.println(lf.get());
        listeningExecutorService.shutdown();
    }
}
