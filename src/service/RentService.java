package service;
import java.util.List;

import dao.BookDAO;
import dao.RentDAO;
import model.Rent;

public class RentService {
    private RentDAO rentDAO = new RentDAO();
    private BookDAO bookDAO = new BookDAO();  // 재고 업데이트를 위해 필요

    // 1. 도서 대출
    public boolean rentBook(int memberId, int bookId) {
        // 회원의 현재 대출 권수 확인 (최대 5권 제한)
        int rentedCount = rentDAO.getActiveRentCount(memberId);
        if (rentedCount >= 5) {
            System.out.println("대출 가능 권수를 초과하였습니다. (최대 5권)");
            return false;
        }

        // 책 재고 확인
        if (!bookDAO.isBookAvailable(bookId)) {
            System.out.println("해당 도서가 재고 부족으로 대출이 불가능합니다.");
            return false;
        }

        // 대출 처리
        if (rentDAO.rentBook(memberId, bookId)) {
            // 대출 성공 시, 도서 재고 감소
            bookDAO.updateStock(bookId, -1);
            return true;
        }
        return false;
    }

    // 2. 대출 내역 조회
    public void viewRentHistory(int memberId) {
        List<Rent> rentList = rentDAO.getRentHistory(memberId);
        if (rentList.isEmpty()) {
            System.out.println("대출 내역이 없습니다.");
        } else {
            rentList.forEach(System.out::println);
        }
    }

    // 3. 도서 반납
    public boolean returnBook(int rentId) {
        // 반납 처리
        if (rentDAO.returnBook(rentId)) {
            // 반납 성공 시, 도서 재고 증가
            int bookId = rentDAO.getBookIdByRentId(rentId);
            bookDAO.updateStock(bookId, 1);
            return true;
        }
        return false;
    }

    // 4. 대출 연장
    public boolean extendRent(int rentId) {
        // 이미 연장한 경우, 연장 불가
        if (rentDAO.isRentExtended(rentId)) {
            System.out.println("이미 연장된 대출입니다.");
            return false;
        }

        // 연체 상태인지 확인
        if (rentDAO.isOverdue(rentId)) {
            System.out.println("연체된 도서는 연장이 불가능합니다.");
            return false;
        }

        // 연장 처리
        return rentDAO.extendRent(rentId);
    }
}
