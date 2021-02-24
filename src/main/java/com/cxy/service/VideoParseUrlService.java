package com.cxy.service;

import com.cxy.po.ResultDto;

public interface VideoParseUrlService {

    ResultDto dyParseUrl(String redirectUrl) throws Exception;

    ResultDto hsParseUrl(String redirectUrl) throws Exception;

    ResultDto QMParseUrl(String redirectUrl) throws Exception;
}
