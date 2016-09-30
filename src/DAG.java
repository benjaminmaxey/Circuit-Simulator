//*****************************************************************************
//	Author: Ben Maxey
//
//	DAG contains the set of nodes and edges that make up the logic circuit, and
//	(currently) handles simulation of the circuit.
//*****************************************************************************

package circuitsim;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;

public class DAG
{
	private String filePath;
	private ArrayList<DAGNode> nodeList;
	private ArrayList<Integer> inputs;
	private ArrayList<Integer> outputs;
	private int maxDelay;

	//Default constructor.
	public DAG()
	{
		nodeList = new ArrayList<DAGNode>();
		inputs = new ArrayList<Integer>();
		outputs = new ArrayList<Integer>();
	}

	public DAG(String file)
	{
		nodeList = new ArrayList<DAGNode>();
		inputs = new ArrayList<Integer>();
		outputs = new ArrayList<Integer>();
		filePath = file;
	}

	public void clear()
	{
		nodeList.clear();
		inputs.clear();
		outputs.clear();
	}

	//Initializes the DAG based on specifications listed in a text file. This
	//method takes the path to the specification file as a parameter.
	public void build() throws FileNotFoundException
	{
		File file = new File(filePath);
		Scanner scan = new Scanner(file);
		int nodeID = 0;

		try
		{
			while (scan.hasNextLine())
			{
				String buffer = scan.next();
				
				//If the next token is a nodeType, create a new node of that
				//nodeType.
				if (Character.isUpperCase(buffer.charAt(0)))
				{
					NodeType type = NodeType.matchString(buffer);
					DAGNode newNode = new DAGNode(type);
					nodeList.add(newNode);

					if (buffer.equals("IN"))
					{
						inputs.add(nodeID);

						buffer = scan.next();
						if (buffer.equals("0"))
							newNode.setInput(false);
						else if (buffer.equals("1"))
							newNode.setInput(true);
					}

					if (buffer.equals("OUT"))
						outputs.add(nodeID);

					nodeID++;
				}
				//If the next token is a number, connect the nodes at the
				//indices given by this number and the next.
				else if (Character.isDigit(buffer.charAt(0)))
				{
					int first = Integer.parseInt(buffer);
					buffer = scan.next();
					int second = Integer.parseInt(buffer);

					DAGEdge connector = new DAGEdge();
					nodeList.get(first - 1).connectOutput(connector);
					nodeList.get(second - 1).connectInput(connector);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		scan.close();
	}

	//Returns the maximum delay of all nodes in the graph.
	public int getMaxDelay()
	{
		int max = 0;

		for (int i = 0; i < nodeList.size(); i++)
		{
			int current = nodeList.get(i).getDelay();
			if (current > max)
				max = current;
		}

		return max;
	}

	//Simulates the logic circuit. After running this method, all output nodes
	//should have their correct values.
	public void initialize()
	{
		//ArrayList holds indices of nodes that have not calculated and sent
		//outputs.
		ArrayList<Integer> remaining = new ArrayList<Integer>();
		for (int i = 0; i < nodeList.size(); i++)
			remaining.add(i);

		//Begin by sending all input values to their connecting edges.
		for (int i = 0; i < inputs.size(); i++)
		{
			nodeList.get(inputs.get(i)).update();
			nodeList.get(inputs.get(i)).sendOutput();
		}
		for (int i = 0; i < inputs.size(); i++)
			remaining.remove(Integer.valueOf(inputs.get(i)));

		//Loop until all nodes have computed their output values.
		while (remaining.size() > 0)
		{
			for (int i = 0; i < remaining.size(); i++)
			{
				if (nodeList.get(remaining.get(i)).update())
				{
					nodeList.get(remaining.get(i)).sendOutput();
					System.out.println("Node: " + (remaining.get(i) + 1) + ", Output: " + nodeList.get(remaining.get(i)).getOutput());
					remaining.remove(i);
				}
			}
		}
	}

	//Changes the value of an input node.
	public void setInput(int id, Boolean value)
	{
		DAGNode current = nodeList.get(id);
		if (current.getType() != NodeType.IN)
			return;

		current.setInput(value);
	}

	//Allows access to a node in the graph.
	public DAGNode get(int id)
	{
		return nodeList.get(id);
	}

	//Returns an ArrayList with the values of all output nodes.
	public ArrayList<Boolean> getOutputs()
	{
		ArrayList<Boolean> results = new ArrayList<Boolean>();

		for (int i = 0; i < outputs.size(); i++)
			results.add(nodeList.get(outputs.get(i)).getOutput());

		return results;
	}
}