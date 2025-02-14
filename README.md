# 📚 도서 대여 관리 시스템 (Library Management System)

## 📌 프로젝트 개요
이 프로젝트는 Java와 MySQL을 기반으로 한 **도서 대여 관리 시스템**입니다.
사용자는 회원으로 가입하여 도서를 대출하고 반납할 수 있으며,
관리자는 도서를 관리하고 회원 정보를 확인할 수 있습니다.

---

## 🛠️ 기술 스택
- **Back-end:** Java (JDK 17+)
- **Database:** MySQL
- **Build Tool:** Maven / Gradle
- **Version Control:** GitHub
- **Design Pattern:** MVC (Model-View-Controller)

---

## 📂 프로젝트 구조 (패키지)
```
📂 src
 ├── 📂 controller  (사용자 요청 처리)
 │    ├── BookController.java    
 │    ├── MemberController.java  
 │    ├── RentController.java    
 ├── 📂 dao  (데이터베이스 접근)
 │    ├── BookDAO.java    
 │    ├── MemberDAO.java  
 │    ├── RentDAO.java    
 ├── 📂 model  (DTO 데이터 객체)
 │    ├── Book.java    
 │    ├── Member.java  
 │    ├── Rent.java    
 ├── 📂 service  (비즈니스 로직 담당)
 │    ├── BookService.java    
 │    ├── MemberService.java  
 │    ├── RentService.java    
 ├── 📂 util  (유틸리티)
 │    ├── DBConnection.java  
 ├── Main.java (프로그램 실행)
```

---

## 🚀 기능 구현
### **1. 회원 관리**
- 회원 가입, 로그인
- 회원 정보 수정, 탈퇴

### **2. 도서 관리**
- 도서 등록, 조회, 검색
- 도서 수정, 삭제

### **3. 대출 관리**
- 도서 대출 (최대 5권 제한)
- 대출 내역 조회
- 도서 반납, 연장 (연체 시 연장 불가)

---

## 🔧 실행 방법
### **1. 데이터베이스 설정(예시)**
1. MySQL에서 `library_db` 데이터베이스를 생성:
```sql
CREATE DATABASE library_db;
```
2. `MEMBER`, `BOOK`, `RENT` 테이블 생성:
```sql
-- 출판사 테이블
CREATE TABLE Publisher (
    publisher_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

-- 저자 테이블
-- 📌 출판사 테이블
CREATE TABLE Publisher (
    publisher_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

-- 📌 저자 테이블
CREATE TABLE Author (
    author_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(128) NOT NULL UNIQUE
);

-- 📌 책 정보 테이블 (ISBN 단위)
CREATE TABLE Books (
    book_id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    isbn VARCHAR(20) NOT NULL UNIQUE,
    publisher_id INT,
    issue_year INT,
    qty INT UNSIGNED DEFAULT 1,
    author_id INT,
    FOREIGN KEY (publisher_id) REFERENCES Publisher(publisher_id) ON DELETE SET NULL,
    FOREIGN KEY (author_id) REFERENCES Author(author_id) ON DELETE SET NULL
);

-- 📌 개별 책 인스턴스 테이블 (실제 물리적 책 관리)
CREATE TABLE BookInstance (
    instance_id INT AUTO_INCREMENT PRIMARY KEY,
    book_id INT NOT NULL,
    book_condition ENUM('New', 'Good', 'Fair', 'Poor') DEFAULT 'Good',
    status ENUM('Available', 'Borrowed', 'Lost') DEFAULT 'Available',
    created DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_renewed TINYINT(1) DEFAULT 0,  -- 연장 여부 추가
    FOREIGN KEY (book_id) REFERENCES Books(book_id) ON DELETE CASCADE
);

-- 📌 회원 정보 테이블
CREATE TABLE Member (
    username VARCHAR(50) PRIMARY KEY,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(100) NOT NULL,
    role ENUM('USER', 'ADMIN') NOT NULL DEFAULT 'USER',
    gender ENUM('M', 'F') DEFAULT NULL,
    mobile VARCHAR(20) UNIQUE DEFAULT NULL,
    email VARCHAR(100) UNIQUE DEFAULT NULL,
    registered TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    current_rentals INT DEFAULT 0,
    has_overdue TINYINT(1) DEFAULT 0
);

-- 📌 도서 대출 테이블 (현재 대출 중인 책)
CREATE TABLE Rent (
    rent_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    instance_id INT NOT NULL,
    rent_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    due_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    return_date TIMESTAMP DEFAULT NULL,
    late_fee INT DEFAULT 0,
    renewal_count INT DEFAULT 3,  -- 연장 가능 횟수 추가
    FOREIGN KEY (username) REFERENCES Member(username) ON DELETE CASCADE,
    FOREIGN KEY (instance_id) REFERENCES BookInstance(instance_id) ON DELETE CASCADE
);

-- 📌 대출 이력 테이블 (반납된 책 기록)
CREATE TABLE RentHistory (
    history_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    instance_id INT NOT NULL,
    rent_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    due_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    return_date TIMESTAMP DEFAULT NULL,
    late_fee INT DEFAULT 0,
    renewal_count INT DEFAULT 3,
    FOREIGN KEY (username) REFERENCES Member(username) ON DELETE CASCADE,
    FOREIGN KEY (instance_id) REFERENCES BookInstance(instance_id) ON DELETE CASCADE
);

```

### **2. 프로젝트 실행**
1. `src` 폴더에서 `Main.java` 실행
2. CLI(콘솔)에서 프로그램 실행 후 회원 가입 및 로그인 진행

---

## 📌 UML 다이어그램
자세한 UML 다이어그램은 아래 파일에서 확인할 수 있습니다:

📄 [UML 다이어그램 보기](./library_management_uml.md)

---

## 📈 개발 진행 단계
- [x] 회원 관리 (CRUD)
- [x] 도서 관리 (CRUD)
- [x] 대출 관리 (대출/반납/연장)
- [ ] GUI 개발 (추후 추가 가능)

---

## 📩 문의 및 기여
- **GitHub Issues**를 통해 버그 리포트 및 피드백을 남겨주세요.
- Pull Request 환영합니다! 🎉

