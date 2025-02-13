package service;
import dao.BookDAO;
import model.Book;
import java.util.List;
import java.util.Scanner;

public class BookService {
    private BookDAO bookDAO = new BookDAO();
    private Scanner scanner = new Scanner(System.in);

    // 도서 등록
    public void addBook() {
        System.out.print("도서 제목: ");
        String title = scanner.nextLine();
        System.out.print("ISBN: ");
        String isbn = scanner.next();
        System.out.print("저자: ");
        String author = scanner.next();
        System.out.print("출판사: ");
        String publisher = scanner.next();
        System.out.print("출판 연도: ");
        int issueYear = scanner.nextInt();
        System.out.print("가격: ");
        double price = scanner.nextDouble();
        System.out.print("보유 권수: ");
        int stock = scanner.nextInt();

        Book book = new Book(0, title, isbn, author, publisher, issueYear, price, stock);
        if (bookDAO.addBook(book)) {
            System.out.println("도서가 성공적으로 등록되었습니다.");
        } else {
            System.out.println("도서 등록에 실패하였습니다.");
        }
    }

    // 도서 목록 조회
    public List<Book> getAllBooks() {
        return bookDAO.getAllBooks();
    }

    // 도서 검색 (제목, 저자)
    public void searchBooks() {
        System.out.print("검색할 키워드(제목 또는 저자): ");
        String keyword = scanner.next();
        List<Book> books = bookDAO.searchBooks(keyword);
        if (books.isEmpty()) {
            System.out.println("검색 결과가 없습니다.");
        } else {
            books.forEach(System.out::println);
        }
    }

    // 도서 정보 수정
    public void updateBook() {
        System.out.print("수정할 도서의 ISBN 입력: ");
        String isbn = scanner.next();

        Book existingBook = bookDAO.getBookByISBN(isbn);
        if (existingBook == null) {
            System.out.println("해당 ISBN의 도서가 존재하지 않습니다.");
            return;
        }

        System.out.print("새 제목: ");
        String title = scanner.next();
        System.out.print("새 저자: ");
        String author = scanner.next();
        System.out.print("새 출판사: ");
        String publisher = scanner.next();
        System.out.print("새 출판 연도: ");
        int issueYear = scanner.nextInt();
        System.out.print("새 가격: ");
        double price = scanner.nextDouble();
        System.out.print("새 보유 권수: ");
        int stock = scanner.nextInt();

        Book updatedBook = new Book(existingBook.getId(), title, isbn, author, publisher, issueYear, price, stock);
        if (bookDAO.updateBook(updatedBook)) {
            System.out.println("도서 정보가 성공적으로 수정되었습니다.");
        } else {
            System.out.println("도서 정보 수정에 실패하였습니다.");
        }
    }

    // 도서 삭제
    public void deleteBook() {
        System.out.print("삭제할 도서의 ISBN 입력: ");
        String isbn = scanner.next();

        if (bookDAO.deleteBook(isbn)) {
            System.out.println("도서가 삭제되었습니다.");
        } else {
            System.out.println("도서 삭제에 실패하였습니다.");
        }
    }
}

