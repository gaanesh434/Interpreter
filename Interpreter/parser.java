import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Parser {
  private final List<String> tokens;
  private int current = 0;
  private final Map<String, Object> symbolTable = new HashMap<>();
  private final List<Object> executionHistory = new ArrayList<>();
  private int checkpointCounter = 0;

  // AST Node classes
  abstract static class Node {
    abstract Object evaluate();
  }

  static class DeadlineNode extends Node {
    private final int deadlineMs;
    private final Node body;

    DeadlineNode(int deadlineMs, Node body) {
      this.deadlineMs = deadlineMs;
      this.body = body;
    }

    @Override
    Object evaluate() {
      long startTime = System.currentTimeMillis();
      Object result = body.evaluate();
      long endTime = System.currentTimeMillis();

      if (endTime - startTime > deadlineMs) {
        throw new RuntimeException("Deadline exceeded: " + deadlineMs + "ms");
      }
      return result;
    }
  }

  static class BinaryNode extends Node {
    private final Node left;
    private final String operator;
    private final Node right;

    BinaryNode(Node left, String operator, Node right) {
      this.left = left;
      this.operator = operator;
      this.right = right;
    }

    @Override
    Object evaluate() {
      Object leftVal = left.evaluate();
      Object rightVal = right.evaluate();

      switch (operator) {
        case "+":
          return (Integer) leftVal + (Integer) rightVal;
        case "-":
          return (Integer) leftVal - (Integer) rightVal;
        case "*":
          return (Integer) leftVal * (Integer) rightVal;
        case "/":
          return (Integer) leftVal / (Integer) rightVal;
        default:
          throw new RuntimeException("Unknown operator: " + operator);
      }
    }
  }

  public Parser(List<String> tokens) {
    this.tokens = tokens;
  }

  private String peek() {
    if (current >= tokens.size())
      return null;
    return tokens.get(current);
  }

  private String advance() {
    if (current >= tokens.size())
      return null;
    return tokens.get(current++);
  }

  private boolean match(String type) {
    if (peek() == null)
      return false;
    if (peek().startsWith(type)) {
      advance();
      return true;
    }
    return false;
  }

  private Node parseDeadline() {
    // Expect @Deadline(ms=X)
    if (!match("SYMBOL:@")) {
      throw new RuntimeException("Expected @ symbol for deadline annotation");
    }
    if (!match("IDENTIFIER:Deadline")) {
      throw new RuntimeException("Expected Deadline identifier");
    }
    if (!match("SYMBOL:(")) {
      throw new RuntimeException("Expected ( after Deadline");
    }
    if (!match("IDENTIFIER:ms")) {
      throw new RuntimeException("Expected ms parameter");
    }
    if (!match("OPERATOR:=")) {
      throw new RuntimeException("Expected = after ms");
    }

    String number = advance();
    if (!number.startsWith("NUMBER:")) {
      throw new RuntimeException("Expected number after =");
    }
    int deadlineMs = Integer.parseInt(number.substring(7));

    if (!match("SYMBOL:)")) {
      throw new RuntimeException("Expected ) after deadline value");
    }

    Node body = parseExpression();
    return new DeadlineNode(deadlineMs, body);
  }

  private Node parseExpression() {
    Node left = parseTerm();

    while (match("OPERATOR:+") || match("OPERATOR:-")) {
      String operator = tokens.get(current - 1).substring(9);
      Node right = parseTerm();
      left = new BinaryNode(left, operator, right);
    }

    return left;
  }

  private Node parseTerm() {
    Node left = parseFactor();

    while (match("OPERATOR:*") || match("OPERATOR:/")) {
      String operator = tokens.get(current - 1).substring(9);
      Node right = parseFactor();
      left = new BinaryNode(left, operator, right);
    }

    return left;
  }

  private Node parseFactor() {
    if (match("NUMBER:")) {
      String number = tokens.get(current - 1).substring(7);
      return new NumberNode(Integer.parseInt(number));
    }

    if (match("IDENTIFIER:")) {
      String identifier = tokens.get(current - 1).substring(11);
      return new VariableNode(identifier);
    }

    if (match("SYMBOL:(")) {
      Node expr = parseExpression();
      if (!match("SYMBOL:)")) {
        throw new RuntimeException("Expected ')' after expression");
      }
      return expr;
    }

    throw new RuntimeException("Unexpected token: " + peek());
  }

  // Time-travel debugging support
  public void checkpoint() {
    executionHistory.add(symbolTable.clone());
    checkpointCounter++;
  }

  public void stepBack() {
    if (checkpointCounter > 0) {
      checkpointCounter--;
      symbolTable.clear();
      symbolTable.putAll((Map<String, Object>) executionHistory.get(checkpointCounter));
    }
  }

  public void replay(int steps) {
    if (checkpointCounter + steps < executionHistory.size()) {
      checkpointCounter += steps;
      symbolTable.clear();
      symbolTable.putAll((Map<String, Object>) executionHistory.get(checkpointCounter));
    }
  }

  static class NumberNode extends Node {
    private final int value;

    NumberNode(int value) {
      this.value = value;
    }

    @Override
    Object evaluate() {
      return value;
    }
  }

  static class VariableNode extends Node {
    private final String name;

    VariableNode(String name) {
      this.name = name;
    }

    @Override
    Object evaluate() {
      return symbolTable.get(name);
    }
  }
}