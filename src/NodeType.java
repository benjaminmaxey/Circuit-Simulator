//*****************************************************************************
//	Author: Ben Maxey
//
//	NodeType enumerates all of the possibilities for the types of DAGNodes.
//	It also includes a method that converts Strings to NodeType values.
//*****************************************************************************

package circuitsim;

public enum NodeType 
{
	IN, OUT, NOT, AND, OR, NAND, NOR, XOR, NULL;

	//Converts a String to its corresponding NodeType.
	static public NodeType matchString(String type)
	{
		switch(type)
		{
			case "IN":
				return IN;
			case "OUT":
				return OUT;
			case "NOT":
				return NOT;
			case "AND":
				return AND;
			case "OR":
				return OR;
			case "NAND":
				return NAND;
			case "NOR":
				return NOR;
			case "XOR":
				return XOR;
			default:
				System.err.println("String did not match any NodeType.");
				return NULL;
		}
	}
}