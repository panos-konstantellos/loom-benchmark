package com.the_experts.Benchmarks;

import com.the_experts.TaskLambda;
import com.the_experts.ThreadExecutor;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.profile.GCProfiler;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

@Fork(1)
@Threads(1)
@State(Scope.Benchmark)
@BenchmarkMode(Mode.All)
@OutputTimeUnit(TimeUnit.SECONDS)
public class ThreadExecutorBenchmarks {

    private static final List<Integer> threads = IntStream.range(0, 10_000)
            .boxed()
            .toList();

    private static final TaskLambda<Integer, Integer> lambda = x -> {

        try {
            Thread.sleep(Duration.ofSeconds(1));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return x;
    };

    @Benchmark
    public void PlatformThreads() {
        ThreadExecutor.<Integer, Integer>ofPlatform().execute(threads, lambda);
    }

    @Benchmark
    public void VirtualThreads() {
        ThreadExecutor.<Integer, Integer>ofVirtual().execute(threads, lambda);
    }

    public static void main(String[] args) throws Exception {
        var opt = new OptionsBuilder()
                .include(ThreadExecutorBenchmarks.class.getSimpleName())
                .addProfiler(GCProfiler.class)
                .shouldDoGC(true)
                .build();

        new Runner(opt).run();
    }
}