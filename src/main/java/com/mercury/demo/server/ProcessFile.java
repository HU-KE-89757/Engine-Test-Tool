package com.mercury.demo.server;

import java.io.File;

import com.tuling.asr.sdk.client.IstRestClient;
import com.tuling.asr.sdk.domain.IstActionResult;
import lombok.extern.slf4j.Slf4j;

/**
 * 非转码引擎调用接口
 */

@Slf4j
public class ProcessFile {

    public IstActionResult post(String url, String hotwords, File audioFile) {
        IstRestClient istRestClient = new IstRestClient(url, 1000);
        IstActionResult istActionResult = null;
        try {
            log.info("===调用非转码引擎接口===");
            istActionResult = istRestClient.process(hotwords, audioFile);
//            log.info("引擎调用结果为："+istActionResult);
            log.info("======引擎请求结束======");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return istActionResult;

    }

}
