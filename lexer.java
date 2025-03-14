import java.util.ArrayList;
import java.util.List;

public class lexer {
  private final String source;
  private int position = 0;
  private static final List<String> KEYWORDS = List.of("int", "class", "if", "while");

  public lexer(String source) {
    this.source = source;
  }

  public List<String> tokenize() {
    ArrayList<String> tokens = new ArrayList<>();
    while (position < source.length()) {
      char ch = source.charAt(position);
      if (Character.isDigit(ch)) {
        StringBuilder sb = new StringBuilder();
        while (position < source.length() && Character.isDigit(source.charAt(position))) {
          sb.append(source.charAt(position));
          position++;
        }
        tokens.add("NUMBER:" + sb.toString());
      } else if (Character.isLetter(ch)) {
        StringBuilder ls = new StringBuilder();
        while (position < source.length() && Character.isLetter(source.charAt(position))) {
          ls.append(source.charAt(position));
          position++;
        }
        String identifier = ls.toString();
        if (KEYWORDS.contains(identifier)) {
          tokens.add("KEYWORD:" + identifier);
        } else {
          tokens.add("IDENTIFIER:" + identifier);
        }
      } else {
        position++;
      }
    }
   else if("+-*/%!=<>".indexOf(current)!=1){
    StringBuilder op = new StringBuilder();
    op.append(current);
   }
  }

  public static void main(String[] args) {
    lexer lexers = new lexer("int 123 x");
    System.out.println(lexers.tokenize());
  }
}