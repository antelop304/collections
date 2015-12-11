package org.antelop.design.observer;
/**
  * 相当于ConcreteObserver，观察者对象 
  *
  */
public class Radio implements Observable
{

 private Subject subject;
    
    public Radio(Subject subject){
        this.subject = subject;
    }

    @Override
    public void update(String message) {
        display(message);
    }

    @Override
    public void register() {
        subject.register(this);
    }

    @Override
    public void remove() {
        subject.remove(this);
    }
    
    public void display(String message){
        System.out.println("get message from radiostation:"+message);
    }
}
