package org.my.protocol.modbus.protobuf.gps.gen;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author 李杰  210242
 * @description:
 * @date 2021/6/29 15:00
 */
public class Temp {
    public static void main(String[] args) {
        ConcurrentMap<String, TeacherA> dispatchers = new ConcurrentHashMap<>();
        dispatchers.putIfAbsent("sdfasf", new TeacherA());
        System.out.println( dispatchers.putIfAbsent("sdfasf", new TeacherA()));
    }
}
