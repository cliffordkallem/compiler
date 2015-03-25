

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class ScannerPart
{

    static boolean endOfLine = false;
    static int colNum = 0;
    static int rowNum = 0;
    static char[] currentLine;
    static String state;
    static int lastState;
    static FileInputStream fstream;
    static String strLine;
    static DataInputStream in;
    static BufferedReader br;
    public static ArrayList<Token> tokenArray = new ArrayList<Token>();
    static String[] reservedWordsLexeme =
    {
        "and", "begin", "div", "do", "downto", "else", "end", "fixed", "float", "for", "function", "if", "integer", "mod", "not", "or", "procedure", "program", "read", "repeat", "then", "to", "until", "var", "while", "write", "writeln"
    };
    static String[] reservedWordsToken =
    {
        "MP_AND", "MP_BEGIN", "MP_DIV", "MP_DO", "MP_DOWNTO", "MP_ELSE", "MP_END", "MP_FIXED", "MP_FLOAT", "MP_FOR", "MP_FUNCTION", "MP_IF", "MP_INTEGER", "MP_MOD", "MP_NOT", "MP_OR", "MP_PROCEDURE", "MP_PROGRAM", "MP_READ", "MP_REPEAT", "MP_THEN", "MP_TO", "MP_UNTIL", "MP_VAR", "MP_WHILE", "MP_WRITE", "MP_WRITELN"
    };
    static String[] reservedSymbolsLexeme =
    {
        "(", ")", "*", "+", ",", "-", ".", "/", ":", ":=", ";", "<", "<=", "<>", "=", ">", ">="
    };
    static String[] reservedSymbolsToken =
    {
        "MP_LPAREN", "MP_RPAREN", "MP_TIMES", "MP_PLUS", "MP_COMMA", "MP_MINUS", "MP_PERIOD", "MP_DIVF", "MP_COLON", "MP_ASSIGN", "MP_SCOLON", "MP_LTHAN", "MP_LEQUAL", "MP_NEQUAL", "MP_EQUAL", "MP_GTHAN", "MP_GEQUAL"
    };

    public static void dispatcher()
    {
        try
        {
             String file = "input.txt";
//            char temp[] = file.toCharArray();
//            String newFile = String.valueOf(temp, 0, temp.length - 3);
            fstream = new FileInputStream(file);
            in = new DataInputStream(fstream);
            br = new BufferedReader(new InputStreamReader(in));
           // new FileOut(newFile+"pas");
            new FileOut("C:\\output.txt");
            while ((strLine = br.readLine()) != null)
            {
                while (strLine.isEmpty())
                {
                    strLine = br.readLine();
                    rowNum++;
                    colNum = 0;
                    endOfLine = false;
                }
                //System.out.println(strLine);
                currentLine = strLine.toCharArray();

                char charAt;
                String stringChar = "";

                charAt = currentLine[colNum];
                stringChar = Character.toString(charAt);


                while (stringChar.matches("[{'A-Za-z0-9(*+,-./:)=;<>\\t\\s]") && endOfLine == false)
                {
                    if (stringChar.matches("[A-Za-z]"))
                    {
                        identifier();
                    } else if (stringChar.matches("[0-9]"))
                    {
                        numeral();
                    } else if (stringChar.matches("[(*+,-./:)=;<>]"))
                    {
                        symbol();
                    } else if (stringChar.matches("[']"))
                    {
                        string_LIT();
                    } else if (stringChar.matches("[{]"))
                    {
                        comments();
                    } else if ((stringChar.matches("[\\s]") || stringChar.matches("[\\t]")))
                    {
                        while ((stringChar.matches("[\\s]") || stringChar.matches("[\\t]")))
                        {
                            //System.out.println("White Space / Tab Space");
                            colNum++;
                            if (colNum < currentLine.length)
                            {
                                charAt = currentLine[colNum];
                                stringChar = Character.toString(charAt);
                            } else
                            {
                                endOfLine = true;
                                break;
                            }

                        }
                    }


                    if (endOfLine == false)
                    {
                        charAt = currentLine[colNum];
                        stringChar = Character.toString(charAt);
                    }




                }

                //System.out.println("COL POINT: " + colNum);
                rowNum++;
                colNum = 0;
                endOfLine = false;
            }
        } catch (IOException e)
        {
        }
    }

    public static void symbol()
    {
        int curState = 0;
        int beginToken = colNum;
        char charAt;
        boolean passedAccept = false;
        String stringChar = "";

        while (curState >= 0)
        {
            if (colNum < currentLine.length)
            {
                charAt = currentLine[colNum];
                stringChar = Character.toString(charAt);
            } else
            {
                stringChar = "";
            }

            switch (curState)
            {
                case 0:
                    if (stringChar.matches("[.]"))
                    {
                        curState = 1;
                    } else if (stringChar.matches("[,]"))
                    {
                        curState = 2;
                    } else if (stringChar.matches("[;]"))
                    {
                        curState = 3;
                    } else if (stringChar.matches("[(]"))
                    {
                        curState = 4;
                    } else if (stringChar.matches("[)]"))
                    {
                        curState = 5;
                    } else if (stringChar.matches("[=]"))
                    {
                        curState = 6;
                    } else if (stringChar.matches("[>]"))
                    {
                        curState = 7;
                    } else if (stringChar.matches("[<]"))
                    {
                        curState = 9;
                    } else if (stringChar.matches("[:]"))
                    {
                        curState = 12;
                    } else if (stringChar.matches("[+]"))
                    {
                        curState = 14;
                    } else if (stringChar.matches("[-]"))
                    {
                        curState = 15;
                    } else if (stringChar.matches("[\\*]"))
                    {
                        curState = 16;
                    }
                    else if (stringChar.matches("[/]"))
                    {
                        curState = 17;
                    }
                    colNum++;
                    break;
                case 1:
                    curState = 1000;
                    break;
                case 2:
                    curState = 1000;
                    break;
                case 3:
                    curState = 1000;
                    break;
                case 4:
                    curState = 1000;
                    break;
                case 5:
                    curState = 1000;
                    break;
                case 6:
                    curState = 1000;
                    break;
                case 7:
                    if (stringChar.matches("[=]"))
                    {
                        curState = 8;
                        colNum++;
                    } else
                    {
                        curState = 1000;
                    }
                    break;
                case 8:
                    curState = 1000;
                    break;
                case 9:
                    if (stringChar.matches("[=]"))
                    {
                        curState = 10;
                        colNum++;
                    } else if (stringChar.matches("[>]"))
                    {
                        curState = 11;
                        colNum++;
                    } else
                    {
                        curState = 1000;
                    }
                    break;
                case 10:
                    curState = 1000;
                    break;
                case 11:
                    curState = 1000;
                    break;
                case 12:
                    if (stringChar.matches("[=]"))
                    {
                        curState = 13;
                        colNum++;
                    } else
                    {
                        curState = 1000;
                    }
                    break;
                case 13:
                    curState = 1000;
                    break;
                case 14:
                    curState = 1000;
                    break;
                case 15:
                    curState = 1000;
                    break;
                case 16:
                    curState = 1000;
                    break;
                case 17:
                    curState = 1000;
                    break;

                // FINISH    
                case 1000:
                    curState = -1000;
                    break;

            }

        }
        if (colNum < currentLine.length)
        {
            charAt = currentLine[colNum];
            stringChar = Character.toString(charAt);
        } else
        {
            stringChar = "EOL";
            endOfLine = true;
        }

        //System.out.println("Pointing At: " + stringChar + " colNum: " + colNum);
        if (curState == -1000)
        {
            String token = "";
            for (int i = beginToken; i < colNum; i++)
            {
                token = token + currentLine[i];
            }
            //System.out.println("Token: " + token);
            int foundIndex = Arrays.binarySearch(reservedSymbolsLexeme, token);
            //System.out.println(foundIndex);
            if (foundIndex >= 0)
            {
                Token newToken = new Token("MP_SYMBOLS", reservedSymbolsToken[foundIndex], rowNum, (beginToken));
                tokenArray.add(newToken);
               // FileOut.writerln("MP_SYMBOLS", reservedSymbolsToken[foundIndex], rowNum, (beginToken));
            } else
            {
                Token newToken = new Token("MP_SYMBOLS", token, rowNum, (beginToken));
                tokenArray.add(newToken);
               // FileOut.writerln("MP_SYMBOLS", token, rowNum, (beginToken));
            }

        } else
        {
            System.out.println("Scanner Error");
        }
    }

    public static void identifier()
    {
        int curState = 0;
        int beginToken = colNum;
        char charAt;
        boolean passedAccept = false;
        String stringChar = "";

        while (curState >= 0)
        {
            if (colNum < currentLine.length)
            {
                charAt = currentLine[colNum];
                stringChar = Character.toString(charAt);
            } else
            {
                stringChar = "";
            }


            switch (curState)
            {
                case 0:
                    if (stringChar.matches("[A-Za-z]"))
                    {
                        curState = 1;
                        colNum++;
                    } else if (stringChar.matches("[_]"))
                    {
                        curState = 2;
                        colNum++;
                    } else
                    {
                        curState = 100;
                    }
                    break;
                case 1:
                    if (stringChar.matches("[A-Za-z0-9]"))
                    {
                        curState = 1;
                        passedAccept = true;
                        colNum++;
                    } else if (stringChar.matches("[_]"))
                    {
                        curState = 2;
                        passedAccept = true;
                        colNum++;
                    } else
                    {
                        curState = 1000;
                    }
                    break;
                case 2:
                    if (stringChar.matches("[A-Za-z0-9]"))
                    {
                        curState = 1;
                        colNum++;
                    } else
                    {
                        curState = 102;
                    }
                    break;

                // FINISH    
                case 1000:
                    curState = -1000;
                    break;
                // ERROR CASES
                case 100: // colNum is pointing at giberish.
                    //System.out.println("Found Giberish At Begining");
                    curState = -1;
                    break;
                case 102: // Did not find a Letter or a Digit following an underscore.
                    if (passedAccept == true)
                    {
                        curState = -1000;
                        colNum--;
                    } else
                    {
                        //System.out.println("Found: " + stringChar + " Expected: Letter/Digit");
                        curState = -1;
                    }
                    break;


            }// End Switch
        }//End While
        if (colNum < currentLine.length)
        {
            charAt = currentLine[colNum];
            stringChar = Character.toString(charAt);
        } else
        {
            stringChar = "EOL";
            endOfLine = true;
        }

        //System.out.println("Pointing At: " + stringChar + " colNum: " + colNum);
        if (curState == -1000)
        {
            String token = "";
            for (int i = beginToken; i < colNum; i++)
            {
                token = token + currentLine[i];
            }
            //System.out.println("Token: " + token);

            int foundIndex = Arrays.binarySearch(reservedWordsLexeme, token.toLowerCase());
            if (foundIndex >= 0)
            {
                Token newToken = new Token(reservedWordsToken[foundIndex], token, rowNum, (beginToken));
                tokenArray.add(newToken);
              //  FileOut.writerln("MP_IDENTIFIER", reservedWordsToken[foundIndex], rowNum, (beginToken));
            } else
            {
                Token newToken = new Token("MP_IDENTIFIER", token, rowNum, (beginToken));
                tokenArray.add(newToken);
               // FileOut.writerln("MP_IDENTIFIER", token, rowNum, (beginToken));
            }

        } else
        {
            System.out.println("Scanner Error");
        }
    }

    public static void comments()
    {
        int curState = 0;
        int beginToken = colNum;
        char charAt;
        boolean passedAccept = false;
        String stringChar = "";
        String comment = "";

        if (colNum < currentLine.length - 1)
        {
            colNum++;
            charAt = currentLine[colNum];
            stringChar = Character.toString(charAt);
            comment = comment + stringChar;
        } else
        {
            colNum = 0;
            rowNum++;
            try
            {
                strLine = br.readLine();
                currentLine = strLine.toCharArray();

            } catch (Exception e)
            {
            }
        }
        
        while (strLine.isEmpty())
                {
                    try {
                    strLine = br.readLine();
                    } catch (Exception e){}
                    rowNum++;
                    colNum = 0;
                    endOfLine = false;
                }


        while (!stringChar.matches("[}]"))
        {

            if (colNum < currentLine.length - 1)
            {
                colNum++;
                charAt = currentLine[colNum];
                stringChar = Character.toString(charAt);
                if (!stringChar.matches("[}]"))
                {
                    comment = comment + stringChar;
                }
            } else
            {
                colNum = 0;
                rowNum++;

                try
                {
                    strLine = br.readLine();
                    currentLine = strLine.toCharArray();
                    charAt = currentLine[colNum];
                    stringChar = Character.toString(charAt);
                    if (!stringChar.matches("[}]"))
                    {
                        comment = comment + stringChar;
                    }

                } catch (Exception e)
                {
                }
            }


        }
        if (colNum < currentLine.length - 1)
        {
            colNum++;
        } else
        {
            endOfLine = true;
        }
        //FileOut.writerln("MP_COMMENT", comment, rowNum, (beginToken));

    }

    public static void numeral()
    {
        int start = colNum;
        String number = null;
        int i;

        boolean enteredFixed = false;
        boolean enteredending = false;
        boolean enteredFloat = false;
        for (i = colNum; i + 1 <= currentLine.length && isInt(i); i++)
        {
            if (number == null)
            {
                number = currentLine[colNum] + "";
            } else
            {
                number = number + currentLine[colNum];
            }
            colNum++;
        }
        if (i == currentLine.length || (currentLine[colNum] != '.' && currentLine[colNum] != 'e' && currentLine[colNum] != 'E'))
        {
            Token newToken = new Token("MP_INTEGER_LIT", number, rowNum, start);
            tokenArray.add(newToken);
            //FileOut.writerln("MP_INTEGER_LIT", number, rowNum, start);
            if (colNum >= currentLine.length)
            {
                endOfLine = true;
            }
            return;
        }
        if (currentLine[colNum] == '.')
        {
            ++i;
            number = number + ".";
            colNum++;
        }
        while (i < currentLine.length && isInt(i))
        {
            enteredFixed = true;
            //System.out.println(number);
            number = number + currentLine[i];
            colNum++;
            i++;

        }
        if (enteredFixed == false && !(i <= currentLine.length - 2 && ((currentLine[colNum] == 'e' || currentLine[colNum] == 'E') && (currentLine[colNum + 1] == '+' || currentLine[colNum + 1] == '-'))))
        {
            colNum = colNum - 1;
            char temp[] = number.toCharArray();
            number = String.valueOf(temp, 0, temp.length - 1);
            Token newToken = new Token("MP_INTEGER_LIT", number, rowNum, start);
            tokenArray.add(newToken);
           // FileOut.writerln("MP_INTEGER_LIT", number, rowNum, start);
            if (colNum >= currentLine.length)
            {
                endOfLine = true;
            }
            return;
        }
        if (i == currentLine.length || (currentLine[colNum] != 'e' && currentLine[colNum] != 'E'))
        {
            Token newToken = new Token("MP_FIXED_LIT", number, rowNum, start);
            tokenArray.add(newToken);
          //  FileOut.writerln("MP_FIXED_LIT", number, rowNum, start);
            if (colNum >= currentLine.length)
            {
                endOfLine = true;
            }
            return;
        }
        if (i <= currentLine.length - 2 && ((currentLine[colNum] == 'e' || currentLine[colNum] == 'E') && (currentLine[colNum + 1] == '+' || currentLine[colNum + 1] == '-')))
        {
            //System.out.println("here");
            enteredending = true;
            number = number + currentLine[colNum] + currentLine[colNum + 1];
            i = i + 2;
            colNum = 2 + colNum;
        }
        while (i + 1 <= currentLine.length && isInt(i))
        {
            enteredFloat = true;
            number = number + currentLine[i];
            i++;
            colNum++;
        }
        if (enteredending == false)
        {
            Token newToken = new Token("MP_FIXED_LIT", number, rowNum, start);
            tokenArray.add(newToken);
            //FileOut.writerln("MP_FIXED_LIT", number, rowNum, start);
            if (colNum >= currentLine.length)
            {
                endOfLine = true;
            }
            return;
        }
        if (enteredFloat == false)
        {
            colNum = colNum - 2;
            char temp[] = number.toCharArray();
            number = String.valueOf(temp, 0, temp.length - 2);
            Token newToken = new Token("MP_FIXED_LIT", number, rowNum, start);
            tokenArray.add(newToken);
            //FileOut.writerln("MP_FIXED_LIT", number, rowNum, start);
            if (colNum >= currentLine.length)
            {
                endOfLine = true;
            }
            return;
        }
        if (enteredFloat == true)
        {
            Token newToken = new Token("MP_FIXED_LIT", number, rowNum, start);
            tokenArray.add(newToken);
           // FileOut.writerln("MP_FLOAT_LIT", number, rowNum, start);
            if (colNum >= currentLine.length)
            {
                endOfLine = true;
            }
            return;
        }

    }

    private static boolean isInt(int num)
    {

        try
        {
            Integer.parseInt(currentLine[num] + "");
            return true;
        } catch (NumberFormatException e)
        {
            return false;
        }
    }

    public static void string_LIT()
    {
        int start = colNum;
        String string = null;
        int i;
        for (i = colNum + 1; i + 1 <= currentLine.length && !((currentLine[i] + "").equals("'")); i++)
        {
            if (string == null)
            {
                string = currentLine[i] + "";
            } else
            {
                string = string + currentLine[i];
            }
        }
        if (i + 2 <= currentLine.length && (currentLine[i] + "" + currentLine[i + 1]).equals("''"))
        {
            string = string + "'";
            i = i + 2;
            while (i + 1 <= currentLine.length && !((currentLine[i] + "").equals("'")))
            {
                if (string == null)
                {
                    string = currentLine[i] + "";
                } else
                {
                    string = string + currentLine[i];
                    i++;
                }
            }
        }
        if (i + 1 <= currentLine.length && (currentLine[i] + "").equals("'"))
        {
            colNum = i + 1;
            Token newToken = new Token("MP_STRING_LIT", string, rowNum, start);
            tokenArray.add(newToken);
           // FileOut.writerln("MP_STRING_LIT", string, rowNum, start);
            if (colNum >= currentLine.length)
            {
                endOfLine = true;
            }
            return;
        } else
        {
            colNum = start;
        }
    }
}
