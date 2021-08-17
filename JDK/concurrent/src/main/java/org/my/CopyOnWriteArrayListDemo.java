package org.my;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * @author 李杰  210242
 * @description:
 * @date 2021/8/5 17:05
 */
public class CopyOnWriteArrayListDemo {
    public static void main(String[] args) {
        CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>();
        list.add("12");
        list.add("34");
        list.add("56");

        ArrayList<String> arrayList = new ArrayList<>();
        list.stream().filter(str -> str.equals("12")).collect(Collectors.toList()).forEach( str -> System.out.println(str));

//        for (int i = 0; i < list.size(); i++) {
//            System.out.println(list.get(i));
//            list.remove(i);
//        }

        for (Iterator<String> i = list.iterator(); i.hasNext();){
            String next = i.next();
            System.out.println(next);
            i.remove();
        }

    }
}
