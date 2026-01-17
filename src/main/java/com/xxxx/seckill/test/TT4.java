package com.xxxx.seckill.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class TT4 {
    public static void main(String[] args) {
        snakesAndLadders(new int[][]{{-1,-1},{-1,3}});
    }
    public static int[] findOrder(int numCourses, int[][] prerequisites) {
        List[] lists = new List[numCourses];
        for(int i = 0; i < numCourses; i++){
            lists[i] = new ArrayList<Integer>();
        }
        int[] ingree = new int[numCourses];
        for(int[]pre:prerequisites)
        {
            int x = pre[0];
            int y = pre[1];
            //有一个图，从y指向x
            lists[y].add(x);
            ingree[x]++;
        }
        //想一想为什么需要出度和入度
        LinkedList<Integer> linked = new LinkedList<>();
        int[] res = new int[numCourses];
        int index = 0;
        for(int i=0;i<numCourses;i++)
        {
            if(ingree[i]==0)
            {
                linked.addLast(i);
                res[index++] = i;
            }
        }
        while(linked.size()>0)
        {
            int size = linked.size();
            for(int i=0;i<size;i++)
            {
                int t = linked.removeFirst();
                for(int j=0;j<lists[t].size();j++)
                {
                    int tt = (int)lists[t].get(j);
                    if(t==1&&tt==0)
                    {
                        System.out.println("0的入度是多少？"+ingree[tt]);
                    }
                    ingree[tt]--;
                    if(ingree[tt]==0)
                    {
                        linked.addLast(tt);
                        res[index++] = tt;
                    }
                }
            }
        }
        if(index==numCourses)
        {
            return res;
        }
        else
        {
            return new int[0];
        }
    }
    public static int snakesAndLadders(int[][] board) {
        int n = board.length;
        boolean[] visit = new boolean[n * n + 1];
        String s = "1";
        //第一个参数指的是位置，第二个参数指的是步数
        LinkedList<int[]> queue = new LinkedList<>();
        queue.add(new int[]{1, 0});
        while (queue.size() > 0) {
            int[] nums = queue.removeFirst();
            int pos = nums[0];
            int step = nums[1];
            if (pos == n * n) {
                System.out.println("pos:" + pos + " step:" + step);
                return step;
            }
            if (visit[pos]) {
                continue;
            }
            step = step + 1;
            for (int cur = pos + 1; cur <= Math.min(pos + 6, n * n); cur++) {
                int t1 = (n - 1) - (cur - 1) / n;
                int t2 = (cur - 1) % n;
                if ((cur - 1) / n % 2 == 0) {
                    t2 = (cur - 1) % n;
                } else {
                    t2 = (n - 1) - (cur - 1) % n;
                }
                System.out.println("行：" + t1 + " 列：" + t2);
                if (visit[cur]) {
                    continue;
                }
                //下面就是存入
                if (board[t1][t2] != -1) {
                    queue.add(new int[]{board[t1][t2], step});
                } else if (cur == Math.min(pos + 6, n * n)) {
                    queue.add(new int[]{cur, step});
                } else {
                    //普通节点，不作考虑
                    visit[cur] = true;
                }
            }
            visit[pos] = true;
        }
        return -1;
    }
}
