package com.omar.treelock;


class Counter implements Runnable {

	private int tCount = 0;
	private int bCount = 0;

	private final BakeryAlg bLock;

	private final int numIncrementsPerThr;

	private final int expectedTotalIncrements;


	Counter(int numThr, int numIncrements) {
		this.numIncrementsPerThr = numIncrements / numThr;
		this.expectedTotalIncrements = numIncrementsPerThr * numThr;
		bLock = new BakeryAlg(numThr);
	}

	@Override
	public void run() {
		for (int i = 0; i < numIncrementsPerThr; i++) {
			bLock.lock();
			bCount++;
			bLock.unlock();
		}
	}

	public void printCounterStatus() {
		String bSymbol = expectedTotalIncrements == bCount ? "✅" : "❌";

		System.out.printf("⭐ Expected count: %d\n", expectedTotalIncrements);
		System.out.printf(bSymbol + " Bakery count: %d\n", bCount);
	}
}
