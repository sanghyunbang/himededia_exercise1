package dao;

import model.Member;
import util.DBConnection;

import java.sql.*;

public class MemberDAO {

    // 📌 회원 가입 (Create)
    public boolean registerMember(Member member) {
        String query = "INSERT INTO MEMBER (USERNAME, PASSWORD, NAME, ROLE, GENDER, MOBILE, EMAIL) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, member.getUsername());
            pstmt.setString(2, member.getPassword()); // 🚨 비밀번호 해싱 필요
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

    // 📌 로그인 기능 (Read)
    public Member login(String username, String password) {
        String query = "SELECT USERNAME, PASSWORD, NAME, ROLE, GENDER, MOBILE, EMAIL, current_rentals, has_overdue FROM MEMBER WHERE USERNAME = ? AND PASSWORD = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, username);
            pstmt.setString(2, password); // 🚨 비밀번호는 해싱된 값과 비교 필요
            
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return new Member(
                    rs.getString("USERNAME"),  // ✅ `ID`가 아닌 `USERNAME`을 사용
                    rs.getString("PASSWORD"),
                    rs.getString("NAME"),
                    rs.getString("ROLE"),
                    rs.getString("GENDER"),
                    rs.getString("MOBILE"),
                    rs.getString("EMAIL"),
                    rs.getInt("current_rentals"),  // ✅ 존재하는 컬럼 정확히 가져오기
                    rs.getBoolean("has_overdue")   // ✅ 존재하는 컬럼 정확히 가져오기
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    // 📌 회원 정보 수정 (Update)
    public boolean updateMember(Member member) {
        String query = "UPDATE MEMBER SET NAME = ?, GENDER = ?, MOBILE = ?, EMAIL = ? WHERE USERNAME = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, member.getName());
            pstmt.setString(2, member.getGender());
            pstmt.setString(3, member.getMobile());
            pstmt.setString(4, member.getEmail());
            pstmt.setString(5, member.getUsername());  // ✅ `id` 대신 `username` 사용
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 📌 회원 탈퇴 (Delete)
    public boolean deleteMember(String username) {  // ✅ `id` 대신 `username` 사용
        String query = "DELETE FROM MEMBER WHERE USERNAME = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    
 // 📌 특정 대출 ID(rentId)에 해당하는 회원의 USERNAME 조회
    public String getUsernameByRentId(Connection conn, int rentId) throws SQLException {
        String query = "SELECT username FROM RENT WHERE rent_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, rentId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("username");
            }
        }
        return null; // 🚨 대출 기록이 존재하지 않을 경우 NULL 반환
    }

    // 📌 MEMBER 테이블에서 current_rentals 감소
    public boolean updateMemberRentalCount(Connection conn, String username) throws SQLException {
        String query = "UPDATE MEMBER SET current_rentals = current_rentals - 1 " +
                       "WHERE username = ? AND current_rentals > 0";

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            return pstmt.executeUpdate() > 0;
        }
    }

    // 📌 MEMBER 테이블에서 연체 상태 업데이트
    public boolean updateMemberOverdueStatus(Connection conn, String username) throws SQLException {
        String query = "UPDATE MEMBER " +
                       "SET has_overdue = (SELECT EXISTS (" +
                       "    SELECT 1 FROM RENT " +
                       "    WHERE RENT.username = MEMBER.username " +
                       "    AND due_date < CURRENT_TIMESTAMP " +
                       "    AND return_date IS NULL)) " +
                       "WHERE username = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            return pstmt.executeUpdate() > 0;
        }
    }
    
    
    
//    // 📌 MEMBER 테이블에서 current_rentals 감소
//    public boolean updateMemberRentalCount(Connection conn, String username) throws SQLException {
//        String query = "UPDATE MEMBER SET current_rentals = current_rentals - 1 " +
//                       "WHERE username = ? AND current_rentals > 0";
//
//        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
//            pstmt.setString(1, username);
//            return pstmt.executeUpdate() > 0;
//        }
//    }
//
//    // 📌 MEMBER 테이블에서 연체 상태 업데이트
//    public boolean updateMemberOverdueStatus(Connection conn, String username) throws SQLException {
//        String query = "UPDATE MEMBER " +
//                       "SET has_overdue = (SELECT EXISTS (" +
//                       "    SELECT 1 FROM RENT " +
//                       "    WHERE RENT.username = MEMBER.username " +
//                       "    AND due_date < CURRENT_TIMESTAMP " +
//                       "    AND return_date IS NULL)) " +
//                       "WHERE username = ?";
//
//        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
//            pstmt.setString(1, username);
//            return pstmt.executeUpdate() > 0;
//        }
//    }
}
