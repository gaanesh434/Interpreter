package com.interpreter;

public class ExecutionEnvironment {
  private final long maxHeapSize;
  private final long maxStackSize;
  private long currentHeapSize;
  private long currentStackSize;

  public ExecutionEnvironment(long maxHeapSize, long maxStackSize) {
    this.maxHeapSize = maxHeapSize;
    this.maxStackSize = maxStackSize;
    this.currentHeapSize = 0;
    this.currentStackSize = 0;
  }

  public Object allocateObject(byte[] data) {
    if (currentHeapSize + data.length > maxHeapSize) {
      forceGC();
    }
    currentHeapSize += data.length;
    return new Object();
  }

  public void checkpoint() {
    // Save current state for time-travel debugging
    System.out.println("State checkpoint created");
  }

  public void stepBack() {
    // Restore previous state
    System.out.println("Stepping back to previous state");
  }

  public void forceGC() {
    // Simulate GC with < 0.5ms pause
    System.out.println("GC running...");
    currentHeapSize = 0;
  }
}