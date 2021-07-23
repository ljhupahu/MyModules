package org.my.guava.concurrent.settableFuture;

import com.google.common.util.concurrent.*;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author 李杰  210242
 * @description:
 * @date 2021/7/9 14:18
 */
public class MySettableFuture {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        SettableFuture<Object> settableFuture = SettableFuture.create();
        settableFuture.addListener(()->{
            System.out.println("========");
        }, executorService);

        settableFuture.set("success  settableFuture");

//        ListeningExecutorService listeningExecutorService = MoreExecutors.listeningDecorator(Executors.newSingleThreadExecutor());
//        ListenableFuture<?> submit = listeningExecutorService.submit(() -> {
//            System.out.println("====");
//        });


//        FutureCallback<String> callback = new FutureCallback<String>() {
//            @Override
//            public void onSuccess(String meal) {
//                System.out.println("success callback");
//            }
//            @Override
//            public void onFailure(Throwable throwable) {
//                System.out.println("failed");
//            }
//        };
//
//        Futures.addCallback(settableFuture, callback, executorService);

        executorService.shutdown();

    }
}
