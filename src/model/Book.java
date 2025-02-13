package model;

public class Book {
    private int id;
    private String title;
    private String isbn;
    private String author;
    private String publisher;
    private int issueYear;
    private double price;
    private int stock;

    // 생성자
    public Book(int id, String title, String isbn, String author, String publisher, int issueYear, double price, int stock) {
        this.id = id;
        this.title = title;
        this.isbn = isbn;
        this.author = author;
        this.publisher = publisher;
        this.issueYear = issueYear;
        this.price = price;
        this.stock = stock;
    }

    // Getter & Setter 메서드
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getIsbn() { return isbn; }
    public String getAuthor() { return author; }
    public String getPublisher() { return publisher; }
    public int getIssueYear() { return issueYear; }
    public double getPrice() { return price; }
    public int getStock() { return stock; }

    public void setId(int id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public void setAuthor(String author) { this.author = author; }
    public void setPublisher(String publisher) { this.publisher = publisher; }
    public void setIssueYear(int issueYear) { this.issueYear = issueYear; }
    public void setPrice(double price) { this.price = price; }
    public void setStock(int stock) { this.stock = stock; }
}

