package me.dong.spocksample

import spock.lang.Specification
import spock.lang.Unroll

/**
 * Created by ethan.kim on 2018. 8. 25..
 */
class SpockDataDrivenTest extends Specification {

    /*
    Data Driven Testing
    다양한 입력 및 예상 결과로 동일한 테스트 코드를 여러번 실행하는 것이 유용
    Spock는 Data Driven Testing support
     */

    def "maximum of two numbers"() {
        expect:
        Math.max(1, 3) == 3
        Math.max(7, 4) == 7
        Math.max(0, 0) == 0
    }

    /*
    위 같은 접근법은 간단한 경우에는 괜찮지만 잠재적인 단점 존재

    코드와 데이터가 섞여있어 쉽게 독립적으로 변경할 수 없다
    외부 소스에서 데이터를 쉽게 자동생성하거나 가져올 수 없다
    동일한 코드를 여러번 실행하려면 중복되거나 별도의 메소드로 추출
    장애가 발생한 경우 장애를 유발한 입력이 즉시 명확하지 않을 수있다
    동일한 코드를 여러번 실행하는것은 별도의 메소드를 실행하는 것과 동일한 격리로 인해 이익을 얻지 못한다
     */

    def "maximum of two numbers"(int a, int b, int c) {
        expect:
        Math.max(a, b) == c

        // 사용할 데이터 제공
        where:
        // Data table
        // 고정된 데이터 값 집합을 사용하여 feature method를 실행하는 편리한 방법
        a | b | c
        1 | 3 | 3
        7 | 4 | 7
        0 | 0 | 0

        /*
        data table은 2개의 column 필요
        단일 column로 사용하려면

        where:
        a | _
        1 | _
        7 | _
        0 | _

        iteration은 feature와 동일한 방식으로 격리된다
        iteration의 feature method 전후로 setup, cleanup 실행되어 격리
         */
    }

    def "maximum of two numbers3"() {
        // where block에서 모든 데이터 변수를 선언하기 때문에 매개변수에서 생략 가능
        // || 로 입력, 결과를 시각적으로 구
        expect:
        Math.max(a, b) == c

        where:
        a | b || c
        1 | 3 || 3
        7 | 4 || 7
        0 | 1 || 0
    }

    /*
Condition not satisfied:

Math.max(a, b) == c
|   |  |  |  |
1   0  1  |  0
       false

Expected :0

Actual   :1

위 같은 경우 실패를 판단하기 쉽지만, 만약 어려운 경우라면 @Unroll 사용
 */

    @Unroll
    def "maximum of two numbers #a and #b is #c"() {
        expect:
        Math.max(a, b) == c

        where:
        a | b | c
        1 | 3 | 3
        7 | 4 | 4
        0 | 0 | 0
    }
    /*
    maximum of 3 and 5 is 5   PASSED
maximum of 7 and 4 is 7   FAILED

Math.max(a, b) == c
    |    |  |  |  |
    |    7  4  |  7
    42         false

maximum of 0 and 0 is 0   PASSED

#{variable}를 사용한 자리에 test시 사용했던 데이터들이 들어가서 더 명확하다
     */

    /*
    Data Pipes

    data table은 데이터 변수에 값을
    하나 이상의 data pipe에 대한 문법적 설탕

     */

    def "maximum of two numbers4"() {
        expect:
        Math.max(a, b) == c

        where:
        a << [1, 7, 0]
        b << [3, 4, 0]
        c << [3, 7, 0]
    }

    /*
    Multi-Variable Data Pipes
     */

//    @Shared sql = Sql.newInstance("jdbc:h2.mem:", "org.h2.Drover")

    def "maximum of two numbers5"() {
        expect:
        Math.max(a, b) == c

        where:
        // data provider가 iteration 당 여러 값을 반환하면 여러 데이터 변수에 동시에 연결 가능
        [a, b, c] << sql.rows("select a, b, c from maxdata")

//        관심없는 데이터는 _로 무시
//        [a, b, _, c] << sql.rows("select * from maxdata")
    }

    /*
    data variable 할당
     */

    def "maximum of two numbers6"() {
        expect:
        Math.max(a, b) == c

        where:
        // data provider가 iteration 당 여러 값을 반환하면 여러 데이터 변수에 동시에 연결 가능
        row << sql.rows("select * from maxdata")
        a = row.a
        b = row.b
        c = row.c
    }

    /*
    data table, data pipe, data 변수 할당을 결합 가능
     */

    def "maximum of two numbers7"() {
        where:
        a | _
        3 | _
        7 | _
        0 | _

        b << [5, 0, 0]

        c = a > b ? a : b
    }

    /*
    Unroll 메소드 이름은 Groovy GString과 비슷하지만 차이점 존재
    1. $ 대신 # 사용. ${..} 랑 같지 않다
    2. property 접근과 zero-arg method call 만 지원
     */

    @Unroll
    def "#lastName"() {
        given:
        def person = new Person()
        person.age = 3
        person.name = "aaaa"

        where:
        persion << [new Person(age: 14, name: 'Phil Cole')]
        lastName = person.name.split(' ')[1]
    }


    class Person {
        int age
        String name

        Person() {
        }

        Person(int age, String name) {
            this.age = age
            this.name = name
        }
    }


}
