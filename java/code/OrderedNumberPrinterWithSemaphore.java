package code;

import java.util.concurrent.Semaphore;

/**
 * This class demonstrates printing numbers in sequence using two threads
 * and two Semaphores for synchronization.
 */
public class OrderedNumberPrinterWithSemaphore {

    private final int max;
    // Semaphore for the odd thread, starts with 1 permit to allow it to run first.
    private final Semaphore oddSemaphore = new Semaphore(1);
    // Semaphore for the even thread, starts with 0 permits, forcing it to wait.
    private final Semaphore evenSemaphore = new Semaphore(0);

    public OrderedNumberPrinterWithSemaphore(int max) {
        this.max = max;
    }

    /**
     * Prints odd numbers from 1 up to max.
     */
    public void printOdd() {
        for (int i = 1; i <= max; i += 2) {
            try {
                // Acquire a permit from the odd semaphore. Blocks until one is available.
                oddSemaphore.acquire();
                System.out.println(Thread.currentThread().getName() + ": " + i);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Odd thread interrupted.");
            } finally {
                // Release a permit to the even semaphore, allowing it to run.
                evenSemaphore.release();
            }
        }
    }

    /**
     * Prints even numbers from 2 up to max.
     */
    public void printEven() {
        for (int i = 2; i <= max; i += 2) {
            try {
                // Acquire a permit from the even semaphore. Blocks until one is available.
                evenSemaphore.acquire();
                System.out.println(Thread.currentThread().getName() + ": " + i);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Even thread interrupted.");
            } finally {
                // Release a permit back to the odd semaphore, allowing it to run next.
                oddSemaphore.release();
            }
        }
    }

    public static void main(String[] args) {
        final int MAX_NUMBER = 10;
        OrderedNumberPrinterWithSemaphore printer = new OrderedNumberPrinterWithSemaphore(MAX_NUMBER);

        Thread oddThread = new Thread(printer::printOdd, "OddThread");
        Thread evenThread = new Thread(printer::printEven, "EvenThread");

        oddThread.start();
        evenThread.start();
    }
}
