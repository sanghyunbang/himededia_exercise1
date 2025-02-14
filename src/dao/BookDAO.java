package dao;

import model.Book;
import util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {

    // 📌 도서 등록 (Create)
    public boolean addBook(Book book) {
        String bookQuery = "INSERT INTO books (title, isbn, publisher_id, issue_year, qty, author_id) " +
                           "VALUES (?, ?, (SELECT publisher_id FROM publisher WHERE name = ?), ?, ?, " +
                           "(SELECT author_id FROM author WHERE name = ?))";
        
        String instanceQuery = "INSERT INTO bookinstance (book_id, book_condition, status) " +
                               "VALUES ((SELECT book_id FROM books WHERE isbn = ?), ?, ?)";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false); // 🚨 트랜잭션 시작

            try (PreparedStatement bookStmt = conn.prepareStatement(bookQuery);
                 PreparedStatement instanceStmt = conn.prepareStatement(instanceQuery)) {

                // 📌 books 테이블에 삽입
                bookStmt.setString(1, book.getBooksTitle());
                bookStmt.setString(2, book.getBooksISBN());
                bookStmt.setString(3, book.getPublisherName());
                bookStmt.setString(4, book.getBooksIssue());
                bookStmt.setInt(5, book.getBooksQty());
                bookStmt.setString(6, book.getAuthorName());

                boolean bookInserted = bookStmt.executeUpdate() > 0;

                // 📌 bookinstance 테이블에 삽입
                instanceStmt.setString(1, book.getBooksISBN());
                instanceStmt.setString(2, book.getInstanceCondition());
                instanceStmt.setString(3, book.getInstanceStatus());

                boolean instanceInserted = instanceStmt.executeUpdate() > 0;

                if (bookInserted && instanceInserted) {
                    conn.commit();
                    return true;
                } else {
                    conn.rollback(); // ❌ 하나라도 실패하면 롤백
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 📌 모든 도서 조회 (Read)
    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        String query = "SELECT b.book_id, b.title, b.isbn, p.name AS publisher, b.issue_year, b.qty, a.name AS author, " +
                       "bi.book_condition, bi.status " +
                       "FROM books b " +
                       "JOIN publisher p ON b.publisher_id = p.publisher_id " +
                       "JOIN author a ON b.author_id = a.author_id " +
                       "LEFT JOIN bookinstance bi ON b.book_id = bi.book_id";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                books.add(new Book(
                        rs.getString("publisher"),
                        rs.getString("book_condition"),
                        rs.getString("status"),
                        null, null, // `instanceCreated` and `instanceUpdated` are handled in DB
                        rs.getString("title"),
                        rs.getString("isbn"),
                        rs.getString("issue_year"),
                        rs.getInt("qty"),
                        rs.getString("author")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    // 📌 특정 도서 검색 (제목 또는 저자로 검색)
    public List<Book> searchBooks(String keyword) {
        List<Book> books = new ArrayList<>();
        String query = "SELECT b.book_id, b.title, b.isbn, p.name AS publisher, b.issue_year, b.qty, a.name AS author, " +
                       "bi.book_condition, bi.status " +
                       "FROM books b " +
                       "JOIN publisher p ON b.publisher_id = p.publisher_id " +
                       "JOIN author a ON b.author_id = a.author_id " +
                       "LEFT JOIN bookinstance bi ON b.book_id = bi.book_id " +
                       "WHERE b.title LIKE ? OR a.name LIKE ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, "%" + keyword + "%");
            pstmt.setString(2, "%" + keyword + "%");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                books.add(new Book(
                        rs.getString("publisher"),
                        rs.getString("book_condition"),
                        rs.getString("status"),
                        null, null,
                        rs.getString("title"),
                        rs.getString("isbn"),
                        rs.getString("issue_year"),
                        rs.getInt("qty"),
                        rs.getString("author")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    // 📌 특정 도서 조회 (ISBN으로 검색)
    public Book getBookByISBN(String isbn) {
        String query = "SELECT b.title, b.isbn, p.name AS publisher, b.issue_year, b.qty, a.name AS author, " +
                       "bi.book_condition, bi.status " +
                       "FROM books b " +
                       "JOIN publisher p ON b.publisher_id = p.publisher_id " +
                       "JOIN author a ON b.author_id = a.author_id " +
                       "LEFT JOIN bookinstance bi ON b.book_id = bi.book_id " +
                       "WHERE b.isbn = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, isbn);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Book(
                        rs.getString("publisher"),
                        rs.getString("book_condition"),
                        rs.getString("status"),
                        null, null,
                        rs.getString("title"),
                        rs.getString("isbn"),
                        rs.getString("issue_year"),
                        rs.getInt("qty"),
                        rs.getString("author")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 📌 도서 정보 수정 (Update)
    public boolean updateBook(Book book) {
        String query = "UPDATE books SET title=?, publisher_id=(SELECT publisher_id FROM publisher WHERE name=?), " +
                       "issue_year=?, qty=?, author_id=(SELECT author_id FROM author WHERE name=?) WHERE isbn=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, book.getBooksTitle());
            pstmt.setString(2, book.getPublisherName());
            pstmt.setString(3, book.getBooksIssue());
            pstmt.setInt(4, book.getBooksQty());
            pstmt.setString(5, book.getAuthorName());
            pstmt.setString(6, book.getBooksISBN());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
 // 📌 도서 재고 업데이트 (대출 시 감소, 반납 시 증가)
    public boolean updateStock(int bookId, int change) {
        String query = "UPDATE books SET qty = qty + ? WHERE book_id = ? AND qty + ? >= 0";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, change);
            pstmt.setInt(2, bookId);
            pstmt.setInt(3, change);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    // 📌 도서 삭제 (Delete)
    public boolean deleteBook(String isbn) {
        String query = "DELETE FROM books WHERE isbn=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, isbn);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 📌 도서 대출 가능 여부 확인
    public boolean isBookAvailable(int bookId) {
        String query = "SELECT qty FROM books WHERE book_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, bookId);
            ResultSet rs = pstmt.executeQuery();
            return rs.next() && rs.getInt("qty") > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
