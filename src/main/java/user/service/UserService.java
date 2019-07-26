package user.service;

import user.dao.UserDao;

// UserService 클래스를 만들고 사용할 UserDao 오브젝트를 저장해둘 인스턴스 변수를 선언
// UserDao 오브젝트의 DI가 가능하도록 수정자 메서드 추가
public class UserService {
    UserDao userDao;

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
}
