package org.antelop.design.adapter.clazz;

/**
 * Created by zhanls on 2019/5/9.
 * 类的适配器模式
 * 核心思想就是：有一个Source类，拥有一个方法，待适配，目标接口是Targetable，通过Adapter类，将Source的功能扩展到Targetable里
 */
public class Adapter extends Source implements Targerable {

    @Override
    public void method2() {
        System.err.println("in adapter method2");
    }
}
