package org.my.protocol.modbus.thingsboard.server.actor;

/**
 * @author 李杰  210242
 * @description:
 * @date 2021/7/1 13:08
 */
public interface TbActor {
    boolean process(TbActorMsg msg);
    TbActorRef getActorRef();
    default void init(TbActorCtx ctx) throws TbActorException {
    }
}
