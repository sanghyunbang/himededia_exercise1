package model;
import java.sql.Timestamp;

public class Rent {
	
    private int rentID;
    private String username;
    private int bookInstance;
    private Timestamp rentDate;
    private Timestamp dueDate;
    private Timestamp returnedDate;
    
    private int renewalCount;
    private int lateFee;


    // 생성자
    public Rent() {}
    
    public Rent (int rentID,
    		     String username, 
    		     int bookInstance, 
    		     Timestamp rentDate, 
    		     Timestamp dueDate, 
    		     Timestamp returnedDate, 
    		     int renewalCount,
    		     int lateFee) {
    	
        this.rentID = rentID;
        this.bookInstance = bookInstance;
        this.username = username;
        this.rentDate = rentDate;
        this.dueDate = dueDate;
        this.returnedDate = returnedDate;
        this.renewalCount=renewalCount;
	    this.lateFee = lateFee;
    }

    // Getter & Setter
    public int getId() { return rentID; }
    public int getBookId() { return bookInstance; }
    public String getMemberId() { return username; }
    public Timestamp getRentDate() { return rentDate; }
    public Timestamp getdueDate() { return dueDate; }
    public Timestamp getReturnedDate() { return returnedDate; }
    public int getRenwalCount() { return renewalCount; }
    public int getLateFee() { return lateFee; }

    
    
    public void setId(int rentID) { this.rentID = rentID; }
    public void setBookId(int bookInstance) { this.bookInstance = bookInstance; }
    public void setMemberId(String username) { this.username = username; }
    public void setRentDate(Timestamp rentDate) { this.rentDate = rentDate; }
    public void setdueDate(Timestamp dueDate) { this.dueDate = dueDate; }
    public void setReturnedDate(Timestamp returnedDate) { this.returnedDate = returnedDate; }

    public void setRenwalCount(int renewalCount) { this.renewalCount = renewalCount; }
    public void setLateFee(int lateFee) { this.lateFee = lateFee; }

    
    
    @Override
    public String toString() {
        return "대출 rentID: " + rentID +
                ", 도서 ID: " + bookInstance +
                ", 회원 ID: " + username +
                ", 대출일: " + rentDate +
                ", 반납 예정일: " + dueDate +
                ", 반납일: " + (returnedDate != null ? returnedDate : "미반납") +
                ", 연장가능횟수: " + renewalCount +
                 ", 연체료: " + lateFee;
    }
}
