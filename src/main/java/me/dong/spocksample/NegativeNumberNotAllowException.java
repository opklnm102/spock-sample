package me.dong.spocksample;

/**
 * Created by ethan.kim on 2018. 8. 20..
 */
public class NegativeNumberNotAllowException extends RuntimeException {

    public NegativeNumberNotAllowException() {
        super("음수는 계산할 수 없습니다");
    }
}
