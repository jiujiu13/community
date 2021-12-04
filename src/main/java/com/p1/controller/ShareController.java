package com.p1.controller;

import com.p1.event.EventProducer;
import com.p1.pojo.Event;
import com.p1.util.CommunityConstant;
import com.p1.util.CommunityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class ShareController implements CommunityConstant {

    private static final Logger logger = LoggerFactory.getLogger(ShareController.class);

    @Value("${community.path.domain}")
    private String domain;

    @Value("${wk.image.storage}")
    private String wkImageStorage;

    @Value("${qiniu.bucket.share.url}")
    private String shareBucketUrl;

    @Autowired
    private EventProducer eventProducer;

    @GetMapping("/share")
    @ResponseBody
    public String share(String htmlUrl) {
        //给文件一个随机名
        String fileName = CommunityUtil.generateUUID();
        //异步生成长图
        Event event = new Event()
                .setTopic(TOPIC_SHARE)
                .setData("htmlUrl", htmlUrl)
                .setData("fileName", fileName)
                .setData("suffix", ".png");
        eventProducer.fireEvent(event);
        //返回路径
        Map<String, Object> map = new HashMap<>();
//        map.put("shareUrl", domain + "/share/image/" + fileName);
        map.put("shareUrl", shareBucketUrl + "/" + fileName);


        return CommunityUtil.getJSONString(0, null, map);
    }

    //废弃
//    @GetMapping("/share/image/{fileName}")
//    public void getShareImage(@PathVariable("fileName") String fileName, HttpServletResponse response) {
//        if (StringUtils.isBlank(fileName)) {
//            throw new IllegalArgumentException("文件名不能为空!");
//        }
//        response.setContentType("image/png");
//        File file = new File(wkImageStorage + "/" + fileName + ".png");
//        try {
//            OutputStream os = response.getOutputStream();
//            FileInputStream fis = new FileInputStream(file);
//            byte[] buffer = new byte[1024];
//            int b = 0;
//            while ((b = fis.read(buffer)) != -1) {
//                os.write(buffer, 0, b);
//            }
//        } catch (IOException e) {
//            logger.error("获取长图失败: " + e.getMessage());
//        }
//    }
}
