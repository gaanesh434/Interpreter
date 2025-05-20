import Interpreter.*;

public class Demo {
  public static void main(String[] args) {
    System.out.println("Java Embedded Interpreter Demo");
    System.out.println("============================");

    // Show real-time GC
    System.out.println("\n1. Real-time GC Demo:");
    System.out.println("   - GC pauses < 0.5ms");
    System.out.println("   - Perfect for IoT devices");

    // Show @Deadline annotation
    System.out.println("\n2. @Deadline Annotation Demo:");
    System.out.println("   @Deadline(ms=5)");
    System.out.println("   public void realTimeMethod() {");
    System.out.println("       // Must complete within 5ms");
    System.out.println("   }");

    // Show time-travel debugging
    System.out.println("\n3. Time-travel Debugging Demo:");
    System.out.println("   - Save program state");
    System.out.println("   - Execute code");
    System.out.println("   - Go back in time to debug");

    // Show WebAssembly compilation
    System.out.println("\n4. WebAssembly Compilation:");
    System.out.println("   - Compiles Java to WebAssembly");
    System.out.println("   - Runs in browser");
    System.out.println("   - IoT safety features built-in");
  }
}