package com.xxxx.seckill.test;

import java.util.Arrays;
import java.util.TreeSet;

public class t6 {
    public static void main(String[] args) {
        Integer[]nums = new Integer[]{1,5,6,3,2,0};
        Arrays.sort(nums,(Integer a,Integer b)->{return a-b;});
        System.out.println(Arrays.toString(nums));
        TreeSet<int[]> L = new TreeSet<>((a, b) -> a[0] != b[0] ? a[0] - b[0] : a[1] - b[1]);
        int[]p = new int[]{1,2};
        int[]q = new int[]{2,3};
        L.add(q);
        L.add(p);
        System.out.println(Arrays.toString(L.pollFirst()));
        System.out.println(Arrays.toString(L.pollFirst()));

    }

}
