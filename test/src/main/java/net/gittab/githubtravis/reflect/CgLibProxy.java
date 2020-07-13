package net.gittab.githubtravis.reflect;

import org.springframework.cglib.proxy.Callback;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.NoOp;

/**
 * CgLibProxy.
 *
 * @author xiaohua zhou
 **/
public class CgLibProxy {

    public static void main(String[] args) {
        ServiceInterceptor interceptor = new ServiceInterceptor();
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(Service.class);
        enhancer.setCallbacks(new Callback[]{interceptor, NoOp.INSTANCE});
        enhancer.setCallbackFilter(new ServiceFilter());
        Service service = (Service) enhancer.create();
        service.test();

    }



}
