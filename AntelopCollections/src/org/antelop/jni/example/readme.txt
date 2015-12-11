教程:
http://blog.csdn.net/xw13106209/article/details/6989415
HelloWorld.h里面的#include <jni.h>要改成#include “jni.h”。
http://www.cnblogs.com/BloodAndBone/archive/2010/12/16/1908509.html


java出现no XXX in java.library.path的解决办法及eclipse配置:
1.选择项目，点击右键>Build path>Configure Build path

2.左侧选择Java Build Path>Libraries>JRE System Library>Native library location

3.点击编辑输入library路径，我这里选择的是工作空间的相对路径，也可以直接输入绝对路径

4.点击OK，设置完成。