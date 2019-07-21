package user.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import user.domain.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// UserDao를 분리된 JdbcContext를 DI 받아서 사용할 수 있게 리팩토링
public class UserDao {
    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private JdbcContext jdbcContext;

    public void setJdbcContext(JdbcContext jdbcContext) {
        this.jdbcContext = jdbcContext;
    }

    public void add(final User user) throws SQLException {
        this.jdbcContext.workWithStatementStrategy(
                new StatementStrategy() {
                    public PreparedStatement makePreparedStatement(Connection c)
                            throws SQLException {
                        PreparedStatement ps =
                                c.prepareStatement("insert into users(id, name," +
                                        "password) values(?, ?, ?)");
                        ps.setString(1, user.getId());
                        ps.setString(2, user.getName());
                        ps.setString(3, user.getPassword());

                        return ps;
                    }
                }
        );
    }

    public User get(String id) throws SQLException {
        Connection c = dataSource.getConnection();

        PreparedStatement ps = c.prepareStatement(
                "select * from users where id = ?"
        );
        ps.setString(1, id);

        ResultSet rs = ps.executeQuery();

        User user = null; // User는 null상태로 초기화
        // id를 조건으로 한 쿼리의 결과가 있으면 User 오브젝트를 만들고 값을 넣어준다
        if (rs.next()) {
            user = new User();
            user.setId(rs.getString("id"));
            user.setName(rs.getString("name"));
            user.setPassword(rs.getString("password"));
        }
        // ->
        rs.close();
        ps.close();
        c.close();

        // 결과가 없으면 User는 null 상태 그대로 일 것이다
        // 이를 확인해서 예외를 던져준다
        if (user == null) throw new EmptyResultDataAccessException(1);

        return user;
    }

    // deleteAll() 메서드 : 테이블의 모든 레코드를 삭제
    public void deleteAll() throws SQLException {
        this.jdbcContext.executeSql("delete from users");

    }

    // getCount() 메서드 : 테이블의 레코드 개수를 반환
    public int getCount() throws SQLException {
        Connection c = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            c = dataSource.getConnection();
            ps = c.prepareStatement("select count(*) from users");
            rs = ps.executeQuery();
            rs.next();

            return rs.getInt(1);

        } catch (SQLException e) {
            throw e;

        } finally {
            if (rs != null) {
                try {
                    rs.close();

                } catch (SQLException e) {

                }
            } // if(rs != null){} 끝

            if (ps != null) {
                try {
                    ps.close();

                } catch (SQLException e) {

                }
            } // if(ps != null){} 끝

            if (c != null) {
                try {
                    c.close();

                } catch (SQLException e) {

                }
            } // if(c != null){} 끝
        } // try~catch~finally문 끝

    }

}
