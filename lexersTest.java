import org.junit.jupiter.api.Test; 
import static org.junit.jupiter.api.Assertions.assertEquals; 
import static org.junit.jupiter.api.Assertions.assertThrows; 

import java.util.Arrays;
import java.util.List;

class lexersTest {

    @Test
    void testEmptySource() {
        Lexers lexer = new Lexers("");
        assertEquals(List.of(), lexer.tokenize());
    }

    @Test
    void testWhitespaceOnly() {
        Lexers lexer = new Lexers("   \t\n  \r  ");
        assertEquals(List.of(), lexer.tokenize());
    }

    @Test
    void testSingleLineComment() {
        Lexers lexer = new Lexers("// This is a comment\n");
        assertEquals(List.of(), lexer.tokenize());
    }

    @Test
    void testMultiLineComment() {
        Lexers lexer = new Lexers("/* This is a \n multi-line \n comment */");
        assertEquals(List.of(), lexer.tokenize());
    }

    @Test
    void testMixedCommentsAndWhitespace() {
        Lexers lexer = new Lexers("   // comment 1\n  /* comment 2 */  \t  // comment 3\n");
        assertEquals(List.of(), lexer.tokenize());
    }

    @Test
    void testKeywords() {
        Lexers lexer = new Lexers("int class public void");
        List<String> expected = Arrays.asList("KEYWORD:int", "KEYWORD:class", "KEYWORD:public", "KEYWORD:void");
        assertEquals(expected, lexer.tokenize());
    }

    @Test
    void testIdentifiers() {
        Lexers lexer = new Lexers("variableName myVar _anotherVar var123");
        List<String> expected = Arrays.asList("IDENTIFIER:variableName", "IDENTIFIER:myVar", "IDENTIFIER:_anotherVar",
                "IDENTIFIER:var123");
        assertEquals(expected, lexer.tokenize());
    }

    @Test
    void testDecimalNumbers() {
        Lexers lexer = new Lexers("123 4567 0 987654321");
        List<String> expected = Arrays.asList("NUMBER:123", "NUMBER:4567", "NUMBER:0", "NUMBER:987654321");
        assertEquals(expected, lexer.tokenize());
    }

    @Test
    void testHexNumbers() {
        Lexers lexer = new Lexers("0x1A 0XFF 0x0 0xABCDEF");
        List<String> expected = Arrays.asList("NUMBER:0x1A", "NUMBER:0XFF", "NUMBER:0x0", "NUMBER:0xABCDEF");
        assertEquals(expected, lexer.tokenize());
    }

    @Test
    void testBinaryNumbers() {
        Lexers lexer = new Lexers("0b1010 0B1111 0b0 0b100101");
        List<String> expected = Arrays.asList("NUMBER:0b1010", "NUMBER:0B1111", "NUMBER:0b0", "NUMBER:0b100101");
        assertEquals(expected, lexer.tokenize());
    }

    @Test
    void testUnderscoreInNumbers() {
        Lexers lexer = new Lexers("1_000_000 0x1_A_F 0b101_010");
        List<String> expected = Arrays.asList("NUMBER:1000000", "NUMBER:0x1AF", "NUMBER:0b101010");
        assertEquals(expected, lexer.tokenize());
    }

    @Test
    void testStringLiterals() {
        Lexers lexer = new Lexers("\"Hello\" \"World\\n\" \"\\\"Quoted\\\"\" ''");
        List<String> expected = Arrays.asList("STRING:Hello", "STRING:World\n", "STRING:\"Quoted\"", "STRING:");
        assertEquals(expected, lexer.tokenize());
    }

    @Test
    void testUnterminatedString() {
        Lexers lexer = new Lexers("\"Unterminated");
        assertThrows(RuntimeException.class, lexer::tokenize);
    }

    @Test
    void testOperators() {
        Lexers lexer = new Lexers("== != >= <= && || + - * / %");
        List<String> expected = Arrays.asList("OPERATOR:==", "OPERATOR:!=", "OPERATOR:>=", "OPERATOR:<=", "OPERATOR:&&",
                "OPERATOR:||", "OPERATOR:+", "OPERATOR:-", "OPERATOR:*", "OPERATOR:/", "OPERATOR:%");
        assertEquals(expected, lexer.tokenize());
    }

    @Test
    void testSymbols() {
        Lexers lexer = new Lexers("( )");
        List<String> expected = Arrays.asList("SYMBOL:(", "SYMBOL:)");
        assertEquals(expected, lexer.tokenize());
    }

    @Test
    void testUnicodeEscapeSequences() {
        Lexers lexer = new Lexers("\\u0041\\u0042\\u0043"); // ABC
        List<String> expected = Arrays.asList("IDENTIFIER:ABC");
        assertEquals(expected, lexer.tokenize());
    }

    @Test
    void testComplexExample() {
        Lexers lexer = new Lexers("int x = 0b1010 + 0x1AF; String msg = \"Hello\\nWorld\"; // comment\n if(x >= 10){}");
        List<String> expected = Arrays.asList(
                "KEYWORD:int", "IDENTIFIER:x", "OPERATOR:=", "NUMBER:0b1010", "OPERATOR:+", "NUMBER:0x1AF",
                "OPERATOR:;", "KEYWORD:String", "IDENTIFIER:msg", "OPERATOR:=", "STRING:Hello\nWorld", "OPERATOR:;",
                "KEYWORD:if", "SYMBOL:(", "IDENTIFIER:x", "OPERATOR:>=", "NUMBER:10", "SYMBOL:)", "SYMBOL:{",
                "SYMBOL:}");
        assertEquals(expected, lexer.tokenize());
    }

    @Test
    void testUnexpectedCharacter() {
        Lexers lexer = new Lexers("@");
        assertThrows(RuntimeException.class, lexer::tokenize);
    }

    @Test
    void testUnicodeEscapeInString() {
        Lexers lexer = new Lexers("\"\\u0048\\u0065\\u006C\\u006C\\u006F\"");
        List<String> expected = Arrays.asList("STRING:Hello");
        assertEquals(expected, lexer.tokenize());
    }

    @Test
    void testUnicodeEscapeInIdentifier() {
        Lexers lexer = new Lexers("\\u0061\\u0062\\u0063");
        List<String> expected = Arrays.asList("IDENTIFIER:abc");
        assertEquals(expected, lexer.tokenize());
    }

    @Test
    void testUnicodeEscapeInNumber() {
        Lexers lexer = new Lexers("0x\\u0031\\u0041");
        List<String> expected = Arrays.asList("NUMBER:0x1A");
        assertEquals(expected, lexer.tokenize());
    }

    @Test
    void testUnicodeEscapeInComment() {
        Lexers lexer = new Lexers("// \\u0048\\u0065\\u006C\\u006C\\u006F");
        List<String> expected = List.of();
        assertEquals(expected, lexer.tokenize());
    }

    @Test
    void testUnicodeEscapeInMultiLineComment() {
        Lexers lexer = new Lexers("/* \\u0048\\u0065\\u006C\\u006C\\u006F */");
        List<String> expected = List.of();
        assertEquals(expected, lexer.tokenize());
    }

    @Test
    void testUnicodeEscapeInMixedCode() {
        Lexers lexer = new Lexers("int \\u0078 = 10; // \\u0048\\u0065\\u006C\\u006C\\u006F");
        List<String> expected = Arrays.asList("KEYWORD:int", "IDENTIFIER:x", "OPERATOR:=", "NUMBER:10", "OPERATOR:;");
        assertEquals(expected, lexer.tokenize());
    }
}
