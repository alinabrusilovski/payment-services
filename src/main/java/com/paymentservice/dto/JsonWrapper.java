package com.paymentservice.dto;

public class JsonWrapper<T> {
    private T value;

    public JsonWrapper(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}