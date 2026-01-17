package com.xxxx.seckill.test;

public class t5 {
        int[][] image;
        public int[][] floodFill(int[][] image, int sr, int sc, int color) {
            this.image = image;
            dfs(sr,sc,color,image[sr][sc]);
            return image;
        }
        public void dfs(int x,int y,int color,int pre)
        {
            if(x<0||x>=image.length||y<0||y>=image[0].length||image[x][y]!=pre)
            {
                return;
            }
            image[x][y] = color;
            dfs(x-1,y,color,pre);
            dfs(x+1,y,color,pre);
            dfs(x,y-1,color,pre);
            dfs(x,y+1,color,pre);
            return;
        }

    public static void main(String[] args) {
        t5 t5 = new t5();
        t5.floodFill(new int[][]{{1,1,1},{1,1,0},{1,0,1}},1,1,2);
    }
}
