package com.xxxx.seckill.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class T8 {
    public static void main(String[] args) {
        T8 t8 = new T8();
        t8.groupAnagrams(new String[]{"eat", "tea", "tan", "ate", "nat", "bat"});
    }
    public List<List<String>> groupAnagrams(String[] strs) {
        List<List<String>> res = new ArrayList<>();
        Map<Integer,List<String>> map = new HashMap<>();
        for(String s:strs)
        {
            int id = cal(s);
            if(map.get(id)!=null&&map.get(id).get(0).length()==s.length())
            {
                List<String> list = map.get(id);
                        list.add(s);
            }
            else
            {
                List<String> list = new ArrayList<>();
                list.add(s);
                map.put(id,list);
            }
        }
        for(Map.Entry<Integer,List<String>> entry:map.entrySet())
        {
            res.add(entry.getValue());
        }
        return res;
    }
    public int cal(String S)
    {

        int id = 0;
        for (char c : S.toCharArray()) {
            id +=  c * c * c * c;
        }
        return id;
    }
}
