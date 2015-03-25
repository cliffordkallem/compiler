


public class Main
{
    public static int labelNum=0;
    public static int condNum=0;
    public static int offset=0;
    private static Parser newParser;
    public static void main(String[] args)
    {
       
        ScannerPart.dispatcher();
        
        newParser = new Parser(ScannerPart.tokenArray);
        
        if(newParser.Parse())
        {
            System.out.println("Valid Program");
        }
        else{
        	FileOut.delete();
        	System.out.println("fail");
        }
        
        System.out.println();
//        for(int i = 0; i < Parser.genCode.size(); i++)
//        {
//            System.out.println(Parser.genCode.get(i));
//        }
//        
//        System.out.println("");
//        
//        for(int i = 0; i < ScannerPart.tokenArray.size(); i++)
//        {
//            Token token = ScannerPart.tokenArray.get(i);
//            System.out.format("%s %s %d %d\n", token.getToken(), token.getTokenType(), token.getTokenStartRow(), token.getTokenStartCol());
//            //System.out.format("%s %s %d %d\n", token.getTokenType(), token.getToken(), token.getTokenStartRow(), token.getTokenStartCol());
//        }
    
    }
    public static void changeValidProgram()
    {
    	newParser.setValid();
    }
	public static String createLabel()
	{
		//Main.labelNum
		return "D"+ (Main.labelNum++);
	}
        public static String createCond()
	{
		//Main.labelNum
		return "L"+ (Main.condNum++);
	}
    
}
