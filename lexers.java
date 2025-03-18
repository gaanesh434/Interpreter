import java.util.*;

import javax.management.RuntimeErrorException;

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
    return sb.toString();
  }

  private void advance() {
    position++;
    currentChar = position < source.length() ? source.charAt(position) : '\0';
  }

  private char peekNext(int offset) {
    int pos = position + offset;
    return pos < source.length() ? source.charAt(pos) : '\0';
  }

  private void skipWhitespaceAndComments() { // Mathod for processing com
    while (currentChar != '\0') {
      if (Character.isWhitespace(currentChar)) { // Empty space - skip
        advance();
      } else if (currentChar == '/') {
        char next = peekNext(1);
        if (next == '/') {
          while (currentChar != '\0' && currentChar != '\n')
            advance();
        } else if (next == '*') {
          advance(); // single and multiline comments
          advance();
        }
        while (currentChar != '\0') {
          if (currentChar == '*' && peekNext(1) == '/') {
            advance();
            advance();
            break;
          }
          advance();
        }
      } else {
        break;
      }
    }

  }

  // Check if the are a part of unicoode characters

  // Methods to use are
  // 1) .isUnicodeIdentifierPart()
  // 2) .isUnicodeIdentifierStart()

  // yeh 2 yaad Rakho
  private String readIdentifier() {
    StringBuilder id = new StringBuilder();
    while (Character.isUnicodeIdentifierPart(currentChar)
        || (id.length() == 0 && Character.isUnicodeIdentifierStart(currentChar))) {
      id.append(currentChar);
      advance();
    }
    return id.toString();
  }

  // Handling the hexamdecimal or the binary
  private String readNumber() {
    boolean isHex = false;
    boolean isBinary = false;

    StringBuilder num = new StringBuilder();

    if (currentChar == '0' && peekNext(1) == 'x' || peekNext(1) == 'X') {
      isHex = true;
      num.append(currentChar);
      advance();
      num.append(currentChar);
      advance();
    } else if (currentChar == '0' && peekNext(1) == 'b' || peekNext(1) == 'B') {
      isBinary = true;
      num.append(currentChar);
      advance();
      num.append(currentChar);
      advance();
    }
    while (currentChar != '\0' &&
        (Character.isDigit(currentChar) ||
            currentChar == '_' ||
            (isHex && Character.isLetter(currentChar)) ||
            (isBinary && (currentChar == '0' || currentChar == '1')))) {
      if (currentChar != '_')
        num.append(currentChar);
      advance();

    }
    if (!isBinary && !isHex && currentChar == '.' && Character.isDigit(peekNext(1))) {
      num.append(currentChar);
      advance();
      while (Character.isDigit(currentChar)) {
        num.append(currentChar);
        advance();
      }
    }
    if (!isBinary && (currentChar == 'e' || currentChar == 'E')) {
      num.append(currentChar);
      advance();
      if (currentChar == '+' || currentChar == '-') {
        num.append(currentChar);
        advance();
      }
    }
    return num.toString().replace("_", "");
  }

  private String readString() {
    char quote = currentChar;
    advance();
    StringBuilder str = new StringBuilder();

    while (currentChar != '\0' && currentChar != quote) {
      if (currentChar == '\\') {
        advance();
        switch (currentChar) {
          case 'n':
            str.append('\n');
            break;
          case 't':
            str.append('\t');
            break;
          case '"':
            str.append('\"');
            break;
          case '\\':
            str.append('\\');
            break;
          default:
            str.append(currentChar);
            break;
        }
      } else {
        str.append(currentChar);
      }
      advance();
    }

    if (currentChar == quote)
      advance();
    else
      throw new RuntimeException("Unterminated String at position " + position);

    return str.toString();
  }

  public List<String> tokenize() {
    List<String> tokens = new ArrayList<>();
    currentChar = source.isEmpty() ? '\0' : source.charAt(position);

    while (currentChar != '\0') {
      skipWhitespaceAndComments();

      if (Character.isUnicodeIdentifierStart(currentChar)) {
        String identifier = readIdentifier();

        if (KEYWORDS.contains(identifier)) {
          tokens.add("KEYWORD:" + identifier);
        } else {

        }
      }
    }
  }
}