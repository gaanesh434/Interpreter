import edu.berkeley.cs.jqf.fuzz.Fuzz;
import edu.berkeley.cs.jqf.fuzz.JQF;
import org.junit.runner.RunWith;
import java.util.Random;

@RunWith(JQF.class)
public class ParserFuzzer {
  private final Random random = new Random();
  private final Parser parser;
  private final Lexers lexer;

  public ParserFuzzer() {
    this.lexer = new Lexers("");
    this.parser = new Parser(new ArrayList<>());
  }

  @Fuzz
  public void testRandomExpressions(String input) {
    try {
      List<String> tokens = lexer.tokenize();
      parser.parse(tokens);
    } catch (Exception e) {
      // Expected exceptions for invalid inputs
      if (!(e instanceof RuntimeException)) {
        throw e;
      }
    }
  }

  @Fuzz
  public void testDeadlineAnnotations(String input) {
    try {
      // Generate random deadline values
      int deadlineMs = random.nextInt(1000);
      String deadlineInput = "@Deadline(ms=" + deadlineMs + ") " + input;

      List<String> tokens = lexer.tokenize();
      parser.parse(tokens);
    } catch (Exception e) {

      if (!(e instanceof RuntimeException)) {
        throw e;
      }
    }
  }

  @Fuzz
  public void testClassDeclarations(String input) {
    try {
      // Generate random class declarations
      String classInput = "class TestClass { " + input + " }";

      List<String> tokens = lexer.tokenize();
      parser.parse(tokens);
    } catch (Exception e) {

      if (!(e instanceof RuntimeException)) {
        throw e;
      }
    }
  }

  @Fuzz
  public void testMethodDeclarations(String input) {
    try {

      int deadlineMs = random.nextInt(1000);
      String methodInput = "@Deadline(ms=" + deadlineMs + ") void testMethod() { " + input + " }";

      List<String> tokens = lexer.tokenize();
      parser.parse(tokens);
    } catch (Exception e) {

      if (!(e instanceof RuntimeException)) {
        throw e;
      }
    }
  }

  @Fuzz
  public void testEmbeddedLoops(String input) {
    try {
      // Generate random loop structures
      String loopInput = "while(true) { " + input + " }";

      List<String> tokens = lexer.tokenize();
      parser.parse(tokens);
    } catch (Exception e) {

      if (!(e instanceof RuntimeException)) {
        throw e;
      }
    }
  }
}