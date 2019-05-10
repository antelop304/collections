package org.antelop.design.adapter.clazz;

/**
 * Created by zhanls on 2019/5/9.
 */
public class AdapterTest {
    public static void main(String[] args) {
        Targerable targerable = new Adapter();
        targerable.method1();
        targerable.method2();
    }
}
