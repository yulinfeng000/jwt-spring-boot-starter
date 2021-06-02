package fun.yulinfeng.jwt.starter;

public class TestUser {
    public String username;
    public String password;

    public TestUser() {
    }

    public TestUser(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public String toString() {
        return "TestUser{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
