package com.p1.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.File;

@Configuration
public class WkConfig {

    private  static final Logger logger= LoggerFactory.getLogger(WkConfig.class);

    @Value("${wk.image.storage}")
    private String wkImageStorage;

    @Value("${wk.pdf.storage}")
    private String wkPdfStorage;

    @PostConstruct
    public void init(){
        File file1=new File(wkImageStorage);
        File file2=new File(wkPdfStorage);
        if(!file1.exists()){
            file1.mkdir();
            logger.info("创建wk-images文件夹"+wkImageStorage);
        }
        if(!file2.exists()){
            file2.mkdir();
            logger.info("创建wk-pdf文件夹"+wkPdfStorage);
        }
    }
}
