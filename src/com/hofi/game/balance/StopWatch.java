package com.hofi.game.balance;

public class StopWatch {
	private long startTime = 0;
	private long stopTime = 0;
	private long elapsed = 0;
	private boolean running = false;

	public void start() {
		startTime = System.nanoTime();
		running = true;
	}

	public void stop() {
		running = false;
		stopTime = System.nanoTime();
		elapsed += stopTime - startTime;
	}

	public void reset() {
		running = false;
		elapsed = 0;
		startTime = System.nanoTime();
		stopTime = startTime;
	}

	public long getElapsedTimeMilli() {
		if (running) {
			return ((elapsed + System.nanoTime() - startTime) / 1000000);
		}
		return elapsed / 1000000;
	}

}
