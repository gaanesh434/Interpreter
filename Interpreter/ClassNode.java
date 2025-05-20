import java.util.*;
import java.util.Set;

public class ClassNode {
  private final String className;
  private String superClassName;
  private final Map<String, FieldNode> fields;
  private final Map<String, MethodNode> methods;
  private final List<String> inheritanceChain;

  public ClassNode(String className) {
    this.className = className;
    this.fields = new HashMap<>();
    this.methods = new HashMap<>();
    this.inheritanceChain = new ArrayList<>();
    this.inheritanceChain.add(className);
  }

  public void setSuperClass(String superClassName) {
    this.superClassName = superClassName;
    this.inheritanceChain.add(superClassName);
  }

  public void addField(String name, String type) {
    fields.put(name, new FieldNode(name, type));
  }

  public void addMethod(MethodNode method) {
    methods.put(method.getName(), method);
  }

  public String getClassName() {
    return className;
  }

  public String getSuperClassName() {
    return superClassName;
  }

  public Map<String, FieldNode> getFields() {
    return new HashMap<>(fields);
  }

  public Map<String, MethodNode> getMethods() {
    return new HashMap<>(methods);
  }

  public List<String> getInheritanceChain() {
    return new ArrayList<>(inheritanceChain);
  }

  public void markReachableObjects(Set<Long> reachable) {
    // Mark field values
    for (FieldNode field : fields.values()) {
      field.markReachableObjects(reachable);
    }

    // Mark method objects
    for (MethodNode method : methods.values()) {
      method.markReachableObjects(reachable);
    }
  }

  public static class FieldNode {
    private final String name;
    private final String type;
    private Object value;

    public FieldNode(String name, String type) {
      this.name = name;
      this.type = type;
    }

    public String getName() {
      return name;
    }

    public String getType() {
      return type;
    }

    public Object getValue() {
      return value;
    }

    public void setValue(Object value) {
      this.value = value;
    }

    public void markReachableObjects(Set<Long> reachable) {
      if (value instanceof Long) {
        reachable.add((Long) value);
      }
    }
  }
}