package benchmarks;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import Interpreter.*;

import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Fork(value = 1, warmups = 1)
@Warmup(iterations = 3)
@Measurement(iterations = 5)
public class GCBenchmark {
  private ExecutionEnvironment env;
  private static final int OBJECT_COUNT = 10000;

  @Setup
  public void setup() {
    env = new ExecutionEnvironment(1024 * 1024, 512 * 1024); // 1MB heap, 512KB threshold
  }

  @Benchmark
  public void testOurGC() {
    // Create and discard objects rapidly
    for (int i = 0; i < OBJECT_COUNT; i++) {
      env.allocateObject(new byte[100]); // 100-byte objects
    }
  }

  @Benchmark
    public void testHotSpotGC() {
        // Same test but using HotSpot's GC
        for (int i = 0; i < OBJECT_COUNT; i++) {
            new byte[100];
        }
    }

  public static void main(String[] args) throws RunnerException {
    Options opt = new OptionsBuilder()
        .include(GCBenchmark.class.getSimpleName())
        .build();
    new Runner(opt).run();
  }
}