package com.mercury.demo.test;

import com.mercury.demo.server.ProcessFile;
import com.mercury.demo.util.Diff_match_patch;
import com.tuling.asr.sdk.domain.IstActionResult;
import com.tuling.asr.sdk.domain.Lattice;

import java.io.File;
import java.util.List;

public class OneTest {

    public static void one(){
        IstActionResult istActionResult = new ProcessFile().post("http://172.31.161.157:33721",
                "",
                new File("C:\\Users\\kehu2\\Desktop\\test\\4s.wav"));
        List<Lattice> lattices=istActionResult.getLattices();
        System.out.println(lattices);
    }

    public static void two(){
        Diff_match_patch dmp = new Diff_match_patch();
//        String text2 = "一桶冰水当头倒下微软的比尔盖茨你倒是而我的扎克伯格跟桑德博格亚马逊的贝索斯苹果的库克全都不惜湿身入镜这些硅谷的科技人人的飞蛾扑火似地牺牲演出其实全为了慈善";
//        String text1 = "一桶冰水当头倒下微软的比尔盖茨带去第七位的报文七百多万器的我的伯格跟桑德博格亚马逊的贝索斯苹果的库克全湿身入镜这些常委常务硅谷的科技人人的飞蛾扑火似我的错演出其实全为了慈善";
        String text1 = "这里是科大讯飞股份有限公司";
        String text2 = "这里是安徽科大讯飞股份公司哈";

        String compareStr = dmp.getHtmlDiffString(text1,text2);
//        int conparenum = dmp.getHtmlDiffString(text1,text2);
        System.out.println(compareStr);

    }

    public static void main(String[] args) {
        //测试引擎是否能转写出音频
        one();

        //测试diff方法
//        two();

    }
}
