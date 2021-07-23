/**
 * Copyright © 2016-2021 The Thingsboard Authors
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.my.protocol.modbus.thingsboard.server.actor;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Data
public final class TbActorMailbox implements TbActorCtx {
    private static final boolean HIGH_PRIORITY = true;
    private static final boolean NORMAL_PRIORITY = false;

    private static final boolean FREE = false;
    private static final boolean BUSY = true;

    private static final boolean NOT_READY = false;
    private static final boolean READY = true;

    private final TbActorSystem system;
    private final TbActorSystemSettings settings;
    private final TbActorId selfId;
    private final TbActorRef parentRef;
    private final TbActor actor;
    private final Dispatcher dispatcher;
    private final ConcurrentLinkedQueue<TbActorMsg> highPriorityMsgs = new ConcurrentLinkedQueue<>();
    private final ConcurrentLinkedQueue<TbActorMsg> normalPriorityMsgs = new ConcurrentLinkedQueue<>();
    private final AtomicBoolean busy = new AtomicBoolean(FREE);
    private final AtomicBoolean ready = new AtomicBoolean(NOT_READY);
    private final AtomicBoolean destroyInProgress = new AtomicBoolean();

    public void initActor() {
        dispatcher.getExecutor().execute(() -> tryInit(1));
    }

    private void tryInit(int attempt) {
        try {
            log.debug("[{}] Trying to init actor, attempt: {}", selfId, attempt);
            if (!destroyInProgress.get()) {
                actor.init(this);
                if (!destroyInProgress.get()) {
                    ready.set(READY);
                    tryProcessQueue(false);
                }
            }
        } catch (Throwable t) {
            log.debug("[{}] Failed to init actor, attempt: {}", selfId, attempt, t);
        }
    }

    private void enqueue(TbActorMsg msg, boolean highPriority) {
        if (!destroyInProgress.get()) {
            if (highPriority) {
                highPriorityMsgs.add(msg);
            } else {
                normalPriorityMsgs.add(msg);
            }
            tryProcessQueue(true);
        } else {
            log.error("Failed to enqueue!");
        }
    }

    private void tryProcessQueue(boolean newMsg) {
        if (ready.get() == READY) {
            if (newMsg || !highPriorityMsgs.isEmpty() || !normalPriorityMsgs.isEmpty()) {
                if (busy.compareAndSet(FREE, BUSY)) {
                    dispatcher.getExecutor().execute(this::processMailbox);
                } else {
                    log.trace("[{}] MessageBox is busy, new msg: {}", selfId, newMsg);
                }
            } else {
                log.trace("[{}] MessageBox is empty, new msg: {}", selfId, newMsg);
            }
        } else {
            log.trace("[{}] MessageBox is not ready, new msg: {}", selfId, newMsg);
        }
    }

    private void processMailbox() {
        boolean noMoreElements = false;
        for (int i = 0; i < settings.getActorThroughput(); i++) {
            TbActorMsg msg = highPriorityMsgs.poll();
            if (msg == null) {
                msg = normalPriorityMsgs.poll();
            }
            if (msg != null) {
                try {
                    log.debug("[{}] Going to process message: {}", selfId, msg);
                    actor.process(msg);
                } catch (Throwable t) {
                    log.debug("[{}] Failed to process message: {}", selfId, msg, t);
                }
            } else {
                noMoreElements = true;
                break;
            }
        }
        if (noMoreElements) {
            busy.set(FREE);
            dispatcher.getExecutor().execute(() -> tryProcessQueue(false));
        } else {
            dispatcher.getExecutor().execute(this::processMailbox);
        }
    }

    @Override
    public TbActorId getSelf() {
        return selfId;
    }

    @Override
    public void tell(TbActorId target, TbActorMsg actorMsg) {
        system.tell(target, actorMsg);
    }


    @Override
    public TbActorId getActorId() {
        return selfId;
    }

    @Override
    public void tell(TbActorMsg actorMsg) {
        enqueue(actorMsg, NORMAL_PRIORITY);
    }

    @Override
    public void tellWithHighPriority(TbActorMsg actorMsg) {
        enqueue(actorMsg, HIGH_PRIORITY);
    }

}
