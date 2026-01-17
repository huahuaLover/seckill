package com.xxxx.seckill.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TT3 {
    public static void main(String[] args) {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        //也就是只需要偶数的
        list.stream().filter(x -> x % 2 == 0)
                .forEach(System.out::println);
        List<String> originalList = Arrays.asList("apple","orange","banana","kwi","ab");
        List<String>filterList = originalList.stream()
                .filter(s->s.length() > 3)
                .collect(Collectors.toList());
        filterList.forEach(System.out::println);


    }
}
