import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.management.RuntimeErrorException;

public class lexers {
  private final String source;
  private int position = 0;
  private char currentChar;

  private static final Set<String> KEYWORDS = new HashSet<>(Arrays.asList(
      "abstract", "assert", "boolean", "break", "byte", "case", "catch",
      "char", "class", "const", "continue", "default", "do", "double",
      "else", "enum", "extends", "final", "finally", "float", "for",
      "goto", "if", "implements", "import", "instanceof", "int",
      "interface", "long", "native", "new", "package", "private",
      "protected", "public", "return", "short", "static", "strictfp",
      "super", "switch", "synchronized", "this", "throw", "throws",
      "transient", "try", "void", "volatile", "while",
      "module", "open", "requires", "exports", "opens", "uses", "provides",
      "var", "record", "yield", "sealed", "permits", "non-sealed"));

  public lexers(String source) {
    this.source = preprocessUnicodeEscapes(source);
    this.currentChar = this.source.isEmpty() ? '\0' : this.source.charAt(position);
  }

  private String preprocessUnicodeEscapes(String input) {
    StringBuilder sb = new StringBuilder();
    int i = 0;
    while (i < input.length()) {
      char c = input.charAt(i);
      if (c == '\\' && i + 1 < input.length() && input.charAt(i + 1) == 'u') {
        i += 2;
        int codePoint = 0;
        for (int j = 0; j < 4; j++) {
          codePoint = codePoint * 16 + Character.digit(input.charAt(i + j), 16);
        }
        sb.append((char) codePoint);
        i += 4;
      } else {
        sb.append(c);
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

<<<<<<< HEAD
  // white space or commments ka handling......
  private void skipWhitespaceAndComments() {
    while (currentChar != '\0') {
      if (Character.isWhitespace(currentChar)) {
        advance();
      } else if (currentChar == '/') {
        if (peekNext(1) == '/') {
          while (currentChar != '\0' && currentChar != '\n')
            advance();
        } else if (peekNext(1) == '*') {
          advance();
          advance();
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
=======
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
>>>>>>> 4a4e6d9615094b0b1518543d3ea02c02a8e25201
        }
      } else {
        break;
      }
    }
<<<<<<< HEAD
  }

  private String readIdentifier() {
    StringBuilder id = new StringBuilder();
    while (Character.isUnicodeIdentifierPart(currentChar) ||
        (id.length() == 0 && Character.isUnicodeIdentifierStart(currentChar))) {
=======

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
>>>>>>> 4a4e6d9615094b0b1518543d3ea02c02a8e25201
      id.append(currentChar);
      advance();
    }
    return id.toString();
  }

<<<<<<< HEAD
  // hex ka handling yaha pe.........
  private String readNumber() {
    StringBuilder num = new StringBuilder();
    boolean isHex = false, isBinary = false;

    if (currentChar == '0') {
      if (peekNext(1) == 'x' || peekNext(1) == 'X') {
        isHex = true;
        num.append(currentChar);
        advance();
        num.append(currentChar);
        advance();
      } else if (peekNext(1) == 'b' || peekNext(1) == 'B') {
        isBinary = true;
        num.append(currentChar);
        advance();
=======
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
    // yaha binary ka ek check or hex ka ek or check
    if (!isBinary && !isHex && currentChar == '.' && Character.isDigit(peekNext(1))) {
      num.append(currentChar);
      advance();
      while (Character.isDigit(currentChar)) {
>>>>>>> 4a4e6d9615094b0b1518543d3ea02c02a8e25201
        num.append(currentChar);
        advance();
      }
    }
<<<<<<< HEAD
    // yaha pe binary ka handling.............
    while (currentChar != '\0') {
      if (currentChar == '_') {
        advance();
        continue;
      }
      if (isHex && !Character.isDigit(currentChar) &&
          !(currentChar >= 'a' && currentChar <= 'f') &&
          !(currentChar >= 'A' && currentChar <= 'F'))
        break;
      if (isBinary && currentChar != '0' && currentChar != '1')
        break;
      if (!isHex && !isBinary && !Character.isDigit(currentChar))
        break;
      num.append(currentChar);
      advance();
=======
    if (!isBinary && (currentChar == 'e' || currentChar == 'E')) {
      num.append(currentChar);
      advance();
      if (currentChar == '+' || currentChar == '-') {
        num.append(currentChar);
        advance();
      }
>>>>>>> 4a4e6d9615094b0b1518543d3ea02c02a8e25201
    }
    return num.toString().replace("_", "");
  }

<<<<<<< HEAD
  // yeh space , new , quations within string ka scene...........
=======
>>>>>>> 4a4e6d9615094b0b1518543d3ea02c02a8e25201
  private String readString() {
    char quote = currentChar;
    advance();
    StringBuilder str = new StringBuilder();
<<<<<<< HEAD
=======

>>>>>>> 4a4e6d9615094b0b1518543d3ea02c02a8e25201
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
<<<<<<< HEAD
          default:
            str.append(currentChar);
=======
          case '\\':
            str.append('\\');
            break;
          default:
            str.append(currentChar);
            break;
>>>>>>> 4a4e6d9615094b0b1518543d3ea02c02a8e25201
        }
      } else {
        str.append(currentChar);
      }
      advance();
    }
<<<<<<< HEAD
    if (currentChar == quote)
      advance();
    else
      throw new RuntimeException("Unterminated string at position " + position);
=======

    if (currentChar == quote)
      advance();
    else
      throw new RuntimeException("Unterminated String at position " + position);

>>>>>>> 4a4e6d9615094b0b1518543d3ea02c02a8e25201
    return str.toString();
  }

  public List<String> tokenize() {
    List<String> tokens = new ArrayList<>();
    currentChar = source.isEmpty() ? '\0' : source.charAt(position);

    while (currentChar != '\0') {
      skipWhitespaceAndComments();

      if (Character.isUnicodeIdentifierStart(currentChar)) {
        String identifier = readIdentifier();
<<<<<<< HEAD
        tokens.add(KEYWORDS.contains(identifier) ? "KEYWORD:" + identifier : "IDENTIFIER:" + identifier);
      } else if (Character.isDigit(currentChar)) {
        tokens.add("NUMBER:" + readNumber());
      } else if (currentChar == '"' || currentChar == '\'') {
        tokens.add("STRING:" + readString());
      } else {

        char next = peekNext(1);
        if (currentChar == '=' && next == '=') {
          advance();
          advance();
          tokens.add("OPERATOR:==");
        } else if (currentChar == '!' && next == '=') {
          advance();
          advance();
          tokens.add("OPERATOR:!=");
        } else if (currentChar == '>' && next == '=') {
          advance();
          advance();
          tokens.add("OPERATOR:>=");
        } else if (currentChar == '<' && next == '=') {
          advance();
          advance();
          tokens.add("OPERATOR:<=");
        } else if (currentChar == '&' && next == '&') {
          advance();
          advance();
          tokens.add("OPERATOR:&&");
        } else if (currentChar == '|' && next == '|') {
          advance();
          advance();
          tokens.add("OPERATOR:||");
        } else if (currentChar == '+') {
          advance();
          tokens.add("OPERATOR:+");
        } else if (currentChar == '-') {
          advance();
          tokens.add("OPERATOR:-");
        } else if (currentChar == '*') {
          advance();
          tokens.add("OPERATOR:*");
        } else if (currentChar == '/') {
          advance();
          tokens.add("OPERATOR:/");
        } else if (currentChar == '%') {
          advance();
          tokens.add("OPERATOR:%");
        } else if (currentChar == '(') {
          advance();
          tokens.add("SYMBOL:(");
        } else if (currentChar == ')') {
          advance();
          tokens.add("SYMBOL:)");
        } else {
          throw new RuntimeException("Unexpected character: '" + currentChar + "' at position " + position);
        }
      }
    }
    return tokens;
  }

  // iska main method.....
  public static void main(String[] args) {
    lexers lexer = new lexers("int x = 0b1010 + 0x1AF; String msg = \"Hello\\nWorld\";");
    System.out.println(lexer.tokenize());
  }
}
=======

        if (KEYWORDS.contains(identifier)) {
          tokens.add("KEYWORD:" + identifier);
        } else {

        }
      }
    }
  }
}
>>>>>>> 4a4e6d9615094b0b1518543d3ea02c02a8e25201
