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
### **1. 데이터베이스 설정**
1. MySQL에서 `library_db` 데이터베이스를 생성:
```sql
CREATE DATABASE library_db;
```
2. `MEMBER`, `BOOK`, `RENT` 테이블 생성:
```sql
CREATE TABLE MEMBER (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    USERNAME VARCHAR(50) NOT NULL UNIQUE,
    PASSWORD VARCHAR(255) NOT NULL,
    NAME VARCHAR(100) NOT NULL,
    ROLE ENUM('USER', 'ADMIN') NOT NULL DEFAULT 'USER',
    REGISTERED TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE BOOK (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    TITLE VARCHAR(200) NOT NULL,
    ISBN VARCHAR(20) NOT NULL UNIQUE,
    AUTHOR VARCHAR(100) NOT NULL,
    PUBLISHER VARCHAR(100),
    ISSUE_YEAR INT,
    PRICE DOUBLE,
    STOCK INT NOT NULL
);

CREATE TABLE RENT (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    BOOK_ID INT NOT NULL,
    MEMBER_ID INT NOT NULL,
    RENT_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    EXPECTED_RETURN TIMESTAMP DEFAULT (CURRENT_TIMESTAMP + INTERVAL 14 DAY),
    RETURNED_DATE TIMESTAMP NULL,
    EXTENDED_DATE TIMESTAMP NULL,
    STATUS ENUM('대출중', '반납됨', '연체') DEFAULT '대출중',
    FOREIGN KEY (BOOK_ID) REFERENCES BOOK(ID),
    FOREIGN KEY (MEMBER_ID) REFERENCES MEMBER(ID)
);
```

### **2. 프로젝트 실행**
1. `src` 폴더에서 `Main.java` 실행
2. CLI(콘솔)에서 프로그램 실행 후 회원 가입 및 로그인 진행

---

## 📌 UML 다이어그램
(UML 다이어그램 추가 예정)

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

