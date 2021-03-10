package com.mercury.demo.controller;

import com.google.gson.internal.$Gson$Preconditions;
import com.mercury.demo.server.ProcessFile;
import com.mercury.demo.server.ProcessFileImpl;
import com.tuling.asr.sdk.domain.IstActionResult;
import com.tuling.asr.sdk.domain.Lattice;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
public class TransTxtController {

    @GetMapping("/mainpage")
    public String mainpage(){
        System.out.println("主页面");
        return "/MainPage";
    }

    @GetMapping("/engineAudio")
    public String engineAudio(){
        System.out.println("音频转写页面");
        return "/TransAudio";
    }

    @GetMapping("/engineSpkAudio")
    public String engineSpkAudio(){
        System.out.println("音频转写页面(角色)");
        return "/TransSpkAudio";
    }

    @GetMapping("/engineTimeSpkAudio")
    public String engineTimeSpkAudio(){
        System.out.println("音频转写页面(角色)");
        return "/TransTimeSpkAudio";
    }

    @GetMapping("/engineAudioTagPath")
    public String engineAudioTagPath(){
        System.out.println("生成对比结果+结果在不同文件");
        return "/CompareAudio";
    }

    @GetMapping("/engineAudioOnePath")
    public String engineAudioOnePath(){
        System.out.println("生成对比结果+结果在同一文件");
        return "/CompareMerge";
    }



}
