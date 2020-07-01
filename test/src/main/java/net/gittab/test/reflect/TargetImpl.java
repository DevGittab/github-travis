package net.gittab.test.reflect;

/**
 * TargetImpl.
 *
 * @author xiaohua zhou
 **/
public class TargetImpl implements Target {

    @Override
    public void execute() {
        System.out.println("target execute");
    }
}
