import controller.MemberController;
import controller.BookController;
import controller.RentController;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        MemberController memberController = new MemberController();
        BookController bookController = new BookController();
        RentController rentController = new RentController();

        while (true) {
            System.out.println("\n=== 도서 대여 관리 시스템 ===");
            System.out.println("1. 회원 관리");
            System.out.println("2. 도서 관리");
            System.out.println("3. 대출 관리");
            System.out.println("4. 종료");
            System.out.print("메뉴 선택: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    memberController.login();
                    break;
                case 2:
                    bookController.manageBooks();
                    break;
                case 3:
                    rentController.manageRentals();
                    break;
                case 4:
                    System.out.println("프로그램을 종료합니다.");
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("올바른 번호를 입력하세요.");
            }
        }
    }
}
