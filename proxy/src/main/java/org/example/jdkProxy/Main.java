package org.example.jdkProxy;

import org.example.jdkProxy.mapper.Dao;
import org.example.jdkProxy.mapper.IDao;
import org.example.jdkProxy.proxy.DaoJDKProxy;
import org.springframework.cglib.core.DebuggingClassWriter;

import java.lang.reflect.Proxy;

/**
 * Created with IntelliJ IDEA.
 *
 * @author： zhen wang
 * @date： 2024/3/21
 * @description：${TODO}
 * @version: 1.0
 */
public class Main {
    public static void main(String[] args) {
        //将生成的代理类文件存到项目根目录下
        System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY, "./");
        IDao iDao = (IDao) Proxy.newProxyInstance(Dao.class.getClassLoader(),
                Dao.class.getInterfaces(),
                new DaoJDKProxy(new Dao()));

        System.out.println(iDao.getClass().getName());
        iDao.insert();
    }
}