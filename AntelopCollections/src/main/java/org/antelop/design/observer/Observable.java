package org.antelop.design.observer;


 /**
  * 相当于Observer，所有的观察者必须实现这个接口 
  * http://www.cnblogs.com/world-mian/p/DesignPattern_Observer.html
  */
public interface Observable
{
    //更新数据
    public void update(String message);
    //成为主题对象的观察者，开始监听
    public void register();
    //不再监听主题对象
    public void remove();
}
