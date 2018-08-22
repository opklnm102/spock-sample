package me.dong.spocksample;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Created by ethan.kim on 2018. 8. 20..
 */
@NoArgsConstructor
public class OrderSheet {

    public static final OrderSheet EMPTY = new OrderSheet();

    @Getter
    private String orderType;

    @Getter
    private long totalOrderAmount;

    @Builder
    public OrderSheet(String orderType, long totalOrderAmount) {
        this.orderType = orderType;
        this.totalOrderAmount = totalOrderAmount;
    }
}
