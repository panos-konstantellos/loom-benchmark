package com.the_experts;

public interface TaskLambda<T, TResult> {
    public TResult Invoke(T input);
}
