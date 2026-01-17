package com.xxxx.seckill.test;

import java.util.ArrayList;
import java.util.List;

public class T9 {
    public static void main(String[] args) {
        T9 t9 = new T9();
        t9.fullJustify(new String[]{"This", "is", "an", "example", "of", "text", "justification."},16);
    }
    public List<String> fullJustify(String[] words, int maxWidth) {
        List<String> res = new ArrayList<>();
        int cur = 0;
        List<String> path = new ArrayList<>();
        for(int i=0;i<words.length;i++)
        {
            if(cur+words[i].length()<maxWidth)
            {
                cur+=words[i].length();
                cur+=1;
                path.add(words[i]);
            }
            else if(cur+words[i].length()==maxWidth)
            {
                cur+=words[i].length();
                path.add(words[i]);
                res.add(justify1(path,cur,maxWidth));

                cur = 0;
                path = new ArrayList<>();

            }
            else if(cur+words[i].length()>maxWidth)
            {
                res.add(justify2(path,cur,maxWidth));

                cur = words[i].length();
                cur+=1;
                path = new ArrayList<>();
                path.add(words[i]);
            }
        }
        StringBuilder lastLine = new StringBuilder();

        for(int i=0;i<path.size();i++)
        {
            lastLine.append(path.get(i));
            if(i!=path.size()-1)
            {
                lastLine.append(" ");
            }
        }
        for(int i=lastLine.length();i<maxWidth;i++)
        {
            lastLine.append(" ");
        }
        res.add(lastLine.toString());
        return res;
    }
    public String justify1(List<String> path,int cur,int maxWidth)
    {
        StringBuilder builder = new StringBuilder();
        for(int i=0;i<path.size();i++)
        {
            builder.append(path.get(i));
            if(i!=path.size()-1)
            {
                builder.append(" ");
            }
        }
        return builder.toString();
    }
    public String justify2(List<String>path,int cur,int maxWidth)
    {
        int size = path.size();
        //除去每个单词末尾加空格，还剩的空格数
        int rest = maxWidth - cur + 1;
        if(size==1)
        {
            StringBuilder bl = new StringBuilder();
            bl.append(path.get(0));
            for(int i=0;i<rest;i++)
            {
                bl.append(" ");
            }
            return bl.toString();
        }
        int base = rest/(size-1)+1;
        rest = rest%(size-1);
        StringBuilder kong = new StringBuilder();
        for(int i=0;i<base;i++)
        {
            kong.append(" ");
        }
        StringBuilder builder = new StringBuilder();
        for(int i=0;i<size;i++)
        {
            builder.append(path.get(i));
            if(i!=size-1)
            {
                builder.append(kong.toString());
            }
            if(rest!=0)
            {
                builder.append(" ");
                rest--;
            }
        }
        return builder.toString();
    }
}
