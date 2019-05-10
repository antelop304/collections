package org.antelop.design.observer;

import java.util.ArrayList;
import java.util.List;
/**
  * 相当于ConcreteSubject，具体的主题类
  *
  */
public class Radiostation implements Subject
{
    private List<Observable> observers;
    
    public Radiostation(){
        observers = new ArrayList<Observable>();
    }

    @Override
    public void register(Observable observer){
        observers.add(observer);
        System.out.println("have "+observers.size()+" observer are listening...");
    }

    @Override
    public void remove(Observable observer){
        observers.remove(observer);
        System.out.println("a observer has gone...left "+observers.size()+" observer");
    }

    @Override
    public void update(String message){
        for(Observable observer:observers){
            observer.update(message);
        }
    }

}
