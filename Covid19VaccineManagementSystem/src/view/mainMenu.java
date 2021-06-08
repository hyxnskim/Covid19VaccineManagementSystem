/**
 * 
 */
package view;

import java.text.ParseException;
import java.util.Scanner;

import model.service.MemberService;
import util.UI;

/**
 * <pre>
 * 메인 화면 구성
 * </pre>
 * @author Hyunsoo Kim
 * @version ver.1.0
 * @since jdk1.8
 */
public class mainMenu {

	public static void main(String[] args) throws ParseException {
		mainMenu menu = new mainMenu();
		menu.showMain();
	}
	
	public void showMain() throws ParseException {
		UI ui = new UI();
		subMenu sm = new subMenu();
		Scanner sc = new Scanner(System.in);
		int num;
		
		System.out.println("=======================================");
		System.out.println("\t코로나19 백신 접종 도우미 시스템\t");
		System.out.println("=======================================");
		
		while(true) {
			ui.printSubMenu("메인 메뉴");
			System.out.println("1. 백신 접종 현황 조회");
			System.out.println("2. 우선접종 대상자 여부 확인");
			System.out.println("3. 예방접종센터 조회");
			System.out.println("4. 2차 접종 대기기간 조회");
			System.out.println("5. 백신 알림 서비스 등록");
			System.out.println("6. 내 정보 관리");
			System.out.println("7. 관리자 메뉴");
			System.out.println("0. 프로그램 종료");
			System.out.println();
		
			System.out.print("사용하실 메뉴 번호를 입력하세요 : ");
			num = sc.nextInt();
			switch(num) {
			case 1:
				sm.printCurrentVac();
				break;
			case 2:
				sm.verifyPrior();
				break;
			case 3:
				sm.printCenter();
				break;
			case 4:
				sm.viewPeriod();
				break;
			case 5:
				sm.joinService();
				break;
			case 6:
				sm.myInfoManagement();
				break;
			case 7:
				sm.adminMenu();
				break;
			case 0:
				System.out.println("프로그램을 종료합니다.");
				System.exit(0);
				break;
			 default:
				 System.out.println("0~7 사이의 숫자를 입력하세요");
			}
		}
	}

	
	
}
