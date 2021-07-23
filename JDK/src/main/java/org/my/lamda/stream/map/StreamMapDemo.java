package org.my.lamda.stream.map;

import lombok.Data;

import java.util.ArrayList;

@Data //省去了get、set方法，需要在pom.xml文件中导入lombok依赖
public class StreamMapDemo {

    private String username;
    private String password;
    private Integer age;
    public StreamMapDemo(){

    }

    public StreamMapDemo(String username, String password, Integer age) {
        this.username = username;
        this.password = password;
        this.age = age;
    }


    //public static void main(final String... args)
    public static void main(String[] args)
    {
        ArrayList<StreamMapDemo> list = new ArrayList<>();

        list.add(new StreamMapDemo("liubei","111",40));
        list.add(new StreamMapDemo("zhangfei","222",30));
        list.add(new StreamMapDemo("guanyu","333",35));

        System.out.println("list: "+list.toString());

        System.out.println("\ntest1:");
        list.stream().map(n->n)
                .forEach(n-> System.out.println(n));

        System.out.println("\ntest2--age:");
        list.stream().map(n->n.getAge())
                .forEach(n-> System.out.println(n));

        System.out.println("\ntest3--age:");
        list.stream().map(n->n.getAge())
                .map(n->n).forEach(n-> System.out.println(n));

        System.out.println("\ntest2--username:");
        list.stream().map(n->n.getUsername())
                .forEach(n-> System.out.println(n));

        System.out.println("\ntest3--username:");
        //list.stream().map(n->n.getUsername()).map(n->n).forEach(n-> System.out.println(n));
        list.stream().map(n->n.getUsername())
                .forEach(n-> System.out.println(n));
    }
}
