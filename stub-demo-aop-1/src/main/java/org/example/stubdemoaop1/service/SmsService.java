package org.example.stubdemoaop1.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
@Slf4j
public class SmsService {
    public Map sendSms(String mobile){
        log.info("发送真实短信");
        Map result = new LinkedHashMap<>();
        result.put("mobile", mobile);
        result.put("result", "OK");
        return result;
    }
}