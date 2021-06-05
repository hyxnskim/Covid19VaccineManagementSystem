/**
 * 
 */
package model.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

import model.dto.Member;
import util.UI;

/**
 * <pre>
 * 등록 사용자 관리 위한 메서드 구현
 * </pre>
 * @author Hyunsoo Kim
 * @version ver.1.0
 * @since jdk1.8
 */
public class MemberService {
	
	public static void main(String[] args) throws ParseException {
		MemberService ms = new MemberService();
		ms.addMember();
	}
	
	/** 회원들을 저장/관리하기 위한 자료 저장구조 */
	private ArrayList<Member> memList = new ArrayList<Member>();
	
	/** 
	 * <pre>
	 * 기본생성자 : 초기화 회원 등록 수행 
	 * <pre>
	 * @throws ParseException 
	 */
	public MemberService() throws ParseException {
		System.out.println("초기 회원 등록작업이 완료되었습니다 : " + initMember());
	}
	
	/** 
	 * <pre>
	 * 현재 등록자수 조회
	 * </pre>
	 * @return 현재 등록자수
	 */
	public int getCount() {
		return memList.size();
	}
	
	/**
	 * <pre>
	 * 회원 중복 체크
	 * </pre>
	 * @param regiNum 주민등록번호
	 * @return 존재시에 저장위치 번호, 존재하지 않으면 -1
	 */
	public int exist(String regiNum) {
		for(int i = 0; i < getCount(); i++) {
			if(regiNum.equals((memList.get(i)).getRegiNum())) {
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * <pre>
	 * 테스트를 위한 회원 초기화 등록 메서드
	 * </pre>
	 * @return 초기화 회원등록 인원수
	 * @throws ParseException 
	 */
	public int initMember() throws ParseException {
		
		Member dto1 = new Member("김현수", "970620-2000000", "01063886503", "서울특별시", "화이자", "2021-05-31");
		Member dto2 = new Member("김진홍", "980828-1000000", "01089971463", "강원도", "화이자", "2021-06-01");
		Member dto3 = new Member("조춘웅", "420519-1000000", "01052535388", "경기도", "AZ", "2021-06-04");
		Member dto4 = new Member("김태재", "970530-1000000", "01093433384", "경상남도", "모더나", "2021-06-02");
		//Member dto5 = new Member("박승현", "971230-2000000", "01091639252", "충청북도", "얀센", "2021-06-03");
		addMember(dto1);
		addMember(dto2);
		addMember(dto3);
		addMember(dto4);
		//addMember(dto5);
		
		return getCount();
	}
	
	/**
	 * <pre>
	 * 날짜 계산 메서드
	 * </pre>
	 * @param strDate 기준일
	 * @param day 더할 일 수
	 * @return 기준일이 day만큼 더해진 날짜
	 * @throws ParseException
	 */
	public String addDate(String strDate, int day) throws ParseException { 
		SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd"); 
		Calendar cal = Calendar.getInstance(); 
		Date dt = dtFormat.parse(strDate); 
		cal.setTime(dt); 

		cal.add(Calendar.DATE, day); 
		return dtFormat.format(cal.getTime()); 
	}
	
	/**
	 * <pre>
	 * 1차 접종받은 백신 종류에 따라 다음 접종까지의 대기 기간을 알려주는 메서드
	 * </pre>
	 * @param vacType 접종받은 백신 종류
	 * @return 다음 접종까지의 대기 기간
	 */
	public int findPeriod(String vacType) {
		if(vacType.equals("화이자")) return 21;
		else if(vacType.equals("모더나")) return 28;
		else if(vacType.equals("AZ")) return 84;
		else return 0;
	}
	
	/**
	 * <pre>
	 * 회원 등록 메서드
	 * </pre>
	 * @param dto 등록 회원
	 * @throws ParseException 
	 */
	public void addMember(Member dto) throws ParseException {
		if(exist(dto.getRegiNum()) == -1) {
			int period = findPeriod(dto.getVacType());
			if(period > 0) {
				String date = addDate(dto.getDateFirst(), period);
				dto.setDateSecond(date);
				dto.setNotiDate(addDate(date, 3));
				memList.add(dto);
			} else {
				System.out.println("[오류] 입력한 백신 이름이 올바르지 않습니다");
			}
		}
		else System.out.println("[오류] " + dto.getName() + "님은 이미 등록되었습니다.");
	}
	
	/**
	 * <pre>
	 * 회원 둥록 메서드
	 * -- 사용자 입력
	 * </pre>
	 * @throws ParseException 
	 */
	public void addMember() throws ParseException {
		Scanner sc = new Scanner(System.in);
		UI ui = new UI();
		ArrayList<String> districts = new CenterService().getDistricts();
		int num = 0;
		
		ui.printSubMenu("회원 정보 등록");
		Member dto = new Member();
		
		System.out.print("이름 : "); dto.setName(sc.next());
		System.out.print("주민번호 : "); dto.setRegiNum(sc.next());
		System.out.print("연락처 (형식 : 01012341234) : "); dto.setContact(sc.next());
		System.out.println("-------- "); 
		for(int i = 0; i < districts.size(); i++) {
			System.out.printf("%d. %s\n", i+1, districts.get(i));
		}
		System.out.println("-------");
		
		boolean close = false;
		while(!close) {
			System.out.print("거주지역을 선택하세요 : ");  num = sc.nextInt();
			if(num > 0 && num <= districts.size()) {
				dto.setDistrict(districts.get(num - 1));
				close = true;
			} else {
				System.out.println("[오류] 1 ~ " + districts.size() + " 사이의 숫자를 입력하세요");
			}
		}
		dto.setDistrict(districts.get(num - 1));
		
		String tmp;
		close = false;
		while(!close) {
			System.out.print("접종 백신 종류 (AZ/화이자/모더나): "); tmp = sc.next(); 
			if(tmp.equals("AZ") || tmp.equals("화이자") || tmp.equals("모더나")) {
				dto.setVacType(tmp);
				close = true;
			} else if(tmp.equals("얀센")) {
				System.out.println("[알림] 얀센은 2차접종이 필요하지 않으므로 등록하지 않으셔도 됩니다.");
				boolean close2 = false;
				while(!close2) {
					System.out.print("다른 백신으로 등록하시겠습니까? (Y/N) : ");
					tmp = sc.next();
					if(tmp.equals("Y")) {
						close2 = true;
					} else if(tmp.equals("N")) {
						System.out.println("이전 메뉴로 되돌아갑니다.");
						return;
					} else {
						System.out.println("[오류] 잘못 입력하셨습니다.");
					}
				}
			} else {
				System.out.println("[오류] 입력하신 백신 이름이 올바르지 않습니다.");
			}
		
		}
		System.out.print("1차 접종일 (형식 : 20210605) : "); dto.setDateFirst(sc.next()); // 검증 메서드 추가해야함
		
		addMember(dto);
	}
}
