package com.scheduler;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import junit.framework.Assert;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cleartcp.scheduler.CustomRoundRobinScheduler;
import com.cleartcp.scheduler.IScheduler;
import com.cleartcp.scheduler.PhilippeRoundRobinScheduler;

public class RaceTest {
	private static final Logger logger = LoggerFactory.getLogger(RaceTest.class);

	private static class RaceResult {
		public String testname;
		public long start;
		public long adds;
		public long schedules;
		public long removes;
		public long numberOfTests;
		public long starttook;
		public long addstook;
		public long schedulestook;
		public long removestook;
		public long startToFinish;

		public RaceResult(String testname) {
			this.testname = testname;
		}

		public void log() {
			logger.debug("{} Results #########################################", testname);
			logger.debug("MAX_RUNNABLES:" + MAX_RUNNABLES);
			logger.debug("MAX_SCHEDULE:" + MAX_SCHEDULE);
			logger.debug("Start: {}", start);
			logger.debug("Finished Adds: {} took {}", adds, adds - start);
			logger.debug("Finished Schedules: {} took {}", schedules, schedules - adds);
			logger.debug("Finished Removes: {} took {}", removes, removes - schedules);
			logger.debug("Start-To-Finish took {}", removes - start);
		}
		
		public void logtooks() {
			logger.debug("{} Results #########################################", testname);
			logger.debug("MAX_RUNNABLES:" + MAX_RUNNABLES);
			logger.debug("MAX_SCHEDULE:" + MAX_SCHEDULE);
			logger.debug("Finished Adds took {}", addstook);
			logger.debug("Finished Schedules took {}", schedulestook);
			logger.debug("Finished Removes took {}", removestook);
			logger.debug("Start-To-Finish took {}", startToFinish);
		}
	}

	static List<RaceResult> raceResults = new ArrayList<RaceResult>();

	private static final int MAX_RACES = 10;
	private static final int MAX_RUNNABLES = 4;
	private static final int MAX_SCHEDULE = 10000;

	static List<Runnable> rl = new LinkedList<Runnable>();
	static List<Integer> wl = new LinkedList<Integer>();
	static Random rand = new Random();

	@BeforeClass
	public static void setUp() {
		for (int i = 0; i < MAX_RUNNABLES; i++) {
			rl.add(new Runnable() {
				@Override
				public void run() {
					System.nanoTime();
				}
			});
			wl.add(rand.nextInt(MAX_RUNNABLES));
		}
	}

	@AfterClass
	public static void printResults() {
		List<RaceResult> avgrclist = new ArrayList<RaceResult>();
		avgrclist.add(new RaceResult("CUSTOM"));
		avgrclist.add(new RaceResult("PHILIPPE"));
		for (RaceResult rc : raceResults) {
			rc.log();
			for (RaceResult avgrc : avgrclist) {
				if (avgrc.testname.equals(rc.testname)) {
					avgrc.addstook += TimeUnit.NANOSECONDS.toMillis(rc.adds - rc.start);
					avgrc.schedulestook += TimeUnit.NANOSECONDS.toMillis(rc.schedules - rc.adds);
					avgrc.removestook += TimeUnit.NANOSECONDS.toMillis(rc.removes - rc.schedules);
					avgrc.startToFinish += TimeUnit.NANOSECONDS.toMillis(rc.removes - rc.start);
					avgrc.numberOfTests++;
					break;
				}
			}
		}

		for (RaceResult avgrc : avgrclist) {
			avgrc.starttook = avgrc.starttook / avgrc.numberOfTests;
			avgrc.addstook = avgrc.addstook / avgrc.numberOfTests;
			avgrc.schedulestook = avgrc.schedulestook / avgrc.numberOfTests;
			avgrc.removestook = avgrc.removestook / avgrc.numberOfTests;
			avgrc.testname = "AVG " + avgrc.testname;
			avgrc.logtooks();
		}
	}

	@Test
	public void raceCustom() {
		for (int i = 0; i < MAX_RACES; i++) {
			IScheduler scheduler = new CustomRoundRobinScheduler();
			race(scheduler, "CUSTOM");
		}
	}

	@Test
	public void racePhilippe() {
		for (int i = 0; i < MAX_RACES; i++) {
			IScheduler scheduler = new PhilippeRoundRobinScheduler();
			race(scheduler, "PHILIPPE");
		}
	}

	/**
	 * @param scheduler
	 * @param rl
	 * @param wl
	 */
	private void race(IScheduler scheduler, String testname) {
		RaceResult rc = new RaceResult(testname);
		logger.debug("Start");
		rc.start = System.nanoTime();
		for (int i = 0; i < MAX_RUNNABLES; i++) {
			scheduler.add(rl.get(i), wl.get(i));
		}
		rc.adds = System.nanoTime();
		for (int i = 0; i < MAX_SCHEDULE * 100; i++) {
			scheduler.schedule().run();
		}
		rc.schedules = System.nanoTime();
		for (int i = 0; i < MAX_RUNNABLES; i++) {
			scheduler.remove(rl.get(i));
		}
		rc.removes = System.nanoTime();
		logger.debug("Finished");
		Assert.assertNull(scheduler.schedule());
		raceResults.add(rc);
	}
}
