package org.my.protocol.modbus.guava.listening;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;


/**
 *为了提高任务处理速度，我们经常会将一些可并行处理的步骤用异步的方式去处理，如果想要获取异步计算的结果，
 * 在Java 8之前更多的用的是Future + Callable的方式来实现，下面是使用Future和Callable的一个demo，
 * 其中的是executor.submit()方法实际返回的就是FutureTask的实例，另外Future的get方法会一直阻塞直至获取结果。
 *
 * ListenableFuture接口扩展自Future接口，并添加了一个新方法 addListener，该方法是给异步任务添加监听
 */
public class ListeningUnBlock {
    public static void main(String[] args) {
        // 创建一个由invoke线程执行的线程池
        ListeningExecutorService executorService = MoreExecutors.newDirectExecutorService();
        ListenableFuture<?> listenableFuture = executorService.submit(new MyCallable(3, 10));
        listenableFuture.addListener(() -> {
            System.out.println("listen success");
            doSomeThing();
        }, executorService);
    }

    private static void doSomeThing() {
        System.out.println("======doSomeThing======");
    }


}
