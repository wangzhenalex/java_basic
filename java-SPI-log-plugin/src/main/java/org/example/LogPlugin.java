package org.example;

import java.util.Map;
import org.example.Plugin;

public class LogPlugin implements Plugin{
    @Override
    public boolean install(Map context) {
        System.out.println("LogPlugin已初始化，logger对象已注入IOC容器");
        return true;
    }
}
