package com.xxxx.seckill.test;

import java.util.Arrays;

public class test04 {
    public static void main(String[] args) {
        test04 test = new test04();
        System.out.println(test.countValidSelections(new int[]{1, 0, 2, 0, 3}));
    }
    public int countValidSelections(int[] nums) {
        int len = nums.length;
        int res = 0;
        for(int i=0;i<len;i++)
        {
            if(nums[i]==0)
            {
                res += find(Arrays.copyOf(nums,nums.length),i,1);
                res += find(Arrays.copyOf(nums,nums.length),i,-1);
            }
        }
        return res;
    }
    public int find(int[]nums,int index,int direction)
    {
        while(index>=0&&index<nums.length)
        {
            if(direction>0)
            {
                index--;
            }
            else
            {
                index++;
            }
            if(index>=0&&index<nums.length&&nums[index]>0)
            {
                nums[index]--;
                direction = -1*direction;
            }
        }
        for(int i=0;i<nums.length;i++)
        {
            if(nums[i]!=0)
            {
                return 0;
            }
        }
        return 1;
    }
}
