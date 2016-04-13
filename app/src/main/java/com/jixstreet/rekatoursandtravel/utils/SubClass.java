package com.jixstreet.rekatoursandtravel.utils;

public class SubClass {

    MyCallbackClass myCallbackClass;

    void registerCallback(MyCallbackClass callbackClass) {
        myCallbackClass = callbackClass;
    }

    void doSomething() {
        myCallbackClass.callbackReturn();
    }

    interface MyCallbackClass {
        void callbackReturn();
    }

}