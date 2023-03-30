final class User {
    private  int userId;
    private  String userName;
    private final String email;
    private  String password;
    public User(int userId, String userName, String email, String password) {
        this.userId = userId;
        this.userName = userName;
        this.email = email;
        this.password = password;
    }
    public User(String email,String userName,int userId) {
        this.email = email;
        this.userName = userName;
        this.userId = userId;

    }
    public User(int userId,String email,String password){
        this.userId = userId;
        this.email = email;
        this.password = password;
    }
    public String getEmail() {
        return email;
    }
    public int getUserId() {
        return userId;
    }
    public String getUserName() {
        return userName;
    }
    public String getPassword() {
        return password;
    }

    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
