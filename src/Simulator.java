package circuitsim;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;

public class Simulator
{
	private String filePath;
	private ArrayList<ArrayList<Event>> wheel;
	private DAG graph;
	private int wheelLength;
	private int current;
	private int t = -1;

	public Simulator()
	{
		wheel = null;
		graph = null;
		wheelLength = 0;
		current = 0;
	}

	public Simulator(DAG circuit, String file)
	{
		filePath = file;
		graph = circuit;
		wheelLength = graph.getMaxDelay() + 3;
		wheel = new ArrayList<ArrayList<Event>>();
		for (int i = 0; i < wheelLength; i++)
			wheel.add(new ArrayList<Event>());
		current = 0;
	}

	public void initialize() throws FileNotFoundException
	{
		File file = new File(filePath);
		Scanner scan = new Scanner(file);

		try
		{
			while (scan.hasNextLine())
			{
				int id = scan.nextInt() - 1;
				int set = scan.nextInt();
				Boolean value = (set > 0);

				graph.setInput(id, value);
				wheel.get(current).add(new Event(graph.get(id), 0));
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void schedule(Event next)
	{
		int delay = next.getDelay();
		wheel.get((current + delay) % wheelLength).add(next);
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
			while (eventList.size() > 0)
			{
				for (int i = 0; i < eventList.size(); i++)
				{
					ArrayList<Event> newEvents = eventList.get(i).run();
					for (int j = 0; j < newEvents.size(); j++)
						schedule(newEvents.get(i));
					eventList.remove(0);
				}
			}

			if (++current == wheelLength)
				current = 0;
			done = wheelLength;
		}
	}
}