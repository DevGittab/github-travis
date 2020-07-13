package net.gittab.githubtravis.reflect;

import java.lang.reflect.Method;

import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

/**
 * ServiceInterceptor.
 *
 * @author xiaohua zhou
 **/
public class ServiceInterceptor implements MethodInterceptor {

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        System.out.println("proxy begin");
        Object result = methodProxy.invokeSuper(o, objects);
        System.out.println("proxy end");
        return result;
    }
}
