
package client;

import compute.Task;
import java.io.Serializable;
import java.math.BigDecimal;

public class Fibonacci implements Task<BigDecimal>, Serializable {

    private static final long serialVersionUID = 227L;

    private final int count;
    
    /**
     * Construct a task to calculate fibonacci to the specified
     * precision.
     */
    public Fibonacci(int count) {
        this.count = count;
    }

    /**
     * Calculate fibonacci.
     */
    public BigDecimal execute() {
        return computeFib(count);
    }

    public static BigDecimal computeFib(int count) {
    	BigDecimal fib0 = new BigDecimal(0);
    	BigDecimal fib1 = new BigDecimal(1);
    	
    	for(int i = 2; i < count; i++) {
    		BigDecimal temp = fib1;
    		fib1 = fib1.add(fib0);
    		fib0 = temp;
    	}
    	return fib1;
    }
}
