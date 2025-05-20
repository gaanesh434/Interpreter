package benchmarks;

import Interpreter.*;
import java.util.*;
import java.io.*;

public class PiStressTest {
  private static final int TEST_DURATION_MS = 60000; // 1 minute
  private static final int OBJECT_SIZE = 100;
  private static final int OBJECT_COUNT = 1000;
  private static final String LOG_FILE = "gc_pauses.log";

  public static void main(String[] args) {
    ExecutionEnvironment env = new ExecutionEnvironment(1024 * 1024, 512 * 1024);
    List<Long> gcPauses = new ArrayList<>();
    long startTime = System.currentTimeMillis();

    try (PrintWriter writer = new PrintWriter(new FileWriter(LOG_FILE))) {
      writer.println("Timestamp,GC Pause (ms)");

      while (System.currentTimeMillis() - startTime < TEST_DURATION_MS) {
        // Create objects to trigger GC
        for (int i = 0; i < OBJECT_COUNT; i++) {
          env.allocateObject(new byte[OBJECT_SIZE]);
        }

        // Measure GC pause
        long gcStart = System.nanoTime();
        System.gc(); // Force GC
        long gcEnd = System.nanoTime();
        long pauseTime = (gcEnd - gcStart) / 1_000_000; // Convert to ms

        // Log results
        writer.printf("%d,%d%n", System.currentTimeMillis(), pauseTime);
        gcPauses.add(pauseTime);

        // Print progress
        System.out.printf("GC Pause: %d ms%n", pauseTime);
      }

      // Print statistics
      printStatistics(gcPauses, writer);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static void printStatistics(List<Long> pauses, PrintWriter writer) {
    if (pauses.isEmpty())
      return;

    long min = Collections.min(pauses);
    long max = Collections.max(pauses);
    double avg = pauses.stream().mapToLong(Long::longValue).average().orElse(0);

    String stats = String.format(
        "GC Pause Statistics:%n" +
            "Min: %d ms%n" +
            "Max: %d ms%n" +
            "Avg: %.2f ms%n",
        min, max, avg);

    System.out.println(stats);
    writer.println(stats);
  }
}