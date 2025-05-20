import Interpreter.*;

public class Demo {
  public static void main(String[] args) {
        // 1. Create execution environment
        ExecutionEnvironment env = new ExecutionEnvironment(1024 * 1024, 512 * 1024);
        
        // 2. Show @Deadline annotation
        @Deadline(ms=5)
        public void realTimeMethod() {
            System.out.println("This method must complete within 5ms!");
        }
        
        // 3. Show time-travel debugging
        System.out.println("\nTime-travel debugging demo:");
        env.checkpoint();
        System.out.println("Current state saved!");
        
        // Do some operations
        System.out.println("Performing operations...");
        
        // Go back in time
        env.stepBack();
        System.out.println("Went back in time!");
        
        // 4. Show real-time GC
        System.out.println("\nReal-time GC demo:");
        for (int i = 0; i < 1000; i++) {
            env.allocateObject(new byte[100]);
        }
        System.out.println("GC Pause: " + measureGCPause(env) + " ms");
    }

  private static double measureGCPause(ExecutionEnvironment env) {
    long start = System.nanoTime();
    env.forceGC();
    long end = System.nanoTime();
    return (end - start) / 1_000_000.0; // Convert to milliseconds
  }
}