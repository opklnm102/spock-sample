package me.dong.spocksample;

import org.springframework.stereotype.Repository;

/**
 * Created by ethan.kim on 2018. 8. 22..
 */
@Repository
public class OrderRepository {

    public OrderSheet findOne(long id) {
        return OrderSheet.EMPTY;
    }

}
