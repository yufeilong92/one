package com.zzzh.akhalteke.mvp.view

interface StringResultInterface{
    fun<T> Success(t: T);
    fun onError(ex: Throwable);
    fun onComplise();

}