package org.antelop.design.decorator;

/**
 * Created by zhanls on 2019/5/9.
 */
public class DecoratorTest {
    public static void main(String[] args) {
        Sourceable sourceable = new Decorator(new Source());
        sourceable.method();
    }
}
