// IMyAidlInterface.aidl
package com.zjl.ipc;

// Declare any non-default types here with import statements
import com.zjl.ipc.IOnEventListenerInterface;

interface IMyAidlInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);

    /**
    *  caculate the sum of two ints.
    */
    int sum(int a, int b);

    void registerOnEventListener(IOnEventListenerInterface listener);
    void unRegisterOnEventListener(IOnEventListenerInterface listener);
}
