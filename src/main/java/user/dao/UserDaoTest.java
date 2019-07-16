package user.dao;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import user.domain.User;

import java.sql.SQLException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


// 2.2.2 JUnit 테스트로 전환

public class UserDaoTest {

    @Test
    public void addAndGet() throws SQLException {

        ApplicationContext context = new GenericXmlApplicationContext(
                "applicationContext.xml");

        UserDao dao = context.getBean("userDao", UserDao.class);

        User user = new User();

        user.setId("gyumee");
        user.setName("박성철");
        user.setPassword("park");

        dao.add(user);

        User user2 = dao.get(user.getId());

        assertThat(user2.getName(), is(user.getName()));
        assertThat(user2.getPassword(), is(user.getPassword()));

    }
}

