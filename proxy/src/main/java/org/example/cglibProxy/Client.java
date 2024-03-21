package org.example.cglibProxy;

import org.example.cglibProxy.mapper.Dao;
import org.example.cglibProxy.proxy.CglibProxy;
import org.springframework.cglib.core.DebuggingClassWriter;
import org.springframework.cglib.proxy.Enhancer;

/**
 * Created with IntelliJ IDEA.
 *
 * @author： zhen wang
 * @date： 2024/3/21
 * @description：
 * @version: 1.0
 */
public class Client {
    public static void main(String[] args) {
        //将生成的代理类文件存到项目根目录下
        System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY, "./");
        //实例化增强器
        Enhancer enhancer = new Enhancer();
        //设置需要增强的类
        enhancer.setSuperclass(Dao.class);
        //设置拦截对象 回调的实现类
        enhancer.setCallback(new CglibProxy(Dao.class));
        //生成代理类
        Dao dao = (Dao) enhancer.create();
        dao.insert();
    }
}
