package me.dong.spocksample

import spock.lang.Specification
import spock.lang.Unroll

import java.math.RoundingMode

/**
 * reference - http://jojoldu.tistory.com/228
 *
 * Created by ethan.kim on 2018. 8. 21..
 */
class FeeCalculateTypeTest extends Specification {

     /*
     BDD(Behaviour-Driven Development)
     TDD 실천자가 테스트의 의도를 더 명확하게 표현하기 위한 용어를 찾는 과정에서 탄생
     테스트란 단어는 `원하는 동작을 정의한다는 정신을 잘 반영하지 못하며 의미가 너무 함축적`이다
     테스트와 테스트 메소드보다는 `명세와 행위`라는 용어를 거론

     Spock BDD framework
     JUnit과 비슷하나, `기대하는 동작과 테스트의 의도를 더 명확하게 드러내고, 산만한 코드는 뒤로 숨겨주는 등`이 장점
     */

    def "495를 원단위로 반올림하면 500이 된다"() {
        given:
        BigDecimal amount = BigDecimal.valueOf(495)

        when:
        BigDecimal result = amount.setScale(-1, RoundingMode.HALF_UP)

        then:
        result == 500
    }
    /*
    given(or setup)
        테스트하기 위한 기본 설정작업(test 환경 구축)
       
    when
        테스트할 대상 코드를 실행(simulus)
    then
        테스트할 대상 코드의 결과를 검증
    expect
        테스트할 대상 코드를 실행 및 검증(when + then)
    where
        feature 메소드를 파라미터 삼아 실행시킵니다
     */

    def "computing the maximum of two numbers"() {
        expect:
        Math.max(a, b) == c

        where:
        a | b | c
        5 | 1 | 5
        3 | 9 | 9
    }

//    @Unroll  // 메소드 이름에 지정된 템플릿에 따라 테스트 결과를 보여준다
    def "금액이 주어지면 원단위 반올림 결과가 반환된다 [금액: #amount, 결과: #result]"() {
        given:
        def feeCalculator = FeeCalculateType.WON_UNIT_CUT

        expect:
        feeCalculator.calculate(amount) == result

        where:
        amount | result
        500L   | 500L
        495L   | 490L
    }

    def "음수가 입력되면 NegativeNumberNotAllowException이 발생한다"() {
        given:
        def feeCalculator = FeeCalculateType.WON_UNIT_CUT

        when:
        feeCalculator.calculate(-1)

        then:
        def e = thrown(NegativeNumberNotAllowException.class)
        e.message == "음수는 계산할 수 없습니다"
    }




}
