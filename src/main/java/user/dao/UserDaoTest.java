package user.dao;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import user.domain.User;

import java.sql.SQLException;


// 2.2.1 테스트 검증의 자동화(Improvement of UserDaoTest, Print a success or failure message)
// 테스트 확인 사항 : add()에 전달한 User 오브젝트에 담긴 사용자 정보와
// get()을 통해 다시 DB에서 가져온 User 오브젝트의 정보가 서로 정확히 일치하는지 확인
// 정확히 일치한다면 모든 정보가 빠짐없이 DB에 등록됐고, 이를 다시
// DB에서 정확히 가져왔다는 사실을 알 수 있다
//
// 개선된 코드는 get()에서 가져온 결과를 콘솔에 출력하여 확인하는 방식을
// "테스트 성공"과 "테스트 실패"라는 메세지가 각각 출력하는 방식으로 작성함
public class UserDaoTest {
    public static void main(String[] args) throws SQLException {
        ApplicationContext context =
                new GenericXmlApplicationContext("applicationContext.xml");
        UserDao dao = context.getBean("userDao", UserDao.class);

        User user = new User();
        user.setId("whiteship");
        user.setName("백기선");
        user.setPassword("married");

        dao.add(user);

        System.out.println(user.getId() + " 등록 성공");

        User user2 = dao.get(user.getId());

        //System.out.println(user2.getName());
        //System.out.println(user2.getPassword());
        //System.out.println(user2.getId() + " 조회 성공");
        // 위 3줄의 코드를 개선
        if (!user.getName().equals(user2.getName())) {
            System.out.println("테스트 실패 : (do not match - name)");
        } else if (!user.getPassword().equals(user2.getPassword())) {
            System.out.println("테스트 실패 : (do not match - password)");
        } else {
            System.out.println("조회 테스트 성공");
        }

    }

}

