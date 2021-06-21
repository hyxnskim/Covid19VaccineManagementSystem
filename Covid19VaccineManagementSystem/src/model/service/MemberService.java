/**
 * 
 */
package model.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import model.dto.Member;
import util.Utility;

/**
 * <pre>
 * 등록 사용자 관리 위한 메서드 구현
 * </pre>
 * @author Hyunsoo Kim
 * @version ver.2.0
 * @since jdk1.8
 */
public class MemberService {
	
	/** 회원들을 저장/관리하기 위한 자료 저장구조 */
	private ArrayList<Member> memList = new ArrayList<Member>();
	
	/** 
	 * <pre>
	 * 기본생성자 : 초기화 회원 등록 수행 
	 * <pre>
	 * @throws ParseException 
	 */
	public MemberService() {
		try {
			initMember();
		} catch (ParseException e) {
			e.printStackTrace();
		}
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
		
		Member dto1 = new Member("김현수", "970620-2000000", "01063886503", "서울특별시", "화이자", "20210531");	//20210531 20210523 20210513
		Member dto2 = new Member("김진홍", "980828-1000000", "01089971463", "강원도", "화이자", "20210519");
		Member dto3 = new Member("조춘웅", "420519-1000000", "01052535388", "경기도", "AZ", "20210604");
		Member dto4 = new Member("김태재", "970530-1000000", "01093433384", "경상남도", "모더나", "20210602");
		Member dto5 = new Member("박승현", "971230-2000000", "01091639252", "충청북도", "AZ", "20210303");
		addMember(dto1);
		addMember(dto2);
		addMember(dto3);
		addMember(dto4);
		addMember(dto5);
		
		return getCount();
	}
	
	/**
	 * <pre>
	 * 전체 회원 출력 메서드
	 * </pre>
	 */
	public void printAllMember() {
		System.out.println("전체 회원 수 : " + memList.size());
		for(int i = 0; i < memList.size(); i++) {
			System.out.println("[" + (i+1) + "] " + memList.get(i));
		}
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
	 * 접종 백신 이름을 입력받아 유효한 이름인지 검사
	 * </pre>
	 * @return 성공시 백신 이름, 실패시 null
	 */
	public String inputVacType() {
		Scanner sc = new Scanner(System.in);
		Utility util = new Utility();
		String tmp;
		boolean close = false;

		while(!close) {
			System.out.print("접종 백신 종류 (AZ/화이자/모더나): "); tmp = sc.next(); 
			if(tmp.equals("AZ") || tmp.equals("화이자") || tmp.equals("모더나")) {
				return tmp;
			} else if(tmp.equals("얀센")) {
				System.out.println("[알림] 얀센은 2차접종이 필요하지 않으므로 등록하지 않으셔도 됩니다.");
				if(!util.getAnswer("다른 백신으로 등록하시겠습니까?")) {
					return null;
				}
			} else {
				System.out.println("[오류] 입력하신 백신 이름이 올바르지 않습니다.");
			}
		}
		return null;
	}
	
	/**
	 * <pre>
	 * 회원 등록 메서드
	 * </pre>
	 * @param dto 등록 회원
	 * @return 성공시 true, 실패시 false
	 * @throws ParseException 
	 */
	public boolean addMember(Member dto) throws ParseException {
		
		if(exist(dto.getRegiNum()) == -1) {
			int period = findPeriod(dto.getVacType());
			if(period > 0) {
				Utility util = new Utility();
				String date = util.addDate(dto.getDateFirst(), period);
				dto.setDateSecond(date);
				dto.setNotiDate(util.addDate(date, -3));
				memList.add(dto);
				return true;
			} else {
				System.out.println("[오류] 입력한 백신 이름이 올바르지 않습니다");
				return false;
			}
		}
		else {
			System.out.println("[오류] " + dto.getName() + "님은 이미 등록되었습니다.");
			return false;
		}
	}
	
	/**
	 * <pre>
	 * 회원 둥록 메서드
	 * -- 사용자 입력
	 * </pre>
	 * @return 성공시 true, 실패시 false
	 * @throws ParseException 
	 */
	public boolean addMember() throws ParseException {
		Scanner sc = new Scanner(System.in);
		String name, vacType, dateFirst = null;
		
		System.out.print("이름 : "); name = sc.next();
		vacType = inputVacType();
		if(vacType == null) {
			return false;
		}
		
		System.out.print("1차 접종일 (형식 : 20210605) : "); dateFirst = sc.next();
		
		return addMember(name, vacType, dateFirst);
	}
	
	/**
	 * <pre>
	 * 회원 등록 메서드
	 * -- 다른 메서드로부터 호출됨
	 * </pre>
	 * @param name 아룸
	 * @param vacType 접종 백신 이름
	 * @param dateFirst 1차 접종일
	 * @return 성공시 true, 실패시 false
	 * @throws ParseException
	 */
	public boolean addMember(String name, String vacType, String dateFirst) throws ParseException {
		Scanner sc = new Scanner(System.in);
		ArrayList<String> districts = new CenterService().getDistricts();
		int num = 0;
		Member dto = new Member();
		
		dto.setName(name);
		dto.setVacType(vacType);
		dto.setDateFirst(dateFirst);
		
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
		
		return addMember(dto);
	}
	
	/**
	 * <pre>
	 * 회원 검증 메서드
	 * </pre>
	 * @param name 이름
	 * @param regiNum 주민번호
	 * @return 성공시 해당 회원 객체, 실패시 null
	 */
	public Member verifyMember(String name, String regiNum) {
		for(int i = 0; i < memList.size(); i++) {
			Member dto = memList.get(i);
			if(dto.getName().equals(name) && dto.getRegiNum().equals(regiNum)) {
				System.out.println("회원 정보가 확인되었습니다.");
				return dto;
			}
		}
		System.out.println("[오류] 입력하신 정보로 등록된 회원이 존재하지 않습니다.");
		return null;
	}
	
	/**
	 * <pre>
	 * 사용자 정보 삭제 메서드
	 * -- 관리자용
	 * </pre>
	 */
	public void delMember() {
		Utility util = new Utility();
		Scanner sc = new Scanner(System.in);
		String name, regiNum;
		
		System.out.print("이름 : "); name = sc.next();
		System.out.print("\n주민번호 : "); regiNum = sc.next();
		
		Member dto = null;
		dto = verifyMember(name, regiNum);
		
		if(dto == null) {
			System.out.println("이전 메뉴로 되돌아갑니다.");
			return;
		}
		
		System.out.println(dto);
		
		if(util.getAnswer("위 회원정보를 삭제하겠습니까?")){
			memList.remove(dto);
			System.out.println("정상적으로 삭제되었습니다.");
		} else {
			System.out.println("이전 메뉴로 되돌아갑니다.");
		}
	}
	
	/**
	 * <pre>
	 * 사용자 정보 삭제 메서드
	 * -- 사용자용
	 * </pre>
	 * @param dto 삭제할 멤버 객체
	 */
	public void delMember(Member dto) {
		Utility util = new Utility();
		
		if(util.getAnswer("코로나 백신 2차 접종 알림 서비스를 해지하시겠습니까?")) {
			memList.remove(dto);
			System.out.println("정상적으로 삭제되었습니다.");
		} else {
			System.out.println("이전 메뉴로 되돌아갑니다.");
		}
	}
	
	/**
	 * <pre>
	 * 전체 회원 정보 삭제 메서드
	 * -- 관리자용
	 * </pre>
	 */
	public void delAllMember() {
		Utility util = new Utility();
		
		if(util.getAnswer("전체 회원 정보를 삭제하시겠습니까?")) {
			memList.clear();
			System.out.println("정상적으로 삭제되었습니다.");
		} else {
			System.out.println("이전 메뉴로 되돌아갑니다.");
		}
	}
	
	/**
	 * <pre>
	 * 사용자 정보 수정 메서드
	 * -- 관리자용
	 * </pre>
	 * @throws ParseException
	 */
	public void reviseMember() throws ParseException {
		Scanner sc = new Scanner(System.in);
		String name, regiNum;
		
		System.out.print("이름 : "); name = sc.next();
		System.out.print("\n주민번호 : "); regiNum = sc.next();
		
		Member dto = null;
		dto = verifyMember(name, regiNum);
		
		if(dto == null) {
			System.out.println("\n이전 메뉴로 되돌아갑니다.");
			return;
		}
		
		reviseMember(dto);
	}
	
	/**
	 * <pre>
	 * 회원 정보 수정 메서드
	 * -- 사용자용
	 * </pre>
	 * @param dto 수정할 회원 객체
	 * @throws ParseException
	 */
	public void reviseMember(Member dto) throws ParseException {
		Utility util = new Utility();
		Scanner sc = new Scanner(System.in);
		ArrayList<String> districts = new CenterService().getDistricts();
		int num;
		
		System.out.println(dto);
		
		if(!util.getAnswer("위 회원정보를 수정하겠습니까?")) {
			System.out.println("\n이전 메뉴로 되돌아갑니다.");
		}
		
		System.out.println("-------------------");
		System.out.println("1. 전화번호");
		System.out.println("2. 거주 지역");
		System.out.println("3. 접종 백신 종류");
		System.out.println("4. 1차 접종일");
		System.out.println("0. 돌아가기");
		System.out.println("-------------------");
		System.out.print("수정할 항목을 선택하세요 : "); num = sc.nextInt();
		
		while(true) {
			switch(num){
				case 1:
					System.out.print("수정할 전화번호 : ");
					dto.setContact(sc.next());
					System.out.println("수정되었습니다.");
					return;
				case 2:
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
					System.out.println("수정되었습니다.");
					return;
				case 3:
					dto.setVacType(inputVacType());
					System.out.println("수정되었습니다.");
					return;
				case 4:
					System.out.print("1차 접종일 (형식 : 20210608) : ");
					dto.setDateFirst(sc.next());
					
					int period = findPeriod(dto.getVacType());
					String date = util.addDate(dto.getDateFirst(), period);
					
					dto.setDateSecond(date);
					dto.setNotiDate(util.addDate(date, -3));
					System.out.println("수정되었습니다.");
					return;
				case 0:
					System.out.println("이전 메뉴로 돌아갑니다.");
					return;
				default:
					System.out.println("0~4 사이의 숫자를 입력하세요");
					
			}
		}
	}
	
	/**
	 * <pre>
	 * 2차 접종 예정일 알림 메서드
	 * </pre>
	 * @param 대상 멤버 객체
	 * @throws ParseException
	 */
	public void notification(Member dto) throws ParseException {
		Scanner sc = new Scanner(System.in);
		
		String today = new SimpleDateFormat("yyyyMMdd").format(new Date());
		String second = dto.getDateSecond();
		
		int td = Integer.parseInt(today);
		int sd = Integer.parseInt(second);
		
		SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");
		Date dt = dtFormat.parse(second); 
		String tmp = new SimpleDateFormat("yyyy년 MM월 dd일").format(dt);
		
		if(sd - td > 3) {
			System.out.println("알림 기간이 아닙니다.\n");
			System.out.println(dto.getName() + "님의 2차 접종 예정일은 " + tmp + " 입니다.");
			
		} else if(sd - td <= 3 && sd - td >= 0) {
			System.out.println(dto.getName() + "님의 " + dto.getVacType() + " 백신 2차 접종 예정일은 " + tmp + " 입니다.");
			System.out.println("거주지역 내의 예방접종 센터는 다음과 같습니다 : ");
			CenterService cs = new CenterService();
			cs.printCenterByDistrict(dto.getDistrict());
			System.out.println("본 내용은 휴대폰 " + dto.getContact() + " 으로 전송되었습니다.");
			
		} else {
			System.out.println("2차 접종 예정일이 지났습니다.");
			System.out.println(dto.getName() + "님의 2차 접종 예정일은 " + tmp + " 이었습니다.");
		}
	}
}
