package user.dao;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import user.domain.User;

import java.sql.SQLException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


// 2.3.5 테스트 코드 개선
// 테스트 코드에서 중복된 코드를 JUnit @Before 을 사용
// * JUnit이 하나의 테스트 클래스를 가져와 테스트를 수행하는 방식 간단 정리
// 1. 테스트 클래스에서 @Test가 붙은 public이고 void형이며 파라미터가 없는
//    테스트 메서드를 모두 찾는다
// 2. 테스트 클래스의 오브젝트를 하나 만든다
// 3. @Before가 붙은 메서드가 있으면 실행한다
// 4. @Test가 붙은 메서드를 하나 호출하고 테스트 결과를 저장해둔다
// 5. @After가 붙은 메서드가 있으면 실행한다
// 6. 나머지 테스트 메서드에 대해 2~5번을 반복한다
// 7. 모든 테스트의 결과를 종합하여 돌려준다
// ** 각 테스트 메서드를 실행할 때마다 테스트 클래스의 오브젝트를 새로 만든다
//    한번 만들어진 테스트 클래스의 오브젝트는 하나의 테스트 메서드를 사용하고 나면 버려진다
//    - 테스트 메서드 실행마다 새로운 오브젝트를 만드는 이유
//      : 각 테스트가 서로 영향을 주지 않고 독립적으로 실행됨을 보장해주기 위해
public class UserDaoTest {

    // setUp() 메서드에서 만드는 오브젝트를 테스트 메서드에서
    // 사용할 수 있도록 인스턴스 변수로 선언한다
    private UserDao dao;

    // 2개의 테스트 메서드에서 반복되는 User 픽스쳐들도 앞으로의 테스트 메서드에서
    // 쓰일 가능성이 높으므로 user1등은 인스턴스 변수를 선언하고
    // 오브젝트 생성은 @Before 에서 진행 시킨다
    private User user1;
    private User user2;
    private User user3;

    // JUnit이 제공하는 애너테이션, @Test 메서드가 실행되기 전에
    // 먼저 실행돼야 하는 메서드를 정의한다
    @Before
    public void setUp() {
        ApplicationContext context = new GenericXmlApplicationContext(
                "applicationContext.xml");

        this.dao = context.getBean("userDao", UserDao.class);

        this.user1 = new User("gyumee", "박성철", "park");
        this.user2 = new User("leegw", "이길원", "lee");
        this.user3 = new User("bumjin", "박범진", "jin");
    }

    @Test
    public void testAddAndGet() throws SQLException {

        dao.deleteAll();
        assertThat(dao.getCount(), is(0));

        dao.add(user1);
        dao.add(user2);
        assertThat(dao.getCount(), is(2));

        User userget1 = dao.get(user1.getId());
        assertThat(userget1.getName(), is(user1.getName()));
        assertThat(userget1.getPassword(), is(user1.getPassword()));

        User userget2 = dao.get(user2.getId());
        assertThat(userget2.getName(), is(user2.getName()));
        assertThat(userget2.getPassword(), is(user2.getPassword()));
    }

    @Test
    public void testGetCount() throws SQLException {

        dao.deleteAll();
        assertThat(dao.getCount(), is(0));

        dao.add(user1);
        assertThat(dao.getCount(), is(1));

        dao.add(user2);
        assertThat(dao.getCount(), is(2));

        dao.add(user3);
        assertThat(dao.getCount(), is(3));

    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void testGetUserFailure() throws SQLException {

        dao.deleteAll();
        assertThat(dao.getCount(), is(0));

        dao.get("unknown_id");
    }

}

