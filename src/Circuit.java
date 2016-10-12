//*****************************************************************************
//	Author: Ben Maxey
//
//	The Circuit class is responsible for parsing input Verilog files, building
//	and connecting nodes based on the input files, and maintaining information
//	on the structure of the circuit.
//*****************************************************************************

package circuitsim;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;

public class Circuit
{
	private String filePath;

	private ArrayList<Node> nodeList;
	private ArrayList<Node> inputs;
	private ArrayList<Node> outputs;

	//Default constructor.
	public Circuit()
	{
		nodeList = new ArrayList<Node>();
		inputs = new ArrayList<Node>();
		outputs = new ArrayList<Node>();
	}

	//Constructor for known file.
	public Circuit(String file)
	{
		filePath = file;

		nodeList = new ArrayList<Node>();
		inputs = new ArrayList<Node>();
		outputs = new ArrayList<Node>();
	}

	//Resets the circuit by removing node lists.
	public void clear()
	{
		nodeList.clear();
		inputs.clear();
		outputs.clear();
	}

	//Declares input, output, and wire nodes given a list of node names. The
	//decType parameter should be "input", "output", or "wire".
	public void parseIOW(String content, String decType)
	{
		int begin = 0;
		for (int i = 0; i < content.length(); i++)
		{
			if (i == content.length() - 1 || content.charAt(i) == ',')
			{
				String name;

				if (i == content.length() - 1)
					name = content.substring(begin, i + 1).trim();
				else
					name = content.substring(begin, i).trim();

				NodeType type = NodeType.matchString(decType);
				Node addition = new Node(name, type);
				nodeList.add(addition);

				if (decType.equals("input"))
					inputs.add(addition);
				else if (decType.equals("output"))
					outputs.add(addition);

				begin = i + 1;
			}
		}
	}

	//Declares and connects a gate node. The name and type parameters are the
	//name and type of the new gate node.
	public void parseGate(String content, String name, NodeType type)
	{
		Node addition = new Node(name, type);

		Boolean first = true;
		int begin = 0;

		for (int i = 0; i < content.length(); i++)
		{
			if (i == content.length() - 1 || content.charAt(i) == ',')
			{
				String connectID;

				if (i == content.length() - 1)
					connectID = content.substring(begin, i + 1).trim();
				else
					connectID = content.substring(begin, i).trim();

				Node connect = find(connectID);

				if (first)
				{
					addition.connectOutput(connect);
					first = false;
				}
				else
					addition.connectInput(connect);

				begin = i + 1;
			}
		}

		nodeList.add(addition);
	}

	//Builds the circuit from the supplied Verilog file.
	public void build() throws FileNotFoundException
	{
		File file = new File(filePath);
		Scanner scan = new Scanner(file);

		String buffer;
		while (scan.hasNext())
		{
			buffer = scan.next();

			if (buffer.equals("input") || buffer.equals("output") ||
				buffer.equals("wire"))
			{
				String decType = buffer;
				scan.useDelimiter(";");
				buffer = scan.next();

				parseIOW(buffer, decType);

				scan.useDelimiter("\\s");
			}
			else if (NodeType.matchString(buffer) != NodeType.NULL)
			{
				NodeType type = NodeType.matchString(buffer);

				scan.useDelimiter("\\(");
				buffer = scan.next();
				String name = buffer.trim();

				scan.useDelimiter("\\);");
				scan.skip("\\(");
				buffer = scan.next();

				parseGate(buffer, name, type);

				scan.useDelimiter("\\s");
			}
		}
	}

	public void display()
	{
		for (int i = 1; i <= nodeList.size(); i++)
		{
			System.out.println("Node " + i + ": " + nodeList.get(i - 1).getID());
		}
		System.out.println("Inputs: ");
		for (int i = 0; i < inputs.size(); i++)
		{
			System.out.print(inputs.get(i).getID() + " ");
		}
		System.out.println("Outputs: ");
		for (int i = 0; i < outputs.size(); i++)
		{
			System.out.print(outputs.get(i).getID() + " ");
		}
	}

	//Returns the node with the given ID.
	public Node find(String name)
	{
		for (int i = 0; i < nodeList.size(); i++)
		{
			if (nodeList.get(i).getID().equals(name))
				return nodeList.get(i);
		}

		return null;
	}

	//Returns the maximum delay across all nodes in the graph.
	public int getMaxDelay()
	{
		int max = 0;

		for (int i = 0; i < nodeList.size(); i++)
		{
			int current = nodeList.get(i).riseDel;
			if (current > max)
				max = current;

			current = nodeList.get(i).fallDel;
			if (current > max)
				max = current;
		}

		return max;
	}
}