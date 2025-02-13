package dao;
import model.Book;
import util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {

    // 도서 등록 (Create)
    public boolean addBook(Book book) {
        String query = "INSERT INTO BOOK (TITLE, ISBN, AUTHOR, PUBLISHER, ISSUE_YEAR, PRICE, STOCK) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getIsbn());
            pstmt.setString(3, book.getAuthor());
            pstmt.setString(4, book.getPublisher());
            pstmt.setInt(5, book.getIssueYear());
            pstmt.setDouble(6, book.getPrice());
            pstmt.setInt(7, book.getStock());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 모든 도서 조회 (Read)
    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        String query = "SELECT * FROM BOOK";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                books.add(new Book(
                        rs.getInt("ID"),
                        rs.getString("TITLE"),
                        rs.getString("ISBN"),
                        rs.getString("AUTHOR"),
                        rs.getString("PUBLISHER"),
                        rs.getInt("ISSUE_YEAR"),
                        rs.getDouble("PRICE"),
                        rs.getInt("STOCK")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    // 특정 도서 검색 (제목 또는 저자로 검색)
    public List<Book> searchBooks(String keyword) {
        List<Book> books = new ArrayList<>();
        String query = "SELECT * FROM BOOK WHERE TITLE LIKE ? OR AUTHOR LIKE ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, "%" + keyword + "%");
            pstmt.setString(2, "%" + keyword + "%");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                books.add(new Book(
                        rs.getInt("ID"),
                        rs.getString("TITLE"),
                        rs.getString("ISBN"),
                        rs.getString("AUTHOR"),
                        rs.getString("PUBLISHER"),
                        rs.getInt("ISSUE_YEAR"),
                        rs.getDouble("PRICE"),
                        rs.getInt("STOCK")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    // 특정 도서 조회 (ISBN으로 검색)
    public Book getBookByISBN(String isbn) {
        String query = "SELECT * FROM BOOK WHERE ISBN = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, isbn);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Book(
                        rs.getInt("ID"),
                        rs.getString("TITLE"),
                        rs.getString("ISBN"),
                        rs.getString("AUTHOR"),
                        rs.getString("PUBLISHER"),
                        rs.getInt("ISSUE_YEAR"),
                        rs.getDouble("PRICE"),
                        rs.getInt("STOCK")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 도서 정보 수정 (Update)
    public boolean updateBook(Book book) {
        String query = "UPDATE BOOK SET TITLE=?, AUTHOR=?, PUBLISHER=?, ISSUE_YEAR=?, PRICE=?, STOCK=? WHERE ISBN=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getAuthor());
            pstmt.setString(3, book.getPublisher());
            pstmt.setInt(4, book.getIssueYear());
            pstmt.setDouble(5, book.getPrice());
            pstmt.setInt(6, book.getStock());
            pstmt.setString(7, book.getIsbn());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 도서 삭제 (Delete)
    public boolean deleteBook(String isbn) {
        String query = "DELETE FROM BOOK WHERE ISBN=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, isbn);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 도서 재고 업데이트 (대출 시 감소, 반납 시 증가)
    public boolean updateStock(int bookId, int change) {
        String query = "UPDATE BOOK SET STOCK = STOCK + ? WHERE ID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, change);
            pstmt.setInt(2, bookId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
 // 도서가 대출 가능한 상태인지 확인 (재고가 1권 이상 있는지 확인)
    public boolean isBookAvailable(int bookId) {
        String query = "SELECT STOCK FROM BOOK WHERE ID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, bookId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("STOCK") > 0;  // 재고가 1 이상이면 대출 가능
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
