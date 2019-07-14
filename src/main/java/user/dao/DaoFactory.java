package user.dao;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import user.domain.User;

/*@Configuration
public class DaoFactory {

    // DConnectionMaker() 는 언제든지 다른걸로 유연하게 바꿀 수 있다
    @Bean
    public ConnectionMaker connectionMaker() {
        return new DConnectionMaker();
    }

    *//*public UserDao userDao() {
        UserDao userDao = new UserDao(connectionMaker());

        return userDao;
    }*//*

    // 위의 userDao() 는 아래의 코드로 대체 가능
    *//*@Bean
    public UserDao userDao() {
        return new UserDao(connectionMaker());
    }
    // UserDao클래스의 생성자를 수정자 메서드 방식으로 변경했으니 위의 코드도 밑의 코드로 대체한다*//*

    @Bean
    public UserDao userDao() {
        UserDao userDao = new UserDao();
        userDao.setConnectionMaker(connectionMaker());
        return userDao;
    }

    // 밑의 accountDao(), messageDao() 처럼 다른 DAO 생성 기능도 간단하게 추가 가능하다
    *//*public AccountDao accountDao() {
        return new AccountDao(connectionMaker());
    }*//*

    *//*public MessageDao messageDao() {
        return new MessageDao(connectionMaker());
    }*//*
}*/
