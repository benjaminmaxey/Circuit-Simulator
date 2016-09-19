//*****************************************************************************
//	Author: Ben Maxey
//
//	DAGEdge connects a pair of DAGNodes. Each DAGEdge specifies its base and
//	end, and has a logic level which is represented by a Boolean variable.
//	DAGNodes mainly use the setValue and getValue methods to pass and receive
//	information from DAGEdges.
//*****************************************************************************

package circuitsim;

public class DAGEdge
{
	private Boolean level;
	private Boolean determined;
	private DAGNode base;
	private DAGNode end;

	//Default constructor.
	public DAGEdge()
	{
		base = null;
		end = null;
		determined = false;
	}

	//Constructor for known base and end.
	public DAGEdge(DAGNode input, DAGNode output)
	{
		base = input;
		end = output;
		determined = false;
	}

	//Sets the value of level.
	public void setValue(Boolean value)
	{
		level = value;
		determined = true;
	}

	//Returns level.
	public Boolean getValue()
	{
		return level;
	}

	//Returns true if the edge's level has been set.
	public Boolean isDetermined()
	{
		return determined;
	}

	//Connects the given node as the base. Should only be called from the
	//DAGNode's connectOutput method, as this method does not update the node's
	//list of output edges.
	public void connectInput(DAGNode input)
	{
		base = input;
	}

	//Connects the given node as the end. Should only be called from the
	//DAGNode's connectInput method, as this method does not update the node's
	//list of input edges.
	public void connectOutput(DAGNode output)
	{
		end = output;
	}
}