package org.antelop.design.observer;

public class Test
{

    public static void main(String[] args)
    {
        Subject radiostation = new Radiostation();
        
        Radio o1 = new Radio(radiostation);
        Radio o2 = new Radio(radiostation);
        
        //注册
        o1.register();
        o2.register();
        
        //更新消息
        radiostation.update("hello world");
        radiostation.update("over");
        
        //退出监听
        o1.remove();
        o2.remove();
    }

}
