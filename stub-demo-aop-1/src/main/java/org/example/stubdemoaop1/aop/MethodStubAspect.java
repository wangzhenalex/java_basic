package org.example.stubdemoaop1.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
@Aspect
@Slf4j
// 如果配置项不存在，默认启用切面
@ConditionalOnProperty(name = "stub.enabled", havingValue = "true", matchIfMissing = false)
public class MethodStubAspect {
    @Around("execution(* org.example.stubdemoaop1.service..*Service.sendSms(..))")
//    @Around("execution(* com.itlaoqi.stub.service..*Service.sendSms(..))")
    public Object sendSms(ProceedingJoinPoint pjp) {
        log.info("执行挡板方法");
        Object[] args = pjp.getArgs();
        String mobile = (String) args[0];
        Map result = new LinkedHashMap<>();
        result.put("mobile", mobile);
        result.put("stub", true);
        result.put("result", "OK");
        return result;
    }
}