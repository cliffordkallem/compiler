


public class Token
{
    private String tokenType;
    private String token;
    private int tokenStartRow;
    private int tokenStartCol;
    
    public Token(String in_tokenType,String in_token,int in_tokenStartRow, int in_tokenStartCol)
    {
        tokenType = in_tokenType;
        token = in_token;
        tokenStartRow = in_tokenStartRow;
        tokenStartCol = in_tokenStartCol;
    }
    
    public String getTokenType()
    {
        return tokenType;
    }
    public String getToken()
    {
        return token;
    }
    public int getTokenStartRow()
    {
        return tokenStartRow;
    }
    public int getTokenStartCol()
    {
        return tokenStartCol;
    }

	public int compareTo(String matchString) {
		// TODO Auto-generated method stub
		return 0;
	}
}
