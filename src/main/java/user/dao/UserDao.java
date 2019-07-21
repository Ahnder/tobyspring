package user.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import user.domain.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;

    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);

        this.dataSource = dataSource;
    }

    public void add(final User user) throws SQLException {
        this.jdbcTemplate.update("insert into users(id, name, password) value(?, ?, ?)",
                user.getId(), user.getName(), user.getPassword());
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
        this.jdbcTemplate.update("delete from users");

    }

    // getCount() 메서드 : 테이블의 레코드 개수를 반환
    public int getCount() throws SQLException {
        // queryForInt()를 사용한 코드
        return this.jdbcTemplate.queryForObject("select count(*) from users", Integer.class);

        // 밑의 코드를 간략화 시킨 코드가 위의 queryForInt()를 사용한 코드이다
        /*return this.jdbcTemplate.query(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection con)
                    throws SQLException {
                return con.prepareStatement("select count(*) from users");
            }
        }, new ResultSetExtractor<Integer>() {
            public Integer extractData(ResultSet rs) throws SQLException,
                    DataAccessException {
                rs.next();
                return rs.getInt(1);
            }
        });*/

    }

}
