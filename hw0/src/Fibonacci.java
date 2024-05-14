	/**
	 * This is part of HW0: Environment Setup and Java Introduction.
	 */
	package hw0;

	import java.util.ArrayList;

	/**
	 * Fibonacci calculates the <var>n</var>th term in the Fibonacci sequence.
	 *
	 * The first two terms of the Fibonacci sequence are 0 and 1,
	 * and each subsequent term is the sum of the previous two terms.
	 *
	 */
	public class Fibonacci {
	
	    /**
	     * Calculates the desired term in the Fibonacci sequence.
	     *
	     * @param n the index of the desired term; the first index of the sequence is 0
	     * @return the <var>n</var>th term in the Fibonacci sequence
	     * @throws IllegalArgumentException if <code>n</code> is not a nonnegative number
	     */

		ArrayList<Long> fib_list = new ArrayList<Long>();
	    public long getFibTerm(int n) {
			if (n < 0) {
				throw new IllegalArgumentException(n + " is negative");
			} else if (fib_list.size() > n) {
				return fib_list.get(n);
			} else if (n == 0) {
				fib_list.add(0L);
				return 0;
			} else if (n == 1 || n == 2) {
				fib_list.add(1L);
				return 1;
			} else {
				fib_list.add(getFibTerm(n - 1) + getFibTerm(n - 2));
				return fib_list.get(n);
			}

	    }
	}
