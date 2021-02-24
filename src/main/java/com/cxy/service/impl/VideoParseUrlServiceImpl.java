package com.cxy.service.impl;

import com.alibaba.fastjson.JSON;

import com.cxy.po.DYResult;
import com.cxy.po.HSResult;
import com.cxy.po.ResultDto;
import com.cxy.service.VideoParseUrlService;
import com.cxy.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;


/**
 * @Author: xiaofu
 * @Description:
 */
@Slf4j
@Component
public class VideoParseUrlServiceImpl implements VideoParseUrlService {

    /**
     * @param redirectUrl
     * @author xiaofu
     * @description 抖音小视频解析
     * @date 2020/9/21 16:23
     */
    @Override
    public ResultDto dyParseUrl(String redirectUrl) throws Exception {

        //获取手机模拟拿到url地址
        redirectUrl = CommonUtils.getLocation(redirectUrl);
        log.info("手机模拟获取url地址"+redirectUrl);

        ResultDto dyDto = new ResultDto();

        if (!StringUtils.isEmpty(redirectUrl)) {
            /**
             * 1、用 ItemId 拿视频的详细信息，包括无水印视频url
             */
            String itemId = CommonUtils.matchNo(redirectUrl);

            StringBuilder sb = new StringBuilder();
            sb.append(CommonUtils.DOU_YIN_BASE_URL).append(itemId);

            String videoResult = CommonUtils.httpGet(sb.toString());

            DYResult dyResult = JSON.parseObject(videoResult, DYResult.class);

            /**
             * 2、无水印视频 url
             */
            String videoUrl = dyResult.getItem_list().get(0)
                    .getVideo().getPlay_addr().getUrl_list().get(0)
                    .replace("playwm", "play");
            //获取手机模拟无水印视频url
            System.out.println(videoUrl);
            String videoRedirectUrl = CommonUtils.getLocation(videoUrl);
            System.out.println(videoRedirectUrl);
            dyDto.setVideoUrl(videoRedirectUrl);
            /**
             * 3、音频 url
             */
            String musicUrl = dyResult.getItem_list().get(0).getMusic().getPlay_url().getUri();
            dyDto.setMusicUrl(musicUrl);
            /**
             * 4、封面
             */
            String videoPic = dyResult.getItem_list().get(0).getVideo().getDynamic_cover().getUrl_list().get(0);
            dyDto.setVideoPic(videoPic);

            /**
             * 5、视频文案
             */
            String desc = dyResult.getItem_list().get(0).getDesc();
            dyDto.setDesc(desc);
        }
        return dyDto;
    }

    /**
     * @param redirectUrl
     * @author xiaofu
     * @description 火山小视频解析
     * @date 2020/9/21 16:23
     */
    @Override
    public ResultDto hsParseUrl(String redirectUrl) throws Exception {

        redirectUrl = CommonUtils.getLocation(redirectUrl);

        ResultDto dyDto = new ResultDto();

        if (!StringUtils.isEmpty(redirectUrl)) {

            /**
             * 1、拿到itemId
             */
            String itemId = CommonUtils.hSMatchNo(redirectUrl);

            StringBuilder sb = new StringBuilder();
            sb.append(CommonUtils.HUO_SHAN_BASE_URL).append(itemId);

            /**
             * 2、itemId 拼接视频详情接口
             */
            String videoResult = CommonUtils.httpGet(sb.toString());

            HSResult hsResult = JSON.parseObject(videoResult, HSResult.class);

            dyDto.setVideoPic(hsResult.getData().getItem_info().getCover());

            /**
             * 3、替换URL地址
             */
            String replace = hsResult.getData().getItem_info().getUrl().replace("_reflow", "_playback");

            dyDto.setVideoUrl(replace.substring(0, replace.indexOf("&")));

            dyDto.setDesc("火山小视频");
        }
        return dyDto;
    }

    /**
     * @param redirectUrl
     * @author xiaofu
     * @description 全民小视频解析
     * @date 2020/9/21 16:23
     */
    @Override
    public ResultDto QMParseUrl(String redirectUrl) throws Exception {
        return null;
    }
}
