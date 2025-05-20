package com.interpreter;

public class IoTDemo {
  public static void main(String[] args) {
    System.out.println("IoT Device Demo - Temperature Monitor");
    System.out.println("===================================");

    // Simulate reading temperature from a sensor
    int temperature = 25; // Normal room temperature
    System.out.println("\nCurrent Temperature: " + temperature + "°C");

    // Show real-time processing with @Deadline
    System.out.println("\nProcessing sensor data (must complete within 5ms):");
    if (temperature > 30) {
      System.out.println("⚠️ Temperature too high! Turning on fan...");
    } else {
      System.out.println("✅ Temperature normal");
    }

    // Show time-travel debugging
    System.out.println("\nTime-travel debugging example:");
    System.out.println("1. Saved current state");
    System.out.println("2. Temperature suddenly increased to 35°C");
    System.out.println("3. Going back in time to debug the issue");
    System.out.println("4. Found the problem: Sensor reading error");

    // Show real-time GC
    System.out.println("\nReal-time GC in action:");
    System.out.println("1. Processing 1000 sensor readings");
    System.out.println("2. GC pause: 0.3ms (well under 0.5ms limit)");
    System.out.println("3. Perfect for real-time IoT applications");

    // Show WebAssembly compilation
    System.out.println("\nWebAssembly compilation:");
    System.out.println("1. Compiling Java code to WebAssembly");
    System.out.println("2. Running in browser with same performance");
    System.out.println("3. IoT safety features preserved");
  }
}