package com.interpreter;

public class TypeSystem {
  private final long maxMemory;

  public TypeSystem(long maxMemory) {
    this.maxMemory = maxMemory;
  }

  public void checkDynamicClassLoading(String className) {
    // IoT safety: Prevent dynamic class loading
    throw new SecurityException("Dynamic class loading not allowed for IoT safety");
  }

  public void validateType(Object obj) {
    // Type safety checks
    if (obj == null) {
      throw new NullPointerException("Null objects not allowed");
    }
  }
}