package controller;
import service.RentService;
import java.util.Scanner;

public class RentController {
    private RentService rentService = new RentService();
    private Scanner scanner = new Scanner(System.in);

    // 대출 관리 메뉴
    public void manageRentals() {
        while (true) {
            System.out.println("\n=== 대출 관리 ===");
            System.out.println("1. 도서 대출");
            System.out.println("2. 대출 내역 조회");
            System.out.println("3. 도서 반납");
            System.out.println("4. 대출 연장");
            System.out.println("5. 뒤로가기");
            System.out.print("메뉴 선택: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    rentBook();
                    break;
                case 2:
                    viewRentHistory();
                    break;
                case 3:
                    returnBook();
                    break;
                case 4:
                    extendRent();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("올바른 번호를 입력하세요.");
            }
        }
    }

    // 1. 도서 대출 요청
    private void rentBook() {
        System.out.print("회원 ID를 입력하세요: ");
        int memberId = scanner.nextInt();
        System.out.print("대출할 도서 ID를 입력하세요: ");
        int bookId = scanner.nextInt();

        if (rentService.rentBook(memberId, bookId)) {
            System.out.println("도서가 성공적으로 대출되었습니다.");
        } else {
            System.out.println("대출에 실패하였습니다. (최대 5권까지 대출 가능 또는 재고 부족)");
        }
    }

    // 2. 대출 내역 조회
    private void viewRentHistory() {
        System.out.print("회원 ID를 입력하세요: ");
        int memberId = scanner.nextInt();

        rentService.viewRentHistory(memberId);
    }

    // 3. 도서 반납
    private void returnBook() {
        System.out.print("반납할 대출 ID를 입력하세요: ");
        int rentId = scanner.nextInt();

        if (rentService.returnBook(rentId)) {
            System.out.println("도서가 성공적으로 반납되었습니다.");
        } else {
            System.out.println("반납에 실패하였습니다. (이미 반납된 도서일 수 있음)");
        }
    }

    // 4. 대출 연장
    private void extendRent() {
        System.out.print("연장할 대출 ID를 입력하세요: ");
        int rentId = scanner.nextInt();

        if (rentService.extendRent(rentId)) {
            System.out.println("대출이 연장되었습니다.");
        } else {
            System.out.println("연장에 실패하였습니다. (이미 연장된 도서일 수 있음)");
        }
    }
}

