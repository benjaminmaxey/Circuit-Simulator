//*****************************************************************************
//	Author: Ben Maxey
//
//	Driver code for circuit simulator.
//*****************************************************************************

package circuitsim;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;

public class CircuitSim
{
	public static void main(String[] args) throws FileNotFoundException
	{
		if (args.length < 2)
		{
			System.err.println("At least two input files required.");
			return;
		}

		DAG test = new DAG(args[0]);
		Simulator sim = new Simulator(test, args[1]);
		System.out.println(args[0] + ": ");

		test.clear();
		test.build();
		test.initialize();

		sim.initialize();
		sim.run();
	}
}