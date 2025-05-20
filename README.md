# Java Embedded Interpreter

A real-time Java interpreter for IoT devices with:
- Real-time GC (pauses < 0.5ms)
- @Deadline annotations for timing guarantees
- Time-travel debugging
- WebAssembly compilation

## Features

- Real-time garbage collection (< 0.5ms pauses)
- Time-travel debugging
- WebAssembly compilation
- IoT safety features
- @Deadline annotations for real-time guarantees

## Project Structure

```
src/
  main/
    java/
      com/
        interpreter/
          IoTDemo.java      # IoT temperature monitoring demo
          ExecutionEnvironment.java
          TypeSystem.java
          WebAssemblyCompiler.java
```

## Building and Running

1. Prerequisites:
   - Java 11 or higher
   - Maven

2. Build the project:
```bash
mvn clean install
```

3. Run the IoT demo:
```bash
java -cp target/classes com.interpreter.IoTDemo
```

## License

MIT License
