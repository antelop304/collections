package org.antelop.design.adapter.interfacer;

/**
 * Created by zhanls on 2019/5/9.
 */
public class WapperTest {
    public static void main(String[] args) {
        Sourceable sourceable1 = new Sourceable1();
        Sourceable sourceable2 = new Sourceable2();
        sourceable1.method1();
        sourceable1.method2();
        sourceable2.method1();
        sourceable2.method2();
    }
}
