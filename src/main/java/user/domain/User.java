package user.domain;

public class User {
    private String id;
    private String name;
    private String password;
    private Level level;
    private int numoflogin; // 로그인 수
    private int numofrecommend; // 추천 수

    public User(String id, String name, String password, Level level,
                int numoflogin, int numofrecommend) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.level = level;
        this.numoflogin = numoflogin;
        this.numofrecommend = numofrecommend;
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

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public int getNumoflogin() {
        return numoflogin;
    }

    public void setNumoflogin(int numoflogin) {
        this.numoflogin = numoflogin;
    }

    public int getNumofrecommend() {
        return numofrecommend;
    }

    public void setNumofrecommend(int numofrecommend) {
        this.numofrecommend = numofrecommend;
    }

}

