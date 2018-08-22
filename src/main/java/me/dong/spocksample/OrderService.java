package me.dong.spocksample;

/**
 * Created by ethan.kim on 2018. 8. 22..
 */
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void order(long id, OrderSheet orderSheet) {
        orderRepository.findOne(id);

        if ("COMPLEX".equals(orderSheet.getOrderType())) {
            orderRepository.findOne(id);
        }
    }
}
