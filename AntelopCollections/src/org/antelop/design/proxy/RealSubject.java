package org.antelop.design.proxy;

/**
  * 
  * 火车站（RealSubject）：真正处理请求的对象
  */
public class RealSubject implements Subject
{

    @Override
    public void soldTickets()
    {
        System.out.println("火车站：已接收到订单，允许出票...");
    }

}
