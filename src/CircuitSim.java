package circuitsim;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;

public class CircuitSim
{
	public static void main(String[] args) throws FileNotFoundException
	{
		DAG test = new DAG();
		test.initialize("example.csim");
		test.simulate();

		ArrayList<Boolean> results = test.getOutputs();

		for (int i = 0; i < results.size(); i++)
			System.out.println(results.get(i));
	}
}