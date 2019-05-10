package org.antelop.design.adapter.object;


import org.antelop.design.adapter.clazz.Source;
import org.antelop.design.adapter.clazz.Targerable;

/**
 * Created by zhanls on 2019/5/9.
 * 对象的适配器模式
 * 基本思路和类的适配器模式相同，只是将Adapter类作修改，这次不继承Source类，而是持有Source类的实例，以达到解决兼容性的问题。
 */
public class Wrapper implements Targerable {

    private Source source;

    public Wrapper(Source source) {
        this.source = source;
    }

    @Override
    public void method1() {
        source.method1();
    }

    @Override
    public void method2() {
        System.err.println("in wapper method2");
    }
}
