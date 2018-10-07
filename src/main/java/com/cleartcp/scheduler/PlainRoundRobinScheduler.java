package com.clearmarkets.cleartcp.scheduler;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * implementation class for plain round-robin. The ready queue is treated as a circular queue.
 * The algorithm returns a thread to the server in the ready queue in order, handling all processes without priority.
 * @author Sudharshan
 *
 */

public class PlainRoundRobinScheduler implements IScheduler {
	
    private static final Logger logger = LoggerFactory.getLogger(PlainRoundRobinScheduler.class);
	private Queue<Robin> queueOfRobins = new LinkedBlockingQueue<>();
	private List<Runnable> listOfRunnables = new LinkedList<>();
	private int id;
	
	/**
	 * schedules a particular thread/robin for the task. 
	 */
	@Override
	public Runnable schedule() {
		if(queueOfRobins.isEmpty()){
			return null;
		}
		Robin robin = null;
		if(queueOfRobins.size() == 1){
			//if size is 1 then just retrieve don't remove the head.
			robin = queueOfRobins.peek();
		}
		else{
			//retrieve and remove from head of queue.
			robin = queueOfRobins.poll();
			//add it back to the end of the queue.
			queueOfRobins.add(robin);
		}
		logger.debug("Scheduling {}", robin);
		return robin.getRunnable();
	}

	/**
	 * adds the robin to the queue and list. Note that the weight specified is not used, instead the object gets instantiated with default weight.
	 */
	@Override
	public void add(Runnable runnable, int weight) {
		Robin robin = new Robin(id++, runnable);
		logger.debug("Added {}", robin);
		queueOfRobins.add(robin);
		listOfRunnables.add(runnable);
	}
	
	/**
	 * returns the entire list of threads. useful for only cache events which are sent to every thread.
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
		for(Robin robin: queueOfRobins){
			if(robin.getRunnable() == runnableToRemove){
				queueOfRobins.remove(robin);
				listOfRunnables.remove(runnableToRemove);
				logger.debug("Removing {}", robin);
			}
		}
	}
}