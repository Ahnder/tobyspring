package user.service;

import user.dao.UserDao;
import user.domain.Level;
import user.domain.User;

import java.util.List;

// UserService 클래스를 만들고 사용할 UserDao 오브젝트를 저장해둘 인스턴스 변수를 선언
// UserDao 오브젝트의 DI가 가능하도록 수정자 메서드 추가
public class UserService {
    UserDao userDao;

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    // 사용자 신규 등록 로직을 담은 add() 메서드
    public void add(User user) {
        if (user.getLevel() == null) user.setLevel(Level.BASIC);
        userDao.add(user);
    }

    // 사용자 레벨 업그레이드 메서드
    public void upgradeLevels() {
        List<User> users = userDao.getAll();
        for (User user : users) {
            Boolean changed = null; // 레벨의 변화가 있는지를 확인하는 단순 플래그
            // BASIC 레벨 업그레이드 작업
            if (user.getLevel() == (Level.BASIC) && (user.getNumoflogin() >= 50)) {
                user.setLevel(Level.SILVER);
                changed = true;
            // SILVER 레벨 업그레이드 작업
            } else if (user.getLevel() == (Level.SILVER) && (user.getNumofrecommend() >= 30)) {
                user.setLevel(Level.GOLD);
                changed = true;
            // GOLD 레벨 이상은 현재 존재하지 않는다.
            } else if (user.getLevel() == Level.GOLD) {
                changed = false; // GOLD 레벨은 최상위 레벨이기 때문에 변경이 일어나지 않는다.

            } else {
                // 일치하는 조건이 없으면 변경 없음
                changed = false;
            }

            // 변경이 있는 경우 (changed = true) update() 메서드를 호출해서 레벨 업그레이드
            if (changed) userDao.update(user);
        }
    }
}
