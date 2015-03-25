import java.util.ArrayList;

/*
 * 
 */
public class procedure {
	private ArrayList<nodes> node=new ArrayList<nodes>();
	private String name;// name of function or procedure and program
	private String type;// is a function or procedure or program
	private int num= 0;
	private ArrayList<nodes> parameter=new ArrayList<nodes>();
	private String label;
	private String returnType;
	/*
	 * This is for procedures and the main program
	 */
	public procedure(String in_name, String in_type) {
		name=(in_name);
		type = in_type;
		label = Main.createLabel();
		Main.offset =0;
	}
	/*
	 * This is for function and has the return
	 */
	public procedure(String in_name, String in_type, String in_returnType) {
		name=(in_name);
		type = in_type;
		label = Main.createLabel();
		returnType =  null;
		Main.offset =0;
	}
	//for everything but parameter
	public void addNode(String in_type, String in_name, String in_lexeme) {
		node.add(new nodes(in_type, in_name, in_lexeme));
	
	}
	//for parameters
	public void addParameter(String in_type, String in_name, String in_lexeme,String in_mode) {
		parameter.add(new nodes(in_type, in_name, in_lexeme, in_mode));
	}
	/*
	 * Adds lexeme to the parameter arraylist
	 */
	public boolean addParameterLexeme(String in_lexeme)
	{
		if (node == null||in_lexeme == null) {
			return false;
		}
		for (int j = 0; j < Parser.parameterNum; j++) {
			if (!((parameter.get(parameter.size() - j-1).getLexeme()).equals("")) ){
				return false;
			}
			else{
				parameter.get(parameter.size() - j-1).setLexeme(in_lexeme);
			}

		}
		return false;
	}
	/*
	 * Gets the name of the procedure 
	 */
	public String getName() {
		return name;
	}
	/*
	 * gets the label of this procedure
	 */
	public String getLabel(){
		return label;
	}
	/*
	 * Set the return type of the function
	 */
	public void setReturnType(String in_returnType)
	{
		returnType = in_returnType;
	}
	/*
	 *Gets the return type 
	 */
	public String getReturnType()
	{
		return returnType;
	}
	/*
	 * Check to see if something has this same name
	 */
	public boolean checkName(String name) {
		if (node == null) {
			return false;
		}
		for (int j = node.size(); j >= 1; j--) {
			if (node.get(j - 1).getName().equals(name)) {
				return true;
			}
		}
		return false;
	}
	/*
	 * Find the node with the same name and return it
	 */
	public nodes checkNode(String name) {

		if (node == null) {
			return null;
		}
		for (int j = node.size(); j >= 1; j--) {
			if (node.get(j - 1).getName().equals(name)) {
				return node.get(j-1);
			}
		}
		return null;
	}
	/*
	 * after you find out what the lexeme of a parameter is this adds it to the last added parameters.
	 */
	public boolean addLemexe(String in_lexeme)//
	{
		boolean entered=false;
		boolean enteredAgain = false;
		if (node == null||in_lexeme == null) {
			return false;
		}
		for (int j = 0; j < Parser.num; j++) {
			entered =true;
			if (!((node.get(node.size() - j-1).getLexeme()).equals("")) ){
				return false;
			}
			else{
				node.get(node.size() - j-1).setLexeme(in_lexeme);
			}

		}
		if(entered == true)
		{
			int i;
			for(i=num;i<node.size()-1;i++)
			{
				entered =true;
				node.get(i).nextOffset(in_lexeme);
			}
			num=i;
		}
		if(entered == true&& enteredAgain == true)
		{
			return true;
		}
		return false;
	}
	/*
	 *	Return the type 
	 */
	public String getType() {
		return type;
	}
	/*
	 * Prints every node 
	 */
	public void print()
	{
            System.out.println("\n");
		for(int i=node.size();i>0;i--)
			System.out.println(node.get(i-1).getLexeme()+" "+node.get(i-1).getOffset()+" "+node.get(i-1).getName()+" "+node.get(i-1).hasAssignment());
	}
    public ArrayList<nodes> getNodes()
    {
    	return node;
    }
	///////////////////////////////////////////////////////////////////////////
    /*
     * Says that this variable has been assigned 
     */
	public boolean addAssignment(String name) {

		if (node == null) {
			return false;
		}
		for (int j = node.size(); j >= 1; j--) {
			if (node.get(j - 1).getName().equals(name)) {
				node.get(j - 1).beenAssignment();
				return true;
			}
		}
		return false;
	}
	/*
	 *  Get if this varaible has been assigned 
	 */
	public int getAssignment(String name) {

		if (node == null) {
			return -1;
		}
		for (int j = node.size(); j >= 1; j--) {
			if (node.get(j - 1).getName().equals(name)) {
				if(node.get(j - 1).hasAssignment()==true)
				{
					return 2;
				}
				return 1;
			}
		}
		return -1;
	}
}