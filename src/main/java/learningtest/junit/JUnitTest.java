package learningtest.junit;

// JUnit 테스트 오브젝트 테스트
// JUnit이 테스트 메서드를 수행할 때마다 새로운 오브젝트를 만드는것이 맞는지
// 테스트 코드를 통해 확인
// 테스트 클래스 자신의 타입으로 스태팁ㄱ 변수를 하나 선언
// 매 테스트 메서드에서 현재 스태틱 변수에 담긴 오브젝트와 자신을 비교해서
// 같지 않다는 사실을 확인
// 이 후 현재 오브젝트를 그 스태틱 변수에 저장한다

// * not()은 뒤에 나오는 결과를 부정하는 매처
// * sameInstance() : 실제로 같은 오브젝트인지 비교

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

public class JUnitTest {
    static JUnitTest testObject;

    @Test
    public void test1() {
        assertThat(this, is(not(sameInstance(testObject))));
        testObject = this;
    }

    @Test
    public void test2() {
        assertThat(this, is(not(sameInstance(testObject))));
        testObject = this;
    }

    @Test
    public void test3() {
        assertThat(this, is(not(sameInstance(testObject))));
        testObject = this;
    }

}
