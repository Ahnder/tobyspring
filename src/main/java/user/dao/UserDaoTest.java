package user.dao;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import user.domain.User;

import java.sql.SQLException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


// 2.3.3 포괄적인 테스트
// * 각 테스트는 실행 순서에 상관없이 독립적으로 항상 동일한 결과를 낼 수 있도록 해야 한다
//
// 1) getCount() 테스트
//  여러개의 User를 등록해가며 getCount()의 결과를 매번 확인
// 테스트 메서드는 한 번에 한 가지 검증 목적에만 충실한 것이 좋으므로
// 새로운 테스트 메서드 생성 - testGetCount()
// ~테스트 시나리오~
//  1. 먼저 테이블의 데이터를 모두 지우고 getCount()로 레코드 개수가 0임을 확인
//  2. 3개의 사용자 정보를 하나씩 추가하면서 매번 getCount()의 결과가 하니씩 증가하는지 확인
//  ps) 테스트를 만들기 전에 먼저 User 클래스에 한 번에 모든 정보를 넣을 수 있도록
//      초기화가 가능한 생성자를 추가한다[public User(...id, ...name, ...password){...}]

// 2) testAddAndGet() 테스트 보완
//  get()이 파라미터로 주어진 id에 해당하는 사용자를 가져온것인지,
// 그냥 아무거나 가져온 것인지 검증해야 한다
// 두 개의 User를 add() 하고, 각 User의 id를 파라미터로 전달해서 get()을 실해하도록 설정

// 3) get 예외조건에 대한 테스트 (testGetUserFailure())
//  get() 메서드에 전달된 id 값에 해당하는 사용자 정보가 없는 경우
// id에 해당하는  정보롤 찾을 수 없다고 예외를 던지게 설정 (EmptyResultDataAccessException 이용)
//  위 테스트들과 반대로 특정 예외가 던져지면 성공,
// 예외가 던져지지 않고 정상적으로 작업을 마치면 실패로 설정
//  1. 우선 모든 데이터를 지우고
//  2. 존재하지 않는 id로 get()을 호출
//  3. 이때 EmptyResultDataAccessException이 던져지면 성공, 아니라면 실패

public class UserDaoTest {

    @Test
    public void testAddAndGet() throws SQLException {

        ApplicationContext context = new GenericXmlApplicationContext(
                "applicationContext.xml");

        UserDao dao = context.getBean("userDao", UserDao.class);

        User user1 = new User("gyumee", "박성철", "park");
        User user2 = new User("leegw", "이길원", "lee");

        dao.deleteAll();
        assertThat(dao.getCount(), is(0));

        dao.add(user1);
        dao.add(user2);
        assertThat(dao.getCount(), is(2));

        // 첫 번째 User의 id로 get()을 실행하면 첫 번째 User의 값을 가진
        // 오브젝트를 돌려주는지 확인
        // 두 번째도 마찬가지로 확인
        User userget1 = dao.get(user1.getId());
        assertThat(userget1.getName(), is(user1.getName()));
        assertThat(userget1.getPassword(), is(user1.getPassword()));

        User userget2 = dao.get(user2.getId());
        assertThat(userget2.getName(), is(user2.getName()));
        assertThat(userget2.getPassword(), is(user2.getPassword()));
    }

    @Test
    public void testGetCount() throws SQLException {
        ApplicationContext context = new GenericXmlApplicationContext(
                "applicationContext.xml");

        UserDao dao = context.getBean("userDao", UserDao.class);

        User user1 = new User("gyumee", "박성철", "park");
        User user2 = new User("leegw", "이길원", "lee");
        User user3 = new User("bumjin", "박범진", "jin");

        dao.deleteAll();
        assertThat(dao.getCount(), is(0));

        dao.add(user1);
        assertThat(dao.getCount(), is(1));

        dao.add(user2);
        assertThat(dao.getCount(), is(2));

        dao.add(user3);
        assertThat(dao.getCount(), is(3));

    }

    // 우선 이 테스트는 실패한다
    // get()메서드에서 쿼리 결과의 첫 번째 로우를 가져오게 하는 rs.next()를
    // 실행할 때 가져올 로우가 없다는 SQLException이 발생한다
    // 이 테스트를 성공시키기 위해선 get() 메서드 코드를 수정해야한다
    @Test(expected = EmptyResultDataAccessException.class)
    public void testGetUserFailure() throws SQLException {
        ApplicationContext context = new GenericXmlApplicationContext(
                "applicationContext.xml");

        UserDao dao = context.getBean("userDao", UserDao.class);

        dao.deleteAll();
        assertThat(dao.getCount(), is(0));

        // 밑의 메서드 실행 중에 예외가 발생해야 한다
        // 예외가 발생하지 않으면 테스트는 실패한다
        dao.get("unknown_id");
    }

}

