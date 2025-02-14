package dao;
import model.Rent;
import util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RentDAO {

    // 1-1. 현재 대출 중인 권수 확인 (최대 5권 제한을 위해 사용)
    public int getActiveRentCount(String memberId) {
        String query = "SELECT current_rentals FROM member WHERE username = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, memberId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;  // 대출 중인 도서가 없으면 0 반환
    }

    
    // 1-2. 현재 연체 여부 확인 (연체가 있을 시 대출 제한)--7과 비교: 7은 해당 책의 연체 여부
    public int getOverdue(String memberId) {
        String query = "SELECT has_overdue FROM member WHERE username = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, memberId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;  // 연체 중인 도서가 없으면 0 반환
    }
    
    
    // 2. 도서 대출 (INSERT INTO RENT)
    
    public boolean rentBook(String memberId, int bookId) {
        String rentQuery = "INSERT INTO RENT (username, instance_id, rent_date, due_date) " +
                           "VALUES (?, ?, CURRENT_TIMESTAMP, DATE_ADD(CURRENT_TIMESTAMP, INTERVAL 14 DAY))";

        String historyQuery = "INSERT INTO RENTHISTORY (username, instance_id, rent_date, due_date) " +
                              "VALUES (?, ?, CURRENT_TIMESTAMP, DATE_ADD(CURRENT_TIMESTAMP, INTERVAL 14 DAY))";

        String updateMemberQuery = "UPDATE member SET current_rentals = current_rentals + 1 WHERE username = ?";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false); // 🚨 트랜잭션 시작

            try (PreparedStatement rentStmt = conn.prepareStatement(rentQuery);
                 PreparedStatement historyStmt = conn.prepareStatement(historyQuery);
                 PreparedStatement updateMemberStmt = conn.prepareStatement(updateMemberQuery)) {
                
                // RENT 테이블에 데이터 삽입
                rentStmt.setString(1, memberId);
                rentStmt.setInt(2, bookId);
                int rentResult = rentStmt.executeUpdate();

                // RENTHISTORY 테이블에 데이터 삽입
                historyStmt.setString(1, memberId);
                historyStmt.setInt(2, bookId);
                int historyResult = historyStmt.executeUpdate();

                // MEMBER 테이블의 current_rentals 증가
                updateMemberStmt.setString(1, memberId);
                int updateResult = updateMemberStmt.executeUpdate();

                // 모든 쿼리가 성공하면 커밋
                if (rentResult > 0 && historyResult > 0 && updateResult > 0) {
                    conn.commit();  // 🚀 트랜잭션 성공
                    return true;
                } else {
                    conn.rollback(); // ❌ 하나라도 실패하면 롤백
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // 예외 발생 시 false 반환
    }



    // 3. 특정 회원의 대출 내역 조회
    public List<Rent> getRentHistory(String memberId) {
    	
        List<Rent> rentList = new ArrayList<>();
        String query = "SELECT * FROM RENT WHERE username = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, memberId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                rentList.add(new Rent(
                        rs.getInt("rent_id"),
                        rs.getString("username"),
                        rs.getInt("instance_id"),
                        rs.getTimestamp("rent_date"),
                        rs.getTimestamp("due_date"),
                        rs.getTimestamp("return_date"),
                        rs.getInt("late_fee"),
                        rs.getInt("renewal_count")
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

    
    
    
    
    
    
    
//    // 5. 도서 반납 (UPDATE RENT 테이블에서 반납 날짜 추가)
//    public boolean returnBook(int rentId) {
//        String updateRentQuery = "UPDATE RENT " +
//                                 "SET return_date = CURRENT_TIMESTAMP, " +
//                                 "late_fee = CASE WHEN due_date < CURRENT_TIMESTAMP " +
//                                 "THEN TIMESTAMPDIFF(DAY, due_date, CURRENT_TIMESTAMP) * 500 ELSE 0 END, " +
//                                 "status = '반납됨' " +
//                                 "WHERE rent_id = ? AND return_date IS NULL";
//
//        String moveToHistoryQuery = "INSERT INTO RENTHISTORY " +
//                                    "(rent_id, username, instance_id, rent_date, due_date, return_date, late_fee, renewal_count) " +
//                                    "SELECT rent_id, username, instance_id, rent_date, due_date, return_date, late_fee, renewal_count " +
//                                    "FROM RENT WHERE rent_id = ?";
//
//        String deleteFromRentQuery = "DELETE FROM RENT WHERE rent_id = ?";
//
//        String updateBookStatusQuery = "UPDATE BOOKINSTANCE SET status = 'Available' " +
//                                       "WHERE instance_id = (SELECT instance_id FROM RENT WHERE rent_id = ?)";
//
//        String updateMemberQuery = "UPDATE MEMBER SET current_rentals = current_rentals - 1 " +
//                                   "WHERE username = (SELECT username FROM RENT WHERE rent_id = ?) AND current_rentals > 0";
//
//        String updateOverdueQuery = "UPDATE MEMBER " +
//                                    "SET has_overdue = (SELECT EXISTS (" +
//                                    "    SELECT 1 FROM RENT " +
//                                    "    WHERE RENT.username = MEMBER.username " +
//                                    "    AND due_date < CURRENT_TIMESTAMP " +
//                                    "    AND return_date IS NULL)) " +
//                                    "WHERE username = (SELECT username FROM RENT WHERE rent_id = ?)";
//
//        try (Connection conn = DBConnection.getConnection()) {
//            conn.setAutoCommit(false); // 🚨 트랜잭션 시작
//
//            try (PreparedStatement updateRentStmt = conn.prepareStatement(updateRentQuery);
//                 PreparedStatement moveToHistoryStmt = conn.prepareStatement(moveToHistoryQuery);
//                 PreparedStatement deleteFromRentStmt = conn.prepareStatement(deleteFromRentQuery);
//                 PreparedStatement updateBookStmt = conn.prepareStatement(updateBookStatusQuery);
//                 PreparedStatement updateMemberStmt = conn.prepareStatement(updateMemberQuery);
//                 PreparedStatement updateOverdueStmt = conn.prepareStatement(updateOverdueQuery)) {
//
//                // 1️⃣ RENT 테이블에서 반납 처리 및 연체료 계산
//                updateRentStmt.setInt(1, rentId);
//                int rentUpdateResult = updateRentStmt.executeUpdate();
//
//                // 2️⃣ RENTHISTORY 테이블로 데이터 이동
//                moveToHistoryStmt.setInt(1, rentId);
//                int historyInsertResult = moveToHistoryStmt.executeUpdate();
//
//                // 3️⃣ RENT 테이블에서 데이터 삭제
//                deleteFromRentStmt.setInt(1, rentId);
//                int rentDeleteResult = deleteFromRentStmt.executeUpdate();
//
//                // 4️⃣ BOOKINSTANCE 테이블에서 상태 변경 ('Borrowed' → 'Available')
//                updateBookStmt.setInt(1, rentId);
//                int bookUpdateResult = updateBookStmt.executeUpdate();
//
//                // 5️⃣ MEMBER 테이블에서 대출 수 감소
//                updateMemberStmt.setInt(1, rentId);
//                int memberUpdateResult = updateMemberStmt.executeUpdate();
//
//                // 6️⃣ MEMBER 테이블에서 연체 상태 업데이트
//                updateOverdueStmt.setInt(1, rentId);
//                int overdueUpdateResult = updateOverdueStmt.executeUpdate();
//
//                // 🚀 모든 작업이 성공하면 커밋
//                if (rentUpdateResult > 0 && historyInsertResult > 0 && rentDeleteResult > 0 &&
//                    bookUpdateResult > 0 && memberUpdateResult > 0 && overdueUpdateResult > 0) {
//                    conn.commit();
//                    return true;
//                } else {
//                    conn.rollback(); // ❌ 하나라도 실패하면 롤백
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return false; // 예외 발생 시 false 반환
//    }
//    
    
//    public boolean returnBook(int rentId) {
//        try (Connection conn = DBConnection.getConnection()) {
//            conn.setAutoCommit(false); // 🚨 트랜잭션 시작
//
//            // MemberDAO 객체 생성
//            MemberDAO memberDAO = new MemberDAO();
//
//            boolean rentUpdated = updateRentReturnInfo(conn, rentId);  // ✅ RENT 테이블에서 반납 정보 업데이트 (return_date, late_fee)
//            boolean historyInserted = moveRentToHistory(conn, rentId);  // ✅ RENT → RENTHISTORY 테이블로 대출 내역 이동
//            boolean rentDeleted = deleteRentRecord(conn, rentId);  // ✅ RENT 테이블에서 반납 완료된 대출 기록 삭제
//            boolean bookUpdated = updateBookStatus(conn, rentId);  // ✅ BOOKINSTANCE 테이블에서 대출된 책 상태 변경 ('Borrowed' → 'Available')
//            boolean memberUpdated = memberDAO.updateMemberRentalCount(conn, rentId);  // ✅ MEMBER 테이블에서 current_rentals(대출 중인 책 수) 감소
//            boolean overdueUpdated = memberDAO.updateMemberOverdueStatus(conn, rentId);  // ✅ MEMBER 테이블에서 has_overdue(연체 여부) 갱신
//
//
//            // 🚀 모든 작업이 성공하면 커밋
//            if (rentUpdated && historyInserted && rentDeleted && bookUpdated && memberUpdated && overdueUpdated) {
//                conn.commit();
//                return true;
//            } else {
//                conn.rollback(); // ❌ 하나라도 실패하면 롤백
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return false; // 예외 발생 시 false 반환
//    }


    public boolean returnBook(int rentId) {
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false); // 🚨 트랜잭션 시작

            // MemberDAO 객체 생성
            MemberDAO memberDAO = new MemberDAO();

            // 🚀 `rentId`를 기반으로 `USERNAME`을 조회
            String username = memberDAO.getUsernameByRentId(conn, rentId);
            if (username == null) {
                System.out.println("해당 대출 기록이 존재하지 않습니다.");
                return false;
            }

            boolean rentUpdated = updateRentReturnInfo(conn, rentId);  // ✅ RENT 테이블에서 반납 정보 업데이트 (return_date, late_fee)
            boolean historyInserted = moveRentToHistory(conn, rentId);  // ✅ RENT → RENTHISTORY 테이블로 대출 내역 이동
            boolean rentDeleted = deleteRentRecord(conn, rentId);  // ✅ RENT 테이블에서 반납 완료된 대출 기록 삭제
            boolean bookUpdated = updateBookStatus(conn, rentId);  // ✅ BOOKINSTANCE 테이블에서 대출된 책 상태 변경 ('Borrowed' → 'Available')
            boolean memberUpdated = memberDAO.updateMemberRentalCount(conn, username);  // ✅ MEMBER 테이블에서 current_rentals(대출 중인 책 수) 감소
            boolean overdueUpdated = memberDAO.updateMemberOverdueStatus(conn, username);  // ✅ MEMBER 테이블에서 has_overdue(연체 여부) 갱신

            // 🚀 모든 작업이 성공하면 커밋
            if (rentUpdated && historyInserted && rentDeleted && bookUpdated && memberUpdated && overdueUpdated) {
                conn.commit();
                return true;
            } else {
                conn.rollback(); // ❌ 하나라도 실패하면 롤백
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // 예외 발생 시 false 반환
    }
 
    
    
    // RENT 테이블에서 반납 처리 및 연체료 계산
    private boolean updateRentReturnInfo(Connection conn, int rentId) throws SQLException {
        String query = "UPDATE RENT " +
                       "SET return_date = CURRENT_TIMESTAMP, " +
                       "late_fee = CASE WHEN due_date < CURRENT_TIMESTAMP " +
                       "THEN TIMESTAMPDIFF(DAY, due_date, CURRENT_TIMESTAMP) * 500 ELSE 0 END, " +
                       "status = '반납됨' " +
                       "WHERE rent_id = ? AND return_date IS NULL";

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, rentId);
            return pstmt.executeUpdate() > 0;
        }
    }

    // RENT 데이터를 RENTHISTORY로 이동
 
    private boolean moveRentToHistory(Connection conn, int rentId) throws SQLException {
        String query = "INSERT INTO RENTHISTORY (rent_id, username, instance_id, rent_date, due_date, return_date, late_fee, renewal_count) " +
                       "SELECT rent_id, username, instance_id, rent_date, due_date, return_date, late_fee, renewal_count " +
                       "FROM RENT WHERE rent_id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, rentId);
            return pstmt.executeUpdate() > 0;
        }
    }

    // RENT 테이블에서 삭제
    
    private boolean deleteRentRecord(Connection conn, int rentId) throws SQLException {
        String query = "DELETE FROM RENT WHERE rent_id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, rentId);
            return pstmt.executeUpdate() > 0;
        }
    }
    
    // BOOKINSTANCE 테이블에서 상태 변경 (Borrowed → Available)
    
    private boolean updateBookStatus(Connection conn, int rentId) throws SQLException {
        String query = "UPDATE BOOKINSTANCE SET status = 'Available' " +
                       "WHERE instance_id = (SELECT instance_id FROM RENT WHERE rent_id = ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, rentId);
            return pstmt.executeUpdate() > 0;
        }
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

