package com.omar.treelock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

class BakeryAlg implements Lock {

	private final AtomicInteger[] ticket;
	private final AtomicBoolean[] choosing;
	private final int n;

	public BakeryAlg(int n) {
		this.n = n;
		this.ticket = new AtomicInteger[n];
		this.choosing = new AtomicBoolean[n];

		for (int i = 0; i < n; i++) {
			ticket[i] = new AtomicInteger(0);
			choosing[i] = new AtomicBoolean(false);
		}
	}

	@Override
	public void lock() {
		int id = ThreadID.get();
		choosing[id].set(true);

		int maxTicket = 0;
		for (int i = 0; i < n; i++) {
			int ticket_i = ticket[i].get();
			maxTicket = Math.max(ticket_i, maxTicket);
		}

		ticket[id].set(maxTicket + 1);
		choosing[id].set(false);

		for (int i = 0; i < n; i++) {
			if (i != id) {
				while (choosing[i].get()) {
					Thread.yield();
				}

				while ((ticket[i].get() != 0) && (ticket[id].get() > ticket[i].get() || (ticket[id].get() == ticket[i].get() && id > i))) {
					Thread.yield();
				}
			}
		}
	}

	@Override
	public void unlock() {
		int id = ThreadID.get();
		ticket[id].set(0);
	}

	// The compiler wants these declarations since BakeryAlg implements Lock
	@Override
	public void lockInterruptibly() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Condition newCondition() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean tryLock() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean tryLock(long time, TimeUnit unit) {
		throw new UnsupportedOperationException();
	}
}
