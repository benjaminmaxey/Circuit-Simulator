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
	public Node target;
	public Boolean value;

	//Default constructor.
	public Event()
	{
		target = null;
	}

	//Constructor for known targets and delays.
	public Event(Node tar, Boolean set)
	{
		target = tar;
		value = set;
	}

	//Sends target's output value to its connected output edges, then updates
	//the output value of connected nodes. Returns ArrayList of Events that
	//must be scheduled as a result of running this Event.
	/*public ArrayList<Event> run()
	{
		
	}*/
}