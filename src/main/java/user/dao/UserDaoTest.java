package user.dao;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import user.domain.Level;
import user.domain.User;

import java.sql.SQLException;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/test_applicationContext.xml")

public class UserDaoTest {
    @Autowired
    ApplicationContext context;

    @Autowired
    UserDao dao;

    private User user1;
    private User user2;
    private User user3;

    @Before
    public void setUp() {

        this.dao = this.context.getBean("userDao", UserDaoJdbc.class);

        this.user1 = new User("gyumee", "박성철", "park",
                Level.BASIC, 1, 0);
        this.user2 = new User("leegw", "이길원", "lee",
                Level.SILVER, 55, 10);
        this.user3 = new User("bumjin", "박범진", "jin",
                Level.GOLD, 100, 40);

    }

    @Test
    public void testUpdate() {
        dao.deleteAll();

        dao.add(user1); // 수정할 사용자
        dao.add(user2); // 수정하지 않을 사용자

        user1.setName("오민규");
        user1.setPassword("oh");
        user1.setLevel(Level.GOLD);
        user1.setNumoflogin(1000);
        user1.setNumofrecommend(999);

        dao.update(user1);

        User user1update = dao.get(user1.getId());
        checkSameUser(user1, user1update);

        User user2same = dao.get(user2.getId());
        checkSameUser(user2, user2same);
        // 테스트 보강
        // UPDATE는 WHERE가 없어도 아무런 경고 없이 정상적으로 작동하는 것 처럼 보인다.
        // 하지만 수정하지 않아야 할 내용까지 수정되었는지는 확인 하지 못한다
        // 그러므로 user2same을 넣어 테스트를 보강한다
        // 위의 user2same 은 원하는 사용자 외의 정보는 변경되지 않았음을 직접 학인하는 테스트이다.
        // update() 메서드의 SQL에서 WHERE을 빼먹었다면 이 테스트는 실패로 끝난다.
    }

    @Test
    public void testAddAndGet() throws SQLException {

        dao.deleteAll();
        assertThat(dao.getCount(), is(0));

        dao.add(user1);
        dao.add(user2);
        assertThat(dao.getCount(), is(2));

        User userget1 = dao.get(user1.getId());
        checkSameUser(userget1, user1);

        User userget2 = dao.get(user2.getId());
        checkSameUser(userget2, user2);
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

    // 중복된 키를 등록했을시 예외 발생 학습 테스트
    @Test(expected = DuplicateKeyException.class)
    public void testDuplicatekey() {
        dao. deleteAll();

        dao.add(user1);
        dao.add(user1);
    }

    // 현재 등록되어 있는 모든 사용자 정보를 가져오는 getAll() 메서드 테스트
    @Test
    public void testGetAll() throws SQLException {
        dao.deleteAll();

        // 데이터가 없는 경우에 대한 검증코드를 추가
        List<User> users0 = dao.getAll();
        assertThat(users0.size(), is(0));

        dao.add(user1); // id : gyumee
        List<User> users1 = dao.getAll();
        assertThat(users1.size(), is(1));
        checkSameUser(user1, users1.get(0));

        dao.add(user2); // id : leegw
        List<User> users2 = dao.getAll();
        assertThat(users2.size(), is(2));
        checkSameUser(user1, users2.get(0));
        checkSameUser(user2, users2.get(1));

        dao.add(user3); // id : bumjin
        List<User> users3 = dao.getAll();
        assertThat(users3.size(), is(3));
        checkSameUser(user3, users3.get(0)); // user3의 id 값이 알파벳순으로 가장 빠르므로 첫 번째 엘리먼트가 된다
        checkSameUser(user1, users3.get(1));
        checkSameUser(user2, users3.get(2));

    }

    private void checkSameUser(User user1, User user2) {
        assertThat(user1.getId(), is(user2.getId()));
        assertThat(user1.getName(), is(user2.getName()));
        assertThat(user1.getPassword(), is(user2.getPassword()));
        assertThat(user1.getLevel(), is(user2.getLevel()));
        assertThat(user1.getNumoflogin(), is(user2.getNumoflogin()));
        assertThat(user1.getNumofrecommend(), is(user2.getNumofrecommend()));
    }

}

