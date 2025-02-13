package model;
import java.sql.Timestamp;

public class Rent {
    private int id;
    private int bookId;
    private int memberId;
    private Timestamp rentDate;
    private Timestamp expectedReturn;
    private Timestamp returnedDate;
    private Timestamp extendedDate;
    private String status;

    // 생성자
    public Rent(int id, int bookId, int memberId, Timestamp rentDate, Timestamp expectedReturn, Timestamp returnedDate, Timestamp extendedDate, String status) {
        this.id = id;
        this.bookId = bookId;
        this.memberId = memberId;
        this.rentDate = rentDate;
        this.expectedReturn = expectedReturn;
        this.returnedDate = returnedDate;
        this.extendedDate = extendedDate;
        this.status = status;
    }

    // Getter & Setter
    public int getId() { return id; }
    public int getBookId() { return bookId; }
    public int getMemberId() { return memberId; }
    public Timestamp getRentDate() { return rentDate; }
    public Timestamp getExpectedReturn() { return expectedReturn; }
    public Timestamp getReturnedDate() { return returnedDate; }
    public Timestamp getExtendedDate() { return extendedDate; }
    public String getStatus() { return status; }

    public void setId(int id) { this.id = id; }
    public void setBookId(int bookId) { this.bookId = bookId; }
    public void setMemberId(int memberId) { this.memberId = memberId; }
    public void setRentDate(Timestamp rentDate) { this.rentDate = rentDate; }
    public void setExpectedReturn(Timestamp expectedReturn) { this.expectedReturn = expectedReturn; }
    public void setReturnedDate(Timestamp returnedDate) { this.returnedDate = returnedDate; }
    public void setExtendedDate(Timestamp extendedDate) { this.extendedDate = extendedDate; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return "대출 ID: " + id +
                ", 도서 ID: " + bookId +
                ", 회원 ID: " + memberId +
                ", 대출일: " + rentDate +
                ", 반납 예정일: " + expectedReturn +
                ", 반납일: " + (returnedDate != null ? returnedDate : "미반납") +
                ", 연장일: " + (extendedDate != null ? extendedDate : "미연장") +
                ", 상태: " + status;
    }
}
