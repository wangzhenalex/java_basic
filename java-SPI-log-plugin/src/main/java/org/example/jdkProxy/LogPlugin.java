package org.example.jdkProxy;

import java.util.Map;

public class LogPlugin implements Plugin{
    @Override
    public boolean install(Map context) {
        System.out.println("LogPlugin已初始化，logger对象已注入IOC容器");
        return true;
    }
}
