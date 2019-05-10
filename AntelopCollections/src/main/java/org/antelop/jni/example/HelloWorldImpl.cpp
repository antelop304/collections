#include "org_antelop_jni_example_HelloWorld.h"  
#include <stdio.h>  
#include "jni.h"
/* 
 * Class:     HelloWorld 
 * Method:    displayHelloWorld 
 * Signature: ()V 
 */  
JNIEXPORT int JNICALL Java_org_antelop_jni_example_HelloWorld_displayHelloWorld1  
  (JNIEnv *, jobject)  
 {  
    printf("Hello World!\n");  
    return 3;  
}  