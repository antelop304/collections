package org.antelop.design.decorator;

/**
 * Created by zhanls on 2019/5/9.
 */
public class Source implements Sourceable{

    @Override
    public void method() {
        System.err.println("in source");
    }
}
