package user.domain;

public class User {
    String id;
    String name;
    String password;

    // 2.3.3 getCount() 테스트를 위해 생성자 추가
    public User(String id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }

    public User() {
        // 파라미터가 없는 디폴트 생성자 추가
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}

