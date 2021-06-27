package org.my.protobuf;

import com.google.protobuf.InvalidProtocolBufferException;
import org.my.protobuf.gps.gen.Teacher;

import java.util.Arrays;

public class ProtobufTest {
    public static void main(String[] args) {
        byte[] serialize = serialize();
        System.out.println(Arrays.toString(serialize));
    }

    public static byte[] serialize(){
        Teacher.tea.Builder builder = Teacher.tea.newBuilder();
        builder.setId(11).setName("asdfwefasdf Teacher").setAge(28).setCourse("kkkwe");
        return builder.build().toByteArray();
    }

    public static Teacher.tea deserialize(byte[] byteArray) throws InvalidProtocolBufferException {
        return Teacher.tea.parseFrom(byteArray);
    }
}
