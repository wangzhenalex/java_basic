package org.example.cglibProxy.proxy;

import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * Created with IntelliJ IDEA.
 *
 * @author： zhen wang
 * @date： 2024/3/21
 * @description：
 * @version: 1.0
 */
public class CglibProxy implements MethodInterceptor {
    private Class<?> target;

    public CglibProxy(Class<?> targetClass) {
        this.target = targetClass;
    }


    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {

        System.out.println("before intercept");

        methodProxy.invokeSuper(o, objects);

        System.out.println("end intercept");

        return  o;
    }
}
