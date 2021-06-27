package org.my.guava.listening;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

import com.google.common.util.concurrent.*;

public class TestListenableFuture2 {
    // 创建线程池
    final static ListeningExecutorService service = MoreExecutors.listeningDecorator(Executors.newCachedThreadPool());


//    public static void main(String[] args) {
//        // 创建一个由invoke线程执行的线程池
//        ListeningExecutorService executorService = MoreExecutors.newDirectExecutorService();
//        ListenableFuture<?> listenableFuture = executorService.submit(new MyCallable(3, 10));
//        listenableFuture.addListener(() -> {
//            System.out.println("listen success");
//            doSomeThing();
//        }, executorService);
//    }


    public static void main(String[] args) throws Exception {
        Long t1 = System.currentTimeMillis();
        // 任务1
        ListenableFuture<Boolean> booleanTask = service.submit(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return true;
            }
        });

//        booleanTask.addListener(() -> {
//            System.out.println("======");
//        }, service);


        Futures.addCallback(booleanTask, new FutureCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                System.err.println("BooleanTask: " + result);
            }

            @Override
            public void onFailure(Throwable t) {
            }
        }, service);

        // 任务2
        ListenableFuture<String> stringTask = service.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return "Hello World";
            }
        });

        Futures.addCallback(stringTask, new FutureCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.err.println("StringTask: " + result);
            }

            @Override
            public void onFailure(Throwable t) {
            }
        }, service);

        // 任务3
        ListenableFuture<Integer> integerTask = service.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return new Random().nextInt(100);
            }
        });

        Futures.addCallback(integerTask, new FutureCallback<Integer>() {
            @Override
            public void onSuccess(Integer result) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.err.println("IntegerTask: " + result);
            }

            @Override
            public void onFailure(Throwable t) {
            }
        }, service);

        // 执行时间
        System.err.println("time: " + (System.currentTimeMillis() - t1));
    }

}