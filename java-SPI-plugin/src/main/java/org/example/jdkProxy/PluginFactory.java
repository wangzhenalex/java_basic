package org.example.jdkProxy;

import java.util.*;

public class PluginFactory {
    public void installPlugins(){
        Map context = new LinkedHashMap();
        //模拟插件安装或运行时所需要的各类应用本体信息
        //当前IOC容器中的对象集合
        context.put("_beans", new ArrayList<>());
        context.put("_version", "1.0.0");
        //切面类
        context.put("_aspects", new LinkedHashMap<>());
        ServiceLoader<Plugin> loader = ServiceLoader.load(Plugin.class);
        Iterator<Plugin> iterator = loader.iterator();
        while(iterator.hasNext()){
            Plugin plugin = iterator.next();
            plugin.install(context);
        }
    }
}