package user.dao;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import user.domain.User;

import javax.sql.DataSource;

/*
@Configuration
public class DaoFactory {

    // DConnectionMaker() 는 언제든지 다른걸로 유연하게 바꿀 수 있다
    @Bean
    public DataSource dataSource() {
        SimpleDriverDataSource dataSource
    }

    @Bean
    public UserDao userDao() {
        UserDao userDao = new UserDao();
        userDao.setConnectionMaker(connectionMaker());
        return userDao;
    }


}*/
