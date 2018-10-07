package com.cleartcp.scheduler;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * implementation class for custom round-robin. If same weights, then do plain
 * round-robin otherwise, higher weights get scheduled always. Lower weight
 * thread is a backup to the higher weights i.e. gets scheduled only when higher
 * weighted thread(s) are down.
 * 
 * @author Sudharshan
 *
 */
public class CustomRoundRobinScheduler implements IScheduler {
    private static final Logger logger = LoggerFactory.getLogger(CustomRoundRobinScheduler.class);
	private Queue<Robin> queueOfRobins = new LinkedBlockingQueue<>();
	private List<Runnable> listOfRunnables = new LinkedList<Runnable>();
	private int id;
	private int highestWeight;
	
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
		} else {
			robin = queueOfRobins.poll();
			while(robin.getWeight() != highestWeight){
				//add it back to the end of the queue.
				queueOfRobins.add(robin);
				robin = queueOfRobins.poll();
			}
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
		Robin robin = new Robin(id++, runnable, weight);
		logger.debug("Added {}", robin);
		queueOfRobins.add(robin);
		setHighestWeight(robin);
		synchronized(listOfRunnables){
			listOfRunnables.add(runnable);
		}
	}
	
	/**
	 * returns the entire list of threads. useful for only broadcast events which are sent to every thread.
	 */
	@Override
	public Collection<Runnable> scheduleAll() {
		synchronized(listOfRunnables){
			return listOfRunnables;
		}
	}
	
	/**
	 * remove the thread from the queue and the list. To be used on disconnect of the client socket.
	 */
	@Override
	public void remove(Runnable runnableToRemove) {
		Iterator<Robin> it = queueOfRobins.iterator();
		while(it.hasNext()) {
			Robin robin = it.next();
			if (robin.getRunnable() == runnableToRemove) {
				synchronized(listOfRunnables){
					listOfRunnables.remove(robin.getRunnable());
				}
				it.remove();
				if(robin.getWeight() == highestWeight){
					findAndUpdateNextHighestWeight();
				}
				logger.debug("Removing {}", robin);
				break;
			}
		}
	}

	private void findAndUpdateNextHighestWeight() {
		for(Robin robin: queueOfRobins){
			setHighestWeight(robin);
		}
	}

	/**
	 * @param robin
	 */
	private void setHighestWeight(Robin robin) {
		if(highestWeight < robin.getWeight()){
			highestWeight = robin.getWeight();
		}
	}
}
