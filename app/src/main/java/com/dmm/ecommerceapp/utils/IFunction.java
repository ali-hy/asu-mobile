package com.dmm.ecommerceapp.utils;

@FunctionalInterface
public interface IFunction<T, R> {
    R apply(T v);
}

