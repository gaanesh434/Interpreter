package com.interpreter;

public class WebAssemblyCompiler {
  private final TypeSystem typeSystem;

  public WebAssemblyCompiler(TypeSystem typeSystem) {
    this.typeSystem = typeSystem;
  }

  public void compileToFile(String outputFile, Object[] classes) {
    // Compile Java classes to WebAssembly
    System.out.println("Compiling to WebAssembly: " + outputFile);
    for (Object cls : classes) {
      typeSystem.validateType(cls);
    }
  }
}