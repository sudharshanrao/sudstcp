package com.clearmarkets.cleartcp.scheduler;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PhilippeRoundRobinScheduler implements IScheduler{
	
	private final Comparator<Robin> robinComparator = new Comparator<Robin>() {
		@Override
		public int compare(Robin o1, Robin o2) {
			return Integer.compare(o2.getWeight(), o1.getWeight());
		}
	};
	
    private static final Logger logger = LoggerFactory.getLogger(PhilippeRoundRobinScheduler.class);
	private List<Robin> sortedListOfRobins = new LinkedList<>();
	private List<Runnable> listOfRunnables = new LinkedList<>();
	private int id;
	private int index = 0;
	private int highestWeight;
	/**
	 * schedules a particular thread/robin for the task. 
	 */
	@Override
	public Runnable schedule() {
		if(sortedListOfRobins.isEmpty()){
			return null;
		}
		Robin robin = sortedListOfRobins.get(index);
		index++;
		resetIndexIfAtEndOfLine();
		logger.debug("Scheduling {}", robin);
		return robin.getRunnable();
	}

	/**
	 * @param robin
	 */
	private void resetIndexIfAtEndOfLine() {
		if(index == sortedListOfRobins.size() || highestWeight > sortedListOfRobins.get(index).getWeight()){
			//reset index if index reaches max size of list or next robin's weight is less than the highest weight.
			index = 0;
		}
	}

	/**
	 * adds the robin to the queue and list. Note that the weight specified is not used, instead the object gets instantiated with default weight.
	 */
	@Override
	public void add(Runnable runnable, int weight) {
		Robin robin = new Robin(id++, runnable, weight);
		logger.debug("Added {}", robin);
		sortedListOfRobins.add(robin);
		Collections.sort(sortedListOfRobins, robinComparator);
		highestWeight = sortedListOfRobins.get(0).getWeight();
		listOfRunnables.add(runnable);
		System.out.println(highestWeight);
	}
	
	/**
	 * returns the entire list of threads. useful for only broadcast events which are sent to every thread.
	 */
	@Override
	public Collection<Runnable> scheduleAll() {
		return listOfRunnables;
	}
	
	/**
	 * remove the thread from the queue and the list. To be used on disconnect of the client socket.
	 */
	@Override
	public void remove(Runnable runnableToRemove) {
		Iterator<Robin> it = sortedListOfRobins.iterator();
		while(it.hasNext()) {
			Robin robin = it.next();
			if (robin.getRunnable() == runnableToRemove) {
				listOfRunnables.remove(robin.getRunnable());
				it.remove();
				logger.debug("Removing {}", robin);
				break;
			}
		}
		if (!sortedListOfRobins.isEmpty()) {
			highestWeight = sortedListOfRobins.get(0).getWeight();
			resetIndexIfAtEndOfLine();
		}
	}
}
