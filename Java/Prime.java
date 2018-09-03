package com.ibm.practice;

import java.util.ArrayList;

/**
 * Created by clare on 2018/9/3.
 *
 * 输入一个整数，输出该整数包含的所有质数。
 */
public class Prime {

    static Integer[] resolvePrime(Integer num) {
        ArrayList<Integer> ans = new ArrayList<>();
        int i = 2;

        while (i <= num) {
            if (num % i == 0) {
                ans.add(i);
                num = num / i;
                i = 2;
            } else {
                i++;
            }


        }

        Integer[] intArr = new Integer[ans.size()];
        return ans.toArray(intArr);
    }

    public static void main(String[] args) {
        for (Integer ele : resolvePrime(8)) {
            System.out.println(ele);
        }

    }
}
