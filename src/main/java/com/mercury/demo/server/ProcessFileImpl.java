package com.mercury.demo.server;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mercury.demo.entity.ComRes;
import com.mercury.demo.util.Diff_match_patch;
import com.mercury.demo.util.FileUtil;
import com.tuling.asr.sdk.domain.IstActionResult;
import com.tuling.asr.sdk.domain.Lattice;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.NumberFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class ProcessFileImpl {



    public static String resultHtml = "";

    /**
     * 获得转写结果——纯结果
     * @param url
     * @param folderpath
     * @return
     * @throws IOException
     */
    public static String processFileImpl(String url,String folderpath) throws IOException {
//        String url = "http://172.31.161.157:33721";
//        String path = "C:\\Users\\kehu2\\Desktop\\test\\";
        String transResult = null;
        String lastResult = null;

        File file = new File(folderpath);
        String[] filelist = file.list();
        for(int i = 0;i<filelist.length;i++){
            File readfile = new File(folderpath + filelist[i]);
            if(readfile.isDirectory()){
                continue;
            }
            String[] split = readfile.getName().split("\\.");

            //创建转写结果文件
            String outpath = folderpath+ "转写结果\\" + split[0] + ".txt";
            File outfile = new File(outpath);
            PrintStream ps = createOutFile(outpath);

            IstActionResult istActionResult = new ProcessFile().post(url, "", readfile);
            StringBuffer stringBuffer = new StringBuffer();
            List<Lattice> lattices = istActionResult.getLattices();

            for(Lattice lat : lattices){
                parseResult(stringBuffer,lat.getCnOnebestJson());
                transResult = lat.getOneBest();
                System.out.print(transResult);
                ps.append(transResult);
            }
            lastResult = stringBuffer.toString();
//            lastResult = ps.toString();
            ps.close();
        }
        return lastResult;
    }

    /**
     * 获得转写结果——角色分离+纯结果
     * @param url
     * @param folderpath
     * @return
     * @throws IOException
     */
    public static String processFileRoleImpl(String url,String folderpath) throws IOException {
//        String url = "http://172.31.161.157:33721";
//        String path = "C:\\Users\\kehu2\\Desktop\\test\\";
        String transResult = null;
        String lastResult = null;

        File file = new File(folderpath);
        String[] filelist = file.list();
        for(int i = 0;i<filelist.length;i++){
            File readfile = new File(folderpath + filelist[i]);
            if(readfile.isDirectory()){
                continue;
            }
            String[] split = readfile.getName().split("\\.");

            //创建转写结果文件
            String outpath = folderpath+ "转写结果\\" + split[0] + ".txt";
            File outfile = new File(outpath);
            PrintStream ps = createOutFile(outpath);

            IstActionResult istActionResult = new ProcessFile().post(url, "", readfile);
            StringBuffer stringBuffer = new StringBuffer();
            List<Lattice> lattices = istActionResult.getLattices();

            for(Lattice lat : lattices){
                parseResult(stringBuffer,lat.getCnOnebestJson());
                transResult = "spk"+lat.getSpkIndex()+": "+lat.getOneBest();
                System.out.println(transResult);
                ps.append(transResult+"\r\n");
            }
            lastResult = stringBuffer.toString();
            ps.close();
        }
        return lastResult;
    }

    /**
     * 获得转写结果——时间戳+角色分离+纯结果
     * @param url
     * @param folderpath
     * @return
     * @throws IOException
     */
    public static String processFileTimeRoleImpl(String url,String folderpath) throws IOException {
//        String url = "http://172.31.161.157:33721";
//        String path = "C:\\Users\\kehu2\\Desktop\\test\\";
        String transResult = null;
        String lastResult = null;

        File file = new File(folderpath);
        String[] filelist = file.list();
        for(int i = 0;i<filelist.length;i++){
            File readfile = new File(folderpath + filelist[i]);
            if(readfile.isDirectory()){
                continue;
            }
            String[] split = readfile.getName().split("\\.");

            //创建转写结果文件
            String outpath = folderpath+ "转写结果\\" + split[0] + ".txt";
            File outfile = new File(outpath);
            PrintStream ps = createOutFile(outpath);

            IstActionResult istActionResult = new ProcessFile().post(url, "", readfile);
            StringBuffer stringBuffer = new StringBuffer();
            List<Lattice> lattices = istActionResult.getLattices();

            for(Lattice lat : lattices){
                String startTime=formatTime(lat.getBeginTime());
                String endTime=formatTime(lat.getEndTime());
                parseResult(stringBuffer,lat.getCnOnebestJson());
                transResult = startTime+"-->"+endTime+" [spk"+lat.getSpkIndex()+"]: "+lat.getOneBest();
                System.out.println(transResult);
                ps.append(transResult+"\r\n");
            }
            lastResult = stringBuffer.toString();
            ps.close();
        }
        return lastResult;
    }


    public static void main(String[] args) throws IOException {
        String url = "http://172.31.161.157:33721";
        String audiopath = "C:\\Users\\kehu2\\Desktop\\test\\";
//        processFileImpl(url,audiopath);
//        processFileRoleImpl(url,audiopath);
        processFileTimeRoleImpl(url,audiopath);
    }

    /**
     * 单个音频转写
     * @param url
     * @param audiofile
     * @return
     * @throws IOException
     */
    public static String processOneFile(String url,File audiofile) throws IOException {
//        String url = "http://172.31.161.157:33721";
//        audiofile = "C:\\Users\\kehu2\\Desktop\\test\\4s.wav";
        String transResult = null;

        IstActionResult istActionResult = new ProcessFile().post(url, "", audiofile);
        StringBuffer stringBuffer = new StringBuffer();
        List<Lattice> lattices = istActionResult.getLattices();
        StringBuilder stringBuilder = new StringBuilder();

        for(Lattice lat : lattices){
            parseResult(stringBuffer,lat.getCnOnebestJson());
            transResult = lat.getOneBest();
            stringBuilder.append(transResult);
        }
        System.out.println(stringBuilder.toString());
        return stringBuilder.toString();
    }

    /**
     * 写入txt比对结果+转写结果+标注结果
     * @param url   引擎url   String url = "http://172.31.161.157:33721";
     * @param folderpath    音频文件夹路径 String path = "C:\\Users\\kehu2\\Desktop\\test\\";
     * @param filename  标注文件路径  String filename = "C:\\Users\\kehu2\\Desktop\\标注结果.txt";
     * @return
     * @throws IOException
     */
    public static String processComFileImpl(String url,String folderpath,String filename) throws IOException {
        File file = new File(filename);
        String transResult = null;
        String lastResult = null;

        File file2 = new File(folderpath);
        String[] filelist = file2.list();
        for(int i = 0;i<filelist.length;i++) {
            File readfile = new File(folderpath + filelist[i]);
            if (readfile.isDirectory()){
                continue;
            }else {
                log.info("音频文件名称与标注文件名称无法对应");
            }
            String[] split = readfile.getName().split("\\.");

            //创建转写结果文件
            String outpath = folderpath + split[0] + ".txt";
            File outfile = new File(outpath);
            PrintStream ps = createOutFile(outpath);

            IstActionResult istActionResult = new ProcessFile().post(url, "", readfile);
            StringBuffer stringBuffer = new StringBuffer();
            List<Lattice> lattices = istActionResult.getLattices();

            for (Lattice lat : lattices) {
                String text1 = ProcessFileImpl.processFileImpl(url, folderpath);
                String text2 = FileUtils.readFileToString(file, "UTF-8"); //读取txt文件内容，标注结果
                StringBuilder artificial = new StringBuilder();
                StringBuilder machine = new StringBuilder();

                String compareresult = comparetext(text1,text2);
                ps.append("比对结果：" + compareresult);

                parseResult(stringBuffer, lat.getCnOnebestJson());
                transResult = lat.getOneBest(); //引擎转写结果
                System.out.println(transResult);
                ComRes comRes = generateReport(transResult, text2, artificial, machine);

                ps.append("\r\n转写结果：" + comRes.getTranstext() + "\r\n标注结果：" + comRes.getTaggingtext());

                lastResult = compareresult + "\r\n" + comRes.getTranstext() + "\r\n" + comRes.getTaggingtext();
            }
            ps.close();
        }
        return lastResult;
    }


    /**
     * 批量音频转写/标注对比,生成文件为不同文件
     * @param url   引擎路径
     * @param audiopath 音频文件夹路径
     * @param tagpath   标注文件夹路径
     * @return
     * @throws IOException
     */
    public static String processComPathImpl(String url,String audiopath,String tagpath) throws IOException {
        String transResult = null;
        String lastResult = null;

        File file = new File(tagpath);
        String[] taglist = file.list(); //获取标注文件夹中文件名称，包含.txt
        File file2 = new File(audiopath);
        String[] audiolist = file2.list();   //获取音频文件夹中文件名称，包含.wav

        for(int i = 0;i<audiolist.length;i++) {  //以音频文件夹中数量为循环次数
            File audiofile = new File(audiopath + audiolist[i]);   //获取音频文件夹中文件名称，包含.wav
            File tagfile = new File(tagpath+taglist[i]);    //获取音频文件夹中音频名称，包含.txt

            if(audiofile.isDirectory()||tagfile.isDirectory()){
                continue;
            }else{
                log.info("获取为文件非文件夹");
            }

            String[] split = audiofile.getName().split("\\.");  //将4s.wav分为4s\wav存储
            String[] tagsplit = tagfile.getName().split("\\.");

            if (split[0].equals(tagsplit[0])){
                log.info("音频文件名称与标注文件名称一一对应");
            }else{
                continue;
            }

            //创建转写结果文件
            String outpath = audiopath + split[0] + ".txt";
            File outfile = new File(outpath);
            PrintStream ps = createOutFile(outpath);

            String text1 = ProcessFileImpl.processOneFile(url,audiofile);   //获取转写结果
            String text2 = FileUtils.readFileToString(tagfile, "UTF-8"); //读取txt文件内容，标注结果
            StringBuilder artificial = new StringBuilder();
            StringBuilder machine = new StringBuilder();
            System.out.println(text1);

            String compareresult = comparetext(text1,text2);
            ComRes comRes = generateReport(text1, text2, artificial, machine);    //生成最终报表
            ps.append("Res:" + compareresult+"\r\nTra:" + comRes.getTranstext() + "\r\nTag:" + comRes.getTaggingtext());
            //"AudioName: "+audiofile+
            lastResult = compareresult + "\r\n" + comRes.getTranstext() + "\r\n" + comRes.getTaggingtext();
            ps.close();
        }
        return lastResult;
    }


    /**
     * 批量音频转写/标注对比,生成文件为相同
     * @param url   引擎路径
     * @param audiopath 音频文件夹路径
     * @param tagpath   标注文件夹路径
     * @return
     * @throws IOException
     */
    public static String processComPathOneImpl(String url,String audiopath,String tagpath) throws IOException {
        String transResult = null;
        String lastResult = null;

        File file = new File(tagpath);
        String[] taglist = file.list(); //获取标注文件夹中文件名称，包含.txt
        File file2 = new File(audiopath);
        String[] audiolist = file2.list();   //获取音频文件夹中文件名称，包含.wav
        String resultfile = audiopath+"\\result.txt";  //创建输出文件
        PrintStream ps = createOutFile(resultfile);

        for(int i = 0;i<audiolist.length;i++) {  //以音频文件夹中数量为循环次数
            File audiofile = new File(audiopath + audiolist[i]);   //获取音频文件夹中文件名称，包含.wav
            File tagfile = new File(tagpath+taglist[i]);    //获取音频文件夹中音频名称，包含.txt

            if(audiofile.isDirectory()||tagfile.isDirectory()){
                continue;
            }else{
                log.info("获取为文件非文件夹");
            }

            String[] split = audiofile.getName().split("\\.");  //将4s.wav分为4s\wav存储
            String[] tagsplit = tagfile.getName().split("\\.");

            if (split[0].equals(tagsplit[0])){
                log.info("音频文件名称与标注文件名称一一对应");
            }else{
                continue;
            }

            String text1 = ProcessFileImpl.processOneFile(url,audiofile);   //获取转写结果
            String text2 = FileUtils.readFileToString(tagfile, "UTF-8"); //读取txt文件内容，标注结果
            StringBuilder artificial = new StringBuilder();
            StringBuilder machine = new StringBuilder();
            System.out.println(text1);

            String compareresult = comparetext(text1,text2);
            ComRes comRes = generateReport(text1, text2, artificial, machine);    //生成最终报表
            FileUtil.appendStr(resultfile,"AudioName: "+split[0]+"\r\nRes:" + compareresult+"\r\nTra:" + comRes.getTranstext() + "\r\nTag:" + comRes.getTaggingtext()+"\r\n");
            //"AudioName: "+audiofile+
            lastResult = compareresult + "\r\n" + comRes.getTranstext() + "\r\n" + comRes.getTaggingtext();
            ps.close();
        }
        return lastResult;
    }

//    public static void main(String[] args) throws IOException {
//        String url = "http://172.31.161.157:33721";
//        String audiopath = "C:\\Users\\kehu2\\Desktop\\test\\";
//        String tagpath = "C:\\Users\\kehu2\\Desktop\\test标注\\";
//        File file = new File("C:\\Users\\kehu2\\Desktop\\test\\001.wav");
////        processComPathImpl(url,audiopath,tagpath);
////        processFileImpl(url,audiopath);
////        processOneFile(url,file);
//        processComPathOneImpl(url,audiopath,tagpath);
//    }



    /**
     * 创建输出文件
     * @param outPath
     * @throws IOException
     */
    private static PrintStream createOutFile(String outPath) throws IOException {
        File outfile = new File(outPath);
        if(!outfile.getParentFile().exists()){
            outfile.getParentFile().mkdirs();
        }
        if(!outfile.exists()){
            outfile.createNewFile();
        }
        PrintStream ps = new PrintStream(new FileOutputStream(outfile));

        return ps;
    }

    /**
     * json结果解析
     * @param resultBuff
     * @param result
     */
    private static void parseResult(StringBuffer resultBuff, String result) {
        if (StringUtils.isEmpty(result)) return;
        JSONObject json = JSON.parseObject(result);
        JSONArray rtArray = json.getJSONArray("ws");
        if (rtArray != null && rtArray.size() != 0) {
            for (Object rt : rtArray) {
                JSONArray cws = ((JSONObject) rt).getJSONArray("cw");
                for (Object cw : cws) {
                    String w = ((JSONObject) cw).getString("w");
                    resultBuff.append(w);
                }
            }
        }
    }

    /**
     * 比较两个文本的差异
     */
    public static Map<String, Integer> doCompare(String text1, String text2) {
        String dest1 = "";
        String dest2 = "";
        //去除换行符号和空格
        Pattern p = Pattern.compile("\\s*|\t|\r|\n");
        Matcher m1 = p.matcher(text1);
        Matcher m2 = p.matcher(text2);
        dest1 = m1.replaceAll("");
        dest2 = m2.replaceAll("");
        /**
         * 核心计算两个文本的差异算法
         */
        Diff_match_patch dmp = new Diff_match_patch();
        LinkedList<Diff_match_patch.Diff> diffs = dmp.diff_main(dest2, dest1);
        //获取结果数据
        Map<String, Integer> map = generateResults(diffs);
        return map;
    }

    /**
     * 生成html页面
     *
     * @param diffs
     */
    private static Map<String, Integer> generateResults(LinkedList<Diff_match_patch.Diff> diffs) {
        StringBuilder html = new StringBuilder();
        Map<String, Integer> map = new HashMap<>();
        String preColor = "";
        String preText = "";
        //替换错误字数
        int replaceError = 0;
        //删除错误字数
        int deleteError = 0;
        //插入错误字数
        int insertError = 0;
        //正确的个数
        int correct = 0;
        int i = 0;
        try {
            //生成html并统计错误字数
            for (Diff_match_patch.Diff aDiff : diffs) {
                String text = aDiff.text;
                switch (aDiff.operation) {
                    case INSERT:
//                        log.info("插入：" + text);
                        html.append("<INS STYLE=\"background:green;\" TITLE=\"i=").append(i)
                                .append("\">").append(text).append("</INS>");
                        //说明仅有绿色 统计删除错误字数
                        if (preColor.equals("") || preColor.equals("none")) {
                            deleteError = deleteError + text.length();
                        } else if (preColor.equals("red")) {
                            replaceError = replaceError + preText.length();
                        }
                        preColor = "green";
                        preText = text;
                        break;
                    case DELETE:
//                        log.info("删除：" + text);
                        html.append("<DEL STYLE=\"background:red;\" TITLE=\"i=").append(i)
                                .append("\">").append(text).append("</DEL>");
                        preColor = "red";
                        preText = text;
                        break;
                    case EQUAL:
//                        log.info("相等：" + text);
                        html.append("<SPAN TITLE=\"i=").append(i).append("\">").append(text)
                                .append("</SPAN>");
                        //仅有红色说明 插入错误数
                        if (preColor.equals("red")) {
                            insertError = insertError + preText.length();
                        }
                        preColor = "none";
                        preText = text;
                        correct = correct + text.length();
                        break;
                }
                if (aDiff.operation != Diff_match_patch.Operation.DELETE) {
                    i += aDiff.text.length();
                }
            }
            map.put("deleteError", deleteError);
            map.put("replaceError", replaceError);
            map.put("insertError", insertError);
            map.put("correct", correct);
            String context = "<!DOCTYPE HTML>\n" +
                    "<head>\n" +
                    "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n" +
                    "    <title>haha</title>\n" +
                    "</head>\n" +
                    "<html>\n" +
                    "<body>" + html + "</body>\n" +
                    "</html>";
            resultHtml = context;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 计算文本准确率
     *
     * @param map
     */
    public static String getRestltDes(Map<String, Integer> map, int allAnswerNum) {
        // 创建一个数值格式化对象
        NumberFormat numberFormat = NumberFormat.getInstance();
        // 设置精确到小数点后2位
        numberFormat.setMaximumFractionDigits(2);
        //开始计算准确率
        String accuracy = numberFormat.format((float) map.get("correct") / (float) allAnswerNum * 100);//所占百分比
        String recognitionRate = numberFormat.format((float) (map.get("correct") - map.get("insertError")) / (float) allAnswerNum * 100);//所占百分比
        return "字正确率Corr：" + accuracy + "%" +
                "   字识别率ACC：" + recognitionRate + "%  " +
                "   正确的字数H：" + map.get("correct") +
                "   删减的错误D：" + map.get("deleteError") +
                "   替换的错误S：" + map.get("replaceError") +
                "   插入的错误I：" + map.get("insertError")  +
                "   总字数N：" + allAnswerNum;
    }

    /**
     * 上传文本文件生成结果
     */
    public static String comparetext(String text1,String text2) throws IOException {
        //开始对比
        Map<String, Integer> map = ProcessFileImpl.doCompare(text1, text2);
        //生成结果数据
        String compareResult = ProcessFileImpl.getRestltDes(map, text1.length());
        //compareResult="字正确率Corr：61.9%   字识别率ACC：61.9%     正确的字数H：13   删减的错误D：8   替换的错误S：0   插入的错误I：0   总字数N：21"
        System.out.print(compareResult);

        return compareResult;
    }


    /**
     * 生成最终报表
     *
     * @param text1
     * @param text2
     */
    public static ComRes generateReport(String text1, String text2, StringBuilder artificial, StringBuilder machine) {
        String dest1 = "";
        String dest2 = "";
        String text = null;
        ComRes comRes = new ComRes();
        //去除换行符号和空格
        Pattern p = Pattern.compile("\\s*|\t|\r|\n");
        Matcher m1 = p.matcher(text1);
        Matcher m2 = p.matcher(text2);
        dest1 = m1.replaceAll("");
        dest2 = m2.replaceAll("");
        /**
         * 核心计算两个文本的差异算法
         */
//        log.info("人工标注:" + dest2);
//        log.info("引擎转写:" + dest1);
        Diff_match_patch dmp = new Diff_match_patch();
        LinkedList<Diff_match_patch.Diff> diffs = dmp.diff_main(dest2, dest1);
        int i = 0;
        String preColor = "";
        String preText = "";
        int preTextLength = 0;


        //生成html并统计错误字数
        for (Diff_match_patch.Diff aDiff : diffs) {
            text = aDiff.text;
            switch (aDiff.operation) {
                case INSERT:
                    if (preColor.equals("") || preColor.equals("none")) {
                        //说明仅有绿色 统计删除错误字数
                        artificial.append(text);
                        supplementBlack(machine, "", text);
                    } else if (preColor.equals("red")) {
                        //红绿交替 翻译错误
                        if (preTextLength > text.length()) {
                            supplementBlack(artificial, preText, text);
                        artificial.append(text);
                        machine.append(preText);
                    } else if (preTextLength < text.length()) {
                        supplementBlack(machine, preText, text);
                        artificial.append(text);
                        machine.append(preText);
                    } else {
                        //两个字符串一样  但是中文来那个空格  其他一个 （GS |节食）
                        oneOrTwoBlack(artificial,machine,text,preText);
                    }
                    }
                    preColor = "green";
                    preText = text;
                    preTextLength = text.length();
                    break;
                case DELETE:
                    preColor = "red";
                    preText = text;
                    preTextLength = text.length();
                    break;
                case EQUAL:
                    //仅有红色说明 插入错误数
                    if (preColor.equals("red")) {
                        supplementBlack(artificial, preText, "");
                        machine.append(preText);
                    } else {
                        artificial.append(text);
                        machine.append(text);
                    }
                    preColor = "none";
                    preText = text;
                    preTextLength = text.length();
                    break;
            }
//            if (aDiff.operation != Diff_match_patch.Operation.DELETE) {
//                i += aDiff.text.length();
//            }
        }
        comRes.setTaggingtext(machine);
        comRes.setTranstext(artificial);

        return comRes;
    }

    /**
     * 相同字数 英文一个空格，中文两个空格
     * @param artificial
     * @param machine
     * @param text
     * @param preText
     */
    private static void oneOrTwoBlack(StringBuilder artificial, StringBuilder machine, String text, String preText) {
        char[] chars1 = text.toCharArray();
        char[] chars2 = preText.toCharArray();

        String d="";
        String f="";
        for(int i=0;i<chars1.length;i++){
            String c1=((Character)chars1[i]).toString();
            String c2=((Character)chars2[i]).toString();
            if(isContainChinese(c1) &&!(isContainChinese(c2))){
                d=d+c1;
                f=f+c2+" ";
            }else if(!(isContainChinese(c1)) && isContainChinese(c2)){
                d=d+c1+" ";
                f=f+c2;
            }else{
                d=d+c1;
                f=f+c2;
            }
        }
        artificial.append(d);
        machine.append(f);
    }

    /**
     * 计算并补充空格
     *
     * @param sb
     * @param preText
     * @param text
     */
    public static void supplementBlack(StringBuilder sb, String preText, String text) {
        int count1 = 0;
        int count2 = 0;

        /**
         * 一个中文加两个空格，其他的一个空格
         */
        if (org.apache.commons.lang3.StringUtils.isNotBlank(preText)) {
            char[] chars = preText.toCharArray();

            for (Character c1 : chars) {
                if (isContainChinese(c1.toString())) {
                    count1 = count1 + 2;
                } else {
                    count1++;
                }
            }
        }
        if (org.apache.commons.lang3.StringUtils.isNotBlank(text)) {
            char[] chars2 = text.toCharArray();

            for (Character c2 : chars2) {
                if (isContainChinese(c2.toString())) {
                    count2 = count2 + 2;
                } else {
                    count2++;
                }
            }
        }

        for(int j=0;j<Math.abs(count1-count2);j++){
            sb.append(" ");
        }
    }


    /**
     * 字符串是否包含中文
     *
     * @param str 待校验字符串
     * @return true 包含中文字符 false 不包含中文字符
     * @throws RuntimeException
     */
    public static boolean isContainChinese(String str) {

        if (org.apache.commons.lang3.StringUtils.isEmpty(str)) {
            throw new RuntimeException("sms context is empty!");
        }
        Pattern p = Pattern.compile("[\u4E00-\u9FA5|\\！|\\，|\\。|\\（|\\）|\\《|\\》|\\“|\\、|\\？|\\”|\\？|\\：|\\；|\\【|\\】]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }

    public static String formatTime(int ms) {
        Integer ss = 1000;
        Integer mi = ss * 60;
        Integer hh = mi * 60;
        Integer dd = hh * 24;

        int day = ms / dd;
        int hour = (ms - day * dd) / hh;
        int minute = (ms - day * dd - hour * hh) / mi;
        int second = (ms - day * dd - hour * hh - minute * mi) / ss;
        int milliSecond = ms - day * dd - hour * hh - minute * mi - second * ss;

        StringBuffer sb = new StringBuffer();
        if (day > 0) {
            sb.append(day + "天");
        }
        //if (hour > 0) {
        sb.append((hour==0?"00:":(hour>=10?hour:"0"+hour)+":"));
        //}
        //if (minute > 0) {
        sb.append((minute==0?"00:":(minute>=10?minute:"0"+minute)+":"));
        //}
        //if (second > 0) {
        sb.append((second==0?"00:":(second>=10?second:"0"+second)+":"));
        //}
        if (milliSecond > 0) {
            sb.append(milliSecond);
        }
        return sb.toString();

    }

    /*
    public static void main(String[] args) throws IOException {
        String filename = "C:\\Users\\kehu2\\Desktop\\标注结果.txt";
        File file = new File(filename);
//        processFileImpl();
        String url = "http://172.31.161.157:33721";
        String path = "C:\\Users\\kehu2\\Desktop\\test\\";

        String text1 = ProcessFileImpl.processFileImpl(url,path);
        String text2 = FileUtils.readFileToString(file,"UTF-8");

        StringBuilder artificial = new StringBuilder();
        StringBuilder machine = new StringBuilder();
        generateReport(text1, text2, artificial, machine);

        comparetext(file);
    }
     */

//    public static void main(String[] args) throws IOException {
//
//       /*
//        String url = "http://172.31.161.157:33721";
//        String path = "C:\\Users\\kehu2\\Desktop\\test\\";
//        String filename = "C:\\Users\\kehu2\\Desktop\\标注结果.txt";
//        processComFileImpl(url,path,filename);
//        */
//        String text1 = "安徽省合肥市科大讯飞没有";
//        String text2 = "安徽省合肥是科大讯飞股份公司";
//
//        StringBuilder artificial = new StringBuilder();
//        StringBuilder machine = new StringBuilder();
//
//        ComRes comRes = generateReport(text1,text2,artificial,machine);
//        System.out.println("标注："+comRes.getTaggingtext());
//        System.out.println("转写："+comRes.getTranstext());
////        System.out.print(result);
//    }
}
