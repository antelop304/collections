package org.antelop.design.decorator;

/**
 * Created by zhanls on 2019/5/9.
 * 装饰模式
 * 顾名思义，装饰模式就是给一个对象增加一些新的功能，而且是动态的，要求装饰对象和被装饰对象实现同一个接口，装饰对象持有被装饰对象的实例
 */
public class Decorator implements Sourceable {

    private Source source;

    public Decorator(Source source) {
        this.source = source;
    }

    @Override
    public void method() {
        System.err.println("before");
        source.method();
        System.err.println("after");
    }
}
