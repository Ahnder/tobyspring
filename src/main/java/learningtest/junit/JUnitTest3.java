package learningtest.junit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

// - 테스트 메서드에서 매번 동일한 애플리케이션 컨텍스트가 context 변수에 주입됐는지 확인
// 1. context를 저장해둘 스태틱 변수인 contextObject가 null인지 확인한다
// 2. null이라면 첫 번쨰 테스트일 테니깐 일단 통과, 그리고 contextObject에 현재 context를 저장
// 3. 다음부터는 저장된 contextObject가 null이 아닐 테니 현재의 context가 같은지 비교 가능
// * 한 번이라도 다른 오브젝트가 나오면 테스트는 실패

// 첫 번째 방법 - assertThat()을 이용
// 1. 매처와 비교할 대상인 첫 번째 파라미터에 Boolean 타입의 결과가 나오는 조건문을 넣는다
// 2. 그 결과를 is() 매처를 써서 true와 비교, is()는 타입만 일치하면 어떤 값이든 검증할 수 있다

// 두 번째 방법 - 조건문을 받아서 그 결과가 true인지 false인지를 확인하도록 만들어진
//  assertTrue() 라는 검증용 메서드를 assertThat() 대신에 사용

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/junit.xml")
public class JUnitTest3 {

    @Autowired
    ApplicationContext context; // 테스트 컨텍스트가 매번 주입해주는 애플리케이션
    // 컨텍스트는 항상 같은 오브젝트인지 테스트로 확인

    static Set<JUnitTest3> testObjects = new HashSet<JUnitTest3>();
    static ApplicationContext contextObject = null;

    @Test
    public void test1() {
        assertThat(testObjects, not(hasItem(this)));
        testObjects.add(this);

        assertThat(contextObject == null || contextObject == this.context,
                is(true));
        contextObject = this.context;
    }

    @Test
    public void test2() {
        assertThat(testObjects, not(hasItem(this)));
        testObjects.add(this);

        assertTrue(contextObject == null || contextObject == this.context);
        contextObject = this.context;
    }

}
