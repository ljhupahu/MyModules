package org.my.protocol.modbus.thingsboard.server.actor;

import org.thingsboard.server.common.data.id.DeviceId;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author 李杰  210242
 * @description:
 * @date 2021/7/1 13:39
 */
public class ActorTest {
    public static final String ROOT_DISPATCHER = "root-dispatcher";
    private static final int _100K = 100 * 1024;

    public static void main(String[] args) throws InterruptedException {
        TbActorSystemSettings settings = new TbActorSystemSettings(5, 6, 42);
        TbActorSystem actorSystem = new DefaultTbActorSystem(settings);
        actorSystem.createDispatcher(ROOT_DISPATCHER, Executors.newWorkStealingPool(6));
        testActorsAndMessages(1, _100K, 1, actorSystem);
    }


    public static void testActorsAndMessages(int actorsCount, int msgNumber, int times, TbActorSystem actorSystem) throws InterruptedException {
        Random random = new Random();
        int[] randomIntegers = new int[msgNumber];
        //计算出总的结果
        long sumTmp = 0;
        for (int i = 0; i < msgNumber; i++) {
            int tmp = random.nextInt();
            randomIntegers[i] = tmp;
            sumTmp += tmp;
        }
        long expected = sumTmp;

        //根据actor个数，创建actor上下文。
        List<ActorTestCtx> testCtxes = new ArrayList<>();
        //根据actor个数，创建actorRef, 相当于mailbox.
        List<TbActorRef> actorRefs = new ArrayList<>();

        for (int actorIdx = 0; actorIdx < actorsCount; actorIdx++) {
            ActorTestCtx testCtx = getActorTestCtx(msgNumber);
            //根据actor的个数actorsCount创建actorRef, 并将其添加到actorRef中，
            actorRefs.add(actorSystem.createRootActor(ROOT_DISPATCHER, new TestRootActor.TestRootActorCreator(
                    //TbActorId分为TbEntityActorId 和 TbStringActorId
                    //新建actorCreator时， 需要传入TbActorID和actorCtx
                    new TbEntityActorId(new DeviceId(UUID.randomUUID())), testCtx)));
            testCtxes.add(testCtx);
        }

//        for (int t = 0; t < times; t++) {
//            long start = System.nanoTime();
//            for (int i = 0; i < msgNumber; i++) {
//                int tmp = randomIntegers[i];
//                submitPool.execute(() -> actorRefs.forEach(actorId -> actorId.tell(new IntTbActorMsg(tmp))));
//            }
//            log.info("Submitted all messages");
//            testCtxes.forEach(ctx -> {
//                try {
//                    boolean success = ctx.getLatch().await(1, TimeUnit.MINUTES);
//                    if (!success) {
//                        log.warn("Failed: {}, {}", ctx.getActual().get(), ctx.getInvocationCount().get());
//                    }
//                    Assert.assertTrue(success);
//                    Assert.assertEquals(expected, ctx.getActual().get());
//                    Assert.assertEquals(msgNumber, ctx.getInvocationCount().get());
//                    ctx.clear();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            });
//            long duration = System.nanoTime() - start;
//            log.info("Time spend: {}ns ({} ms)", duration, TimeUnit.NANOSECONDS.toMillis(duration));
//        }
    }

    private static ActorTestCtx getActorTestCtx(int i) {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        AtomicLong actual = new AtomicLong();
        AtomicInteger invocations = new AtomicInteger();
        return new ActorTestCtx(countDownLatch, invocations, i, actual);
    }
}
