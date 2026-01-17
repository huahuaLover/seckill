package com.xxxx.seckill.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TT2 {
    public static void main(String[] args) {
        TT2 tt2 = new TT2();
        System.out.println(tt2.intersectionSizeTwo(new int[][]{{1, 3}, {3, 7}, {5, 7},{7,8}}));
    }
    public int intersectionSizeTwo(int[][] intervals) {
        //Arrays.sort(intervals,(a,b)->{return a[1]!=b[1]?a[1]-b[1]:a[0]-b[0];});

        Arrays.sort(intervals,(a,b)->{return a[1]!=b[1]?a[1]-b[1]:b[0]-a[0];});
       int a = -1, b = -1, ans = 0;
       for(int[] i:intervals)
       {
           if(i[0]>b)
           {
               a=i[1]-1;
               b = i[1];
               ans += 2;
           }
           else if(i[0]>a)
           {
                a = b;
                b = i[1];
                ans++;
           }
       }
       return ans;
    }
    private int lowerBound(List<int[]> st, int target) {
        int left = -1, right = st.size(); // 开区间 (left, right)
        while (left + 1 < right) { // 区间不为空
            // 循环不变量：
            // st[left] < target
            // st[right] >= target
            int mid = (left + right) >>> 1;
            if (st.get(mid)[0] < target) {
                left = mid; // 范围缩小到 (mid, right)
            } else {
                right = mid; // 范围缩小到 (left, mid)
            }
        }
        return right;
    }
}
