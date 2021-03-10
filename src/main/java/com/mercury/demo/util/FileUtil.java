package com.mercury.demo.util;


import org.springframework.util.StringUtils;

import java.io.*;
import java.util.List;


public class FileUtil {

    /**
     * 向文件追加内容
     */
    public static void appendStr(String filePathName,String content) {
        File file = new File(filePathName);
        checkCreateFile(file.getParent());
        FileOutputStream fos = null;
        OutputStreamWriter osw = null;
        try {
            if (!file.exists()) {
                boolean hasFile = file.createNewFile();
                if(hasFile){
                    //log.debug("file not exists, create new file");
                }
                fos = new FileOutputStream(file);
            } else {
                //log.debug("file exists");
                fos = new FileOutputStream(file, true);
            }
            osw = new OutputStreamWriter(fos, "utf-8");
            osw.write(content);
            osw.write("\r\n");
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if (osw != null) {
                    osw.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *  创建目录
     */
    public static void checkCreateFile(String filePath){
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public void test(String filePath) {
        InputStream fis = null;
        try {
            int i = 0;
            fis = new FileInputStream(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void deleteFileByPath(String path){
        try{
            if(!StringUtils.isEmpty(path)){
                File file = new File(path);
                deleteFile(file);
            }
        }catch (Exception e){
            //log.error("文件删除异常={}", e.getMessage());
        }
    }

    public static void deleteFile(File file){
        try{
            if(file != null && file.exists()){
                file.delete();
            }
        }catch (Exception e){
            //log.error("文件删除异常={}", e.getMessage());
        }
    }

    public static void deleteFileList(List<String> paths){
        try{
            if(paths != null && paths.size() > 0){
                for(String path : paths){
                    deleteFileByPath(path);
                }
            }
        }catch (Exception e){
            //log.error("文件删除异常={}", e.getMessage());
        }
    }

    public static void  createFile(String filePath, String fileName) throws IOException {
        File testFile = new File(filePath + "/" + fileName);
        File fileParent = testFile.getParentFile();//返回的是File类型,可以调用exsit()等方法
        if (!fileParent.exists()) {
            fileParent.mkdirs();// 能创建多级目录
        }
        if (!testFile.exists()){
            testFile.createNewFile();//有路径才能创建文件
        }
    }

//    public static void  generateTmpFileList(List<String> filePaths){
//        if(filePaths != null && filePaths.size() > 0){
//            for(String filePath : filePaths){
//                generateTmpFile(filePath);
//            }
//        }
//    }

//    public static void  generateTmpFile(String filePath){
//        try{
//            File file = new File(filePath);
//            String filePathParent = file.getParent();
//            String fileName = file.getName() + FtpConstant.tmp;
//            createFile(filePathParent, fileName);
//        }catch (Exception e){
//            //log.error("删除较大文件，生成空临时文件异常={}", e.getMessage());
//        }
//    }

}
