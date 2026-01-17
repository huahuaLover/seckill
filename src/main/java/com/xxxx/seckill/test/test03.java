package com.xxxx.seckill.test;

public class test03 {
    public static void main(String[] args) {
        test03 test03 = new test03();
        System.out.println(test03.lexGreaterPermutationLingShen("abc", "bba"));
    }
    public String lexGreaterPermutation(String s, String target) {
        char[] sc = s.toCharArray();
        int cnt[] = new int[26];
        for(int i=0;i<sc.length;i++)
        {
            cnt[sc[i]-'a']++;
        }
        char[] tc = target.toCharArray();
        //进行抵消
        StringBuilder builder = new StringBuilder();
        for(int i=tc.length-1;i>=0;i--)
        {
            //在进行替换的时候，需要保证前面是
            //下面就是找到一个比tc[i]大的数
            for(int j=tc[i]-'a'+1;j<26;j++)
            {
                if(cnt[j]>0)
                {
                    builder.append((char)('a'+j));
                    cnt[j]--;
                    break;
                }
            }
            for(int j=0;j<26;j++)
            {
                for(int k=0;k<cnt[j];k++)
                {
                    builder.append((char)('a'+j));
                }
            }
            return builder.toString();
        }
        return builder.toString();
    }
    public String lexGreaterPermutationLingShen(String s, String target) {
        char[] t = target.toCharArray();
        int n = t.length;
        int[] left = new int[26];
        for (int i = 0; i < n; i++) {
            left[s.charAt(i) - 'a']++;
            left[t[i] - 'a']--; // 消耗 s 中的一个字母 t[i]
        }

        // 从右往左尝试
        next:
        for (int i = n - 1; i >= 0; i--) {
            int b = t[i] - 'a';
            left[b]++; // 撤销消耗
            for (int c : left) {
                if (c < 0) { // [0,i-1] 无法做到全部一样
                    continue next;
                }
            }

            // target[i] 增大到 j
            for (int j = b + 1; j < 26; j++) {
                if (left[j] == 0) {
                    continue;
                }

                left[j]--;
                StringBuilder ans = new StringBuilder(target.substring(0, i + 1));
                ans.setCharAt(i, (char) ('a' + j));

                for (int k = 0; k < 26; k++) {
                    for(int l = 0; l < left[k]; l++)
                    {
                        ans.append((char)('a' + k));
                    }
                }
                return ans.toString();
            }
        }
        return "";
    }
}
