//// <<< 회원 관리>>>


package dao;
import model.Member;
import util.DBConnection;

import java.sql.*;

public class MemberDAO {

    // 회원 가입 (Create)
    public boolean registerMember(Member member) {
        String query = "INSERT INTO MEMBER (USERNAME, PASSWORD, NAME, ROLE, GENDER, MOBILE, EMAIL) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, member.getUsername());
            pstmt.setString(2, member.getPassword()); // 비밀번호 해싱 적용 필요
            pstmt.setString(3, member.getName());
            pstmt.setString(4, member.getRole());
            pstmt.setString(5, member.getGender());
            pstmt.setString(6, member.getMobile());
            pstmt.setString(7, member.getEmail());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 로그인 기능 (Read)
    public Member login(String username, String password) {
        String query = "SELECT * FROM MEMBER WHERE USERNAME = ? AND PASSWORD = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password); // 비밀번호는 해싱된 값과 비교 필요
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Member(
                        rs.getInt("ID"),
                        rs.getString("USERNAME"),
                        rs.getString("PASSWORD"),
                        rs.getString("NAME"),
                        rs.getString("ROLE"),
                        rs.getString("GENDER"),
                        rs.getString("MOBILE"),
                        rs.getString("EMAIL")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
 // 회원 정보 수정 (Update)
    public boolean updateMember(Member member) {
        String query = "UPDATE MEMBER SET NAME = ?, GENDER = ?, MOBILE = ?, EMAIL = ? WHERE ID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, member.getName());
            pstmt.setString(2, member.getGender());
            pstmt.setString(3, member.getMobile());
            pstmt.setString(4, member.getEmail());
            pstmt.setInt(5, member.getId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    
 // 회원 탈퇴 (Delete)
    public boolean deleteMember(int memberId) {
        String query = "DELETE FROM MEMBER WHERE ID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, memberId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    
    
}
