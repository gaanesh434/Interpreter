import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

public class ExecutionEnvironment {
  private final Map<String, Object> globalVariables;
  private final Map<String, MethodNode> methods;
  private final Map<String, ClassNode> classes;
  private final ConcurrentHashMap<Long, Object> heap;
  private final AtomicLong nextObjectId;
  private final ScheduledExecutorService gcExecutor;
  private final int maxHeapSize;
  private final int gcThreshold;
  private final Map<String, Long> methodStartTimes;
  private final Map<String, List<Long>> methodExecutionTimes;

  // Time-travel logging
  private static final int LOG_CAPACITY = 100;
  private final Deque<VMState> stateLog = new ArrayDeque<>(LOG_CAPACITY);
  private VMState checkpointState = null;

  public ExecutionEnvironment(int maxHeapSize, int gcThreshold) {
    this.globalVariables = new ConcurrentHashMap<>();
    this.methods = new ConcurrentHashMap<>();
    this.classes = new ConcurrentHashMap<>();
    this.heap = new ConcurrentHashMap<>();
    this.nextObjectId = new AtomicLong(1);
    this.gcExecutor = Executors.newSingleThreadScheduledExecutor();
    this.maxHeapSize = maxHeapSize;
    this.gcThreshold = gcThreshold;
    this.methodStartTimes = new ConcurrentHashMap<>();
    this.methodExecutionTimes = new ConcurrentHashMap<>();

    // Start GC scheduler
    startGCScheduler();
  }

  private void startGCScheduler() {
    gcExecutor.scheduleAtFixedRate(() -> {
      if (heap.size() > gcThreshold) {
        performGC();
      }
    }, 0, 100, TimeUnit.MILLISECONDS);
  }

  public void registerMethod(String name, MethodNode method) {
    methods.put(name, method);
    methodExecutionTimes.put(name, new ArrayList<>());
  }

  public void registerClass(String name, ClassNode clazz) {
    classes.put(name, clazz);
  }

  public Object executeMethod(String methodName, Object... args) {
    MethodNode method = methods.get(methodName);
    if (method == null) {
      throw new RuntimeException("Method not found: " + methodName);
    }

    long startTime = System.currentTimeMillis();
    methodStartTimes.put(methodName, startTime);

    try {
      return method.execute(this, args);
    } finally {
      methodStartTimes.remove(methodName);
    }
  }

  public long allocateObject(Object value) {
    long objectId = nextObjectId.getAndIncrement();
    heap.put(objectId, value);
    return objectId;
  }

  public Object getObject(long objectId) {
    return heap.get(objectId);
  }

  private void performGC() {
    Set<Long> reachable = new HashSet<>();

    // Mark phase
    markReachableObjects(reachable);

    // Sweep phase
    sweepUnreachableObjects(reachable);
  }

  private void markReachableObjects(Set<Long> reachable) {
    // Mark global variables
    for (Object value : globalVariables.values()) {
      if (value instanceof Long) {
        reachable.add((Long) value);
      }
    }

    // Mark method arguments and local variables
    for (MethodNode method : methods.values()) {
      method.markReachableObjects(reachable);
    }

    // Mark class static fields
    for (ClassNode clazz : classes.values()) {
      clazz.markReachableObjects(reachable);
    }
  }

  private void sweepUnreachableObjects(Set<Long> reachable) {
    heap.entrySet().removeIf(entry -> !reachable.contains(entry.getKey()));
  }

  public void setGlobalVariable(String name, Object value) {
    if (value instanceof Long) {
      globalVariables.put(name, value);
    } else {
      long objectId = allocateObject(value);
      globalVariables.put(name, objectId);
    }
  }

  public Object getGlobalVariable(String name) {
    Object value = globalVariables.get(name);
    if (value instanceof Long) {
      return getObject((Long) value);
    }
    return value;
  }

  public Map<String, List<Long>> getMethodExecutionTimes() {
    return Collections.unmodifiableMap(methodExecutionTimes);
  }

  public void verifyMemoryUsage() {
    if (heap.size() > maxHeapSize * 0.9) { // 90% threshold
      throw new RuntimeException("Memory usage exceeds safety threshold");
    }
  }

  public void verifyDeadlineCompliance(String methodName) {
    Long startTime = methodStartTimes.get(methodName);
    if (startTime != null) {
      long currentTime = System.currentTimeMillis();
      if (currentTime - startTime > 1000) { // 1 second threshold
        throw new RuntimeException("Method " + methodName + " exceeds deadline");
      }
    }
  }

  public void shutdown() {
    gcExecutor.shutdown();
    try {
      if (!gcExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
        gcExecutor.shutdownNow();
      }
    } catch (InterruptedException e) {
      gcExecutor.shutdownNow();
    }
  }

  public void logState() {
    if (stateLog.size() == LOG_CAPACITY) {
      stateLog.removeFirst();
    }
    stateLog.addLast(captureState());
  }

  public void stepBack() {
    if (!stateLog.isEmpty()) {
      VMState prev = stateLog.removeLast();
      restoreState(prev);
    }
  }

  public void checkpoint() {
    checkpointState = captureState();
  }

  public void replay(int steps) {
    if (checkpointState == null)
      return;
    restoreState(checkpointState);
    Iterator<VMState> it = stateLog.iterator();
    for (int i = 0; i < steps && it.hasNext(); i++) {
      restoreState(it.next());
    }
  }

  private VMState captureState() {
    // For demo: only heap and globals (deep copy recommended for real use)
    return new VMState(new HashMap<>(globalVariables), new HashMap<>(heap));
  }

  private void restoreState(VMState state) {
    globalVariables.clear();
    globalVariables.putAll(state.globals);
    heap.clear();
    heap.putAll(state.heap);
  }

  private static class VMState {
    final Map<String, Object> globals;
    final Map<Long, Object> heap;

    VMState(Map<String, Object> globals, Map<Long, Object> heap) {
      this.globals = globals;
      this.heap = heap;
    }
  }
}