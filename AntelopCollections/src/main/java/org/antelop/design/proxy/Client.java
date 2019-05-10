package org.antelop.design.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;


public class Client
{

    private Subject proxy;

    public Client()
    {
        proxy = new Proxy();
    }

    public void buyTickets()
    {
        /*Subject subjectProxy = (Subject)java.lang.reflect.Proxy.newProxyInstance(proxy.getClass().getClassLoader(), proxy.getClass().getInterfaces(), new InvocationHandler() {
            
            @Override
            public Object invoke(Object oProxy, Method method, Object[] args)
                throws Throwable
            {
                return method.invoke(proxy, args);
            }
        });*/
        System.out.println("小王：我要买票...");
        proxy.soldTickets();
//        subjectProxy.soldTickets();
        System.out.println("小王：已买到票...");
    }
}
