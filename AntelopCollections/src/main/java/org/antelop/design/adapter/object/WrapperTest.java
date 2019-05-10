package org.antelop.design.adapter.object;


import org.antelop.design.adapter.clazz.Source;

/**
 * Created by zhanls on 2019/5/9.
 */
public class WrapperTest {

    public static void main(String[] args) {
        Wrapper wapper = new Wrapper(new Source());
        wapper.method1();
        wapper.method2();

    }
}
