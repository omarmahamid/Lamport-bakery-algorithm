package com.omar.treelock;

class TestDriver {
	private static void runBasicTest(int numThr, int numIncrements) {
		Thread threads[] = new Thread[numThr];
		Counter ctr = new Counter(numThr, numIncrements);

		for (int i = 0; i < numThr; i++) {
			threads[i] = new Thread(ctr);
			threads[i].start();
		}

		try {
			for (int i = 0; i < numThr; i++) {
				threads[i].join();
			}
		} catch (InterruptedException ex) {
			System.out.println("Something went wrong during joining.");
		}

		ctr.printCounterStatus();
	}

	public static void main(String args[]) throws InterruptedException {
		// Make sure to reset ThreadID after each test so that you can use ThreadID.get()
		// in your implementations.
		runBasicTest(2, 1000000);
		ThreadID.reset();

		// TODO: Write some more tests to make sure your implementation works
		// in all situations!
	}
}
