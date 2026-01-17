package com.xxxx.seckill.test;

import java.util.Stack;
import java.util.concurrent.CopyOnWriteArrayList;

public class t4 {
    public static void main(String[] args) {
        CopyOnWriteArrayList<Integer> list = new CopyOnWriteArrayList<>();
        System.out.println(maximalRectangle(new char[][]{{'1', '0'}}));
    }
    public static int maximalRectangle(char[][] matrix) {
        //全部转化为int类型
        int res = 0;
        int[] height = new int[matrix[0].length];
        for(int i=0;i<matrix.length;i++)
        {
            for(int j=0;j<matrix[0].length;j++)
            {
                if(matrix[i][j]=='1')
                {
                    height[j]++;
                }
                else
                {
                    height[0]=0;
                }
            }
            res=Math.max(res,findMax(height));
        }
        return res;
    }

    public static int findMax(int[]nums)
    {
        int res = 0;
        int num[]=new int[nums.length+2];
        num[0]=0;
        num[nums.length+1]=0;
        for(int i=1;i<=nums.length;i++)
        {
            num[i]=nums[i-1];
        }
        //0 1 0
        //找到左边第一个比他小的
        Stack<Integer> stack = new Stack<>();
        for(int i=0;i<num.length;i++)
        {
            while(!stack.isEmpty()&&num[stack.peek()]>num[i])
            {
                int target=stack.pop();//求的是它。num[i]是右边第一个比他小的，stack.peek()就是左边第一个比他小的

                res=Math.max(res,(i-stack.peek()-1)*num[target]);
            }
            stack.push(i);
        }
        return res;
    }

}
