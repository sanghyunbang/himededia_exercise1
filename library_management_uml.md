# 📌 UML 다이어그램

## **1️⃣ 회원 관리 시스템**
```
+------------------+
|      Main       |
+------------------+
        |
        v
+------------------------+
|   MemberController    |
|------------------------|
| + register()          |
| + login()             |
| + updateMember()      |
| + deleteMember()      |
+------------------------+
        |
        v
+------------------------+
|   MemberService       |
|------------------------|
| + register()          |
| + login()             |
| + updateMember()      |
| + deleteMember()      |
+------------------------+
        |
        v
+------------------------+
|   MemberDAO           |
|------------------------|
| + registerMember()    |
| + getMemberById()     |
| + updateMember()      |
| + deleteMember()      |
+------------------------+
        |
        v
+------------------------+
|   DBConnection        |
|------------------------|
| + getConnection()     |
+------------------------+
```

## **2️⃣ 도서 관리 시스템**
```
+----------------------+
|  BookController     |
|----------------------|
| + addBook()         |
| + getAllBooks()     |
| + searchBooks()     |
| + updateBook()      |
| + deleteBook()      |
+----------------------+
        |
        v
+----------------------+
|  BookService        |
|----------------------|
| + addBook()         |
| + getAllBooks()     |
| + searchBooks()     |
| + updateBook()      |
| + deleteBook()      |
+----------------------+
        |
        v
+----------------------+
|  BookDAO            |
|----------------------|
| + addBook()         |
| + getAllBooks()     |
| + searchBooks()     |
| + updateBook()      |
| + deleteBook()      |
| + isBookAvailable() |
+----------------------+
        |
        v
+----------------------+
|  DBConnection       |
|----------------------|
| + getConnection()   |
+----------------------+
```

## **3️⃣ 대출 관리 시스템**
```
+----------------------+
|  RentController     |
|----------------------|
| + rentBook()        |
| + viewRentHistory() |
| + returnBook()      |
| + extendRent()      |
+----------------------+
        |
        v
+----------------------+
|  RentService        |
|----------------------|
| + rentBook()        |
| + viewRentHistory() |
| + returnBook()      |
| + extendRent()      |
+----------------------+
        |
        v
+----------------------+
|  RentDAO            |
|----------------------|
| + rentBook()        |
| + getRentHistory()  |
| + returnBook()      |
| + extendRent()      |
| + isRentExtended()  |
| + isOverdue()       |
+----------------------+
        |
        v
+----------------------+
|  DBConnection       |
|----------------------|
| + getConnection()   |
+----------------------+
```

## **4️⃣ 데이터 모델 (DTO)**
```
+------------------+
|   Member        |
|------------------|
| - id            |
| - username      |
| - password      |
| - name          |
| - role          |
+------------------+

+------------------+
|   Book          |
|------------------|
| - id            |
| - title         |
| - isbn          |
| - author        |
| - publisher     |
| - issueYear     |
| - price         |
| - stock         |
+------------------+

+------------------+
|   Rent          |
|------------------|
| - id            |
| - bookId        |
| - memberId      |
| - rentDate      |
| - expectedReturn|
| - returnedDate  |
| - extendedDate  |
| - status        |
+------------------+
```

