package com.dsapotd.linkedlists;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Demonstration of StampedLock behavior in AllOne data structure.
 * This file shows how StampedLock allows concurrent reads while writes are happening,
 * without causing writer starvation like ReentrantReadWriteLock might.
 */
public class AllOneStampedLockDemo {

    public static void main(String[] args) throws InterruptedException {
        AllOneThreadSafe allOne = new AllOneThreadSafe();
        int numReaders = 5;
        int numWriters = 2;
        
        ExecutorService executor = Executors.newFixedThreadPool(numReaders + numWriters);
        AtomicInteger readOperations = new AtomicInteger(0);
        AtomicInteger writeOperations = new AtomicInteger(0);
        
        // Start Writers
        for (int i = 0; i < numWriters; i++) {
            final int writerId = i;
            executor.submit(() -> {
                try {
                    for (int j = 0; j < 1000; j++) {
                        String key = "key" + (j % 5);
                        if (j % 2 == 0) {
                            allOne.inc(key);
                        } else {
                            allOne.dec(key);
                        }
                        writeOperations.incrementAndGet();
                        // Small sleep to simulate work and allow thread interleaving
                        Thread.sleep(1);
                    }
                    System.out.println("Writer " + writerId + " completed.");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }
        
        // Start Readers
        for (int i = 0; i < numReaders; i++) {
            final int readerId = i;
            executor.submit(() -> {
                try {
                    for (int j = 0; j < 2000; j++) {
                        String max = allOne.getMaxKey();
                        String min = allOne.getMinKey();
                        readOperations.incrementAndGet();
                        // Small sleep to simulate work and allow thread interleaving
                        Thread.sleep(1);
                    }
                    System.out.println("Reader " + readerId + " completed.");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }
        
        executor.shutdown();
        boolean completed = executor.awaitTermination(10, TimeUnit.SECONDS);
        
        System.out.println("\n--- Execution Summary ---");
        System.out.println("All threads completed: " + completed);
        System.out.println("Total Write Operations: " + writeOperations.get());
        System.out.println("Total Read Operations: " + readOperations.get());
        System.out.println("Final Max Key: " + allOne.getMaxKey());
        System.out.println("Final Min Key: " + allOne.getMinKey());
        System.out.println("\nNotice how both readers and writers could progress concurrently.");
        System.out.println("With StampedLock, writes don't get permanently blocked by continuous reads (no writer starvation).");
    }
}
