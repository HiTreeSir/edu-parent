package cn.szxy;

import org.junit.Test;

public class testmian {

    @Test
    public void   test1(){
        System.out.println(sum(10));

    }

    public static Integer sum(int n){
        if(n == 1){
            return 1;
        }

        return n + sum(n -1);
    }
}
