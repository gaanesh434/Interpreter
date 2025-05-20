package Interpreter;

import java.util.*;

public abstract class MethodNode {
  private final String name;
  private final String[] parameterTypes;
  private final String returnType;
  private final Map<String, Object> localVariables;
  private final List<Object> parameterValues;

  public MethodNode(String name, String[] parameterTypes, String returnType) {
    this.name = name;
    this.parameterTypes = parameterTypes;
    this.returnType = returnType;
    this.localVariables = new HashMap<>();
    this.parameterValues = new ArrayList<>();
  }

  public abstract Object execute(ExecutionEnvironment env, Object... args);

  public String getName() {
    return name;
  }

  public String[] getParameterTypes() {
    return parameterTypes;
  }

  public String getReturnType() {
    return returnType;
  }

  public void setLocalVariable(String name, Object value) {
    localVariables.put(name, value);
  }

  public Object getLocalVariable(String name) {
    return localVariables.get(name);
  }

  public void setParameterValue(int index, Object value) {
    while (parameterValues.size() <= index) {
      parameterValues.add(null);
    }
    parameterValues.set(index, value);
  }

  public Object getParameterValue(int index) {
    return parameterValues.get(index);
  }

  public void markReachableObjects(Set<Long> reachable) {
    // Mark local variables
    for (Object value : localVariables.values()) {
      if (value instanceof Long) {
        reachable.add((Long) value);
      }
    }

    // Mark parameter values
    for (Object value : parameterValues) {
      if (value instanceof Long) {
        reachable.add((Long) value);
      }
    }
  }

  public void clear() {
    localVariables.clear();
    parameterValues.clear();
  }
}