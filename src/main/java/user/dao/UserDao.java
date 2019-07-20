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

    // 3.2.2 전략과 클라이언트의 동거
    // 1) 로컬 클래스
    //  현재 add() 메서드의 두가지 개선하고 싶은 문제점
    //  1. DAO 메서드마다 새로운 StatementStrategy 구현 클래스를 만들어야 한다는점
    //    이렇게 되면 기존 UserDao 때보다 클래스 파일 개수가 많이 늘어난다
    //  2. DAO 메서드에서 StatementStrategy에 전달할 User와 같은 부가적인 정보가 있는 경우,
    //    이를 위해 오브젝트를 전달받는 생성자와 이를 저장해둘 인스턴스 변수를 번거롭게
    //    만들어야 한다
    //
    //  클래스 파일이 많아지는 문제는 간단한 해결 방법이 있다
    // StatementStrategy 전략 클래스를 매번 독립된 파일로 만들지 말고 UserDao 클래스 안에
    // 내부 클래스로 정의해 버리는 것이다.
    //  DelecteAllStatement나 AddStatement는 UserDao 밖에서는 사용되지 않는다
    // 둘 다 UserDao에서만 사용되고, UserDao의 메서드 로직에 강하게 결합되어 있다
    //  이런 경우라면 내부클래스로 만들 수 있다

    // 1)-1 로컬클래스를 이용한 add() 메서드
    //  AddStatement가 사용될 곳이 add() 메서드뿐이라면, 이렇게 로컬 클래스로 선언하여
    // 사용하는 것도 좋다. 덕분에 클래스파일이 하나 줄었고, add() 메서드 안에
    // PreparedStatement 생성 로직을 함께 볼 수 있으니 코드를 이해하기도 좋다
    /*public void add(User user) throws SQLException {
        class AddStatement implements StatementStrategy {
            User user;

            public AddStatement(User user) {
                this.user = user;
            }

            public PreparedStatement makePreparedStatement(Connection c)
                    throws SQLException {
                PreparedStatement ps =
                        c.prepareStatement("insert into users(id, name, password)" +
                                "values(?, ?, ?)");

                ps.setString(1, user.getId());
                ps.setString(2, user.getName());
                ps.setString(3, user.getPassword());

                return ps;
            }
        }

        StatementStrategy st = new AddStatement(user);
        jdbcContextWithStatementStrategy(st);
    }*/

    // 1)-2 로컬 클래스에는 또 한 가지 장점이 있다
    //  바로 로컬 클래스는 클래스가 내부 클래스이기 때문에 자신이 선언된 곳의 정보에
    // 접근할 수 있다는 점이다
    //  AddStatement는 User 정보를 필요로 한다. 이를 위해 위 코드에서 생성자를 만들어
    // add() 메서드에서 이를 전달해 주도록 했다.
    //  그런데 이렇게 add() 메서드 내에 AddStatement 클래스를 정의하면 번거롭게
    // 생성자를 통해 User 오브젝트를 전달해줄 필요가 없다.
    //  내부 메서드는 자신이 정의된 메서드의 로컬 변수에 직접 전근할 수 있기 때문이다
    // 메서드 파라미터도 일종의 로컬 변수이므로 add() 메서드의 user 변수를
    // AddStatement에서 직접 사용할 수 있다.
    //  다만 내부 클래스에서 외부의 변수를 사용할 때는 외부 변수는 반드시 final로
    // 선언해줘야 한다. user 파라미터는 메서드 내부에서 변경될 일이 없으므로
    // final로 선언해도 무방하다.
    //  이렇게 내부 클래스의 장점을 이용하면 user 정보를 전달받기 위해 만들었던
    // 생성자와 인스턴스 변수를 제거할 수 있으므로 AddStatement가 더 간결해진다
    /*public void add(final User user) throws SQLException {
        class AddStatement implements StatementStrategy {
            public PreparedStatement makePreparedStatement(Connection c)
                    throws SQLException {
                PreparedStatement ps =
                        c.prepareStatement("insert into users(id, name, password)" +
                                "values(?, ?, ?)");

                ps.setString(1, user.getId());
                ps.setString(2, user.getName());
                ps.setString(3, user.getPassword());

                return ps;
            }
        }

        StatementStrategy st = new AddStatement(); //생성자파라미터로 user를 전달하지 않아도 된다
        jdbcContextWithStatementStrategy(st);
    }*/

    // 2)-1 익명 내부 클래스
    //  AddStatement 클래스는 add() 메서드에서만 사용할 용도로 만들어졌다.
    // 그러므로 익명 내부 클래스를 사용, 좀 더 간결하게 클래스 이름도 제거할 수 있다
    /*public void add(final User user) throws SQLException {
        StatementStrategy st = new StatementStrategy() {
            public PreparedStatement makePreparedStatement(Connection c)
                    throws SQLException {
                PreparedStatement ps =
                        c.prepareStatement("insert into users(id, name, password)" +
                                "values(?, ?, ?)");
                ps.setString(1, user.getId());
                ps.setString(2, user.getName());
                ps.setString(3, user.getPassword());

                return ps;
            }
        };
        jdbcContextWithStatementStrategy(st);
    }*/

    // 2)-2
    //  만들어진 익명 내부 클래스의 오브젝트는 딱 한 번만 사용할 테니 굳이 변수에 담아두지
    // 말고 jdbcContextWithStatementStrategy() 메서드의 파라미터에서 바로 생성하는 편이 낫다.
    public void add(final User user) throws SQLException {
        jdbcContextWithStatementStrategy(
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
