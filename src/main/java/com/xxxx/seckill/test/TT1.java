package com.xxxx.seckill.test;

import java.util.Random;

public class TT1 {
    public static void main(String[] args) {
        TT1 t1 = new TT1();
        t1.partSort(new int[]{5,1,1,2,0,0},0,5);
    }
    private static final Random rand = new Random();
    //快排,110,100,0
    public int[] sortArray(int[] nums) {
        partSort(nums,0,nums.length-1);
        return nums;
    }
    public int partition(int[]arr,int begin,int end)
    {
        int i = begin + rand.nextInt(end-begin+1);
        swap(arr,begin,i);
        int left = begin+1;
        int right = end;
        int povit = arr[begin];
        //把pivot与子数组第一个元素交换，避免pivot干扰后续划分
        while(left<right)
        {
            while(left<right && arr[right]>=povit)
            {
                right--;
            }
            while(left<right && arr[left]<=povit)
            {
                left++;
            }
            if(left<right)
            {
                int tem = arr[left];
                arr[left] = arr[right];
                arr[right] = tem;
            }
        }
        int tem = arr[right];
        arr[right] = arr[begin];
        arr[begin] = tem;
        return right;
        //6 4 7 5 10 12
        //    l r
        //6 4 5 7 10 12
        //    r
        //5 4 6 7 10 12
    }
    public void partSort(int[]arr,int begin,int end)
    {
        if(begin>=end)
        {
            return;
        }
        if(begin>=end)
        {
            return;
        }
        boolean ordered = true;
        for(int i=begin;i<end;i++)
        {
            if(arr[i]>arr[i+1])
            {
                ordered = false;
                break;
            }
        }
        if(ordered)
        {
            return;
        }
        int povit = partition(arr,begin,end);
        partSort(arr,begin,povit-1);
        partSort(arr,povit+1,end);
    }
    public void swap(int[]nums,int i,int j)
    {
        int t = nums[i];
        nums[i] = nums[j];
        nums[j] = t;
    }
}
