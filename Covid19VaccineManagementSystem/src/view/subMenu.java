/**
 * 
 */
package view;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Scanner;

import model.service.Service;
import util.UI;

/**
 * <pre>
 * 메인 메뉴로부터 호출됨, 각 클래스에서 메서드를 호출
 * </pre>
 * @author Hyunsoo Kim
 * @version ver.1.0
 * @since jdk1.8
 */
public class subMenu {
	UI ui = new UI();
	
	public void printCurrentVac() throws ParseException {
		Service service = new Service();
		Scanner sc = new Scanner(System.in);
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
				int sdate, edate;
				ui.printSubSubMenu("날짜별 검색");
				System.out.println("시작일과 종료일을 입력하세요. (형식 : 20210607)");
				System.out.print("시작일 : "); sdate = sc.nextInt();
				System.out.print("종료일 : "); edate = sc.nextInt();
				
				int tmp;
				for(int i = 0; i < edate - sdate + 1; i++) {
					tmp = sdate + i;
					SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");
					Date dt = dtFormat.parse(Integer.toString(tmp)); 
					
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
	

}
