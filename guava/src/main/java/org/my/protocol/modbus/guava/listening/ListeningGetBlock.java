package org.my.protocol.modbus.guava.listening;

import java.util.concurrent.*;

/**
 *为了提高任务处理速度，我们经常会将一些可并行处理的步骤用异步的方式去处理，如果想要获取异步计算的结果，
 * 在Java 8之前更多的用的是Future + Callable的方式来实现，下面是使用Future和Callable的一个demo，
 * 其中的是executor.submit()方法实际返回的就是FutureTask的实例，另外Future的get方法会一直阻塞直至获取结果。
 *
 */
public class ListeningGetBlock {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<Integer> future = executor.submit(new MyCallable(3, 10));
        // get方法会阻塞，直至获取结果
        System.out.println(future.get());
        executor.shutdown();
    }
}

class MyCallable implements Callable<Integer> {
    private int a;
    private int b;

    public MyCallable(int a, int b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public Integer call() throws Exception {
        return a * b;
    }
}

