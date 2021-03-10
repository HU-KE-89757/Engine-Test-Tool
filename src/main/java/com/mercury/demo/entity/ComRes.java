package com.mercury.demo.entity;

import lombok.Data;

@Data
public class ComRes {
    public String tagTextRes;  //标注文本
    public String transTextRes;    //转写文本

    public StringBuilder taggingtext; //对比后标注文本
    public StringBuilder transtext;    //对比后转写文本
}
