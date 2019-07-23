package user.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import user.domain.User;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserDaoJdbc implements UserDao {

    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    private JdbcTemplate jdbcTemplate;

    private RowMapper<User> userMapper =
            new RowMapper<User>() {
                public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                    User user = new User();
                    user.setId(rs.getString("id"));
                    user.setName(rs.getString("name"));
                    user.setPassword(rs.getString("password"));

                    return user;
                }
            }; // userMapper end

    public void add(final User user) {
        this.jdbcTemplate.update("insert into users(id, name, password) value(?, ?, ?)",
                user.getId(), user.getName(), user.getPassword());
    }

    public User get(String id) {

        return this.jdbcTemplate.queryForObject("select * from users where id = ?",
                new Object[]{id}, this.userMapper);
    }

    // deleteAll() 메서드 : 테이블의 모든 레코드를 삭제
    public void deleteAll() {
        this.jdbcTemplate.update("delete from users");

    }

    // getCount() 메서드 : 테이블의 레코드 개수를 반환
    public int getCount() {
        // queryForInt()를 사용한 코드
        return this.jdbcTemplate.queryForObject("select count(*) from users", Integer.class);

    } // getCount() end

    public List<User> getAll() {

        return this.jdbcTemplate.query("select * from users order by id",
                this.userMapper);
    } // getAll() end

}
