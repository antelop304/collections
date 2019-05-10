package org.antelop.design.observer;
/**
  * 具体的主题类必须实现这个接口
  *
  */
public interface Subject
{

  //注册观察者
         public void register(Observable observer);
         //移除观察者
         public void remove(Observable observer);
         //推送消息
         public void update(String message);
}
