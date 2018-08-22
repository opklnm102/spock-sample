package me.dong.spocksample

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import spock.lang.Specification

import static org.mockito.BDDMockito.given;

/**
 * Created by ethan.kim on 2018. 8. 22..
 */
@SpringBootTest
class SpockSampleApplicationTest extends Specification {

    @Autowired
    OrderService orderService

    @MockBean
    OrderRepository orderRepository

    def "id로 OrderSheet조회"() {
        given:
        long id = 1L
        given(orderRepository.findOne(id))
                .willReturn(OrderSheet.builder()
                .orderType("PHONE")
                .build())

        when:
        OrderSheet result = orderService.findOrder(id)

        then:
        result.orderType == "PHONE"
    }
}

