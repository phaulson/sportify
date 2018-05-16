package com.spogss.sportifycommunity.data.Connection;

public class AsyncResult <T>{
    private Exception error;
    private T result;

    public AsyncResult(Exception error) {
        this.error = error;
    }

    public AsyncResult(T result) {
        this.result = result;
    }

    public Exception getError() {
        return error;
    }
    public T getResult() {
        return result;
    }
}
