// MyAidl.aidl
package com.example.componentaction;
import com.example.componentaction.ServiceCallback;

// Declare any non-default types here with import statements

interface MyAidl {
    void test();

    void registerCallback(ServiceCallback callback);

    void callback();

    void callbackInNewThread();
}
