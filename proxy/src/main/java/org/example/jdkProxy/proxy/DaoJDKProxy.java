package org.example.jdkProxy.proxy;

import java.lang.reflect.Method;

/**
 * Created with IntelliJ IDEA.
 *
 * @author： zhen wang
 * @date： 2024/3/21
 * @description：
 * @version: 1.0
 */
public class DaoJDKProxy implements java.lang.reflect.InvocationHandler {

    private Object targetObject;

    public DaoJDKProxy(Object target) {
        this.targetObject = target;
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // before 日志、事务、记录开始时间
        return method.invoke(targetObject, args);
    }
}
