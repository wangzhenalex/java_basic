package org.example.stubdemoaop1.service;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

@Service
public class UserService {
    @Resource
    private SmsService smsService;
    public void registe(String mobile){
        Map result = smsService.sendSms(mobile);
        System.out.println(result);
    }
}
