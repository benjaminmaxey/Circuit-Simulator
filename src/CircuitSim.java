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
		Circuit test = new Circuit("testCircuits/ISCAS85/c432.v");
		test.build();
		Simulator sim = new Simulator(test, "sampleSimulation.txt");
		sim.initialize();
		sim.run();
	}
}