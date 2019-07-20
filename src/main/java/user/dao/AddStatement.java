package user.dao;

import user.domain.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddStatement implements StatementStrategy {
    // PreparedStatement를 만들때 user라는 부가적인 정보가 필요하기 때문에
    // 생성자로 User 타입 오브젝트를 제공받게 설정
    User user;

    public AddStatement(User user) {
        this.user = user;
    }

    public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
        PreparedStatement ps = c.prepareStatement("insert into users(id, name, password)" +
                "values(?, ?, ?)");
        ps.setString(1, user.getId());
        ps.setString(2, user.getName());
        ps.setString(3, user.getPassword());

        return ps;
    }
}
