package dao;
import model.Rent;
import util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RentDAO {

    // 1. 현재 대출 중인 권수 확인 (최대 5권 제한을 위해 사용)
    public int getActiveRentCount(int memberId) {
        String query = "SELECT COUNT(*) FROM RENT WHERE MEMBER_ID = ? AND RETURNED_DATE IS NULL";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, memberId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;  // 대출 중인 도서가 없으면 0 반환
    }

    // 2. 도서 대출 (INSERT INTO RENT)
    public boolean rentBook(int memberId, int bookId) {
        String query = "INSERT INTO RENT (BOOK_ID, MEMBER_ID, RENT_DATE, EXPECTED_RETURN, STATUS) " +
                       "VALUES (?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL 14 DAY, '대출중')";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, bookId);
            pstmt.setInt(2, memberId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 3. 특정 회원의 대출 내역 조회
    public List<Rent> getRentHistory(int memberId) {
        List<Rent> rentList = new ArrayList<>();
        String query = "SELECT * FROM RENT WHERE MEMBER_ID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, memberId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                rentList.add(new Rent(
                        rs.getInt("ID"),
                        rs.getInt("BOOK_ID"),
                        rs.getInt("MEMBER_ID"),
                        rs.getTimestamp("RENT_DATE"),
                        rs.getTimestamp("EXPECTED_RETURN"),
                        rs.getTimestamp("RETURNED_DATE"),
                        rs.getTimestamp("EXTENDED_DATE"),
                        rs.getString("STATUS")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rentList;
    }

    // 4. 대출 ID로 책 ID 찾기 (반납 시 사용)
    public int getBookIdByRentId(int rentId) {
        String query = "SELECT BOOK_ID FROM RENT WHERE ID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, rentId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("BOOK_ID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;  // 책을 찾지 못한 경우 -1 반환
    }

    // 5. 도서 반납 (UPDATE RENT 테이블에서 반납 날짜 추가)
    public boolean returnBook(int rentId) {
        String query = "UPDATE RENT SET RETURNED_DATE = CURRENT_TIMESTAMP, STATUS = '반납됨' WHERE ID = ? AND RETURNED_DATE IS NULL";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, rentId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 6. 해당 대출이 이미 연장되었는지 확인
    public boolean isRentExtended(int rentId) {
        String query = "SELECT EXTENDED_DATE FROM RENT WHERE ID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, rentId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getTimestamp("EXTENDED_DATE") != null;  // 연장된 경우 true 반환
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 7. 연체 여부 확인 (현재 날짜 > 반납 예정일)
    public boolean isOverdue(int rentId) {
        String query = "SELECT EXPECTED_RETURN FROM RENT WHERE ID = ? AND RETURNED_DATE IS NULL";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, rentId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Timestamp expectedReturn = rs.getTimestamp("EXPECTED_RETURN");
                return expectedReturn.before(new Timestamp(System.currentTimeMillis()));  // 반납 예정일이 현재보다 이전이면 연체
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 8. 대출 연장 (반납 예정일 +14일)
    public boolean extendRent(int rentId) {
        String query = "UPDATE RENT SET EXPECTED_RETURN = EXPECTED_RETURN + INTERVAL 14 DAY, EXTENDED_DATE = CURRENT_TIMESTAMP " +
                       "WHERE ID = ? AND RETURNED_DATE IS NULL AND EXTENDED_DATE IS NULL";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, rentId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}

