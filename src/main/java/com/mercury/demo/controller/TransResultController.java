package com.mercury.demo.controller;

import com.mercury.demo.entity.UrlPath;
import com.mercury.demo.entity.Users;
import com.mercury.demo.server.ProcessFileImpl;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class TransResultController {

    /**
     * 获取既定引擎/既定路径转写结果
     * ps:该方法转写结果自动存入音频所在文件夹中 txt格式
     */
//    @RequestMapping("/TransAudio")
//    public static String engineTransResult() throws IOException {
//        String url = "http://172.31.161.157:33721"; //修改为动态
//        String path = "C:\\Users\\kehu2\\Desktop\\test\\";
//        String lastResult = ProcessFileImpl.processFileImpl(url,path);
//        return lastResult;
//    }

    /**
     * 获取既定引擎/既定路径转写结果+对比结果+标注结果
     * @return
     * @throws IOException
     */
//    public static String engineCompareResult() throws IOException {
//        String url = "http://172.31.161.157:33721"; //修改为动态
//        String path = "C:\\Users\\kehu2\\Desktop\\test\\";
//        String filename = "C:\\Users\\kehu2\\Desktop\\标注结果.txt";
//        String lastResult = ProcessFileImpl.processComFileImpl(url,path,filename);
//        return lastResult;
//    }

    /**
     * 获取活动引擎/活动路径转写结果
     * @param urlPath
     * @return
     * @throws IOException
     */
    @PostMapping("/TransAudio")
    @ResponseBody
    public static String engineTransChangeResult(UrlPath urlPath) throws IOException {
        String urlpath = urlPath.urlpath;   //从前台获取引擎路径
        String audiopath = urlPath.audiopath;  //从前台获取音频文件夹路径
        System.out.print("获取的引擎名称为："+urlpath+"\r\n获取的文件夹为："+audiopath);
        ProcessFileImpl processFile = new ProcessFileImpl();
        //transResult:转写结果
        String transResult = processFile.processFileImpl(urlpath,audiopath);
        return transResult;
    }

    @PostMapping("/TransSpkAudio")
    @ResponseBody
    public static String engineTransSpkResult(UrlPath urlPath) throws IOException {
        String urlpath = urlPath.urlpath;   //从前台获取引擎路径
        String audiopath = urlPath.audiopath;  //从前台获取音频文件夹路径
        System.out.print("获取的引擎名称为："+urlpath+"\r\n获取的文件夹为："+audiopath);
        ProcessFileImpl processFile = new ProcessFileImpl();
        //transResult:转写结果
        String transResult = processFile.processFileRoleImpl(urlpath,audiopath);
        return transResult;
    }

    @PostMapping("/TransTimeSpkAudio")
    @ResponseBody
    public static String engineTransTimeSpkResult(UrlPath urlPath) throws IOException {
        String urlpath = urlPath.urlpath;   //从前台获取引擎路径
        String audiopath = urlPath.audiopath;  //从前台获取音频文件夹路径
        System.out.print("获取的引擎名称为："+urlpath+"\r\n获取的文件夹为："+audiopath);
        ProcessFileImpl processFile = new ProcessFileImpl();
        //transResult:转写结果
        String transResult = processFile.processFileTimeRoleImpl(urlpath,audiopath);
        return transResult;
    }


    @RequestMapping("/CompareAudio")
    public static String enginetxtCompareResult(UrlPath urlPath) throws IOException {
        String url =urlPath.urlpath;    //从前台获取引擎路径
        String audiopath = urlPath.audiopath;   //从前台获取文件夹路径
        String tagpath = urlPath.tagpath;  //从前台获取标注文件夹路径
        System.out.print("获取的引擎名称为："+url+"获取的文件夹为："+audiopath+"获取的标注文件路径："+tagpath);
        String lastResult = ProcessFileImpl.processComPathImpl(url,audiopath,tagpath);
        return lastResult;
    }

    @RequestMapping("/CompareMerge")
    public static String enginetxtCompareMergeResult(UrlPath urlPath) throws IOException {
        String url =urlPath.urlpath;    //从前台获取引擎路径
        String audiopath = urlPath.audiopath;   //从前台获取文件夹路径
        String tagpath = urlPath.tagpath;  //从前台获取标注文件夹路径
        System.out.print("获取的引擎名称为："+url+"获取的文件夹为："+audiopath+"获取的标注文件路径："+tagpath);
        String lastResult = ProcessFileImpl.processComPathOneImpl(url,audiopath,tagpath);
        return lastResult;
    }

}
