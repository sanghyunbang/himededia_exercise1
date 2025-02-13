
package service;
import dao.MemberDAO;
import model.Member;
import java.util.Scanner;

public class MemberService {
    private MemberDAO memberDAO = new MemberDAO();
    private Scanner scanner = new Scanner(System.in);

    
    // 회원 가입
    public void register() {
        System.out.print("사용할 사용자명을 입력하세요: ");
        String username = scanner.next();
        System.out.print("비밀번호를 입력하세요: ");
        String password = scanner.next(); // 실제 구현에서는 비밀번호 해싱 필요
        System.out.print("이름을 입력하세요: ");
        String name = scanner.next();
        System.out.print("역할을 입력하세요 (USER/ADMIN): ");
        String role = scanner.next().toUpperCase();
        System.out.print("성별을 입력하세요 (M/F): ");
        String gender = scanner.next();
        System.out.print("휴대폰 번호를 입력하세요: ");
        String mobile = scanner.next();
        System.out.print("이메일을 입력하세요: ");
        String email = scanner.next();

        Member member = new Member(0, username, password, name, role, gender, mobile, email);
        if (memberDAO.registerMember(member)) {
            System.out.println("회원 가입이 완료되었습니다.");
        } else {
            System.out.println("회원 가입에 실패하였습니다.");
        }
    }

    
    // 로그인
    public Member login() {
        System.out.print("사용자명을 입력하세요: ");
        String username = scanner.next();
        System.out.print("비밀번호를 입력하세요: ");
        String password = scanner.next();

        Member member = memberDAO.login(username, password);
        if (member != null) {
            System.out.println("로그인 성공! " + member.getName() + "님 환영합니다.");
        } else {
            System.out.println("로그인 실패! 사용자명 또는 비밀번호를 확인하세요.");
        }
        return member;
    }
    
    
    public void updateMember(Member member) {
        System.out.println("\n회원 정보 수정");
        System.out.print("새 이름: ");
        String name = scanner.next();
        System.out.print("새 성별(M/F): ");
        String gender = scanner.next();
        System.out.print("새 휴대폰 번호: ");
        String mobile = scanner.next();
        System.out.print("새 이메일: ");
        String email = scanner.next();

        member.setName(name);
        member.setGender(gender);
        member.setMobile(mobile);
        member.setEmail(email);

        if (memberDAO.updateMember(member)) {
            System.out.println("회원 정보가 성공적으로 수정되었습니다.");
        } else {
            System.out.println("회원 정보 수정에 실패하였습니다.");
        }
    }
    
    
    public void deleteMember(Member member) {
        System.out.print("정말 탈퇴하시겠습니까? (Y/N): ");
        String confirm = scanner.next();
        if (confirm.equalsIgnoreCase("Y")) {
            if (memberDAO.deleteMember(member.getId())) {
                System.out.println("회원 탈퇴가 완료되었습니다.");
                System.exit(0); // 프로그램 종료 (또는 로그아웃 처리)
            } else {
                System.out.println("회원 탈퇴에 실패하였습니다.");
            }
        } else {
            System.out.println("회원 탈퇴가 취소되었습니다.");
        }
    }


    
}
