package org.my.protobuf;

import org.my.protobuf.gps.gen.Teacher;
import org.my.protobuf.gps.gen.TeacherA;

import java.io.*;
import java.util.Arrays;

public class JavaSeriObject {

    public static void main(String[] args) throws IOException {
        TeacherA teacher = new TeacherA();
        teacher.setId(11);
        teacher.setName("asdfwefasdf Teacher");
        teacher.setAge(28);
        teacher.setCourse("kkkwe");
        System.out.println(Arrays.toString(serialize(teacher)));
    }

    /**
     * 序列化
     * @param t
     * @return
     * @throws IOException
     */
    private static byte[] serialize(TeacherA t) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(out);
        oos.writeObject(t);
        return  out.toByteArray();
    }

    /**
     * 反序列化
     * @param byteArray
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private static TeacherA deserializer(byte[] byteArray) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(byteArray));
        return (TeacherA)ois.readObject();
    }
}
