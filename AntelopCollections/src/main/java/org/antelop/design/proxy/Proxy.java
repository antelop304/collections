package org.antelop.design.proxy;

/**
  * 
  * 代售点（Proxy）：持有真正做事对象的引用
  * http://www.cnblogs.com/world-mian/p/DesignPattern_Proxy.html
  */
public class Proxy implements Subject
{
    private Subject realSubject;
         
    public Proxy(){
        realSubject = new RealSubject();
    }
    
    @Override
    public void soldTickets()
    {
        System.out.println("代售点：有顾客需要买火车票...");
        realSubject.soldTickets();
        System.out.println("代售点：正在出票...");

    }

}
