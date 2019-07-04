package com.zzzh.akhalteke.mvp.view

interface RequestResultInterface{
    fun<T> Success(t: T);
    fun onError(ex: Throwable);
    fun onComplise();
}
