package model;

public class Member {
    private int id;
    private String username;
    private String password;
    private String name;
    private String role;
    private String gender;
    private String mobile;
    private String email;

    // 생성자
    public Member(int id, String username, String password, String name, String role, String gender, String mobile, String email) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.role = role;
        this.gender = gender;
        this.mobile = mobile;
        this.email = email;
    }

    // Getter & Setter
    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getName() { return name; }
    public String getRole() { return role; }
    public String getGender() { return gender; }
    public String getMobile() { return mobile; }
    public String getEmail() { return email; }

    public void setId(int id) { this.id = id; }
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setName(String name) { this.name = name; }
    public void setRole(String role) { this.role = role; }
    public void setGender(String gender) { this.gender = gender; }
    public void setMobile(String mobile) { this.mobile = mobile; }
    public void setEmail(String email) { this.email = email; }
}

