
package view;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import model.dto.Member;
import model.service.CenterService;
import model.service.MemberService;
import model.service.Service;
import util.UI;
import util.Utility;

/**
 * <pre>
 * 메인 메뉴로부터 호출됨, 각 클래스에서 메서드를 호출
 * </pre>
 * @author Hyunsoo Kim
 * @version ver.2.0
 * @since jdk1.8
 */
public class SubMenu {
	UI ui = new UI();
	Service service = new Service();
	MemberService ms = new MemberService();
	CenterService cs = new CenterService();
	Utility util = new Utility();
	Scanner sc = new Scanner(System.in);
	
	/**
	 * <pre>
	 * 백신 접종 현황 조회 메서드
	 * </pre>
	 * @throws ParseException
	 */
	public void printCurrentVac() throws ParseException {
		int num;

		while(true) {
			ui.printSubMenu("백신 접종 현황 조회");
		
			System.out.println("1. 오늘 현황 조회");
			System.out.println("2. 날짜별 검색");
			System.out.println("0. 돌아가기");
			
			System.out.print("사용하실 메뉴 번호를 입력하세요 : ");
			num = sc.nextInt();
			
			switch(num) {
			case 1:
				ui.printSubSubMenu("오늘 현환 조회");
				service.inocNumToday(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
				break;
				
			case 2:
				String sdate, edate;
				ui.printSubSubMenu("날짜별 검색");
				System.out.println("시작일과 종료일을 입력하세요. (형식 : 20210607)");
				
				while(true) {
					sdate = util.inputDate("시작일");
					if(util.isDateInRange(sdate)) break;
				}
				
				while(true) {
					edate = util.inputDate("종료일");
					if(util.isDateInRange(edate)) break;
				}

				String tmp;
				for(int i = 0; i < Integer.parseInt(edate) - Integer.parseInt(sdate) + 1; i++) {
					tmp = util.addDate(sdate, i);
					SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");
					Date dt = dtFormat.parse(tmp); 
					
					System.out.println();
					service.inocNumToday(new SimpleDateFormat("yyyy-MM-dd").format(dt));
				}
				break;
				
			case 0:
				System.out.println("메인 메뉴로 돌아갑니다.");
				return;
				
			 default:
				 System.out.println("0~2 사이의 숫자를 입력하세요.");
			}
		}
	}
	
	/**
	 * <pre>
	 * 우선접종 대상자 여부 확인 메서드
	 * </pre>
	 * @throws ParseException
	 */
	public void verifyPrior() throws ParseException {
		String yn;
		
		ui.printSubMenu("우선접종 대상자 여부 확인");
		
		if(!service.isPrior()) {
			System.out.println("우선접종 대상자가 아닙니다.\n메인 메뉴로 되돌아갑니다.");
			return;
		}
		
		System.out.println("우선접종 대상자입니다.");
		while(true) {
			System.out.print("근처 예방접종 센터 정보를 조회하시겠습니까? (Y/N) : "); yn = sc.next();
			if(yn.equals("Y")) {
				printCenter();
				return;
			} else if(yn.equals("N")) {
				System.out.println("메인 메뉴로 돌아갑니다.");
				return;
			} else {
				System.out.println("[오류] 입력 형식을 확인해주세요");
			}
		}
	}
	
	/**
	 * <pre>
	 * 예방접종센터 조회 메서드
	 * </pre>
	 * @throws ParseException
	 */
	public void printCenter() {
		int num;
		
		while(true) {
			ui.printSubMenu("예방접종센터 조회");
			
			System.out.println("1. 전체 센터 조회");
			System.out.println("2. 지역별 센터 조회");
			System.out.println("3. 키워드로 조회");
			System.out.println("0. 돌아가기");
			
			System.out.print("사용하실 메뉴 번호를 입력하세요 : ");
			num = sc.nextInt();
			switch(num) {
			case 1:
				ui.printSubSubMenu("전체 센터 조회");
				cs.printAllCenter();
				break;
			case 2:
				ui.printSubSubMenu("지역별 센터 조회");
				cs.printCenterByDistrict();
				break;
			case 3:
				ui.printSubMenu("키워드 조회");
				cs.printCenterByKeywords();
				break;
			case 0:
				System.out.println("메인 메뉴로 돌아갑니다.");
				return;
			default:
				System.out.println("0~3 사이의 숫자를 입력하세요.");
			}
		}
	}
	
	/**
	 * <pre>
	 * 2차 접종 대기기간 조회 메서드
	 * </pre>
	 * @throws ParseException
	 */
	public void viewPeriod() throws ParseException {
		Utility util = new Utility();
		String name, vacType, dateFirst;
		String dateSecond, tmp, yn;
		
		ui.printSubMenu("2차 접종 대기기간 조회");
		
		System.out.print("이름 : "); name = sc.next();
		vacType = ms.inputVacType();
		if(vacType == null) return;
		
		dateFirst = util.inputDate("1차 접종일(형식 : 20210608)");
		dateSecond = util.addDate(dateFirst, ms.findPeriod(vacType));
		
		SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");
		Date dt = dtFormat.parse(dateSecond); 
		tmp = new SimpleDateFormat("yyyy년 MM월 dd일").format(dt);
		
		System.out.println(name + "님의 " + vacType + " 백신 2차 접종일은 " + tmp + " 입니다.");
		System.out.println("2차 접종 알림 서비스에 가입하시면 2차 접종일 3일 전에 알림을 받으실 수 있습니다.");
		while(true) {
			System.out.print("서비스에 가입하시겠습니까?(Y/N) : "); yn = sc.next();
			if(yn.equals("Y")) {
				if(ms.addMember(name, vacType, dateFirst)) {
					System.out.println("등록되었습니다.");
				};
				return;
			} else if(yn.equals("N")) {
				System.out.println("메인 메뉴로 돌아갑니다.");
				return;
			} else {
				System.out.println("[오류] 입력 형식을 확인해주세요");
			}
		}
	}
	
	/**
	 * <pre>
	 * 2차 접종 알림 서비스 등록 메서드
	 * </pre>
	 * @throws ParseException
	 */
	public void joinService() throws ParseException {
		ui.printSubMenu("2차 접종 알림 서비스 등록");
		
		if(ms.addMember()) {
			System.out.println("등록 성공");
		} 
	}
	
	/**
	 * <pre>
	 * 회원 인증 메서드
	 * </pre>
	 * @return 등록된 회원이면 해당 회원 객체, 그렇지 않으면 null;
	 * @throws ParseException
	 */
	public Member isRegistered() {
		String name, regiNum;
		ui.printSubSubMenu("회원 정보 확인");
		
		System.out.print("이름 : "); name = sc.next();
		System.out.print("주민등록번호 : "); regiNum = sc.next();
		
		return ms.verifyMember(name, regiNum);
	}
	
	/**
	 * <pre>
	 * 등록 회원 개인정보 관리 메서드
	 * </pre>
	 * @throws ParseException
	 */
	public void myInfoManagement() throws ParseException {
		Member dto = new Member();
		int num;
		
		while(true) {
			ui.printSubMenu("내 정보 관리");
			
			System.out.println("1. 내 정보 조회");
			System.out.println("2. 내 정보 수정");
			System.out.println("3. 서비스 등록 해지");
			System.out.println("4. 알림 받기");
			System.out.println("0. 돌아가기");
			
			System.out.print("사용하실 메뉴 번호를 입력하세요 : ");
			num = sc.nextInt();
			switch(num) {
			case 1:
				dto = isRegistered();
				if(dto != null) {
					ui.printSubSubMenu("내 정보 조회");
					System.out.println(dto);
				}
				break;
			case 2:
				dto = isRegistered();
				if(dto != null) {
					ui.printSubSubMenu("내 정보 수정");
					ms.reviseMember(dto);
				}
				break;
			case 3:
				dto = isRegistered();
				if(dto != null) {
					ui.printSubMenu("서비스 등록 해지");
					ms.delMember(dto);
				}
				break;
			case 4:
				dto = isRegistered();
				if(dto != null) {
					ui.printSubMenu("백신 2차 접종 알림");
					ms.notification(dto);
				}
				break;
			case 0:
				System.out.println("메인 메뉴로 돌아갑니다.");
				return;
			default:
				System.out.println("0~4 사이의 숫자를 입력하세요.");
			}
		}
	}
	
	/**
	 * <pre>
	 * 관리자 전용 메뉴
	 * </pre>
	 * @throws ParseException
	 */
	public void adminMenu() throws ParseException {
		String id, pw;
		int num;
		
		System.out.print("관리자 아이디 : "); id = sc.next();
		System.out.print("비밀번호 : "); pw = sc.next();
		
		if(!id.equals("admin") || !pw.equals("ad1234")) {
			System.out.println("관리자 계정을 확인해주세요.");
			System.out.println("메인 메뉴로 돌아갑니다.");
			return;
		}
		
		while(true) {
			ui.printSubMenu("관리자 메뉴");
			
			System.out.println("1. 등록 회원 관리");
			System.out.println("2. 센터 관리");
			System.out.println("0. 돌아가기");
			
			System.out.print("사용하실 메뉴 번호를 입력하세요 : ");
			num = sc.nextInt();
			switch(num) {
			case 1:
				manageMember();
				break;
			case 2:
				manageCenter();
				break;
			case 0:
				System.out.println("메인 메뉴로 돌아갑니다.");
				return;
			default:
				System.out.println("0~2 사이의 숫자를 입력하세요.");
			}
		}
	}
	
	/**
	 * <pre>
	 * 관리자 전용 회원관리 메서드
	 * </pre>
	 * @throws ParseException
	 */
	public void manageMember() throws ParseException {
		int num;
		
		while(true) {
			ui.printSubMenu("회원 관리");
			
			System.out.println("1. 등록 회원 전체 조회");
			System.out.println("2. 신규 회원 등록");
			System.out.println("3. 회원 정보 수정");
			System.out.println("4. 회원 삭제");
			System.out.println("5. 회원 전체 삭제");
			System.out.println("0. 돌아가기");
		
			System.out.print("사용하실 메뉴 번호를 입력하세요 : ");
			num = sc.nextInt();
			switch(num) {
			case 1:
				ui.printSubSubMenu("등록 회원 전체 조회");
				ms.printAllMember();
				break;
			case 2:
				ui.printSubSubMenu("신규 회원 등록");
				ms.addMember();
				break;
			case 3:
				ui.printSubSubMenu("회원 정보 수정");
				ms.reviseMember();
				break;
			case 4:
				ui.printSubSubMenu("회원 삭제");
				ms.delMember();
				break;
			case 5:
				ui.printSubSubMenu("회원 전체 삭제");
				ms.delAllMember();
				break;
			case 0:
				System.out.println("이전 메뉴로 돌아갑니다.");
				return;
			default:
				System.out.println("0~5 사이의 숫자를 입력하세요.");
			}
		}
	}
	
	/**
	 * <pre>
	 * 관리자 전용 센터관리 메서드
	 * </pre>
	 * @throws ParseException
	 */
	public void manageCenter() {
		int num;
		
		while(true) {
			ui.printSubSubMenu("센터 관리");
			
			System.out.println("1. 등록 센터 전체 조회");
			System.out.println("2. 신규 센터 등록");
			System.out.println("3. 센터 정보 수정");
			System.out.println("4. 센터 삭제");
			System.out.println("5. 센터 전체 삭제");
			System.out.println("0. 돌아가기");
		
			System.out.print("사용하실 메뉴 번호를 입력하세요 : ");
			num = sc.nextInt();
			switch(num) {
			case 1:
				ui.printSubSubMenu("등록 센터 전체 조회");
				cs.printAllCenter();
				break;
			case 2:
				ui.printSubSubMenu("신규 센터 등록");
				cs.addCenter();
				break;
			case 3:
				ui.printSubSubMenu("센터 정보 수정");
				cs.reviseCenter();
				break;
			case 4:
				ui.printSubSubMenu("센터 삭제");
				cs.delCenter();
				break;
			case 5:
				ui.printSubSubMenu("센터 전체 삭제");
				cs.delAllCenter();
				break;
			case 0:
				System.out.println("이전 메뉴로 돌아갑니다.");
				return;
			default:
				System.out.println("0~5 사이의 숫자를 입력하세요.");
			}
		}
	}
}
