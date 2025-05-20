import java.util.*;
import java.nio.ByteBuffer;
import sun.misc.Unsafe;

public class TypeSystem {
  private final Map<String, Type> typeTable;
  private final Map<String, DeadlineInfo> deadlineTable;
  private final Unsafe unsafe;
  private final ByteBuffer offHeapBuffer;
  private final int maxOffHeapSize;
  private final boolean allowDynamicClassLoading = false;

  public TypeSystem(int maxOffHeapSize) {
    this.typeTable = new HashMap<>();
    this.deadlineTable = new HashMap<>();
    this.maxOffHeapSize = maxOffHeapSize;
    this.offHeapBuffer = ByteBuffer.allocateDirect(maxOffHeapSize);

    try {
      this.unsafe = getUnsafe();
    } catch (Exception e) {
      throw new RuntimeException("Failed to initialize unsafe", e);
    }

    // Initialize primitive types
    initializePrimitiveTypes();
  }

  private void initializePrimitiveTypes() {
    typeTable.put("int", new PrimitiveType("int", 4));
    typeTable.put("boolean", new PrimitiveType("boolean", 1));
    typeTable.put("String", new ReferenceType("String"));
    typeTable.put("void", new VoidType());
  }

  public void registerType(String name, Type type) {
    if (typeTable.containsKey(name)) {
      throw new RuntimeException("Type " + name + " already exists");
    }
    typeTable.put(name, type);
  }

  public void registerDeadline(String methodName, int deadlineMs) {
    if (deadlineMs <= 0) {
      throw new RuntimeException("Invalid deadline: " + deadlineMs + "ms");
    }
    deadlineTable.put(methodName, new DeadlineInfo(deadlineMs));
  }

  public boolean checkTypeCompatibility(String sourceType, String targetType) {
    Type source = typeTable.get(sourceType);
    Type target = typeTable.get(targetType);

    if (source == null || target == null) {
      return false;
    }

    return source.isAssignableTo(target);
  }

  public long allocateOffHeap(int size) {
    if (size > maxOffHeapSize) {
      throw new RuntimeException("Allocation size " + size + " exceeds maximum " + maxOffHeapSize);
    }

    long address = unsafe.allocateMemory(size);
    if (address == 0) {
      throw new RuntimeException("Failed to allocate off-heap memory");
    }

    return address;
  }

  public void freeOffHeap(long address) {
    unsafe.freeMemory(address);
  }

  public boolean isDeadlineExceeded(String methodName, long startTime) {
    DeadlineInfo info = deadlineTable.get(methodName);
    if (info == null) {
      return false;
    }

    long currentTime = System.currentTimeMillis();
    return (currentTime - startTime) > info.deadlineMs;
  }

  // Type hierarchy
  public static abstract class Type {
    protected final String name;
    protected final int size;

    protected Type(String name, int size) {
      this.name = name;
      this.size = size;
    }

    public abstract boolean isAssignableTo(Type other);

    public abstract Object defaultValue();
  }

  public static class PrimitiveType extends Type {
    public PrimitiveType(String name, int size) {
      super(name, size);
    }

    @Override
    public boolean isAssignableTo(Type other) {
      if (!(other instanceof PrimitiveType)) {
        return false;
      }
      return name.equals(other.name);
    }

    @Override
    public Object defaultValue() {
      switch (name) {
        case "int":
          return 0;
        case "boolean":
          return false;
        default:
          return null;
      }
    }
  }

  public static class ReferenceType extends Type {
    public ReferenceType(String name) {
      super(name, 8); // Reference size on 64-bit JVM
    }

    @Override
    public boolean isAssignableTo(Type other) {
      if (other instanceof ReferenceType) {
        return name.equals(other.name) ||
            name.equals("Object") ||
            other.name.equals("Object");
      }
      return false;
    }

    @Override
    public Object defaultValue() {
      return null;
    }
  }

  public static class VoidType extends Type {
    public VoidType() {
      super("void", 0);
    }

    @Override
    public boolean isAssignableTo(Type other) {
      return other instanceof VoidType;
    }

    @Override
    public Object defaultValue() {
      return null;
    }
  }

  private static class DeadlineInfo {
    final int deadlineMs;
    final long lastCheck;
    final List<Long> executionTimes;

    DeadlineInfo(int deadlineMs) {
      this.deadlineMs = deadlineMs;
      this.lastCheck = System.currentTimeMillis();
      this.executionTimes = new ArrayList<>();
    }
  }

  private static Unsafe getUnsafe() throws Exception {
    java.lang.reflect.Field f = Unsafe.class.getDeclaredField("theUnsafe");
    f.setAccessible(true);
    return (Unsafe) f.get(null);
  }

  // Memory safety checks
  public void verifyMemoryAccess(long address, int size) {
    if (address < 0 || address + size > maxOffHeapSize) {
      throw new RuntimeException("Memory access out of bounds");
    }
  }

  public void verifyTypeSafety(Object value, String expectedType) {
    Type type = typeTable.get(expectedType);
    if (type == null) {
      throw new RuntimeException("Unknown type: " + expectedType);
    }

    if (!type.isAssignableTo(typeTable.get(value.getClass().getName()))) {
      throw new RuntimeException("Type mismatch: expected " + expectedType +
          ", got " + value.getClass().getName());
    }
  }

  public void checkDynamicClassLoading(String className) {
    if (!allowDynamicClassLoading) {
      throw new RuntimeException("Dynamic class loading is forbidden for IoT safety: " + className);
    }
  }
}