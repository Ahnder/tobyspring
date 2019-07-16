package user.dao;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import user.domain.User;

import java.sql.SQLException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


// 2.3.2 테스트 결과의 일관성
// 이전까지의 UserDaoTest의 문제는 이전 테스트 때문에 DB에 등록된
// 중복 데이터가 있을 수 있다는 점이다
// 이를 개선하기 위해 deleteAll() 과 getCount() 메서드를 추가한다(UserDao 클래스에 메서드 추가)


public class UserDaoTest {

    // addAndGet() 테스트를 확장(deleteAll(), getCount())
    @Test
    public void addAndGet() throws SQLException {

        ApplicationContext context = new GenericXmlApplicationContext(
                "applicationContext.xml");

        UserDao dao = context.getBean("userDao", UserDao.class);

        //deleteAll() 메서드로 테이블을 비우고 getCount()로 테이블수가 0이 맞는지 확인
        dao.deleteAll();
        assertThat(dao.getCount(), is(0));

        User user = new User();

        user.setId("gyumee");
        user.setName("박성철");
        user.setPassword("park");

        dao.add(user);
        //테이블에 레코드가 1개 추가 되었으니 getCount() 가 1이 맞는지 확인
        //getCount()의 기능을 위 테스트와 비교 각각 0과 1이 잘 나오면 검증 통과
        assertThat(dao.getCount(), is(1));

        User user2 = dao.get(user.getId());

        assertThat(user2.getName(), is(user.getName()));
        assertThat(user2.getPassword(), is(user.getPassword()));

    }
}

