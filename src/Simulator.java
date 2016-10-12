package circuitsim;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;

public class Simulator
{
	private String filePath;

	private ArrayList<ArrayList<Event>> wheel;
	private Circuit circuit;

	private int wheelLength;
	private int current;
	private int t = -1;

	public Simulator()
	{
		filePath = "";

		wheel = new ArrayList<ArrayList<Event>>();
		circuit = null;

		wheelLength = 0;
		current = 0;
	}

	public Simulator(Circuit cir, String file)
	{
		filePath = file;

		circuit = cir;
		wheelLength = circuit.getMaxDelay() + 3;
		wheel = new ArrayList<ArrayList<Event>>();
		for (int i = 0; i < wheelLength; i++)
			wheel.add(new ArrayList<Event>());

		current = 0;
	}

	public void initialize() throws FileNotFoundException
	{
		File file = new File(filePath);
		Scanner scan = new Scanner(file);

		scan.useDelimiter(" ");
		int time = scan.nextInt();

		while (scan.hasNextLine())
		{
			String id = scan.next();
			Boolean value = (scan.nextInt() > 0);

			Node node = circuit.find(id);
			Event event = new Event(node, value);

			wheel.get(time % wheelLength).add(event);
		}
	}

	public void run()
	{
		current = 0;
		int done = wheelLength;

		while (done > 0)
		{
			t++;
			System.out.println("t = " + t + ":");
			if (wheel.get(current).size() == 0)
			{
				System.out.println("\tNo events scheduled.");
				if (++current == wheelLength)
					current = 0;
				done--;
				continue;
			}

			ArrayList<Event> eventList = wheel.get(current);
			int numEvents = eventList.size();
			while (eventList.size() > 0)
			{
				Node currentNode = eventList.get(0).target;
				Boolean newValue = eventList.get(0).value;
				currentNode.update(newValue);

				System.out.println("\tNode " + currentNode.getID() +
					" transitions to " + newValue);

				ArrayList<Node> fanout = currentNode.getOutputs();
				for (int i = 0; i < fanout.size(); i++)
				{
					Node currFanout = fanout.get(i);
					if (!currFanout.loadInputs())
						continue;
					Boolean potentialVal = currFanout.computeOutput();

					if (currFanout.lastSchedVal != potentialVal)
					{
						int delay;

						if (potentialVal)
							delay = currFanout.riseDel;
						else
							delay = currFanout.fallDel;

						int schedTime = (current + delay) % wheelLength;
						if (currFanout.lastSchedTime == schedTime)
							cancel(currFanout, schedTime);

						wheel.get(schedTime).add(new Event(currFanout, potentialVal));
						currFanout.lastSchedVal = potentialVal;
						currFanout.lastSchedTime = schedTime;
					}
				}

				eventList.remove(0);
			}
			System.out.println("\n\tTotal number of transitions: " + numEvents);
			System.out.println();

			if (++current == wheelLength)
				current = 0;
			done = wheelLength;
		}
	}

	public void cancel(Node target, int time)
	{
		ArrayList<Event> potential = wheel.get(time);
		String matchID = target.getID();

		for (int i = 0; i < potential.size(); i++)
		{
			String id = potential.get(i).target.getID();
			if (id.equals(matchID))
				potential.remove(i);
		}
	}
}