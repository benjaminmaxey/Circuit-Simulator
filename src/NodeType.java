//*****************************************************************************
//	Author: Ben Maxey
//
//	NodeType enumerates all of the possibilities for the types of DAGNodes.
//	It also includes methods that convert between Strings and NodeTypes.
//*****************************************************************************

package circuitsim;

public enum NodeType 
{
	IN, OUT, BUF, NOT, AND, OR, NAND, NOR, XOR, NULL;

	//Converts a String to its corresponding NodeType.
	static public NodeType matchString(String type)
	{
		switch(type)
		{
			case "input":
				return IN;
			case "output":
				return OUT;
			case "buf":
				return BUF;
			case "not":
				return NOT;
			case "and":
				return AND;
			case "or":
				return OR;
			case "nand":
				return NAND;
			case "nor":
				return NOR;
			case "xor":
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
				return "input";
			case OUT:
				return "output";
			case BUF:
				return "buf";
			case NOT:
				return "not";
			case AND:
				return "and";
			case OR:
				return "or";
			case NAND:
				return "nand";
			case NOR:
				return "nor";
			case XOR:
				return "xor";
			default:
				return "null";
		}
	}
}