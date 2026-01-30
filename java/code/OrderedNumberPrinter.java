package code;

/**
 * This class demonstrates how to print numbers in sequence using two threads,
 * one for odd numbers and one for even numbers.
 */
public class OrderedNumberPrinter {

    private int count = 1;
    private final int max;
    private final Object lock = new Object();

    public OrderedNumberPrinter(int max) {
        this.max = max;
    }

    /**
     * Prints odd numbers. This method will be executed by the odd thread.
     */
    public void printOdd() {
        synchronized (lock) {
            while (count < max) {
                // Wait if the current number is even.
                while (count % 2 == 0) {
                    try {
                        // Release the lock and wait.
                        lock.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        System.err.println("Odd thread interrupted.");
                    }
                }
                // If we are here, it means count is odd.
                if (count <= max) {
                    System.out.println(Thread.currentThread().getName() + ": " + count);
                    count++;
                    // Notify the other thread (even thread) that it can proceed.
                    lock.notifyAll();
                }
            }
        }
    }

    /**
     * Prints even numbers. This method will be executed by the even thread.
     */
    public void printEven() {
        synchronized (lock) {
            while (count <= max) {
                // Wait if the current number is odd.
                while (count % 2 != 0) {
                    try {
                        // Release the lock and wait.
                        lock.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        System.err.println("Even thread interrupted.");
                    }
                }
                // If we are here, it means count is even.
                if (count <= max) {
                    System.out.println(Thread.currentThread().getName() + ": " + count);
                    count++;
                    // Notify the other thread (odd thread) that it can proceed.
                    lock.notifyAll();
                }
            }
        }
    }

    public static void main(String[] args) {
        // We will print numbers up to 10.
        final int MAX_NUMBER = 10;
        OrderedNumberPrinter printer = new OrderedNumberPrinter(MAX_NUMBER);

        // Create the thread for printing odd numbers.
        Thread oddThread = new Thread(printer::printOdd, "OddThread");

        // Create the thread for printing even numbers.
        Thread evenThread = new Thread(printer::printEven, "EvenThread");

        // Start both threads.
        oddThread.start();
        evenThread.start();
    }
}
