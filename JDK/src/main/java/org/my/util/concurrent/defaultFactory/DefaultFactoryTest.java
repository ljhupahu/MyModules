package org.my.util.concurrent.defaultFactory;

import org.my.util.concurrent.threadfactory01.WorkRunnable;
import org.my.util.concurrent.threadfactory01.WorkThreadFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author 李杰  210242
 * @description:
 * @date 2021/6/24 18:03
 */
public class DefaultFactoryTest {
    public static void main(String[] args) {
        ExecutorService es =  Executors.newCachedThreadPool(new DefaultThreadFactory());

        //同时并发5个工作线程
        es.execute(new WorkRunnable());
        es.execute(new WorkRunnable());
        es.execute(new WorkRunnable());
        es.execute(new WorkRunnable());
        es.execute(new WorkRunnable());

    }
}
