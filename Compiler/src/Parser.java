
import java.util.ArrayList;

public class Parser
{

    private boolean found = false;
    private boolean foundnot = false;
    private boolean negative = false;
    //public static ArrayList<String> genCode = new ArrayList<String>();
    private boolean firstTime = true;
    static int num = 0;
    private int lastLoc = 0;
    static int parameterNum = 0;
    static String lastType = null;
    String offsetScope = "";
    static int currentLoc = 0;
    static Token currentToken;
    private boolean validProgram = true;
    private boolean enteredProcedure = false;
    private boolean enteredFunction = false;
    private boolean enteredReturn = false;
    private ArrayList<Token> tokenArray;
    public static ArrayList<procedure> procedure = new ArrayList<procedure>();

    public Parser(ArrayList<Token> in_tokenArray)
    {
        tokenArray = in_tokenArray;
        currentToken = tokenArray.get(currentLoc);
    }

    public boolean Parse()
    {
        SystemGoal();
        for (int i = procedure.size(); i > 0; i--)
        {
            //System.out.println(procedure.get(i-1).getLabel());
            procedure.get(i - 1).print();
        }
        return validProgram;
    }

    public void SystemGoal()
    {
        if (matchType("MP_PROGRAM"))
        {
            Program();
            FileOut.writerln("HLT");
        } else
        {
            validProgram = false;
        }
        //it worked
        //eof
    }

    public void Program()
    {
        if (matchType("MP_PROGRAM"))
        {
            ProgramHeading();
            if (match("MP_SCOLON"))
            {
                incToken();
                Block();
                if (match("MP_PERIOD"))
                {
                    //System.out.println("Found end in Program!!");
                } else
                {
                    validProgram = false;
                }
            } else
            {
                validProgram = false;
            }
        } else
        {
            validProgram = false;
        }
    }

    public void ProgramHeading()
    {
        if (matchType("MP_PROGRAM"))
        {
            incToken();
            ProgramIdentifier();

        } else
        {
            validProgram = false;
        }
    }

    public void Block()
    {
        if (matchType("MP_VAR"))
        {
            VariableDeclarationPart();
            FileOut.writerln("PUSH " + procedure.get(0).getLabel());       // #TAG
            FileOut.writerln("MOV SP " + procedure.get(0).getLabel());     // #TAG
            FileOut.writerln("ADD SP #" + procedure.get(0).getNodes().size() + " SP");   // #TAG
            ProcedureAndFunctionDeclarationPart();
            StatementPart();
        } else
        {
            validProgram = false;
            //System.out.println("Was not var in Block()");
        }
    }

    public void VariableDeclarationPart()
    {
        if (matchType("MP_VAR"))
        {
            incToken();
            VariableDeclaration();
            if (match("MP_SCOLON"))
            {
                incToken();
                VariableDeclarationTail();
            } else
            {
                validProgram = false;
                // System.out.println("Was not ; in VariableDeclarationPart()");
            }
        } else
        {
            validProgram = false;
            //System.out.println("Was not var in VariableDeclarationPart()");
        }
    }

    public void VariableDeclarationTail()
    {
        if (matchType("MP_IDENTIFIER"))
        {
            VariableDeclaration();
            if (match("MP_SCOLON"))
            {
                incToken();
                VariableDeclarationTail();
            } else
            {
                // System.out.println("Empty String found in VariableDeclarationTail()");
            }
        } else
        {
            // System.out.println("Empty String found in VariableDeclarationTail()");
        }
    }

    public void VariableDeclaration()
    {
        if (matchType("MP_IDENTIFIER"))
        {
            IdentifierList();
            if (match("MP_COLON"))
            {
                incToken();
                Type();
            } else
            {
                validProgram = false;
                // System.out.println("Did not find : in VariableDeclaration()");
            }
        } else
        {
            validProgram = false;
            //  System.out.println("Was not identifier in VariableDeclaration()");
        }
    }

    public void Type()
    {
        if (matchType("MP_INTEGER"))
        {
            lastType = "MP_INTEGER";
            incToken();
        } else if (matchType("MP_FLOAT"))
        {
            lastType = "MP_FLOAT";
            incToken();
        } else if (matchType("MP_BOOLEAN"))
        {
            lastType = "MP_BOOLEAN";
            incToken();
        } else if (matchType("MP_FLOAT_LIT"))
        {
            lastType = "MP_FLOAT_LIT";
            incToken();
        } else if (matchType("MP_FIXED_LIT"))
        {
            lastType = "MP_FIXED_LIT";

            incToken();
        } else if (matchType("MP_INTEGER_LIT"))
        {
            lastType = "MP_INTEGER_LIT";
            incToken();
        } else
        {
            validProgram = false;
            // System.out.println("Did not find Integer,Float,Boolean in Type()");
        }
        if (enteredProcedure == true || enteredFunction == true)
        {
            procedure.get(procedure.size() - 1).addParameterLexeme(lastType);
            parameterNum = 0;
        } else if (enteredReturn == true)
        {
            procedure.get(procedure.size() - 1).setReturnType(lastType);
        } else
        {
            procedure.get(procedure.size() - 1).addLemexe(lastType);
            num = 0;
        }
        lastType = null;
    }

    public void ProcedureAndFunctionDeclarationPart()
    {
        if (matchType("MP_PROCEDURE"))
        {
            ProcedureDeclaration();
            ProcedureAndFunctionDeclarationPart();
        } else if (matchType("MP_FUNCTION"))
        {

            FunctionDeclaration();
            ProcedureAndFunctionDeclarationPart();
        } else
        {
            //System.out.println("Found empty string in ProcedureAndFunctionDeclarationPart()");
        }

    }

    public void ProcedureDeclaration()
    {
        ProcedureHeading();
        if (match("MP_SCOLON"));
        {
            incToken();
            Block();
            if (match("MP_SCOLON"))
            {
                incToken();
            } else
            {
                validProgram = false;
            }
        }
    }

    public void FunctionDeclaration()
    {
        FunctionHeading();
        if (match("MP_SCOLON"));
        {
            Block();
            if (match("MP_SCOLON"))
            {
            } else
            {
                validProgram = false;
            }
        }
    }

    public void ProcedureHeading()
    {
        if (matchType("MP_PROCEDURE"))
        {
            enteredProcedure = true;
            incToken();
            ProcedureIdentifier();
            OptionalFormalParameterList();
            enteredProcedure = false;
        }
    }

    public void FunctionHeading()
    {
        if (matchType("MP_FUNCTION"))
        {
            incToken();
            enteredFunction = true;
            FunctionIdentifier();
            OptionalFormalParameterList();
            enteredProcedure = false;
            if (match("MP_COLON"))
            {
                incToken();
                enteredReturn = true;
                Type();
                enteredReturn = false;
            } else
            {
                validProgram = false;
            }
        }
    }

    public void OptionalFormalParameterList()
    {
        if (match("MP_LPAREN"))
        {
            FormalParameterSection();
            FormalParameterSectionTail();
            if (match("MP_RPAREN"))
            {
                incToken();
            } else
            {
                validProgram = false;
            }
        } else
        {
            /*
             * This is a epsilon
             */
        }
    }

    public void FormalParameterSectionTail()
    {
        if (match("MP_SCOLON"))
        {
            FormalParameterSection();
        } else
        {
            /*
             * This is a epsilon
             */
        }
    }

    public void FormalParameterSection()
    {
        if (matchType("MP_IDENTIFIER"))
        {
            ValueParameterSection();
        } else
        {
            VariableParameterSection();
        }
    }

    public void ValueParameterSection()
    {
        IdentifierList();
        if (match("MP_COLON"))
        {
            Type();
        } else
        {
            validProgram = false;
        }
    }

    public void VariableParameterSection()
    {
        if (matchType("MP_VAR"))
        {
            IdentifierList();
            if (match("MP_COLON"))
            {
                Type();
            } else
            {
                validProgram = false;
            }
        }
    }

    public void StatementPart()
    {
        CompoundStatement();
    }

    public void CompoundStatement()
    {
        if (matchType("MP_BEGIN"))
        {
            incToken();
            StatementSequence();
            if (matchType("MP_END"))
            {
                incToken();
//                if(match("MP_SCOLON"))
//                {
//                	procedure.remove(procedure.size());
//                }
            } else
            {
                validProgram = false;
            }
        }
    }

    public void StatementSequence()
    {
        Statement();
        if (lastLoc != -1)
        {
            addAssignment(tokenArray.get(lastLoc).getToken());
            lastLoc = -1;
        }
        StatementTail();
        if (lastLoc != -1)
        {
            addAssignment(tokenArray.get(lastLoc).getToken());
            lastLoc = -1;
        }
    }

    public void StatementTail()
    {
        if (match("MP_SCOLON"))
        {
            incToken();
            if (lastLoc != -1)
            {
                addAssignment(tokenArray.get(lastLoc).getToken());
                lastLoc = -1;
            }
            Statement();
            StatementTail();
        } else
        {
            // System.out.println("Empty string found in StatementTail()");
        }
    }

    public void Statement()
    {
        if (matchType("MP_BEGIN"))
        {
            CompoundStatement();
        } else if (matchType("MP_FOR"))
        {
            ForStatement();
        } else if (matchType("MP_IDENTIFIER"))
        {
            AssignmentStatement();
            //PrcedureStatement();
        } else if (matchType("MP_IF"))
        {
            IfStatement();
        } else if (matchType("MP_READ"))
        {
            ReadStatement();
        } else if (matchType("MP_REPEAT"))
        {
            RepeatStatement();
        } else if (matchType("MP_WHILE"))
        {
            WhileStatement();
        } else if (matchType("MP_WRITE"))
        {
            WriteStatement();
        } else if (matchType("MP_WRITELN"))
        {
            WriteStatement();
        } else
        {
            EmptyStatement();
        }


    }

    public void EmptyStatement()
    {
        // System.out.println("EmptyStatement() nothing to see here");
    }

    public void ReadStatement()
    {
        if (matchType("MP_READ"))
        {
            incToken();
            if (match("MP_LPAREN"))
            {
                incToken();
                int offset = procedure.get(0).checkNode(currentToken.getToken()).getOffset();
                String tempOffsetScope = Integer.toString(offset) + "(" + procedure.get(0).getLabel() + ")";
                FileOut.writerln("RD " + tempOffsetScope);
                ReadParameter();
                ReadParameterTail();
                if (match("MP_RPAREN"))
                {
                    incToken();
                } else
                {
                    //   System.out.println("no ) found in ReadStatement()");
                    validProgram = false;
                }
            } else
            {
                validProgram = false;
                // System.out.println("no ( found in ReadStatement()");
            }
        } else
        {
        }
    }

    public void ReadParameterTail()
    {
        if (match("MP_COMMA"))
        {
            incToken();
            int offset = procedure.get(0).checkNode(currentToken.getToken()).getOffset();
            String tempOffsetScope = Integer.toString(offset) + "(" + procedure.get(0).getLabel() + ")";
            FileOut.writerln("RD " + tempOffsetScope);
          //  check();
            
            ReadParameter();
            ReadParameterTail();
        } else
        {
            // System.out.println("empty string found in  ReadParameterTail()");
        }
    }

    public void ReadParameter()
    {

        VariableIdentifier();
    }

    public void WriteStatement()
    {
        if (matchType("MP_WRITE"))
        {
            incToken();
            if (match("MP_LPAREN"))
            {
                incToken();
                WriteParameter();
                WriteParameterTail();
                if (match("MP_RPAREN"))
                {
                    incToken();
                } else
                {
                    validProgram = false;
                    // System.out.println("no ) in WriteStatement()");
                }
            } else
            {
                validProgram = false;
                // System.out.println("no ( in WriteStatement()");
            }
        } else if (matchType("MP_WRITELN"))
        {
            incToken();
            if (match("MP_LPAREN"))
            {
                incToken();
                WriteParameterLN();
                WriteParameterTailLN();
                if (match("MP_RPAREN"))
                {
                    incToken();
                } else
                {
                    validProgram = false;
                    // System.out.println("no ) in WriteStatement()");
                }
            } else
            {
                validProgram = false;
                // System.out.println("no ( in WriteStatement()");
            }
        } else
        {
            validProgram = false;
            //  System.out.println("no write in WriteStatement()");
        }
    }

    public void WriteParameterTail()
    {
        if (match("MP_COMMA"))
        {
            incToken();
            WriteParameter();
            WriteParameterTail();
        } else
        {
            // System.out.println("Empty string found in  WriteParameterTail()");
        }
    }

    public void WriteParameterTailLN()
    {
        if (match("MP_COMMA"))
        {
            incToken();
            WriteParameterLN();
            WriteParameterTailLN();
        } else
        {
            // System.out.println("Empty string found in  WriteParameterTail()");
        }
    }

    public void WriteParameter()
    {
        if (matchType("MP_STRING_LIT"))
        {
        	FileOut.writer("WRT "); // #TAG
            String();
        } else
        {

            OrdinalExpression();
            FileOut.writer("WRTS "); // #TAG
        }


    }

    public void WriteParameterLN()
    {
        if (matchType("MP_STRING_LIT"))
        {
        	FileOut.writerln("WRTLN "); // #TAG
            String();
        } else
        {

            OrdinalExpression();
            FileOut.writerln("WRTLNS "); // #TAG
        }


    }

    public void String()
    {
        if (matchType("MP_STRING_LIT"))
        {
        	FileOut.writerln("#\"" + currentToken.getToken() + "\"\n"); // #TAG
            incToken();
        }

    }

    public void AssignmentStatement()
    {
        if (matchType("MP_IDENTIFIER"))
        {
            //variableIdentifier 101
            //FunctionIdentifier 103
            //~same rule
            boolean tempFound = false;
            int offset = procedure.get(0).checkNode(currentToken.getToken()).getOffset();
            offsetScope = Integer.toString(offset) + "(" + procedure.get(0).getLabel() + ")";

            if (procedure.get(0).checkNode(currentToken.getToken()).getLexeme().equals("MP_FLOAT"))
            {
                tempFound = true;
            }

            VariableIdentifier();
            //System.out.println("here");
            if (match("MP_ASSIGN"))
            {
                incToken();
                boolean temp = false;
                if (!(hasAssignment(tokenArray.get(currentLoc).getToken())))
                {
                    temp = assignmentCheck(tokenArray.get(currentLoc).getToken());
                }
                if (temp == true)
                {
                    addAssignment(tokenArray.get(lastLoc).getToken());
                }
                Expression();

                if (tempFound == true && found == false)
                {
                	FileOut.writerln("CASTSF");
                } else if (tempFound == false && found == true)
                {
                	FileOut.writerln("CASTSI");
                }

                found = false;
                FileOut.writerln("POP " + offsetScope);
                // System.out.println(lastLoc);
                //  System.out.println(tokenArray.get(lastLoc).getToken());
                addAssignment(tokenArray.get(lastLoc).getToken());
            }
        }
    }

    public void IfStatement()
    {
        if (matchType("MP_IF"))
        {
            incToken();
            BooleanExpression();
            String savedlabel = Main.createCond();
            if(foundnot == false)
            {
                FileOut.writerln("BRFS " + savedlabel);
            }
            else
            {
                FileOut.writerln("BRTS " + savedlabel);
                foundnot = false;
            }
            if (matchType("MP_THEN"))
            {
                incToken();
                Statement();
                String savedlabel2 = Main.createCond();
                FileOut.writerln("BR " + savedlabel2);
                FileOut.writerln(savedlabel + ":");
                OptionalElsePart();
                FileOut.writerln(savedlabel2 + ":");
            }
        }
    }

    public void OptionalElsePart()
    {
        if (matchType("MP_ELSE"))
        {
            incToken();
            Statement();
        } else
        {
            /*
             * Here we need a epsilon
             */
        }
    }

    public void RepeatStatement()
    {
        if (matchType("MP_REPEAT"))
        {
            String savedlabel1 = Main.createCond();
            FileOut.writerln(savedlabel1 + ":");

            incToken();
            StatementSequence();
            if (matchType("MP_UNTIL"))
            {
                incToken();
                BooleanExpression();
                FileOut.writerln("BRFS " + savedlabel1);
            } else
            {
                validProgram = false;
            }
        }
    }

    public void WhileStatement()
    {
        if (matchType("MP_WHILE"))
        {
            String savedlabel1 = Main.createCond();
            FileOut.writerln(savedlabel1 + ":");

            incToken();
            BooleanExpression();

            String savedlabel2 = Main.createCond();
            FileOut.writerln("BRFS " + savedlabel2);

            if (matchType("MP_DO"))
            {
                incToken();
                Statement();
                FileOut.writerln("BR " + savedlabel1);
                FileOut.writerln(savedlabel2 + ":");
            } else
            {
                validProgram = false;
            }
        }
    }

    public void ForStatement()
    {
        if (matchType("MP_FOR"))
        {
            incToken();
            int offset = procedure.get(0).checkNode(currentToken.getToken()).getOffset();
            String tempOffsetScope = Integer.toString(offset) + "(" + procedure.get(0).getLabel() + ")";
            ControlVariable();
            if (match("MP_ASSIGN"))
            {
                incToken();
                InitialValue();
                FileOut.writerln("POP " + tempOffsetScope);
                String savedlabel1 = Main.createCond();
                int step;
                if (currentToken.getTokenType().equals("MP_TO"))
                {
                    step = 1;
                    FileOut.writerln("PUSH " + tempOffsetScope);
                    FileOut.writerln("PUSH #-1");
                    FileOut.writerln("ADDS");
                    FileOut.writerln("POP " + tempOffsetScope);
                } else if (currentToken.getTokenType().equals("MP_DOWNTO"))
                {
                    step = -1;
                    FileOut.writerln("PUSH " + tempOffsetScope);
                    FileOut.writerln("PUSH #1");
                    FileOut.writerln("ADDS");
                    FileOut.writerln("POP " + tempOffsetScope);
                    
                    
                } else
                {
                    validProgram = false;
                    step = 0;
                }
                FileOut.writerln(savedlabel1 + ":");
                FileOut.writerln("PUSH " + tempOffsetScope);
                FileOut.writerln("PUSH #" + step);
                FileOut.writerln("ADDS");
                FileOut.writerln("POP " + tempOffsetScope);
                

                StepValue();
              
                int offset2 = procedure.get(0).checkNode(currentToken.getToken()).getOffset();
                String tempOffsetScope2 = Integer.toString(offset2) + "(" + procedure.get(0).getLabel() + ")";
                
                FinalValue();
                FileOut.writerln("POP " + tempOffsetScope2);
                
                if (matchType("MP_DO"))
                {
                    incToken();
                    Statement();
                    
                    String savedlabel2 = Main.createCond();
                    FileOut.writerln("PUSH " + tempOffsetScope);
                    FileOut.writerln("PUSH " + tempOffsetScope2);
                    FileOut.writerln("CMPEQS");
                    FileOut.writerln("BRTS " + savedlabel2);
                    FileOut.writerln("BR " + savedlabel1);
                    FileOut.writerln(savedlabel2 + ":");



                } else
                {
                    validProgram = false;
                }
            }
        }
    }

    public void ControlVariable()
    {
        VariableIdentifier();
    }

    public void InitialValue()
    {
        OrdinalExpression();
    }

    public void StepValue()
    {
        if (matchType("MP_TO"))
        {
            incToken();
        } else if (matchType("MP_DOWNTO"))
        {
            incToken();
        }
    }

    public void FinalValue()
    {
        OrdinalExpression();
    }

    public void ProcedureStatement()
    {
        ProcedureIdentifier();
        OptionalActualParameterList();
    }

    public void OptionalActualParameterList()
    {
        if (match("MP_LPAREN"))
        {
            incToken();
            ActualParameter();
            ActualParameterTail();
            if (match("MP_RPAREN"))
            {
                incToken();
            }
        } else
        {
            /*
             * Here we need a epsilon
             */
        }
    }

    public void ActualParameterTail()
    {
        if (match("MP_COMMA"))
        {
            incToken();
            ActualParameter();
            ActualParameterTail();
        } else
        {
            /*
             * Here we need a epsilon
             */
        }
    }

    public void ActualParameter()
    {
        OrdinalExpression();
    }

    public void Expression()
    {
        SimpleExpression();
        OptionalRelationalPart();
    }

    public void OptionalRelationalPart()
    {
        if (match("MP_EQUAL"))
        {
            RelationalOperator();
            SimpleExpression();
            FileOut.writerln("CMPEQS");
        } else if (match("MP_LTHAN"))
        {
            RelationalOperator();
            SimpleExpression();
            FileOut.writerln("CMPLTS");
        } else if (match("MP_GTHAN"))
        {
            RelationalOperator();
            SimpleExpression();
            FileOut.writerln("CMPGTS");
        } else if (match("MP_LEQUAL"))
        {
            RelationalOperator();
            SimpleExpression();
            FileOut.writerln("CMPLES");
        } else if (match("MP_GEQUAL"))
        {
            RelationalOperator();
            SimpleExpression();
            FileOut.writerln("CMPGES");
        } else if (match("MP_NEQUAL"))
        {
            RelationalOperator();
            SimpleExpression();
            FileOut.writerln("CMPNES");
        } else
        {
        }
//        if(lastLoc!=-1)
//        {
//        	addAssignment(tokenArray.get(lastLoc).getToken());
//        	lastLoc = -1;
//        }
        /*
         * Here we need a epsilon
         */
    }

    public void RelationalOperator()
    {
        if (match("MP_EQUAL"))
        {
            incToken();
        } else if (match("MP_LTHAN"))
        {
            incToken();
        } else if (match("MP_GTHAN"))
        {
            incToken();
        } else if (match("MP_LEQUAL"))
        {
            incToken();
        } else if (match("MP_GEQUAL"))
        {
            incToken();
        } else if (match("MP_NEQUAL"))
        {
            incToken();
        } else
        {
            validProgram = false;
        }
    }

    public void SimpleExpression()
    {
        OptionalSign();
        Term();
        TermTail();
    }

    public void TermTail()
    {
        if (match("MP_PLUS"))
        {
            AddingOperator();
            Term();
            TermTail();
            outputPlus();
            //System.out.println("ADDS");
        } else if (match("MP_MINUS"))
        {
            AddingOperator();
            Term();
            TermTail();
            outputMinus();
            //System.out.println("SUBS");
        } else if (matchType("MP_OR"))
        {
            AddingOperator();
            Term();
            TermTail();
            System.out.println("ORS");
        } else
        {
            //System.out.println("Found epselon in TermTail()");
        }


        /*
         * Here we need a epsilon
         */
    }

    public void OptionalSign()
    {
        if (match("MP_PLUS"))
        {
            incToken();
        } else if (match("MP_MINUS"))
        {
            negative = true;
            incToken();
        } else
        {
            // System.out.println("Found epsiolon in OptionalSign()");
        }
    }

    public void AddingOperator()
    {
        if (match("MP_PLUS"))
        {
            incToken();
        } else if (match("MP_MINUS"))
        {
            incToken();
        } else if (matchType("MP_OR"))
        {
            incToken();
        } else
        {
            validProgram = false;
            //  System.out.println("Did not find -,+,or in AddingOperator()");
        }
    }

    public void Term()
    {
        Factor();
        FactorTail();
    }

    public void FactorTail()
    {
        if (match("MP_TIMES"))
        {
            MultiplyingOperator();
            Factor();
            outputMul();
            FactorTail();
            if (found == true)
            {
                FileOut.writerln("CASTSF");
            }
            if (negative == true)
            {
                if (found == true)
                {
                	FileOut.writerln("NEGSF");
                } else
                {
                	FileOut.writerln("NEGS");
                }
                negative = false;
            }
           // outputMul();
            //System.out.println("MULS");
        } else if (matchType("MP_DIV"))
        {
            MultiplyingOperator();
            Factor();
          FileOut.writerln("CASTSF");
          FileOut.writerln("POP D1");
          FileOut.writerln("CASTSF");
          FileOut.writerln("PUSH D1");
          FileOut.writerln("DIVSF");
          FileOut.writerln("CASTSI");
            FactorTail();
            if (found == true)
            {
                FileOut.writerln("CASTSF");
            }
            if (negative == true)
            {
                if (found == true)
                {
                	FileOut.writerln("NEGSF");
                } else
                {
                	FileOut.writerln("NEGS");
                }
                negative = false;
            }
           
//            FileOut.writerln("CASTSF");
//            FileOut.writerln("POP D1");
//            FileOut.writerln("CASTSF");
//            FileOut.writerln("PUSH D1");
//            FileOut.writerln("DIVSF");
//            FileOut.writerln("CASTSI");
            
        } else if (match("MP_DIVF"))
        {
            MultiplyingOperator();
            Factor();
            FileOut.writerln("CASTSF");
            FileOut.writerln("POP D1");
            FileOut.writerln("CASTSF");
            FileOut.writerln("PUSH D1");
            FileOut.writerln("DIVSF");
            FactorTail();
    
            if (negative == true)
            {
                if (found == true)
                {
                	FileOut.writerln("NEGSF");
                } else
                {
                	FileOut.writerln("NEGS");
                }
                negative = false;
            }
//            FileOut.writerln("CASTSF");
//            FileOut.writerln("POP D1");
//            FileOut.writerln("CASTSF");
//            FileOut.writerln("PUSH D1");
//            FileOut.writerln("DIVSF");
        } else if (matchType("MP_MOD"))
        {
            MultiplyingOperator();
            Factor();
            FileOut.writerln("MODS");
            FactorTail();
            if (found == true)
            {
                FileOut.writerln("CASTSF");
            }
            if (negative == true)
            {
                if (found == true)
                {
                	FileOut.writerln("NEGSF");
                } else
                {
                	FileOut.writerln("NEGS");
                }
                negative = false;
            }
           // FileOut.writerln("MODS");
        } else if (matchType("MP_AND"))
        {
            MultiplyingOperator();
            Factor();
            FileOut.writerln("ANDS");
            FactorTail();
            if (found == true)
            {
                FileOut.writerln("CASTSF");
            }
            if (negative == true)
            {
                if (found == true)
                {
                	FileOut.writerln("NEGSF");
                } else
                {
                	FileOut.writerln("NEGS");
                }
                negative = false;
            }
           
        } else
        {
            //   System.out.println("Empty string found in FactorTail()");
        }
    }

    public void MultiplyingOperator()
    {
        if (match("MP_TIMES"))
        {
            incToken();
        } else if (matchType("MP_DIV"))
        {
            incToken();
        } else if (match("MP_DIVF"))
        {
            incToken();
        } else if (matchType("MP_MOD"))
        {
            incToken();
        } else if (matchType("MP_AND"))
        {
            incToken();
        } else
        {
            //    System.out.println("Empty string found in MultiplyingOperator()");
        }
    }

    public void Factor()
    {
        if (matchType("MP_INTEGER_LIT") || matchType("MP_FLOAT_LIT") || matchType("MP_FIXED_LIT"))
        {
            check();

            FileOut.writerln("PUSH #" + currentToken.getToken());
            if (found == true)
            {
                FileOut.writerln("CASTSF");
            }
            if (negative == true)
            {
                if (found == true)
                {
                	FileOut.writerln("NEGSF");
                } else
                {
                	FileOut.writerln("NEGS");
                }
                negative = false;
            }

            incToken();
        } else if (matchType("MP_IDENTIFIER"))
        {
            if (tokenArray.get(currentLoc + 1).getToken().equals("MP_LPAREN"))
            {
                if (found == true)
                {
                    FileOut.writerln("CASTSF");
                }
                if (negative == true)
                {
                    if (found == true)
                    {
                    	FileOut.writerln("NEGSF");
                    } else
                    {
                    	FileOut.writerln("NEGS");
                    }
                    negative = false;
                }
                FunctionIdentifier();
                OptionalActualParameterList();
            } else
            {
            	
                boolean temp = false;
                if (!(hasAssignment(tokenArray.get(currentLoc).getToken())))
                {
                    temp = assignmentCheck(tokenArray.get(currentLoc).getToken());
                }
                //  System.out.println(currentToken.getToken());
                // System.out.println(temp);
                if (temp == true)
                {
                    addAssignment(tokenArray.get(currentLoc).getToken());
                }
                temp = hasAssignment(currentToken.getToken());
                //System.out.println(currentToken.getToken()+" 1 "+ temp);
                if (temp == false)
                {
                    validProgram = false;
                }
                int offset = procedure.get(0).checkNode(currentToken.getToken()).getOffset();
                String tempOffsetScope = Integer.toString(offset) + "(" + procedure.get(0).getLabel() + ")";
                FileOut.writerln("PUSH " + tempOffsetScope);
                incToken();
            }

        } else if (matchType("MP_NOT"))
        {
            foundnot = true;
            incToken();
            Factor();
        } else if (match("MP_LPAREN"))
        {
            incToken();
            
            Expression();
            if (negative == true)
            {
                if (found == true)
                {
                	FileOut.writerln("NEGSF");
                } else
                {
                	FileOut.writerln("NEGS");
                }
                negative = false;
            }
            if (match("MP_RPAREN"))
            {
                incToken();
            } else
            {
                validProgram = false;
            }
        } else
        {
            validProgram = false;
        }
    }

    public void ProgramIdentifier()
    {
        procedure.add(new procedure(currentToken.getToken(), "MP_PROGRAM"));
        incToken();

    }

    public void Identifier()
    {
        if (enteredProcedure == true || enteredFunction == true)
        {
            ++parameterNum;
            procedure.get(procedure.size() - 1).addParameter("MP_VAR", currentToken.getToken(), "", "in");
        } else
        {
            ++num;
            procedure.get(procedure.size() - 1).addNode("MP_VAR", currentToken.getToken(), "");
        }
        incToken();

    }

    public void VariableIdentifier()
    {
        boolean temp = false;
        if (!(hasAssignment(tokenArray.get(currentLoc).getToken())))
        {
            temp = assignmentCheck(tokenArray.get(currentLoc).getToken());
        }
        // System.out.println(currentToken.getToken());
        if (temp == true)
        {
            addAssignment(tokenArray.get(currentLoc).getToken());
        }
        temp = check(currentToken.getToken());
        //System.out.println(currentToken.getToken());
        if (firstTime == true)
        {
            addAssignment(currentToken.getToken());
        }
        if (temp == false)
        {
            //System.out.println("herethe");
            validProgram = false;
        } else
        {
            lastLoc = currentLoc;
            //System.out.println("here4");
        }

        incToken();

    }

    public void ProcedureIdentifier()
    {
        procedure.get(procedure.size()).addNode("procedure", currentToken.getToken(), "MP_PROCEDURE");
        procedure temp = new procedure(currentToken.getToken(), "Procedure");
        procedure.add(temp);
        incToken();

    }

    public void FunctionIdentifier()
    {
        procedure.get(procedure.size()).addNode("function", currentToken.getToken(), "MP_FUNCTION");

        incToken();

    }

    public void BooleanExpression()
    {
        Expression();
    }

    public void OrdinalExpression()
    {
        Expression();
    }

    public void IdentifierList()
    {
        if (matchType("MP_IDENTIFIER"))
        {
            Identifier();
            IdentifierTail();
        }
    }

    public void IdentifierTail()
    {
        if (match("MP_COMMA"))
        {
            incToken();
            Identifier();
            IdentifierTail();
        } else
        {
        }
    }

    public void incToken()
    {
        currentLoc++;
        currentToken = tokenArray.get(currentLoc);
    }

    public boolean matchType(String type)
    {
        if (currentToken.getTokenType().equals(type))
        {
            return true;
        } else
        {
            return false;
        }
    }

    public boolean match(String matchString)
    {
        //matchString may need to be of form MP_PLUS
        //token is reference to current token
        if (currentToken.getToken().compareTo(matchString) == 0)
        {
            return true;
        } else
        {
            return false;
        }
    }

    public boolean check(String name)
    {
        //boolean check = false;
        for (int i = procedure.size(); i > 0; i--)
        {
            boolean temp = procedure.get(i - 1).checkName(name);
            if (temp == true)
            {
                return true;
            }

        }
        return false;
    }

    public boolean addAssignment(String name)
    {
        //boolean check = false;
        for (int i = procedure.size(); i > 0; i--)
        {
            boolean temp = procedure.get(i - 1).addAssignment(name);
            if (temp == true)
            {
                return true;
            }

        }
        return false;
    }

    public boolean hasAssignment(String name)
    {
        //boolean check = false;
        for (int i = procedure.size(); i > 0; i--)
        {
            int temp = procedure.get(i - 1).getAssignment(name);
            if (temp == 2)
            {
                return true;
            } else if (temp == 1)
            {
                return false;
            }

        }
        return false;
    }

    public boolean assignmentCheck(String name)
    {
        //boolean temp = true;
        //System.out.println(name);
        for (int i = currentLoc + 1; i < tokenArray.size() - 1; i++)
        {
            //System.out.println(tokenArray.get(i).getToken());
            if (tokenArray.get(i).getToken().equals("MP_SCOLON") || tokenArray.get(i).getToken().equals("MP_END"))
            {
                return true;
            } else if (tokenArray.get(i).getToken().equals(name))
            {
                //System.out.println("here2");
                return false;
            }
        }
        //System.out.println("here");
        return false;
    }

    public nodes getNode(String name)
    {
        //boolean check = false;
        for (int i = procedure.size(); i > 0; i--)
        {
            nodes temp = procedure.get(i - 1).checkNode(name);
            if (temp != null)
            {
                return temp;
            }

        }
        return null;
    }

    public void check()
    {
        if (matchType("MP_FLOAT_LIT") || matchType("MP_FIXED_LIT"))
        {
            found = true;
        }
    }

    public void outputPlus()
    {
        if (found == true)
        {
        	FileOut.writerln("CASTSF");
        	FileOut.writerln("POP D1");
        	FileOut.writerln("CASTSF");
        	FileOut.writerln("PUSH D1");
        	FileOut.writerln("ADDSF");
        } else
        {
        	FileOut.writerln("ADDS");
        }
    }

    public void outputMinus()
    {
        if (found == true)
        {
        	FileOut.writerln("CASTSF");
        	FileOut.writerln("POP D1");
        	FileOut.writerln("CASTSF");
        	FileOut.writerln("PUSH D1");
        	FileOut.writerln("SUBSF");
        } else
        {
        	FileOut.writerln("SUBS");
        }
    }

    public void outputMul()
    {
        if (found == true)
        {
        	FileOut.writerln("CASTSF");
        	FileOut.writerln("POP D1");
        	FileOut.writerln("CASTSF");
        	FileOut.writerln("PUSH D1");
        	FileOut.writerln("MULSF");
        } else
        {
        	FileOut.writerln("MULS");
        }
    }

    public void setValid()
    {
        validProgram = false;
    }
}
