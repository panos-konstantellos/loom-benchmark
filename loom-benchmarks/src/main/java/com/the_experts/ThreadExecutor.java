package com.the_experts;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class ThreadExecutor<T, TResult> {
    private final ThreadFactory factory;

    private ThreadExecutor(ThreadFactory factory) {
        this.factory = factory;
    }

    public static <T, TResult> ThreadExecutor<T, TResult> ofPlatform() {
        return new ThreadExecutor<T, TResult>(Thread.ofPlatform().factory());
    }

    public static <T, TResult> ThreadExecutor<T, TResult> ofVirtual() {
        return new ThreadExecutor<T, TResult>(Thread.ofVirtual().factory());
    }

    public List<TResult> execute(List<T> list, TaskLambda<T, TResult> lambda) {

        try (var executor = Executors.newThreadPerTaskExecutor(this.factory)) {
            return list
                    .stream()
                    .map(x -> executor.submit(() -> lambda.Invoke(x)))
                    .toList()
                    .stream()
                    .map(future -> {
                        try {
                            return future.get();
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .toList();
        }
    }
}