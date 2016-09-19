//*****************************************************************************
//	Author: Ben Maxey
//	
//	DAGNode represents either an input to a logic circuit, an output from a
//	circuit, or a logic gate.  Each DAGNode has a set of input edges and output
//	edges, which represent the wires that connect inputs, gates, and outputs.
//	Input DAGNodes have no input edges, and output DAGNodes have only one input
//	edge and no output edges.
//*****************************************************************************

package circuitsim;

import java.util.ArrayList;

public class DAGNode
{
	private NodeType nType;
	private ArrayList<DAGEdge> inputs;
	private ArrayList<DAGEdge> outputs;
	private ArrayList<Boolean> inValues;
	private Boolean outValue;

	private int delay;

	//Default constructor.
	public DAGNode()
	{
		nType = null;
		inputs = new ArrayList<DAGEdge>();
		outputs = new ArrayList<DAGEdge>();
		inValues = new ArrayList<Boolean>();
	}

	//Constructor for DAGNode of known type.
	public DAGNode(NodeType type)
	{
		nType = type;
		inputs = new ArrayList<DAGEdge>();
		outputs = new ArrayList<DAGEdge>();
		inValues = new ArrayList<Boolean>();
	}

	//Constructor for input DAGNodes. Parameter specifies input DAGNode's
	//output value.
	public DAGNode(Boolean value)
	{
		nType = NodeType.IN;
		inputs = null;
		outputs = new ArrayList<DAGEdge>();
		inValues = null;
		outValue = value;
	}

	//Sets output value for IN DAGNodes.
	public void setInput(Boolean value)
	{
		if (nType == NodeType.IN)
			outValue = value;
		else
			System.err.println("Cannot set input for non-input node.");
	}

	//Returns the output of the DAGNode.
	public Boolean getOutput()
	{
		return outValue;
	}

	//Load the values of each input DAGEdge into the ArrayList inValues.
	private void loadInputs()
	{
		if (nType == NodeType.IN)
		{
			System.err.println("Cannot load inputs of an input node.");
			return;
		}
		
		for (int i = 0; i < inputs.size(); i++)
			inValues.add(inputs.get(i).getValue());
	}

	//Computes the value of the node's output value based on its input values
	//and its logic function (specified by its NodeType).
	private void computeOutput()
	{
		switch (nType)
		{
			//Input node's output should be set manually.
			case IN:
				return;

			//Output node's output is the value of its input edge.
			case OUT:
				if (inValues.size() != 1)
				{
					System.err.println("Output node has too few or too many input edges.");
					return;
				}

				outValue = inValues.get(0);
				break;

			//NOT node's output is the complement of its input.
			case NOT:
				if (inValues.size() != 1)
				{
					System.err.println("NOT node has too few or too many input edges.");
					return;
				}

				outValue = !inValues.get(0);
				break;

			//AND node's output is true if all input edges' values are true.
			case AND:
				outValue = true;

				for (int i = 0; i < inValues.size(); i++)
				{
					if (!inValues.get(i))
					{
						outValue = false;
						break;
					}
				}

				break;

			//OR node's output is true if any input edges' values are true.
			case OR:
				outValue = false;

				for (int i = 0; i < inValues.size(); i++)
				{
					if (inValues.get(i))
					{
						outValue = true;
						break;
					}
				}

				break;

			//NAND node's output is a negation of an AND node's output.
			case NAND:
				outValue = false;

				for (int i = 0; i < inValues.size(); i++)
				{
					if (!inValues.get(i))
					{
						outValue = true;
						break;
					}
				}

				break;

			//NOR node's output is a negation of an OR node's output.
			case NOR:
				outValue = true;

				for (int i = 0; i < inValues.size(); i++)
				{
					if (inValues.get(i))
					{
						outValue = false;
						break;
					}
				}

				break;

			//XOR node's output is true if an odd number of input edges' values
			//are true.
			case XOR:
				int odd = 0;

				for (int i = 0; i < inValues.size(); i++)
				{
					if (inValues.get(i))
						odd++;
				}

				outValue = (odd % 2 == 1);
				break;

			default:
				System.err.println("Node is NULL type.");
		}
	}

	//Sends the output value to each connected output DAGEdge.
	private void sendOutput()
	{
		if (nType == NodeType.OUT)
			return;

		for (int i = 0; i < outputs.size(); i++)
			outputs.get(i).setValue(outValue);
	}

	//If all inputs are determined, calculates output and send it to output
	//edges. Returns false if an input is not yet determined.
	public Boolean send()
	{
		if (nType == NodeType.IN)
		{
			sendOutput();
			return true;
		}

		//Check for undetermined inputs.
		for (int i = 0; i < inputs.size(); i++)
		{
			if (!inputs.get(i).isDetermined())
				return false;
		}

		loadInputs();
		computeOutput();
		sendOutput();

		return true;
	}

	//Connects the given edge as an input edge to this node.
	public void connectInput(DAGEdge input)
	{
		inputs.add(input);
		input.connectOutput(this);
	}

	//Connects all of the given edges as input edges to this node.
	public void connectInput(ArrayList<DAGEdge> input)
	{
		inputs.addAll(input);

		for (int i = 0; i < input.size(); i++)
			input.get(i).connectOutput(this);
	}

	//Connects the given edge as an output edge to this node.
	public void connectOutput(DAGEdge output)
	{
		outputs.add(output);
		output.connectInput(this);
	}

	//Connects all of the given edges as output edges to this node.
	public void connectOutput(ArrayList<DAGEdge> output)
	{
		outputs.addAll(output);

		for (int i = 0; i < output.size(); i++)
			output.get(i).connectInput(this);
	}
}