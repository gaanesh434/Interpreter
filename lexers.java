import java.util.*;

public class lexers {
  private final String source;
  private int position = 0;
  private char currentChar;
  private static final Set<String> KEYWORDS = new HashSet<>(Arrays.asList(
      "int", "class", "if", "while", "public", "private", "void", "return", "static"));

  public lexers(String source) {
    this.source = preprocessUnicodeEscapes(source);
    this.currentChar = this.source.isEmpty() ? '\0' : this.source.charAt(position);
  }

  public String preprocessUnicodeEscapes(String input) {
    int i = 0;
    StringBuilder sb = new StringBuilder();
    while (i < input.length()) {
      char ch = input.charAt(i);

      if (ch == '\\' && i + 1 < input.length() && input.charAt(i + 1) == 'u') {
        i += 2;
        int codePoint = 0;
        for (int j = 0; j < 4; j++) {
          if (i + j >= input.length())
            break;
          codePoint = codePoint * 16 + Character.digit(input.charAt(i + j), 16);
        }
        sb.append((char) codePoint);
        i += 4;
      } else {
        sb.append(ch);
        i++;
      }
    }
    return sb.toString(); //
  }
}
