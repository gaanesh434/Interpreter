import java.util.List;
import java.util.ArrayList;

public class EmbeddedLoopNode extends Parser.Node {
  private final Parser.Node condition;
  private final Parser.Node body;
  private final int maxIterations;
  private final long timeoutMs;
  private final List<Object> iterationHistory;
  private int iterations;
  private long startTime;

  public EmbeddedLoopNode(Parser.Node condition, Parser.Node body, int maxIterations, long timeoutMs) {
    this.condition = condition;
    this.body = body;
    this.maxIterations = maxIterations;
    this.timeoutMs = timeoutMs;
    this.iterationHistory = new ArrayList<>();
  }

  // Getters for KLEE verification
  public Parser.Node getCondition() {
    return condition;
  }

  public Parser.Node getBody() {
    return body;
  }

  public int getMaxIterations() {
    return maxIterations;
  }

  public long getTimeoutMs() {
    return timeoutMs;
  }

  @Override
  Object evaluate() {
    startTime = System.currentTimeMillis();
    iterations = 0;

    while (iterations < maxIterations) {
      // Check timeout
      if (System.currentTimeMillis() - startTime > timeoutMs) {
        throw new RuntimeException("Loop timeout exceeded: " + timeoutMs + "ms");
      }

      // Evaluate condition
      Object condResult = condition.evaluate();
      if (!(condResult instanceof Boolean)) {
        throw new RuntimeException("Loop condition must evaluate to boolean");
      }

      if (!(Boolean) condResult) {
        break;
      }

      // Execute body with checkpoint
      try {
        Object result = body.evaluate();
        iterationHistory.add(result);
        iterations++;
      } catch (Exception e) {
        // If we have history, we can rollback
        if (!iterationHistory.isEmpty()) {
          iterationHistory.remove(iterationHistory.size() - 1);
        }
        throw e;
      }
    }

    return iterationHistory;
  }

  // Time-travel debugging support
  public List<Object> getIterationHistory() {
    return new ArrayList<>(iterationHistory);
  }

  public void rollbackToIteration(int iteration) {
    if (iteration >= 0 && iteration < iterationHistory.size()) {
      while (iterationHistory.size() > iteration + 1) {
        iterationHistory.remove(iterationHistory.size() - 1);
      }
    }
  }

  // Performance monitoring
  public LoopMetrics getMetrics() {
    return new LoopMetrics(
        iterations,
        System.currentTimeMillis() - startTime,
        iterationHistory.size());
  }

  public static class LoopMetrics {
    public final int iterations;
    public final long executionTimeMs;
    public final int successfulIterations;

    public LoopMetrics(int iterations, long executionTimeMs, int successfulIterations) {
      this.iterations = iterations;
      this.executionTimeMs = executionTimeMs;
      this.successfulIterations = successfulIterations;
    }
  }
}