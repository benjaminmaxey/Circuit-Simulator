//*****************************************************************************
//	Author: Ben Maxey
//
//	An Event indicates that a given target node should send its output value to
//	its connected output edges.  Running an Event produces a list of Events
//	that must be scheduled.
//*****************************************************************************

package circuitsim;

import java.util.ArrayList;

public class Event
{
	private DAGNode target;
	private int delay;

	//Default constructor.
	public Event()
	{
		target = null;
	}

	//Constructor for known targets and delays.
	public Event(DAGNode initialTar, int initialDel)
	{
		target = initialTar;
		delay = initialDel;
	}

	//Returns delay.
	public int getDelay()
	{
		return delay;
	}

	//Sends target's output value to its connected output edges, then updates
	//the output value of connected nodes. Returns ArrayList of Events that
	//must be scheduled as a result of running this Event.
	public ArrayList<Event> run()
	{
		ArrayList<Event> scheduled = new ArrayList<Event>();

		//Send current output value of target to its connected edges.
		target.sendOutput();

		ArrayList<DAGEdge> affectedEdges = target.getOutputs();
		ArrayList<DAGNode> affectedNodes = new ArrayList<DAGNode>();
		for (int i = 0; i < affectedEdges.size(); i++)
			affectedNodes.add(affectedEdges.get(i).getEnd());

		//Update affected nodes' outputs and add them to list of scheduled
		//events.
		for (int i = 0; i < affectedNodes.size(); i++)
		{
			DAGNode current = affectedNodes.get(i);
			current.update();

			scheduled.add(new Event(current, current.getDelay()));
		}

		return scheduled;
	}
}