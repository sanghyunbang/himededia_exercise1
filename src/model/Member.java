package model;

public class Member {
    private String username;
    private String password;
    private String name;
    private String role;
    private String email;
    private String gender;
    private String mobile;
    private int currentRentals;
    private boolean hasOverdue;

    // 🚀 기본 생성자
    public Member() {}

    // 🚀 기존 생성자 (currentRentals, hasOverdue 제외)
    public Member(String username, String password, String name, String role, String gender, String mobile, String email) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.role = role;
        this.gender = gender;
        this.mobile = mobile;
        this.email = email;
    }

    // 🚀 **오류 해결을 위한 추가 생성자**
    public Member(String username, String password, String name, String role, String gender, String mobile, String email, int currentRentals, boolean hasOverdue) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.role = role;
        this.gender = gender;
        this.mobile = mobile;
        this.email = email;
        this.currentRentals = currentRentals;
        this.hasOverdue = hasOverdue;
    }

    // 📌 Getter & Setter
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getName() { return name; }
    public String getRole() { return role; }
    public String getGender() { return gender; }
    public String getMobile() { return mobile; }
    public String getEmail() { return email; }
    public int getCurrentRentals() { return currentRentals; }
    public boolean getHasOverdue() { return hasOverdue; }

    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setName(String name) { this.name = name; }
    public void setRole(String role) { this.role = role; }
    public void setGender(String gender) { this.gender = gender; }
    public void setMobile(String mobile) { this.mobile = mobile; }
    public void setEmail(String email) { this.email = email; }
    public void setCurrentRentals(int currentRentals) { this.currentRentals = currentRentals; }
    public void setHasOverdue(boolean hasOverdue) { this.hasOverdue = hasOverdue; }
}


