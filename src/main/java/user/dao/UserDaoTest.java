package user.dao;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import user.domain.User;

import java.sql.SQLException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

// 2.4 스프링 테스트 적용
// 2.4.1 테스트를 위한 애플리케이션 컨텍스트 관리
// - 스프링 테스트 컨텍스트 프레임워크 적용
//   @Before 메서드에서 애플리케이션 컨텍스트를 생성하는 코드르 제거하고
//   @Autowired 애너테이션과 @Runwith, @ContextConfiguration 애너테이션을 사용한다
@RunWith(SpringJUnit4ClassRunner.class) // 스프링의 테스트 컨텍스트 프레임워크의 JUnit 확장기능 지정
@ContextConfiguration(locations = "/test_applicationContext.xml") // 테스트 컨텍스트가 자동으로 만들어줄
// 애플리케이션 컨텍스트의 위치 지정, test전용 설정파일을 연결
public class UserDaoTest {
    @Autowired
    private ApplicationContext context;

    @Autowired
    private UserDao dao;

    private User user1;
    private User user2;
    private User user3;

    @Before
    public void setUp() {

        this.dao = this.context.getBean("userDao", UserDao.class);

        this.user1 = new User("gyumee", "박성철", "park");
        this.user2 = new User("leegw", "이길원", "lee");
        this.user3 = new User("bumjin", "박범진", "jin");

        // 테스트 메서드의 컨텍스트 공유 확인용 코드
        //  출력된 context와 this의 오브젝트 값을 잘 살펴보면, context는 세 번 모두 동일하다
        // 따라서 하나의 애플리케이션 컨텍스트가 만들어져 모든 테스트 메서드에서 사용되고
        // 있음을 알 수 있다
        //  반면에 UserDaoTest의 오브젝트는 매번 주소 값이 다르다
        // JUnit이 테스트 메서드를 실행할 때마다 새로운 테스트 오브젝트를 만들기 때문이다
        //  그렇다면 context 변수에 어떻게 애플리케이션 컨텍스트가 들어 있는 것일까
        // 스프링의 JUnit 확장기능은 테스트가 실행되기 전에 딱 한 번만 애플리케이션 컨텍스트를
        // 만들어두고, 테스트 오브젝트가 만들어질 때마다 특별한 방법을 이용해
        // 애플리케이션 컨텍스트 자신을 테스트 오브젝트의 특정 필드에 주입해주는 것이다

        System.out.println(this.context);
        System.out.println(this);
        //
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

