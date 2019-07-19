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
        Connection c = dataSource.getConnection();

        PreparedStatement ps = c.prepareStatement(
                "insert into users(id, name, password) values(?,?,?)"
        );
        ps.setString(1, user.getId());
        ps.setString(2, user.getName());
        ps.setString(3, user.getPassword());

        ps.executeUpdate();

        ps.close();
        c.close();
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

    // 3.1.1 예외처리 기능을 갖춘 DAO
    // try문 적용
    //  이 메서드에서는 Connection과 PreparedStatement라는 두 개의 공유 리소스를 가져와서
    // 사용한다. 정상적으로 처리되면 메서드를 마치기전 각각 close()를 호출, 리소스를 반환한다
    //  하지만 PreparedStatement를 처리하는 중에 예외가 발생하면 메서드 실행을 끝마치지 못하고
    // 바로 메서드를 빠져나가게 된다
    //  이때 문제는 Connection과 PrearedStatement의 close() 메서드가 실행되지 않아서
    // 제대로 리소스가 반환되지 않을 수 있다

    // deleteAll() 메서드 : 테이블의 모든 레코드를 삭제
    public void deleteAll() throws SQLException {
        Connection c = null;
        PreparedStatement ps = null;

        try {
            // 예외가 발생할 가능성이 있는 코드를 모두 try 블럭으로 묶어준다
            c = dataSource.getConnection();
            ps = c.prepareStatement("delete from users");
            ps.executeUpdate();

        } catch (SQLException e) {
            throw e; // 예외가 발생했을 때 부가적인 작업을 해줄 수 있도록 catch블록을 둔다
                     // 아직은 예외를 다시 메서드 밖으로 던지는 것 밖에 없다

        } finally {
            // finally이므로 try 블록에서 예외가 발생했을 때나 안 했을 때나 모두 실행된다
            if (ps != null) {
                try {
                    ps.close();

                } catch (SQLException e) {
                    // ps.close() 메서드에서도 SQLException이 발생할 수 있기 때문에
                    // 이를 잡아줘야 한다
                    // 그렇지 않으면 Connection을 close() 하지 못하고 메서드를 빠져나갈 수 있다
                }
            } // if (ps != null){} 끝

            if (c != null) {
                try {
                    c.close();

                } catch (SQLException e) {

                }
            }
        }


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
