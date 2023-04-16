package com.omar.treelock;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/*
	A working implementation of the Peterson Locking Algorithm described in
	the book. Notice that the flag array is no longer a boolean[], and victim
	has an extra "volatile" keyword. The reason for this change can be found 
	in the book right when it shifts discussion from theory to practice--
	basically the reason is because of caching and the Java memory model.

	Note that you can feel free to add whatever fields, functions, constructors,
	etc that you feel are necessary for your TreeLock implementation to work.
	You may even modify the contents of the given lock() and unlock() functions,
	however, be cautious about what you do as the lock may break.

	To test to see if your PetersonNode is still sane after you modify it, 
	feel free to compile this class with your Counter project and have it guard 
	the critical section there.

*/
class PetersonNode {
	private final AtomicBoolean[] flag = new AtomicBoolean[2];
	private final AtomicInteger lockOwnerID = new AtomicInteger(-1);

	private volatile int victim;
	public int index;

	PetersonNode(int index) {
		this.index = index;
		for (int i = 0; i < flag.length; i++) {
			flag[i] = new AtomicBoolean(false);
		}
	}

	public int lockOwnerID() {
		return lockOwnerID.get();
	}

	public void lock(int threadID) {
		int i = threadID % 2;
		int j = 1 - i;

		flag[i].set(true);
		victim = i;
		while (flag[j].get() && victim == i)
			;
		// This CAS call is not in the textbook, since the textbook algorithm
		// doesn't maintain a lockOwnerID field.
		while (!lockOwnerID.compareAndSet(-1, threadID))
			; // winner == lockOwner
	}

	public void unlock(int threadID) {
		int i = threadID % 2;
		while (!lockOwnerID.compareAndSet(threadID, -1))
			; // unset lockOwner
		flag[i].set(false);
	}
}
