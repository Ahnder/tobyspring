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
            if (canUpgradeLevel(user)) callUpgradeLevel(user);
        }
    }

    // 업그레이드 가능 확인 메서드
    private boolean canUpgradeLevel(User user) {
        Level currentLevel = user.getLevel();

        switch (currentLevel) {
            case BASIC: return (user.getNumoflogin() >= 50);
            case SILVER: return (user.getNumofrecommend() >= 30);
            case GOLD: return false;
            // default:
            // 현재 로직에서 다룰 수 없는 레벨이 주어지면 예외를 발생시킨다.
            // 새로운 레벨이 추가되고 로직을 수정하지 않으면 에러가 나서 확인할 수 있다.
            default: throw new IllegalArgumentException("Unknown Level: " +
                    currentLevel);
        }
    }

    // 레벨 업그레이드 작업 메서드
    private void callUpgradeLevel(User user) {
        user.upgradeLevel();
        userDao.update(user);
    }
}
