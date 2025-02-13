package controller;
import service.BookService;
import model.Book;
import java.util.Scanner;
import java.util.List;

public class BookController {
    private BookService bookService = new BookService();
    private Scanner scanner = new Scanner(System.in);

    // 도서 관리 메뉴
    public void manageBooks() {
        while (true) {
            System.out.println("\n=== 도서 관리 ===");
            System.out.println("1. 도서 등록");
            System.out.println("2. 도서 목록 조회");
            System.out.println("3. 도서 검색");
            System.out.println("4. 도서 정보 수정");
            System.out.println("5. 도서 삭제");
            System.out.println("6. 뒤로가기");
            System.out.print("메뉴 선택: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    bookService.addBook();
                    break;
                case 2:
                    List<Book> books = bookService.getAllBooks();
                    books.forEach(System.out::println);
                    break;
                case 3:
                    bookService.searchBooks();
                    break;
                case 4:
                    bookService.updateBook();
                    break;
                case 5:
                    bookService.deleteBook();
                    break;
                case 6:
                    return;
                default:
                    System.out.println("올바른 번호를 입력하세요.");
            }
        }
    }
}
