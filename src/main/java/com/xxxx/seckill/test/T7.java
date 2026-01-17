package com.xxxx.seckill.test;

import java.util.PriorityQueue;
import java.util.Queue;

public class T7 {
    public static void main(String[] args) {
        T7 t = new T7();
        t.findXSum(new int[]{1,1,2,2,3,4,2,3},6,2);
    }
    public int[] findXSum(int[] nums, int k, int x) {
        int len = nums.length;
        int[] res = new int[len-k+1];
        int[][]cnt = new int[51][2];
        for(int i=0;i<51;i++)
        {
            cnt[i][0] = i;
        }
        int left = 0;
        int jilu = 0;
        for(int i=0;i<len;i++)
        {
            cnt[nums[i]][1]++;
            if(i<k-1)
            {
                continue;
            }
            res[jilu++] = find(cnt,x);
            cnt[nums[left]][1]--;
            left++;
        }
        return res;
    }
    public int find(int[][]cnt,int x)
    {
        //找到数量最多的x位
        Queue<int[]> queue = new PriorityQueue<>((int[]a, int[]b)->{
            if(a[1]!=b[1])
            {
                return b[1]-a[1];
            }
            else
            {
                return a[0]-b[0];
            }
        });
        int res = 0;
        for(int i=0;i<cnt.length;i++)
        {
            queue.add(cnt[i]);
        }
        for(int i=0;i<x;i++)
        {
            int[]t = queue.poll();
            res+=t[0]*t[1];
        }
        return res;
    }
}
