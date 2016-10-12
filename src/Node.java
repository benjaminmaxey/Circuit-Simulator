//*****************************************************************************
//	Author: Ben Maxey
//	
//	The Node class represents either an input, output, wire, or logic gate.
//	Each node contains a list of parent and child nodes, and is responsible for
//	computing an output value based on a set of input values.  Nodes also keep
//	track of their last scheduled values and time of last scheduling.
//*****************************************************************************

package circuitsim;

import java.util.ArrayList;

public class Node
{
	private String id;
	private NodeType nType;

	private ArrayList<Node> inputs;
	private ArrayList<Node> outputs;

	private ArrayList<Boolean> inValues;
	private Boolean outValue;

	public int riseDel;
	public int fallDel;
	public int lastSchedTime;
	public Boolean lastSchedVal;

	//Default constructor.
	public Node()
	{
		id = "";
		nType = NodeType.NULL;

		inputs = new ArrayList<Node>();
		outputs = new ArrayList<Node>();
		inValues = new ArrayList<Boolean>();

		riseDel = 1;
		fallDel = 1;
		lastSchedTime = -1;
		lastSchedVal = null;
	}

	//Constructor for known type.
	public Node(String name, NodeType type)
	{
		id = name;
		nType = type;

		inputs = new ArrayList<Node>();
		outputs = new ArrayList<Node>();
		inValues = new ArrayList<Boolean>();

		riseDel = 1;
		fallDel = 1;
		lastSchedTime = -1;
		lastSchedVal = null;
	}

	//Constructor for known type and delays.
	public Node(String name, NodeType type, int rise, int fall)
	{
		id = name;
		nType = type;

		inputs = new ArrayList<Node>();
		outputs = new ArrayList<Node>();
		inValues = new ArrayList<Boolean>();

		riseDel = rise;
		fallDel = fall;
		lastSchedTime = -1;
		lastSchedVal = null;
	}

	//Returns id.
	public String getID()
	{
		return id;
	}

	//Returns nType.
	public NodeType getType()
	{
		return nType;
	}

	//Returns the output of the node.
	public Boolean getOutValue()
	{
		return outValue;
	}

	//Load the values of each input node into the ArrayList inValues.
	public Boolean loadInputs()
	{
		inValues.clear();

		if (nType == NodeType.IN)
		{
			System.err.println("Cannot load inputs of an input node.");
			return false;
		}
		
		for (int i = 0; i < inputs.size(); i++)
		{
			if (inputs.get(i).getOutValue() == null)
				return false;
			else
				inValues.add(inputs.get(i).getOutValue());
		}

		return true;
	}

	//Computes the value of the node's output value based on its input values
	//and its logic function (specified by its NodeType).
	public Boolean computeOutput()
	{
		Boolean output;

		switch (nType)
		{
			//Input node's output should be set manually.
			case IN:
				return outValue;

			//Following three types pass input directly to output.
			case OUT:
			case WIRE:
			case BUF:
				if (inValues.size() != 1)
				{
					System.err.println("Too few or too many input nodes.");
					return null;
				}

				return inValues.get(0);

			//NOT node's output is the complement of its input.
			case NOT:
				if (inValues.size() != 1)
				{
					System.err.println("Too few or too many input nodes.");
					return null;
				}

				return !inValues.get(0);

			//AND node's output is true if all input edges' values are true.
			case AND:
				output = true;

				for (int i = 0; i < inValues.size(); i++)
				{
					if (!inValues.get(i))
					{
						output = false;
						break;
					}
				}

				return output;

			//OR node's output is true if any input edges' values are true.
			case OR:
				output = false;

				for (int i = 0; i < inValues.size(); i++)
				{
					if (inValues.get(i))
					{
						output = true;
						break;
					}
				}

				return output;

			//NAND node's output is a negation of an AND node's output.
			case NAND:
				output = false;

				for (int i = 0; i < inValues.size(); i++)
				{
					if (!inValues.get(i))
					{
						output = true;
						break;
					}
				}

				return output;

			//NOR node's output is a negation of an OR node's output.
			case NOR:
				output = true;

				for (int i = 0; i < inValues.size(); i++)
				{
					if (inValues.get(i))
					{
						output = false;
						break;
					}
				}

				return output;

			//XOR node's output is true if an odd number of input edges' values
			//are true.
			case XOR:
				int odd = 0;

				for (int i = 0; i < inValues.size(); i++)
				{
					if (inValues.get(i))
						odd++;
				}

				return (odd % 2 == 1);

			default:
				System.err.println("Node is NULL type.");
				return null;
		}
	}

	//Manually updates a node's output value.
	public void update(Boolean value)
	{
		outValue = value;
	}

	//Connects the given node as an input to this node.
	public void connectInput(Node connect)
	{
		inputs.add(connect);
		connect.outputs.add(this);
	}

	//Connects the given node as an output of this node.
	public void connectOutput(Node connect)
	{
		outputs.add(connect);
		connect.inputs.add(this);
	}

	//Returns input nodes.
	public ArrayList<Node> getInputs()
	{
		return inputs;
	}

	//Returns output nodes.
	public ArrayList<Node> getOutputs()
	{
		return outputs;
	}
}