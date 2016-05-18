#include <com_jni_Account.h>
#include<stdio.h>
#include<jni.h>



JNIEXPORT void JNICALL Java_com_jni_Account_statistic(JNIEnv *env, jclass zclass, jobject obj){

      //在子线程中不能这样用
      //jclass tclass = (*env)->FindClass(env, "com/jni/Account");
       //这种写法可以用在子线程中
       // jclass tclass=(*env)->GetObjectClass(evn, object);
       //jmethodID callbackID = (*env)->GetMethodID(env,tclass, "nullback","()V") ;//或得该回调方法句柄
       //if(callbackID == 0) {
     	//	LOG("Fail to find method onNativeCallback");
      //		return 0;
     	//}
     	//jobject jobj = (*env)->AllocObject(env, tclass);
       // if(jobj == 0){
      //      LOG("find jobj error");
       //     return 0;
       // }
      //	(*env)->CallVoidMethod(env, jobj, callbackID);



      //jclass tclass = (*env)->FindClass(env, "com/jni/Account");
      //jmethodID callbackID = (*env)->GetMethodID(env,tclass, "callback","(Ljava/lang/String;)V") ;//或得该回调方法句柄
      //if(callbackID == 0){
      //    LOG("find method error");
      //    return 0;
      // }
      // jobject jobj = (*env)->AllocObject(env, tclass);
      // if(jobj ==0){
      //    LOG("find jobj error");
      //    return 0;
      // }
      //(*env)->CallVoidMethod(env, jobj, callbackID,(*env)->NewStringUTF(env,"haha in C ."));

}



JNIEXPORT jint JNICALL Java_com_jni_Account_isVerfyPoint(JNIEnv *env, jclass zclass, jobject obj){
      //获得jfieldID 以及 该字段的初始值
        //回调Java中的方法
       int result = 4;

        jclass btpoint = (*env)->GetObjectClass(env,obj);
       if(btpoint == 0){
          LOG("class not found");
          return 0;
       }

       jfieldID latFiled = (*env)->GetFieldID(env,btpoint,"lat","D"); //获得得类的属性id
       jfieldID lonFiled = (*env)->GetFieldID(env,btpoint,"lon","D"); // 获得属性ID

       if(latFiled == 0 && lonFiled == 0){
            LOG("Filed not found");
           return 0;
       }

       jdouble lat = (*env)->GetDoubleField(env,obj,latFiled);  //获得属性值
       jdouble lon = (*env)->GetDoubleField(env,obj,lonFiled);  //获得属性值

       double mlat = lat;
       double mlon = lon;
       //LOG(lat);
       LOG("########## lat = %f", mlat);
       LOG("########## lon = %f", mlon);

      return result;
}