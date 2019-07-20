package user.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import user.domain.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {
    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void add(User user) throws SQLException {
        StatementStrategy st = new AddStatement(user);
        jdbcContextWithStatementStrategy(st);
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

        StatementStrategy st = new DeleteAllStatement(); // 선정한 전략 클래스의 오브젝트 생성
        jdbcContextWithStatementStrategy(st); // 컨텍스트 호출. 전략 오브젝트 전달

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

    // 메서드로 분리한 try/catch/finally 컨텍스트 코드
    public void jdbcContextWithStatementStrategy(StatementStrategy stmt) throws SQLException {
        Connection c = null;
        PreparedStatement ps = null;

        try {
            c = dataSource.getConnection();

            ps = stmt.makePreparedStatement(c);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw e;

        } finally {
            if (ps != null) {
                try {
                    ps.close();

                } catch (SQLException e) {

                }
            }

            if (c != null) {
                try {
                    c.close();

                } catch (SQLException e) {

                }
            }
        }
    }

}
