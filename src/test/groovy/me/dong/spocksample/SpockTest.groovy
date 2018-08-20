package me.dong.spocksample

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
    def "금액의 퍼센트 계산 결과값의 소수점을 버림을 검증한다"() {

        given :
        RoundingMode roundingMode = RoundingMode.DOWN

        when :
        def calculate = Calculator.calculate(10000L, 0.1f, roundingMode)

        then :
        calculate == 10L
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
    mocking >> return value
    mocking >> { throw new NegativeNumberNotAllowException() }
     */
    def "주문금액의 소수점 버림을 검증한다"() {
        given:
        RoundingMode roundingMode = RoundingMode.DOWN
        def orderSheet = Mock(OrderSheet.class)

        when:
        long amount = orderSheet.getTotalOrderAmount()

        then:
        orderSheet.getTotalOrderAmount() >> 10000L
        10L == Calculator.calculate(amount, 0.1f, roundingMode)
    }

}
