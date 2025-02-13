package controller;
import service.MemberService;
import model.Member;
import java.util.Scanner;

public class MemberController {
    private MemberService memberService = new MemberService();
    private Scanner scanner = new Scanner(System.in);
    private Member loggedInMember = null; // 로그인한 사용자 정보

    // 회원 가입
    public void register() {
        memberService.register();
    }

    // 로그인
    public void login() {
        loggedInMember = memberService.login();
        if (loggedInMember != null) {
            handleUserMenu();
        }
    }

    // 사용자 메뉴 (회원 정보 수정 / 탈퇴)
    private void handleUserMenu() {
        while (true) {
            System.out.println("\n=== 사용자 메뉴 ===");
            System.out.println("1. 회원 정보 수정");
            System.out.println("2. 회원 탈퇴");
            System.out.println("3. 로그아웃");
            System.out.print("메뉴 선택: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    memberService.updateMember(loggedInMember);
                    break;
                case 2:
                    memberService.deleteMember(loggedInMember);
                    loggedInMember = null;
                    return;
                case 3:
                    System.out.println("로그아웃합니다.");
                    loggedInMember = null;
                    return;
                default:
                    System.out.println("올바른 번호를 입력하세요.");
            }
        }
    }
}
