package me.dong.spocksample

import spock.lang.FailsWith
import spock.lang.Ignore
import spock.lang.Specification

import java.math.RoundingMode

/**
 * Created by ethan.kim on 2018. 8. 19..
 */
class SpockTest extends Specification {

    /*
    http://woowabros.github.io/study/2018/03/01/spock-test.html

    block
    given, when, then과 같은 코드 블록
    test method 내 최소 1개 필수

    given or setup
        test에 필요한 환경을 설정하는 작업
        항상 다른 블록보다 상위에 위치해야 한다
    when
        test target을 실행
    then
        결과 검증, 예외 및 조건에 대한 결과 확인
        작성한 코드 1줄이 assert문
    expect
        test할 코드 실행 및 검증(when + then)
     */
    RoundingMode roundingMode

    def setup() {
        roundingMode = RoundingMode.DOWN
    }

    def "금액의 퍼센트 계산 결과값의 소수점을 버림을 검증한다"() {
        given:
        def amount = 10000L
        def rate = 0.1f

        when:
        def calculate = Calculator.calculate(amount, rate, roundingMode)

        then:
        calculate == 10L
    }

    def cleanup() {
        roundingMode = null
    }

    /*
    where
        다양한 case에 대해 검증시 사용
        실패한 모든 테스트 케이스를 알려준다

      JUnit 기반

      @Test
      public void 금액의_퍼센트_계산_결과값의_소수점_버림을_검증한다() throws Exception {
            given:
            RoundingMode roundingMode = RoundingMode.DOWN;

            when, then:
            long result1 =  Calculator.calculate(10000L, 0.1f, roundingMode);
            assertThat(result1, is(10L));

            long result2 =  Calculator.calculate(2799L, 0.2f, roundingMode);
            assertThat(result1, is(5L));
     }
     문제: 의미없는 중복 코드의 산재
     spock의 where를 사용하면 더 간단하게 표현할 수 있다
     */

    def "여러 금액의 퍼센트 계산 결과값의 소수점 버림을 검증한다"() {

        given:
        RoundingMode roundingMode = RoundingMode.DOWN

        expect:
        Calculator.calculate(amount, rate, roundingMode) == result

        where:
        amount | rate  | result
        10000L | 0.1f  | 10L
        2799L  | 0.2f  | 5L
        159L   | 0.15f | 0L
        2299L  | 0.15f | 3L
    }

    /*
    thrown()
    작성한 흐름에 따라 예외를 확인할 수 있다
     */

    def "음수가 들어오면 예외가 발생하는지 알아보자"() {
        given:
        RoundingMode roundingMode = RoundingMode.DOWN

        when:
        Calculator.calculate(-10000L, 0.1f, roundingMode)

        then:
        def e = thrown(NegativeNumberNotAllowException.class)
        e.message == "음수는 계산할 수 없습니다"
    }

    /*
    Mock

    Mock 생성 2가지 방법
    1. def mock = Mock(Calculator)
    2. Calculator mockCalculator = Mock()

    mocking >> return value
    mocking >> { throw new NegativeNumberNotAllowException() }
     */

    def "주문금액의 소수점 버림을 검증한다"() {
        given:
        RoundingMode roundingMode = RoundingMode.DOWN
        def orderSheet = Mock(OrderSheet.class)
//        OrderSheet orderSheet = Mock()

        when:
        long amount = orderSheet.getTotalOrderAmount()

        then:
        orderSheet.getTotalOrderAmount() >> 10000L
        10L == Calculator.calculate(amount, 0.1f, roundingMode)
    }

    def "complex order는 조회가 2번된다"() {
        given:
        def mockOrderRepository = Mock(OrderRepository)
        OrderService orderService = new OrderService(mockOrderRepository)
        OrderSheet orderSheet = OrderSheet.builder()
                .orderType("COMPLEX")
                .totalOrderAmount(100L)
                .build()
        long id = 1L

        when:
        orderService.order(id, orderSheet)

        then:
//        2 * mockOrderRepository.findOne(id)  // 2번
//        (2.._) * mockOrderRepository.findOne()  // 최소 2번
//        (_..2) * mockOrderRepository.findOne()  // 최대 2번
        2 * mockOrderRepository.findOne(_)  // any parameter
    }

    def "complex order는 조회가 2번된다 with 사용"() {
        given:
        def mockOrderRepository = Mock(OrderRepository)
        OrderService orderService = new OrderService(mockOrderRepository)
        OrderSheet orderSheet = OrderSheet.builder()
                .orderType("COMPLEX")
                .totalOrderAmount(100L)
                .build()
        long id = 1L

        when:
        orderService.order(id, orderSheet)

        then:
        with(mockOrderRepository) {
            2 * findOne(_)
        }
    }

    /*
    spock vs JUnit
    | Spock | JUnit |
    |:------|:------|
    | Specification | Test class |
    | setup() | @Before |
    | cleanup()| @After |
    | setupSpec() | @BeforeClass |
    | cleanupSpec() | @AfterClass |
    | Feature | Test |
    | Feature method | Test method |
    | Data-driven feature | Theory |
    | Condition | Assertion |
    | Exception condition | @Test(expected=..) |
    | Interaction | Mock expectation(e.g. in Mockito) |
     */

    def "offered PC matches preferred configuration"() {
        given:
        def shop = new Shop()

        when:
        def pc = shop.buyPc()

        then:
        pc.vendor == "Sunny"
        pc.clockRate >= 2333
        pc.ram >= 4096
        pc.os == "Linux"
    }

    def "offered PC matches preferred configuration1"() {
        given:
        def shop = new Shop()

        when:
        def pc = shop.buyPc()

        then:
        matchesPreferredConfiguration(pc)
    }

    void matchesPreferredConfiguration(pc) {
        assert pc.vendor == "Sunny"
        assert pc.clockRate >= 2333
        assert pc.ram >= 4096
        assert pc.os == "Linux"
    }


    def "offered PC matches preferred configuration2"() {
        given:
        def shop = new Shop()

        when:
        def pc = shop.buyPc()

        then:
        with(pc) {
            vendor == "Sunny"
            clockRate >= 2333
            ram >= 406
            os == "Linux"
        }
    }

    @Ignore(value = "reason..")
    def "back account credited 10"() {
        given: "open a database connection"

        and: "seed the customer table"  // block 내에서 개별로 설명 가능

        and: "an empty bank account"

        when: "the account is credited 10"

        then: "the account's balance is 10"
    }

    @FailsWith(value = NullPointerException, reason = "reason..")
    def "back account credited 1"() {
        given:
        def shop

        when:
        def pc = shop.buyPc()

        then:
        with(pc) {
            vendor == "Sunny"
            clockRate >= 2333
            ram >= 406
            os == "Linux"
        }
    }

    class Shop {
        static class Pc {

            String vendor

            int clockRate

            int ram

            String os
        }


        Pc buyPc() {
            def pc = new Pc()

            pc.vendor = "Sunny"
            pc.clockRate = 2333
            pc.ram = 4096
            pc.os = "Linux"

            return pc
        }
    }
}
