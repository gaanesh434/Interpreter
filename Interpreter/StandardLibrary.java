import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class StandardLibrary {
  private final Map<String, Object> constants;
  private final Map<String, MethodNode> methods;
  private final Map<String, ClassNode> classes;
  private final ExecutionEnvironment env;
  private final TypeSystem typeSystem;

  public StandardLibrary(ExecutionEnvironment env, TypeSystem typeSystem) {
    this.env = env;
    this.typeSystem = typeSystem;
    this.constants = new HashMap<>();
    this.methods = new HashMap<>();
    this.classes = new HashMap<>();

    initializeConstants();
    initializeMethods();
    initializeClasses();
  }

  private void initializeConstants() {
    constants.put("PI", Math.PI);
    constants.put("E", Math.E);
    constants.put("MAX_INT", Integer.MAX_VALUE);
    constants.put("MIN_INT", Integer.MIN_VALUE);
  }

  private void initializeMethods() {
    // Math operations
    methods.put("abs", new MethodNode("abs", new String[] { "int" }, "int") {
      @Override
      public Object execute(ExecutionEnvironment env, Object... args) {
        return Math.abs((Integer) args[0]);
      }
    });

    methods.put("min", new MethodNode("min", new String[] { "int", "int" }, "int") {
      @Override
      public Object execute(ExecutionEnvironment env, Object... args) {
        return Math.min((Integer) args[0], (Integer) args[1]);
      }
    });

    methods.put("max", new MethodNode("max", new String[] { "int", "int" }, "int") {
      @Override
      public Object execute(ExecutionEnvironment env, Object... args) {
        return Math.max((Integer) args[0], (Integer) args[1]);
      }
    });

    // IoT-specific methods
    methods.put("readSensor", new MethodNode("readSensor", new String[] { "String" }, "int") {
      @Override
      public Object execute(ExecutionEnvironment env, Object... args) {
        String sensorId = (String) args[0];
        // Simulate sensor reading
        return new Random().nextInt(100);
      }
    });

    methods.put("setActuator", new MethodNode("setActuator", new String[] { "String", "int" }, "void") {
      @Override
      public Object execute(ExecutionEnvironment env, Object... args) {
        String actuatorId = (String) args[0];
        int value = (Integer) args[1];
        // Simulate actuator control
        return null;
      }
    });

    // Time operations
    methods.put("currentTime", new MethodNode("currentTime", new String[] {}, "long") {
      @Override
      public Object execute(ExecutionEnvironment env, Object... args) {
        return System.currentTimeMillis();
      }
    });

    methods.put("sleep", new MethodNode("sleep", new String[] { "long" }, "void") {
      @Override
      public Object execute(ExecutionEnvironment env, Object... args) {
        try {
          Thread.sleep((Long) args[0]);
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
        }
        return null;
      }
    });

    // Wasm polyfill: System.out.println
    methods.put("println", new MethodNode("println", new String[] { "String" }, "void") {
      @Override
      public Object execute(ExecutionEnvironment env, Object... args) {
        // In browser, this would call JS: console.log(args[0])
        System.out.println(args[0]);
        return null;
      }
    });

    // Register all methods with execution environment
    for (Map.Entry<String, MethodNode> entry : methods.entrySet()) {
      env.registerMethod(entry.getKey(), entry.getValue());
    }
  }

  private void initializeClasses() {
    // IoT Device class
    ClassNode deviceClass = new ClassNode("Device");
    deviceClass.addField("id", "String");
    deviceClass.addField("status", "int");
    deviceClass.addMethod(new MethodNode("getStatus", new String[] {}, "int") {
      @Override
      public Object execute(ExecutionEnvironment env, Object... args) {
        return 0; // Simulate device status
      }
    });
    classes.put("Device", deviceClass);

    // Sensor class
    ClassNode sensorClass = new ClassNode("Sensor");
    sensorClass.addField("id", "String");
    sensorClass.addField("type", "String");
    sensorClass.addMethod(new MethodNode("read", new String[] {}, "int") {
      @Override
      public Object execute(ExecutionEnvironment env, Object... args) {
        return new Random().nextInt(100); // Simulate sensor reading
      }
    });
    classes.put("Sensor", sensorClass);

    // Actuator class
    ClassNode actuatorClass = new ClassNode("Actuator");
    actuatorClass.addField("id", "String");
    actuatorClass.addField("type", "String");
    actuatorClass.addMethod(new MethodNode("set", new String[] { "int" }, "void") {
      @Override
      public Object execute(ExecutionEnvironment env, Object... args) {
        // Simulate actuator control
        return null;
      }
    });
    classes.put("Actuator", actuatorClass);

    // Register all classes with execution environment
    for (Map.Entry<String, ClassNode> entry : classes.entrySet()) {
      env.registerClass(entry.getKey(), entry.getValue());
    }
  }

  public Object getConstant(String name) {
    return constants.get(name);
  }

  public MethodNode getMethod(String name) {
    return methods.get(name);
  }

  public ClassNode getClass(String name) {
    return classes.get(name);
  }

  // IoT-specific utility methods
  public int readSensorValue(String sensorId) {
    return (Integer) methods.get("readSensor").execute(env, sensorId);
  }

  public void setActuatorValue(String actuatorId, int value) {
    methods.get("setActuator").execute(env, actuatorId, value);
  }

  public long getCurrentTime() {
    return (Long) methods.get("currentTime").execute(env);
  }

  public void sleep(long milliseconds) {
    methods.get("sleep").execute(env, milliseconds);
  }

  // Resource management
  public void verifyResourceAvailability() {
    // Check memory usage
    env.verifyMemoryUsage();

    // Check CPU usage
    if (Thread.activeCount() > 10) {
      throw new RuntimeException("Too many active threads");
    }
  }

  public void verifyDeviceStatus(String deviceId) {
    // Simulate device status check
    if (new Random().nextInt(100) < 5) {
      throw new RuntimeException("Device " + deviceId + " is offline");
    }
  }
}