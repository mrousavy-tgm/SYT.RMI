package Modules;

import java.io.Serializable;
import java.math.BigInteger;

public class Fibonacci implements Task<BigInteger>, Serializable {
    private static final long serialVersionUID = 43L;
    private long _digits;

    public Fibonacci(long digits) {
        _digits = digits;
    }

    @Override
    public BigInteger run() {
        BigInteger previous = BigInteger.ONE;
        BigInteger recent = BigInteger.ONE;
        for (int i = 0; i < _digits; i++) {
            BigInteger temp = previous.add(recent);
            previous = recent;
            recent = temp;
        }
        return recent;
    }
}
