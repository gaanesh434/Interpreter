grammar EmbeddedJava;

// Parser Rules
program: statement* EOF;

statement
    : classDeclaration
    | methodDeclaration
    | variableDeclaration
    | expressionStatement
    | ifStatement
    | whileStatement
    | returnStatement
    ;

classDeclaration
    : 'class' IDENTIFIER ('extends' IDENTIFIER)? '{' classBody '}'
    ;

classBody
    : (fieldDeclaration | methodDeclaration)*
    ;

fieldDeclaration
    : type IDENTIFIER ('=' expression)? ';'
    ;

methodDeclaration
    : deadlineAnnotation? type IDENTIFIER '(' parameterList? ')' block
    ;

deadlineAnnotation
    : '@Deadline' '(' 'ms' '=' NUMBER ')'
    ;

parameterList
    : parameter (',' parameter)*
    ;

parameter
    : type IDENTIFIER
    ;

type
    : 'int'
    | 'boolean'
    | 'String'
    | IDENTIFIER  // For class types
    ;

variableDeclaration
    : type IDENTIFIER ('=' expression)? ';'
    ;

expressionStatement
    : expression ';'
    ;

ifStatement
    : 'if' '(' expression ')' block ('else' block)?
    ;

whileStatement
    : 'while' '(' expression ')' block
    ;

returnStatement
    : 'return' expression? ';'
    ;

block
    : '{' statement* '}'
    ;

expression
    : primary
    | expression '.' IDENTIFIER
    | expression '.' IDENTIFIER '(' argumentList? ')'
    | 'new' IDENTIFIER '(' argumentList? ')'
    | expression ('*' | '/') expression
    | expression ('+' | '-') expression
    | expression ('<' | '>' | '<=' | '>=' | '==' | '!=') expression
    | expression '&&' expression
    | expression '||' expression
    | '!' expression
    ;

primary
    : NUMBER
    | STRING
    | 'true'
    | 'false'
    | 'null'
    | 'this'
    | IDENTIFIER
    | '(' expression ')'
    ;

argumentList
    : expression (',' expression)*
    ;

// Lexer Rules
IDENTIFIER: [a-zA-Z_][a-zA-Z0-9_]*;
NUMBER: [0-9]+;
STRING: '"' (~["\\\r\n] | EscapeSequence)* '"';

fragment EscapeSequence
    : '\\' [btnfr"'\\]
    | '\\' ([0-3]? [0-7])? [0-7]
    | '\\' 'u'+ HexDigit HexDigit HexDigit HexDigit
    ;

fragment HexDigit: [0-9a-fA-F];

WS: [ \t\r\n\u000C]+ -> skip;
COMMENT: '/*' .*? '*/' -> skip;
LINE_COMMENT: '//' ~[\r\n]* -> skip; 