//*****************************************************************************
//	Author: Ben Maxey
//
//	NodeType enumerates all of the possibilities for the types of DAGNodes.
//	It also includes methods that convert between Strings and NodeTypes.
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
				return NULL;
		}
	}

	static public String toString(NodeType type)
	{
		switch(type)
		{
			case IN:
				return "IN";
			case OUT:
				return "OUT";
			case NOT:
				return "NOT";
			case AND:
				return "AND";
			case OR:
				return "OR";
			case NAND:
				return "NAND";
			case NOR:
				return "NOR";
			case XOR:
				return "XOR";
			default:
				return "NULL";
		}
	}
}