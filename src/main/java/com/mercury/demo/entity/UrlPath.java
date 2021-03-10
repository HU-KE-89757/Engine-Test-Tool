package com.mercury.demo.entity;

import lombok.Data;

@Data
public class UrlPath {
    public String urlpath; //引擎路径
    public String audiopath; //带转写音频文件夹路径
    public String tagpath;  //音频标注路径
}
