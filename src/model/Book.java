package model;

public class Book {
	
	// publisher 테이블 관련
    private String publisherName; 
    
    // bookinstance 테이블 관련
    private String instanceCondition;
    private String instanceStatus;
    private String instanceCreated;
    private String instanceUpdated;
    
    // books 테이블 관련
    private String booksTitle;
    private String booksISBN;
    private String booksIssue;
    private int booksQty;
    
    // author 테이블 관련
    private String authorName;


 // 생성자
    public Book() {}
    
    public Book(    		
    		String publisherName,
    		
            String instanceCondition, 
            String instanceStatus, 
            String instanceCreated, 
            String instanceUpdated,
    		
            String booksTitle, 
            String booksISBN,
            String booksIssue, 
            int booksQty, 
            
            String authorName
    		)
    
    {
        this.publisherName = publisherName;
        
        this.instanceCondition = instanceCondition;
        this.instanceStatus = instanceStatus;
        this.instanceCreated = instanceCreated;
        this.instanceUpdated = instanceUpdated;
        
        this.booksTitle = booksTitle;
        this.booksISBN = booksISBN;
        this.booksIssue = booksIssue;
        this.booksQty = booksQty;
        
        this.authorName = authorName;
        

    }


 // Getter & Setter 메서드
    
    public String getPublisherName() { return publisherName; }

    public String getBooksTitle() { return booksTitle; }
    public String getBooksISBN() { return booksISBN; }
    public String getAuthorName() { return authorName; }
    public String getBooksIssue() { return booksIssue; }
    public int getBooksQty() { return booksQty; }

    public String getInstanceCondition() { return instanceCondition; }
    public String getInstanceStatus() { return instanceStatus; }
    public String getInstanceCreated() { return instanceCreated; }
    public String getInstanceUpdated() { return instanceUpdated; }

    public void setAuthorName(String authorName) { this.authorName = authorName; }
    public void setBooksTitle(String booksTitle) { this.booksTitle = booksTitle; }
    public void setBooksISBN(String booksISBN) { this.booksISBN = booksISBN; }
    public void setPublisherName(String publisherName) { this.publisherName = publisherName; }
    public void setBooksIssue(String booksIssue) { this.booksIssue = booksIssue; }
    public void setBooksQty(int booksQty) { this.booksQty = booksQty; }

    public void setInstanceCondition(String instanceCondition) { this.instanceCondition = instanceCondition; }
    public void setInstanceStatus(String instanceStatus) { this.instanceStatus = instanceStatus; }
    public void setInstanceCreated(String instanceCreated) { this.instanceCreated = instanceCreated; }
    public void setInstanceUpdated(String instanceUpdated) { this.instanceUpdated = instanceUpdated; }

    // 📌 `toString()` 오버라이딩
    @Override
    public String toString() {
        return "📖 도서 정보: " +
               "\n- 제목: " + booksTitle +
               "\n- ISBN: " + booksISBN +
               "\n- 저자: " + authorName +
               "\n- 출판사: " + publisherName +
               "\n- 출판 연도: " + booksIssue +
               "\n- 재고 수량: " + booksQty +
               "\n- 상태: " + instanceCondition + " / " + instanceStatus +
               "\n- 생성일: " + instanceCreated +
               "\n- 업데이트일: " + instanceUpdated;
    }
}    
    


