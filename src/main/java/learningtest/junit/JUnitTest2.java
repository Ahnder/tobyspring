package learningtest.junit;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;


import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

// JUnitTest의 개선
// JUnitTest의 방식은 직전 테스트에서 만들어진 테스트 오브젝트와만 비교한다
// 첫 번째와 세 번째 테스트 오브젝트가 같은지는 검증이 되지 않는다

// 1. 스태틱 변수로 테스트 오브젝트를 저장할 수 있는 컬렉션을 생성
// 2. 테스트마다 현재 테스트 오브젝트가 컬렉션에 이미 등록되어 있는지 확인하고,
//    없으면 자기 자신을 추가
// 3. 2를 반복
// * hasItem() : 컬렉션의 원소인지 검사

public class JUnitTest2 {
    static Set<JUnitTest2> testObjects = new HashSet<JUnitTest2>();

    @Test
    public void test1() {
        assertThat(testObjects, not(hasItem(this)));
        testObjects.add(this);
    }

    @Test
    public void test2() {
        assertThat(testObjects, not(hasItem(this)));
        testObjects.add(this);
    }

    @Test
    public void test3() {
        assertThat(testObjects, not(hasItem(this)));
        testObjects.add(this);
    }
}
