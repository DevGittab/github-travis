package net.gittab.githubtravis.reflect;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * JdkProxy.
 *
 * @author xiaohua zhou
 **/
public class JdkProxy implements InvocationHandler {

    private Object target;

    public JdkProxy(Object target){
        this.target = target;
    }

    public static Object bind(Object obj){
        return Proxy.newProxyInstance(obj.getClass().getClassLoader(),
                obj.getClass().getInterfaces(),
                new JdkProxy(obj));
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("proxy begin");
        Object obj = method.invoke(this.target, args);
        System.out.println("proxy end");
        return obj;
    }

    public static void main(String[] args) {
        Target target = new TargetImpl();
        target.execute();

        Target target1 = (Target) JdkProxy.bind(target);
        target1.execute();
    }
}
