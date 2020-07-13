package net.gittab.githubtravis.reflect;

import java.lang.reflect.Method;

import org.springframework.cglib.proxy.CallbackFilter;

/**
 * ServiceFilter.
 *
 * @author xiaohua zhou
 **/
public class ServiceFilter implements CallbackFilter {

    @Override
    public int accept(Method method) {
        if("test".equals(method.getName())){
            System.out.println("proxy start");
            return 1;
        }
        return 0;
    }
}
