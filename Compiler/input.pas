MP_IDENTIFIER MP_PROGRAM 0 0
MP_IDENTIFIER test1 0 8
MP_SYMBOLS MP_SCOLON 0 13
MP_IDENTIFIER MP_VAR 1 0
MP_IDENTIFIER guy1 1 4
MP_SYMBOLS MP_COMMA 1 8
MP_IDENTIFIER guy2 1 10
MP_SYMBOLS MP_COLON 1 14
MP_IDENTIFIER MP_INTEGER 1 16
MP_SYMBOLS MP_SCOLON 1 23
MP_IDENTIFIER MP_BEGIN 3 0
MP_IDENTIFIER MP_WRITE 6 0
MP_SYMBOLS MP_LPAREN 6 5
MP_IDENTIFIER guy1 6 6
MP_SYMBOLS MP_RPAREN 6 10
MP_SYMBOLS MP_SCOLON 6 11
MP_IDENTIFIER MP_READ 9 0
MP_SYMBOLS MP_LPAREN 9 4
MP_IDENTIFIER guy2 9 5
MP_SYMBOLS MP_RPAREN 9 9
MP_SYMBOLS MP_SCOLON 9 10
MP_IDENTIFIER guy1 13 0
MP_SYMBOLS MP_ASSIGN 13 5
MP_IDENTIFIER guy1 13 8
MP_SYMBOLS MP_PLUS 13 13
MP_IDENTIFIER guy2 13 15
MP_IDENTIFIER MP_DIV 13 20
MP_SYMBOLS MP_LPAREN 13 24
MP_IDENTIFIER guy1 13 25
MP_SYMBOLS MP_PLUS 13 30
MP_INTEGER_LIT 5 13 32
MP_SYMBOLS MP_RPAREN 13 33
MP_SYMBOLS MP_SCOLON 13 34
MP_IDENTIFIER MP_IF 16 0
MP_SYMBOLS MP_LPAREN 16 3
MP_IDENTIFIER guy1 16 4
MP_SYMBOLS MP_EQUAL 16 9
MP_IDENTIFIER guy2 16 11
MP_SYMBOLS MP_RPAREN 16 15
MP_IDENTIFIER MP_THEN 16 17
MP_IDENTIFIER MP_WRITE 17 0
MP_SYMBOLS MP_LPAREN 17 5
MP_STRING_LIT Two of them 17 6
MP_SYMBOLS MP_RPAREN 17 19
MP_IDENTIFIER MP_ELSE 18 0
MP_IDENTIFIER MP_WRITE 19 0
MP_SYMBOLS MP_LPAREN 19 5
MP_STRING_LIT Two of them 19 6
MP_SYMBOLS MP_RPAREN 19 19
MP_SYMBOLS MP_SCOLON 19 20
MP_IDENTIFIER MP_IF 22 0
MP_SYMBOLS MP_LPAREN 22 3
MP_IDENTIFIER guy1 22 4
MP_SYMBOLS MP_EQUAL 22 9
MP_IDENTIFIER guy2 22 11
MP_SYMBOLS MP_RPAREN 22 15
MP_IDENTIFIER MP_THEN 22 17
MP_IDENTIFIER MP_BEGIN 23 0
MP_IDENTIFIER MP_WRITE 24 2
MP_SYMBOLS MP_LPAREN 24 7
MP_STRING_LIT Hello 24 8
MP_SYMBOLS MP_RPAREN 24 15
MP_SYMBOLS MP_SCOLON 24 16
MP_IDENTIFIER MP_WRITE 25 2
MP_SYMBOLS MP_LPAREN 25 7
MP_STRING_LIT Hello 25 8
MP_SYMBOLS MP_RPAREN 25 15
MP_IDENTIFIER MP_END 26 0
MP_SYMBOLS MP_SCOLON 26 3
MP_IDENTIFIER MP_IF 29 0
MP_SYMBOLS MP_LPAREN 29 3
MP_IDENTIFIER guy1 29 4
MP_SYMBOLS MP_EQUAL 29 9
MP_IDENTIFIER guy2 29 11
MP_SYMBOLS MP_RPAREN 29 15
MP_IDENTIFIER MP_THEN 29 17
MP_IDENTIFIER MP_BEGIN 30 0
MP_IDENTIFIER MP_WRITE 31 2
MP_SYMBOLS MP_LPAREN 31 7
MP_STRING_LIT Hello 31 8
MP_SYMBOLS MP_RPAREN 31 15
MP_SYMBOLS MP_SCOLON 31 16
MP_IDENTIFIER MP_WRITE 32 2
MP_SYMBOLS MP_LPAREN 32 7
MP_STRING_LIT Hello 32 8
MP_SYMBOLS MP_RPAREN 32 15
MP_IDENTIFIER MP_END 33 0
MP_IDENTIFIER MP_ELSE 34 0
MP_IDENTIFIER MP_WRITE 35 0
MP_SYMBOLS MP_LPAREN 35 5
MP_STRING_LIT Two of them 35 6
MP_SYMBOLS MP_RPAREN 35 19
MP_SYMBOLS MP_SCOLON 35 20
MP_IDENTIFIER MP_END 39 0
MP_SYMBOLS MP_PERIOD 39 3
PUSH D0
MOV SP D0
ADD SP #2 SP
PUSH 1(D0)
WRTS 
RD 0(D0)
PUSH 1(D0)
PUSH 0(D0)
PUSH 1(D0)
PUSH #5
ADDS
DIVS
ADDS
POP 1(D0)
PUSH 1(D0)
PUSH 0(D0)
CMPEQS
BRFS L0
WRT 
#"Two of them"

BR L1
L0:
WRT 
#"Two of them"

L1:
PUSH 1(D0)
PUSH 0(D0)
CMPEQS
BRFS L2
WRT 
#"Hello"

WRT 
#"Hello"

BR L3
L2:
L3:
PUSH 1(D0)
PUSH 0(D0)
CMPEQS
BRFS L4
WRT 
#"Hello"

WRT 
#"Hello"

BR L5
L4:
WRT 
#"Two of them"

L5:
HLT