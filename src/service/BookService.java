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
        String booksTitle = scanner.nextLine();

        System.out.print("ISBN: ");
        String booksISBN = scanner.next();

        System.out.print("출판 연도: ");
        String booksIssue = scanner.next();  // 출판 연도를 문자열(String)로 변경

        System.out.print("보유 권수: ");
        int booksQty = scanner.nextInt();

        scanner.nextLine(); // 🚀 버퍼 클리어 (scanner.nextInt() 후 개행 문자 제거)

        System.out.print("저자 이름: ");
        String authorName = scanner.nextLine();

        System.out.print("출판사: ");
        String publisherName = scanner.nextLine();

        System.out.print("도서 상태 (New, Good, Fair, Poor): ");
        String instanceCondition = scanner.nextLine();

        System.out.print("대출 상태 (Available, Borrowed, Lost): ");
        String instanceStatus = scanner.nextLine();

        // `instanceCreated`, `instanceUpdated`는 DB에서 자동 설정 (CURRENT_TIMESTAMP)

        Book book = new Book(
            publisherName,   // 출판사 정보

            instanceCondition,   // 책 상태
            instanceStatus,      // 대출 상태
            null,  // `instanceCreated` (DB에서 자동 생성)
            null,  // `instanceUpdated` (DB에서 자동 업데이트)

            booksTitle,  // 도서 제목
            booksISBN,   // ISBN
            booksIssue,  // 출판 연도 (String 타입)
            booksQty,    // 보유 권수

            authorName   // 저자 이름
        );

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
        String booksISBN = scanner.next();

        Book existingBook = bookDAO.getBookByISBN(booksISBN);
        if (existingBook == null) {
            System.out.println("해당 ISBN의 도서가 존재하지 않습니다.");
            return;
        }

        scanner.nextLine(); // 🚀 `nextInt()`, `next()` 이후 개행 문자 제거

        System.out.print("새 제목: ");
        String booksTitle = scanner.nextLine();

        System.out.print("새 저자 이름: ");
        String authorName = scanner.nextLine();

        System.out.print("새 출판사: ");
        String publisherName = scanner.nextLine();

        System.out.print("새 출판 연도: ");
        String booksIssue = scanner.next();  // 기존 `int` → `String`으로 변경

        System.out.print("새 보유 권수: ");
        int booksQty = scanner.nextInt();

        scanner.nextLine(); // 🚀 개행 문자 제거

        System.out.print("새 도서 상태 (New, Good, Fair, Poor): ");
        String instanceCondition = scanner.nextLine();

        System.out.print("새 대출 상태 (Available, Borrowed, Lost): ");
        String instanceStatus = scanner.nextLine();

        // `instanceCreated`, `instanceUpdated`는 DB에서 자동 설정 (CURRENT_TIMESTAMP)

        Book updatedBook = new Book(
            publisherName,    // 출판사 정보

            instanceCondition,    // 책 상태
            instanceStatus,       // 대출 상태
            null,  // `instanceCreated` (DB에서 자동 생성)
            null,  // `instanceUpdated` (DB에서 자동 업데이트)

            booksTitle,  // 도서 제목
            booksISBN,   // ISBN (변경되지 않음)
            booksIssue,  // 출판 연도 (String 타입)
            booksQty,    // 보유 권수

            authorName   // 저자 이름
        );

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

