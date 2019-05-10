package org.antelop.jni.example;

public class HelloWorld
{
    public native int displayHelloWorld1();
    public native String displayHelloWorld2();
    static {
        System.loadLibrary("HelloWorldImpl");
    }
    
    public static void main(String[] args)
    {
        HelloWorld helloWorld = new HelloWorld();
        int r1 = helloWorld.displayHelloWorld1();
//        String r2 = helloWorld.displayHelloWorld2();
        System.out.println(r1);
//        System.out.println(r2);
    }
}
