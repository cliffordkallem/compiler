public class nodes {
		private String type;
		//private String labelVar;
		private String name;
		private String lexeme;
		private int offset;
		private String mode;
		private boolean assignment = false;
		public nodes(String in_type, String in_name, String in_lexeme)// type is the like int or parameter and lemexe is var
		{
			setType(in_type);
			setName(in_name);
			setLexeme(in_lexeme);

		}
		public nodes(String in_type, String in_name, String in_lexeme,String in_mode)// type is the like int or parameter and lemexe is var and modes is reference
		{
			mode= in_mode;
			setType(in_type);
			setName(in_name);
			setLexeme(in_lexeme);

		}
		public int getOffset() {
			return offset;
		}
		public boolean hasAssignment()
		{
			return assignment;
		}
		public void beenAssignment()
		{
			assignment = true;
		}
		public void nextOffset(String type)
		{
	        if (type.equals("MP_INTEGER"))
	        {
	        	offset = Main.offset + 1;
	        	Main.offset = offset;
	        } 
	        else if (type.equals("MP_FLOAT"))
	        {
	        	offset = Main.offset + 1;
	        	Main.offset = offset;
	        }
	        else if (type.equals("MP_BOOLEAN"))
	        {
	        	offset = Main.offset + 1;
	        	Main.offset = offset;
	        } 
	        else if (type.equals("MP_FLOAT_LIT"))
	        {
	        	offset = Main.offset + 1;
	        	Main.offset = offset;
	        } 
	        else if (type.equals("MP_FIXED_LIT"))
	        {
	        	offset = Main.offset + 1;
	        	Main.offset = offset;
	        } 
	        else if (type.equals("MP_INTEGER_LIT"))
	        {
	        	offset = Main.offset + 1;
	        	Main.offset = offset;
	        } 
	        else if (type.equals("MP_STRING_LIT"))
	        {
	        	offset = Main.offset + 1;
	        	Main.offset = offset;
	        } 
		}
		public String getLexeme() {
			return lexeme;
		}
		public void setLexeme(String lexeme) {
			this.lexeme = lexeme;
			
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}

	}
