package cn.leo.modular;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;

/**
 * @author : ling luo
 * @date : 2019-12-09
 */
public class Test1 {

    @Test
    public void test() {

        //去重部分，下面开始写
        StringBuilder sb = new StringBuilder();

        //把输出到文件的部分写成下面样式,要换行的追加 \n
        sb.append("word").append("\n");
        //不换行的 不追加
        sb.append("word");

        //while 外面写
        String[] split = sb.toString().split("\n");

        HashSet<String> hashSet = new HashSet<>(Arrays.asList(split));
        //打印去重后的名字
        for (String s : hashSet) {
            System.out.println(s);
        }
    }

}
