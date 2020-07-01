package net.gittab.test.reflect;

import java.lang.reflect.Method;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

/**
 * CgLibFactory for interface.
 *
 * @author xiaohua zhou
 **/
public class CgLibFactory implements MethodInterceptor {

    private Target target;

    public CgLibFactory(Target target){
        this.target = target;
    }



    public Target cgGlibCreator(){
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(Target.class);
        enhancer.setCallback(this);
        Target targetProxy = (Target) enhancer.create();
        return targetProxy;
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        System.out.println("proxy begin");
        Object result = methodProxy.invoke(target, objects);
        System.out.println("proxy end");
        return result;
    }

    public static void main(String[] args) {
        Target target = new TargetImpl();
        CgLibFactory cgLibFactory = new CgLibFactory(target);
        Target proxy = cgLibFactory.cgGlibCreator();
        proxy.execute();
    }
}
