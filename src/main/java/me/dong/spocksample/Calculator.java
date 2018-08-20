package me.dong.spocksample;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by ethan.kim on 2018. 8. 19..
 */
public class Calculator {

    public static long calculate(long amount, float rate, RoundingMode roundingMode) {
        if (amount < 0) {
            throw new NegativeNumberNotAllowException();
        }

        return BigDecimal.valueOf(amount * rate * 0.01)
                .setScale(0, roundingMode).longValue();
    }
}
